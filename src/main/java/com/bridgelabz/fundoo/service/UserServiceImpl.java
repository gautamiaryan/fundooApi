package com.bridgelabz.fundoo.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.validation.BeanPropertyBindingResult;

import com.bridgelabz.fundoo.configure.RabbitMQSender;
import com.bridgelabz.fundoo.dao.IUserDAO;
import com.bridgelabz.fundoo.dto.LoginDTO;
import com.bridgelabz.fundoo.dto.MailObject;
import com.bridgelabz.fundoo.dto.RegistrationDTO;
import com.bridgelabz.fundoo.exception.InvalidRequestException;
import com.bridgelabz.fundoo.exception.UserExceptions;
import com.bridgelabz.fundoo.model.User;
import com.bridgelabz.fundoo.service.helper.UserServiceHelper;
import com.bridgelabz.fundoo.service.validator.UserLoginValidator;
import com.bridgelabz.fundoo.service.validator.UserRegistrationValidator;
import com.bridgelabz.fundoo.utility.JMSProvider;
import com.bridgelabz.fundoo.utility.JWTProvider;

import jakarta.transaction.Transactional;

@Service
public class UserServiceImpl implements IUserService {

    private static final String RESET_PASSWORD_URL = "http://localhost:4200/resetPassword/";
    private static final String VERIFICATION_URL = "http://localhost:8080/api/verify/";
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private IUserDAO userDAO;

    @Autowired
    private UserServiceHelper userServiceHelper;

    @Autowired
    private UserRegistrationValidator userRegistrationValidator;

    @Autowired
    private UserLoginValidator userLoginValidator;

    @Autowired
    private RabbitMQSender rabbitMQSender;

    @Autowired
    private JWTProvider jwtProvider;

    @Autowired
    private JMSProvider jmsProvider;
    
    @Transactional
    @Override
    public RegistrationDTO registerUser(RegistrationDTO registrationDTO) {

	logger.debug("Starting user registration for email={}", registrationDTO.getEmailId());

	BeanPropertyBindingResult errors = new BeanPropertyBindingResult(registrationDTO, "registrationDTO");

	userRegistrationValidator.validate(registrationDTO, errors);

	if (errors.hasErrors()) {
	    logger.warn("Registration validation failed for email={} | error={}", registrationDTO.getEmailId(),
		    errors.getAllErrors().get(0).getDefaultMessage());
	    throw new UserExceptions(errors.getAllErrors().get(0).getDefaultMessage());
	}

	User user = userServiceHelper.convertToUser(registrationDTO);
	user.setPassword(userServiceHelper.encryptPassword(user.getPassword()));

	logger.debug("Persisting user entity for email={}", user.getEmailId());
	try {
	    User savedUser = userDAO.persistUser(user);
	    if (savedUser == null || savedUser.getUserId() == null) {
		logger.error("User persistence failed (duplicate?) for email={}", registrationDTO.getEmailId());
		throw new UserExceptions("User already exists");
	    }

	    logger.info("User persisted successfully with id={} for email={}", savedUser.getUserId(),
		    savedUser.getEmailId());

	    String token = jwtProvider.generateToken(savedUser.getUserId().toString());

	    MailObject mail = new MailObject();
	    mail.setEmail(registrationDTO.getEmailId());
	    mail.setSubject("Account Verification");
	    mail.setMessage(VERIFICATION_URL + token);

	    rabbitMQSender.send(mail);

	    logger.debug("Verification mail sent for email={}", registrationDTO.getEmailId());
	    registrationDTO.setPassword("*****************");
	    return registrationDTO;
	} catch (DataIntegrityViolationException e) {
	    throw new InvalidRequestException("User already exists with emailId : " + user.getEmailId());
	}
    }

    @Override
    public String login(LoginDTO loginDTO) {
	logger.debug("Login attempt for email={}", loginDTO.getEmailId());

	BeanPropertyBindingResult errors = new BeanPropertyBindingResult(loginDTO, "loginDTO");

	userLoginValidator.validate(loginDTO, errors);

	if (errors.hasErrors()) {
	    logger.warn("Login validation failed for email={} | error={}", loginDTO.getEmailId(),
		    errors.getAllErrors().get(0).getDefaultMessage());
	    throw new UserExceptions(errors.getAllErrors().get(0).getDefaultMessage());
	}

	User user = userDAO.getUserByEmail(loginDTO.getEmailId());

	if (user == null) {
	    logger.warn("Login failed: user not found for email={}", loginDTO.getEmailId());
	    throw new UserExceptions("UserId/Password is incorrect");
	}

	if (!user.getStatus()) {
	    logger.warn("Login failed: inactive user for email={}", loginDTO.getEmailId());
	    throw new UserExceptions("User account is not active");
	}

	if (userServiceHelper.matches(loginDTO.getPassword(), user.getPassword())) {

	    String token = jwtProvider.generateToken(user.getUserId().toString());

	    logger.info("Login successful for email={} | userId={}", loginDTO.getEmailId(), user.getUserId());

	    return token;
	}

	logger.warn("Login failed: password mismatch for email={}", loginDTO.getEmailId());

	throw new UserExceptions("UserId/Password is incorrect");
    }

    @Override
    public void parseToken(String token) {
	Long user_id = Long.parseLong(jwtProvider.getEmailFromToken(token));
	List<User> userList = userDAO.getAllUser();
	for (User user : userList) {
	    Long check_user_id = user.getUserId();
	    if (user_id == check_user_id) {
		user.setStatus(true);
		userDAO.persistUser(user);
	    }
	}
    }

    @Override
    public boolean resetPassword(String token, String newPassword) {
	try {
	    String email = jwtProvider.getEmailFromToken(token);
	    if (email == null || email.isEmpty()) {
		logger.warn("Invalid or expired token provided");
		throw new UserExceptions("Invalid token");
	    }

	    User user = userDAO.getUserByEmail(email);
	    if (user == null) {
		logger.warn("No user found for email={}", email);
		throw new UserExceptions("User not found");
	    }

	    if (!user.getEmailId().equals(email)) {
		logger.warn("Token email does not match user email | tokenEmail={} userEmail={}", email,
			user.getEmailId());
		throw new UserExceptions("Token does not match user");
	    }

	    String encryptedPassword = userServiceHelper.encryptPassword(newPassword);
	    user.setPassword(encryptedPassword);
	    userDAO.persistUser(user);

	    logger.info("Password reset successful for email={}", email);
	    return true;

	} catch (UserExceptions ex) {
	    logger.error("Password reset failed: {}", ex.getMessage());
	    throw ex;

	} catch (Exception ex) {
	    logger.error("Unexpected error during password reset", ex);
	    throw new UserExceptions("Password reset failed due to internal error");
	}
    }

    @Override
    public boolean forgetPassword(String emailId) {
	try {
	    User user = userDAO.getUserByEmail(emailId);
	    if (user == null) {
		logger.warn("No user found for email={}", emailId);
		throw new UserExceptions("User not found with email: " + emailId);
	    }

	    if (!user.getEmailId().equals(emailId)) {
		logger.warn("Email mismatch | inputEmail={} storedEmail={}", emailId, user.getEmailId());
		throw new UserExceptions("Email does not match user record");
	    }

	    String token = jwtProvider.generateToken(user.getEmailId());
	    jmsProvider.sendEmail(user.getEmailId(), "Password Reset", RESET_PASSWORD_URL + token);

	    logger.info("Password reset email sent successfully for email={}", emailId);
	    return true;

	} catch (UserExceptions ex) {
	    logger.error("Forget password failed: {}", ex.getMessage());
	    throw ex;

	} catch (Exception ex) {
	    logger.error("Unexpected error during forget password for email={}", emailId, ex);
	    throw new UserExceptions("Failed to process forget password request");
	}
    }

    @Cacheable("user")
    @CachePut(value = "user", key = "#id")
    @Override
    public User getUserById(Long id) {
	try {
	    User user = userDAO.getUserById(id);
	    if (user == null) {
		logger.warn("User not found with id={}", id);
		throw new UserExceptions("User not found with id: " + id);
	    }
	    logger.debug("Fetched user with id={}", id);
	    return user;
	} catch (Exception ex) {
	    logger.error("Error fetching user by id={}", id, ex);
	    throw new UserExceptions("Failed to fetch user");
	}
    }

    @Override
    public List<User> getAllUser() {
	try {
	    List<User> users = userDAO.getAllUser();
	    logger.debug("Fetched {} users", users.size());
	    return users;
	} catch (Exception ex) {
	    logger.error("Error fetching all users", ex);
	    throw new UserExceptions("Failed to fetch users");
	}
    }

    @Override
    public Long getUserId(String token) {
	try {
	    Long id = Long.parseLong(jwtProvider.getEmailFromToken(token));
	    logger.debug("Parsed user id={} from token", id);

	    User user = userDAO.getUserById(id);
	    if (user == null) {
		logger.warn("User not found for token | id={}", id);
		throw new UserExceptions("User not found for provided token");
	    }

	    return user.getUserId();
	} catch (NumberFormatException ex) {
	    logger.error("Invalid token format: {}", token, ex);
	    throw new UserExceptions("Invalid token");
	} catch (Exception ex) {
	    logger.error("Error getting user id from token: {}", token, ex);
	    throw new UserExceptions("Failed to retrieve user id");
	}
    }

}
