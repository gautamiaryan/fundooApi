//package com.bridgelabz.fundoo.service;
//
//import static org.junit.Assert.assertEquals;
//import static org.mockito.Mockito.times;
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import org.junit.Before;
//import org.junit.jupiter.api.Test;
//import org.junit.runner.RunWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.mockito.junit.MockitoJUnitRunner;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import com.bridgelabz.fundoo.controller.UserController;
//import com.bridgelabz.fundoo.dto.RegistrationDTO;
//import com.bridgelabz.fundoo.model.User;
//
//@RunWith(MockitoJUnitRunner.class)
//@SpringBootTest
//public class UserServiceTest {
//	
//	@InjectMocks
//	private UserController controller =new UserController();
//	
//	@Mock
//	private UserService userService;
//	
//	@Before
//	public void init() {
//		MockitoAnnotations.initMocks(this);
//	}
//	
//	@Test
//	public void getUserByIdTest() {
//		int id=1;
//		when(userService.getUserById(id)).thenReturn(new User("Gautam","kumar","gautamiaryan95@gmail.com","Gautam007@7"));
//		
//		User user=controller.
//		
//		assertEquals("Gautam", user.getFirstName());
//        assertEquals("kumar", user.getLastName());
//        assertEquals("gautamiaryan95@gmail.com",user.getEmailId());
//        assertEquals("Gautam007@7",user.getPassword());
//		
//	}
//	@Test
//	public void getAllUserTest() {
//		List <User> userList=new ArrayList<User>();
//		
//		User firstUser=new User("Gautam","Kumar","gautamiaryan@gmail.com","Gautam007@7");
//		
//		User secondUser=new User("Vikash","Kumar","vikash123@gmail.com","Vikah007@7");
//		
//		User thirdUser=new User("Ram","Briksh","rambriksh007@gmail.com","rambriksh007");
//		
//		
//		userList.add(firstUser);
//		userList.add(secondUser);
//		userList.add(thirdUser);
//		
//		when(userService.getAllUser()).thenReturn(userList);
//		
//		List<User> allUser=userService.getAllUser();
//		
//		assertEquals(3, allUser.size());
//		
//		verify(userService,times(1)).getAllUser();
//	
//		
//	}
//	
//	
//	@Test
//	public void registerUserTest() {
//		RegistrationDTO user=new RegistrationDTO("Vibhas","Vickram","vibhashvickram@gmail.com","vibhas007@7925",9939656544L);
//		
//		userService.registerUser(user);
//		
//		verify(userService,times(1)).registerUser(user);
//	}
//	
//	@Test
//	public void loginTest() {
//		when(user)
//	}
//	
//	
//	
//	
//	
//	
//	
//}
