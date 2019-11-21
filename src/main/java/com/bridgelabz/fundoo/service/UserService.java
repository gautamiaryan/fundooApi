package com.bridgelabz.fundoo.service;

import com.bridgelabz.fundoo.dto.UserDto;
import com.bridgelabz.fundoo.model.User;

public interface UserService {
	boolean registerUser(UserDto user);
    boolean login(String userName,String password);
	void parseToken(String token);
	boolean resetPassword(String emailId,String password);
	boolean forgetPassword(String emailId);
	
}

