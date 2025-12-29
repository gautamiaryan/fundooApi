package com.bridgelabz.fundoo.service.validator;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.bridgelabz.fundoo.dto.LoginDTO;
import com.bridgelabz.fundoo.utility.Util;

@Component
public class LoginValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
	return LoginDTO.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
	LoginDTO loginDTO = (LoginDTO) target;

	if (StringUtils.isEmpty(loginDTO.getEmailId())) {
	    errors.rejectValue("emailId", "email.required", "Email is required");
	} else if (!Util.emailIdMacher(loginDTO.getEmailId())) {
	    errors.rejectValue("emailId", "email.invalid", "Invalid email format");
	}

	if (StringUtils.isEmpty(loginDTO.getPassword())) {
	    errors.rejectValue("password", "password.required", "Password is required");
	} else if (!Util.passwordMatcher(loginDTO.getPassword())) {
	    errors.rejectValue("password", "password.invalid", "Invalid password format");
	}
    }

}
