package com.bridgelabz.fundoo.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bridgelabz.fundoo.configure.JMSProvider;
import com.bridgelabz.fundoo.configure.JWTProvider;
import com.bridgelabz.fundoo.dao.UserDAO;
import com.bridgelabz.fundoo.dto.UserDto;
import com.bridgelabz.fundoo.model.User;
import com.bridgelabz.fundoo.service.UserService;

@Service
public class UserServiceImpl implements UserService {
	
	@Autowired
	private UserDAO userDAO;
	
	@Autowired
	private JWTProvider provider;
    @Transactional
	@Override
	public void registerUser(UserDto userDto) {
    	User user=userDTOToUser(userDto);
    	userDAO.register(user);
    	String email=user.getEmailId();
    	String token=provider.generateToken(email);
    	String  url="http://localhost:8080/api/varify/";
    	JMSProvider.sendEmail(email, "for authentication", url+token);
		
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
    public void parseToken(String token) {
    	String email=provider.parseToken(token);
    	List<User> userList=userDAO.getAllUser();
    	for(User user:userList) {
    		String checkEmail=user.getEmailId();
    		if(email.equals(checkEmail)) {
    			user.setStatus(true);
    			userDAO.register(user);
    		}
    	}
    }
    @Transactional
    @Override
	public boolean login(String emailId,String password) {
    	List<User> userList =userDAO.getAllUser(); 
    	for(User user:userList) {
    		if(user.getEmailId().equalsIgnoreCase(emailId) && user.getPassword().equals(password)) {
    			if(userDAO.isVarified(user)) {
    			  return true;
    			}
    			
    		}
    	}
    	
    	return false;
       
	}
    
    
    public String encryptPassword(User user) {
    	
    	return null;
    	
    	
    }






    
  
 
}
