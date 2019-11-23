package com.bridgelabz.fundoo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RegistrationDTO {

	private String firstName;
	
	private String lastName;

	private String emailId;
	
	private String password;

	private Long mobileNumber;	

}
