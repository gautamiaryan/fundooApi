package com.bridgelabz.fundoo.dao;

import java.util.List;

import com.bridgelabz.fundoo.model.User;

public interface IUserDAO {
	
	User register(User user);
		
	List<User> getAllUser();
	
	boolean isVarified(User user);
	
	User getUserById(Long id);
	
	

}
