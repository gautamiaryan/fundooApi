package com.bridgelabz.fundoo.service;

import java.util.List;

import com.bridgelabz.fundoo.dto.RegistrationDTO;
import com.bridgelabz.fundoo.model.User;

public interface IUserService {
	boolean registerUser(RegistrationDTO user);
    String login(String userName,String password);
	void parseToken(String token);
	boolean resetPassword(String emailId,String password);
	boolean forgetPassword(String emailId);
	User getUserById(Long id);
	List<User> getAllUser();
	Long getUserId(String token);
	
}

