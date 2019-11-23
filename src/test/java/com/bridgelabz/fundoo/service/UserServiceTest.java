package com.bridgelabz.fundoo.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.junit4.SpringRunner;

import com.bridgelabz.fundoo.dao.UserDAO;
import com.bridgelabz.fundoo.dto.RegistrationDTO;
import com.bridgelabz.fundoo.model.User;

@RunWith(SpringRunner.class)
public class UserServiceTest {
	
	@InjectMocks
	private UserService userService;
	
	@Mock
	private UserDAO userDAO;
	
	@Before
	public void init() {
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public void getUserById(Integer id) {
		when(userDAO.getUserById(id)).thenReturn(new User("Gautam","kumar","gautamiaryan95@gmail.com","Gautam007@7"));
		
		User user=userDAO.getUserById(1);
		
		assertEquals("Gautam", user.getFirstName());
        assertEquals("kumar", user.getLastName());
        assertEquals("gautamiaryan95@gmail.com",user.getEmailId());
        assertEquals("Gautam007@7",user.getPassword());
		
		
	}
	@Test
	public void register() {
		
		
	}
	
	
	
	
	
	
	
}
