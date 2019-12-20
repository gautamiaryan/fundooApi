package com.bridgelabz.fundoo;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.bridgelabz.fundoo.dto.RegistrationDTO;
import com.bridgelabz.fundoo.model.User;
import com.bridgelabz.fundoo.service.UserServiceImpl;

@RunWith(SpringRunner.class)
@SpringBootTest
class FundooApplicationTests {

	@Autowired
	UserServiceImpl userService;
    
	@Test
	public void getList() {
		List<User> userList=userService.getAllUser();
		int size=userList.size();
		
		assertEquals(3, size);
		
	}
	
	@Test
	public void registerUser() {
		
		RegistrationDTO user=new RegistrationDTO();
		user.setFirstName("Vibhassss");
		user.setLastName("Vikram");
		user.setEmailId("qwertyu@gmail.com");;
		user.setPassword("Vibhass@123");
		boolean status=userService.registerUser(user);
	    assertEquals(true,status);
		
		
	}
	@Test
	public void loginTest() {
		String email="gautamiaryan@gmail.com";
		String password="Gautam007@7";
		String status=userService.login(email, password);
		assertEquals(true, status);
		
	}
	
	
	@Test
	public void getUserById() {
		Long id=1L;
		
		User user=userService.getUserById(id);
		
		assertEquals("Gautam",user.getFirstName());
		assertEquals("Singh",user.getLastName());
	}
	
	

}
