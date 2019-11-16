package com.bridgelabz.fundoo.dao;

import java.util.List;

import com.bridgelabz.fundoo.model.User;

public interface UserDAO {
	
	void register(User user);
	
	boolean login(User user);
	
	List<User> getAllUser();
	
	boolean isVarified(User user);

}
