package com.bridgelabz.fundoo.controller;

import java.util.List;

import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bridgelabz.fundoo.dto.LoginDTO;
import com.bridgelabz.fundoo.dto.RegistrationDTO;
import com.bridgelabz.fundoo.exception.Response;
import com.bridgelabz.fundoo.model.User;
import com.bridgelabz.fundoo.service.UserService;

@RestController
@RequestMapping("/api")
public class UserController {

	@Autowired
	private UserService userService;

	@PostMapping("/register")
	@Cacheable(value="list1")
	public ResponseEntity<Response> register(@Valid @RequestBody RegistrationDTO user) {

		if(userService.registerUser(user)) {
			user.setPassword("**********");
			return new ResponseEntity<Response>(new Response(HttpStatus.OK.value(), "Successfully", user),HttpStatus.OK);
		}
		return new ResponseEntity<Response>(new Response(HttpStatus.BAD_REQUEST.value(), "Unsuccessfull",user),HttpStatus.BAD_REQUEST);

	}

	@GetMapping("/login")
	public ResponseEntity<Response> login(@Valid @RequestBody LoginDTO user) {

		if(userService.login(user.getEmailId(),user.getPassword())) {
			return new ResponseEntity<Response>(new Response(HttpStatus.OK.value(), "Successfully", user),HttpStatus.OK);
		}
		return new ResponseEntity<Response>(new Response(HttpStatus.BAD_REQUEST.value(),"Unsuccesfull",user),HttpStatus.BAD_REQUEST);

	}

	@GetMapping("/varify/{token}")
	public String getTocken(@PathVariable(name="token") String token) {
		userService.parseToken(token);
		return "user is varified";
	}

	@PostMapping("/resetpassword/{token}")
	public ResponseEntity<Response> resetPassword(@PathVariable String token,@RequestBody User user) {
		if(userService.resetPassword(token, user.getPassword())) {
			return new ResponseEntity<Response>(new Response(HttpStatus.OK.value(), "Successfully", user),HttpStatus.OK);
		}
		return new ResponseEntity<Response>(new Response(HttpStatus.BAD_REQUEST.value(),"Unsuccesfull",user),HttpStatus.BAD_REQUEST);


	}

	@PostMapping("/forgetpassword/")
	public ResponseEntity<Response> forgetPassword(@RequestBody User user) {
		if(userService.forgetPassword(user.getEmailId())){
			return new ResponseEntity<Response>(new Response(HttpStatus.OK.value(),"Unsuccesfull",user),HttpStatus.OK);
		}
		return new ResponseEntity<Response>(new Response(HttpStatus.BAD_REQUEST.value(),"Unsuccesfull",user),HttpStatus.BAD_REQUEST);
	}
	@GetMapping("/getUserById/{id}")
	public ResponseEntity<Response> getAllUser(@PathVariable Integer id){

		User user = userService.getUserById(id);
		if(user!=null) {
			return new ResponseEntity<Response>(new Response(HttpStatus.OK.value(),"User found",user),HttpStatus.OK);
		}
		return new ResponseEntity<Response>(new Response(HttpStatus.BAD_REQUEST.value(),"User not found",user),HttpStatus.BAD_REQUEST);


	}

	@GetMapping("/getalluser")
	public ResponseEntity<Response> getAllUser(){
		List<User> userList=userService.getAllUser();
		for(User user:userList) {
			if(user!=null) {
				return new ResponseEntity<Response>(new Response(HttpStatus.OK.value(),"User found",user),HttpStatus.OK);
			}
		    return new ResponseEntity<Response>(new Response(HttpStatus.BAD_REQUEST.value(),"User not found",user),HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<Response>(new Response(HttpStatus.BAD_REQUEST.value(),"User not found",null),HttpStatus.BAD_REQUEST);

		

	}


}
