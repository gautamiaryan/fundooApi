package com.bridgelabz.fundoo.service.helper;

import java.time.LocalDateTime;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.bridgelabz.fundoo.dto.RegistrationDTO;
import com.bridgelabz.fundoo.model.User;

@Component
public class UserServiceHelper {
    
    private Pattern BCRYPT_PATTERN = Pattern.compile("\\A\\$2(a|y|b)?\\$(\\d\\d)\\$[./0-9A-Za-z]{53}");
    private static PasswordEncoder bcryptPassword = new BCryptPasswordEncoder();
    private static final Logger logger = LoggerFactory.getLogger(UserServiceHelper.class);


    public RegistrationDTO convertToRegistrationDTO(User user) {
	RegistrationDTO registrationDTO = new RegistrationDTO();
	BeanUtils.copyProperties(user, registrationDTO);
	return registrationDTO;
    }

    public User convertToUser(RegistrationDTO userDto) {
	User user = new User();
	BeanUtils.copyProperties(userDto, userDto);
	user.setStatus(false);
	user.setCreatedStamp(LocalDateTime.now());
	user.setUpdatedStamp(LocalDateTime.now());
	return user;
    }
    

    public String encryptPassword(String plainTextPassword) {

	return bcryptPassword.encode(plainTextPassword);

    }

    public boolean matches(CharSequence rawPassword, String encodedPassword) {
	if (StringUtils.isEmpty(encodedPassword)) {
	    logger.warn("Empty encoded password");
	    return false;
	}

	if (!BCRYPT_PATTERN.matcher(encodedPassword).matches()) {
	    logger.warn("Encoded password does not look like BCrypt");
	    return false;
	}
	return BCrypt.checkpw(rawPassword.toString(), encodedPassword);
    }


}
