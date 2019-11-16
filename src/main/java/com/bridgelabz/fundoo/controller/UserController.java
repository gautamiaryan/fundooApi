package com.bridgelabz.fundoo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bridgelabz.fundoo.dto.UserDto;
import com.bridgelabz.fundoo.model.User;
import com.bridgelabz.fundoo.service.UserService;

@RestController
@RequestMapping("/api")
public class UserController {
	
	@Autowired
	private UserService userService;
	
	@PostMapping("/register")
	public UserDto register(@RequestBody UserDto user) {
		userService.registerUser(user);
		return user;
	}
	
	@GetMapping("/login")
	public String login(@RequestBody User user) {
		if(userService.login(user)) {
			return "Welcome"+user.getFirstName()+"!!you are suucessfully loged in";
		}
		
		return "email id or password not match";
		
	}

}
