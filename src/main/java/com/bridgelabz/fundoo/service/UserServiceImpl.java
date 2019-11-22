package com.bridgelabz.fundoo.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bridgelabz.fundoo.configure.JMSProvider;
import com.bridgelabz.fundoo.configure.JWTProvider;
import com.bridgelabz.fundoo.dao.UserDAO;
import com.bridgelabz.fundoo.dto.RegistrationDTO;
import com.bridgelabz.fundoo.model.User;
import com.bridgelabz.fundoo.utility.Util;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserDAO userDAO;

	@Autowired
	private static PasswordEncoder bcryptPassword = new BCryptPasswordEncoder();

	private final Log logger = LogFactory.getLog(getClass());

	private Pattern BCRYPT_PATTERN = Pattern.compile("\\A\\$2(a|y|b)?\\$(\\d\\d)\\$[./0-9A-Za-z]{53}");


	private Util util=new Util();

	@Autowired
	private JWTProvider provider;

	@Transactional
	@Override
	public boolean registerUser(RegistrationDTO userDto) {
		User user=userDTOToUser(userDto);
		User user1;
		if(inputValidator(user)) {
			String password=user.getPassword();
			String encryptPassword=encryptPassword(password);
			user.setPassword(encryptPassword);
			user1=userDAO.register(user);
			if(user1!=null) {
				String email=user.getEmailId();
				String token=provider.generateToken(email);
				String  url="http://localhost:8080/api/varify/";
				JMSProvider.sendEmail(email, "for authentication", url+token);
				return true;
			}
		}
		return false;
	}

	public RegistrationDTO userT0UserDto(User user) {
		RegistrationDTO userDto=new RegistrationDTO();
		userDto.setFirstName(user.getFirstName());
		userDto.setLastName(user.getLastName());
		userDto.setEmailId(user.getEmailId());
		userDto.setPassword(user.getPassword());
		userDto.setMobileNumber(user.getMobileNumber());
		return userDto;
	}

	public User userDTOToUser(RegistrationDTO userDto) {
		User user = new User();
		user.setFirstName(userDto.getFirstName());
		user.setLastName(userDto.getLastName());
		user.setEmailId(userDto.getEmailId());
		user.setPassword(userDto.getPassword());
		user.setMobileNumber(userDto.getMobileNumber());
		user.setStatus(false);
		user.setCreatedStamp(LocalDateTime.now());
		user.setUpdatedStamp(LocalDateTime.now());
		return user;
	}

	@Transactional
	@Override
	public void parseToken(String token) {
		String email=provider.parseToken(token);
		List<User> userList=userDAO.getAllUser();
		for(User user:userList) {
			String checkEmail=user.getEmailId();
			if(email.equals(checkEmail)) {
				user.setStatus(true);
				userDAO.register(user);
			}
		}
	}
	@Transactional
	@Override
	public boolean login(String emailId,String password) {
		List<User> userList =userDAO.getAllUser(); 
		for(User user:userList) {
			if(user.getEmailId().equalsIgnoreCase(emailId) && matches(password,user.getPassword())) {
				if(userDAO.isVarified(user)) {
					return true;
				}

			}
		}

		return false;

	}


	private String encryptPassword(String plainTextPassword) {

		return bcryptPassword.encode(plainTextPassword);

	}

	public boolean matches(CharSequence rawPassword, String encodedPassword) {
		if (encodedPassword == null || encodedPassword.length() == 0) {
			logger.warn("Empty encoded password");
			return false;
		}

		if (!BCRYPT_PATTERN.matcher(encodedPassword).matches()) {
			logger.warn("Encoded password does not look like BCrypt");
			return false;
		}

		return BCrypt.checkpw(rawPassword.toString(), encodedPassword);
	}


	@Override
	public boolean resetPassword(String emailId,String password) {
		String email=provider.parseToken(emailId);
		List<User> userList=userDAO.getAllUser();
		for(User user :userList) {
			if(user.getEmailId().equals(email)) {
				String newPassword=encryptPassword(password);
				user.setPassword(newPassword);
				userDAO.register(user);
				return true;
			}
		}
		return false;


	}


	@Override
	public boolean forgetPassword(String emailId) {
		List<User> userList=userDAO.getAllUser();
		for(User user:userList) {
			if(user.getEmailId().equals(emailId)) {
				String email=user.getEmailId();
				String token=provider.generateToken(email);
				String  url="http://localhost:8080/api/resetpassword/";
				JMSProvider.sendEmail(email, "for reset password", url+token);
				return true;
			}
		}
		return false;
	}

	public boolean inputValidator(User user) {
		return util.nameMatcher(user.getFirstName()) && 
				util.nameMatcher(user.getLastName()) && 
				util.emailIdMacher(user.getEmailId()) &&
				util.mobileNumberMatcher(String.valueOf(user.getMobileNumber())) &&
				util.passwordMatcher(user.getPassword());
	}
	@Override
	public User getUserById(Integer id) {
		return userDAO.getUserById(id);
				
	}

	@Override
	public List<User> getAllUser() {
		return userDAO.getAllUser();
	}

}
