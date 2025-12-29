package com.bridgelabz.fundoo.service.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.bridgelabz.fundoo.dto.RegistrationDTO;
import com.bridgelabz.fundoo.utility.Util;

@Component
public class UserRegistrationValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
	return RegistrationDTO.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {

	RegistrationDTO user = (RegistrationDTO) target;

	if (!Util.nameMatcher(user.getFirstName())) {
	    errors.rejectValue("firstName", "firstName.invalid", "Invalid first name");
	}

	if (!Util.nameMatcher(user.getLastName())) {
	    errors.rejectValue("lastName", "lastName.invalid", "Invalid last name");
	}

	if (!Util.emailIdMacher(user.getEmailId())) {
	    errors.rejectValue("emailId", "emailId.invalid", "Invalid email address");
	}

	if (!Util.mobileNumberMatcher(String.valueOf(user.getMobileNumber()))) {
	    errors.rejectValue("mobileNumber", "mobileNumber.invalid", "Invalid mobile number");
	}

	if (!Util.passwordMatcher(user.getPassword())) {
	    errors.rejectValue("password", "password.weak",
		    "Password must contain uppercase, lowercase, number and special character");
	}
    }

}
