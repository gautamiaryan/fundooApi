package com.bridgelabz.fundoo.controller;

import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bridgelabz.fundoo.dto.UserDto;
import com.bridgelabz.fundoo.exception.ResponseError;
import com.bridgelabz.fundoo.model.User;
import com.bridgelabz.fundoo.service.UserService;

@RestController
@RequestMapping("/api")
public class UserController {

	@Autowired
	private UserService userService;

	@PostMapping("/register")
	public ResponseEntity<ResponseError> register(@Valid @RequestBody UserDto user) {
		if(userService.registerUser(user)) {
			return new ResponseEntity<ResponseError>(new ResponseError(HttpStatus.OK.value(), "Successfully", user),HttpStatus.OK);
		}
		return new ResponseEntity<ResponseError>(new ResponseError(HttpStatus.BAD_REQUEST.value(),"Unsuccesfull",user),HttpStatus.BAD_REQUEST);
		
	}

	@GetMapping("/login")
	public ResponseEntity<ResponseError> login(@RequestBody User user) {
		
		if(userService.login(user.getEmailId(),user.getPassword())) {
			return new ResponseEntity<ResponseError>(new ResponseError(HttpStatus.OK.value(), "Successfully", user),HttpStatus.OK);
		}
		return new ResponseEntity<ResponseError>(new ResponseError(HttpStatus.BAD_REQUEST.value(),"Unsuccesfull",user),HttpStatus.BAD_REQUEST);
				
	}

	@GetMapping("/varify/{token}")
	public String getTocken(@PathVariable(name="token") String token) {
		userService.parseToken(token);
		return "user is varified";
	}
	
	@PostMapping("/resetpassword/{token}")
	public ResponseEntity<ResponseError> resetPassword(@PathVariable String token,@RequestBody User user) {
		if(userService.resetPassword(token, user.getPassword())) {
			return new ResponseEntity<ResponseError>(new ResponseError(HttpStatus.OK.value(), "Successfully", user),HttpStatus.OK);
		}
		return new ResponseEntity<ResponseError>(new ResponseError(HttpStatus.BAD_REQUEST.value(),"Unsuccesfull",user),HttpStatus.BAD_REQUEST);

		
	}
	
	@PostMapping("/forgetpassword/")
	public ResponseEntity<ResponseError> forgetPassword(@RequestBody User user) {
		if(userService.forgetPassword(user.getEmailId())){
			return new ResponseEntity<ResponseError>(new ResponseError(HttpStatus.OK.value(),"Unsuccesfull",user),HttpStatus.OK);
		}
		return new ResponseEntity<ResponseError>(new ResponseError(HttpStatus.BAD_REQUEST.value(),"Unsuccesfull",user),HttpStatus.BAD_REQUEST);

		
	}

}
