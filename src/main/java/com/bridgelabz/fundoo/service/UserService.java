package com.bridgelabz.fundoo.service;

import com.bridgelabz.fundoo.dto.UserDto;
import com.bridgelabz.fundoo.model.User;

public interface UserService {
	void registerUser(UserDto user);
    boolean login(String userName,String password);
	void parseToken(String token);
	
}

