package com.bridgelabz.fundoo.service;

import java.util.List;

import com.bridgelabz.fundoo.dto.RegistrationDTO;
import com.bridgelabz.fundoo.model.User;

public interface UserService {
	boolean registerUser(RegistrationDTO user);
    boolean login(String userName,String password);
	void parseToken(String token);
	boolean resetPassword(String emailId,String password);
	boolean forgetPassword(String emailId);
	User getUserById(Integer id);
	List<User> getAllUser();
	
}

