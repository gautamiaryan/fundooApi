package com.bridgelabz.fundoo.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bridgelabz.fundoo.dao.UserDAO;
import com.bridgelabz.fundoo.dto.UserDto;
import com.bridgelabz.fundoo.model.User;
import com.bridgelabz.fundoo.service.UserService;

@Service
public class UserServiceImpl implements UserService {
	
	@Autowired
	private UserDAO userDAO;
	
    @Transactional
	@Override
	public void registerUser(UserDto user) {
    	
    	userDAO.register(userDTOToUser(user));
		
	}
    
    
    public UserDto userT0UserDto(User user) {
    	UserDto userDto=new UserDto();
    	userDto.setFirstName(user.getFirstName());
    	userDto.setLastName(user.getLastName());
    	userDto.setEmailId(user.getEmailId());
    	userDto.setPassword(user.getPassword());
    	userDto.setMobileNumber(user.getMobileNumber());
    	return userDto;
    }
    
    public User userDTOToUser(UserDto userDto) {
    	User user = new User();
    	user.setFirstName(userDto.getFirstName());
    	user.setLastName(userDto.getLastName());
    	user.setEmailId(userDto.getEmailId());
    	user.setPassword(userDto.getPassword());
    	user.setMobileNumber(userDto.getMobileNumber());
    	user.setStatus(false);
    	user.setCreatedStamp(LocalDateTime.now());
    	user.setUpdatedStamp(LocalDateTime.now());
    	return user;
    }

    @Transactional
	@Override
	public boolean login(User user) {
		
		if(userDAO.login(user) && userDAO.isVarified(user)) {
			return true;
		}
		
		else {
			return false;
		}
		
	}
  
 
}
