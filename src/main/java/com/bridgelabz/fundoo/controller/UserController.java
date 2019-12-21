package com.bridgelabz.fundoo.controller;

import java.util.List;

import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import com.bridgelabz.fundoo.dto.LoginDTO;
import com.bridgelabz.fundoo.dto.RegistrationDTO;
import com.bridgelabz.fundoo.exception.Response;
import com.bridgelabz.fundoo.exception.UserExceptions;
import com.bridgelabz.fundoo.model.User;
import com.bridgelabz.fundoo.service.IUserService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@PropertySource("classpath:messages.properties")
@CrossOrigin(allowedHeaders = "*", origins = "*",exposedHeaders = {"jwtToken"})
@Api(value = "Fundoo Notes ")
public class UserController {

	@Autowired
	private IUserService userService;
	
	@PostMapping("users/register")
	@Cacheable(value="list1")
	public ResponseEntity<Response> register(@Valid @RequestBody RegistrationDTO user) {

		if(userService.registerUser(user)) {
			user.setPassword("**********");
			return new ResponseEntity<>(new Response(HttpStatus.OK.value(), "Successfully", user),HttpStatus.OK);
		}
		return new ResponseEntity<>(new Response(HttpStatus.BAD_REQUEST.value(), "Unsuccessfull",user),HttpStatus.BAD_REQUEST);

	}

    @PostMapping("users/login")
	public ResponseEntity<Response> login(@Valid @RequestBody LoginDTO user) {

		String tocken=userService.login(user.getEmailId(),user.getPassword());
		if(tocken!=null) {
			return new ResponseEntity<>(new Response(HttpStatus.OK.value(),tocken, user),HttpStatus.OK);
		}
		return new ResponseEntity<>(new Response(HttpStatus.BAD_REQUEST.value(),tocken,user),HttpStatus.BAD_REQUEST);

	}

	@GetMapping("users/varify/{token}")
	public String getTocken(@PathVariable(name="token") String token) {
		userService.parseToken(token);
		return "user is varified";
	}

	@PostMapping("users/resetpassword/{token}")
	public ResponseEntity<Response> resetPassword(@PathVariable(name="token") String token,@RequestBody User user) {
		if(userService.resetPassword(token, user.getPassword())) {
			
			return new ResponseEntity<>(new Response(HttpStatus.OK.value(), "Successfully", user),HttpStatus.OK);
		}
		return new ResponseEntity<>(new Response(HttpStatus.BAD_REQUEST.value(),"Unsuccesfull",user),HttpStatus.BAD_REQUEST);
	}

	@PostMapping("users/forgetpassword")
	public ResponseEntity<Response> forgetPassword(@RequestBody User user) {
		if(userService.forgetPassword(user.getEmailId())){
			return new ResponseEntity<>(new Response(HttpStatus.OK.value(),"succesfull",user),HttpStatus.OK);
		}
		return new ResponseEntity<>(new Response(HttpStatus.BAD_REQUEST.value(),"Unsuccesfull",user),HttpStatus.BAD_REQUEST);
	}
	@GetMapping("/users/{id}")
	public ResponseEntity<Response> getAllUser(@PathVariable Long id){

		User user = userService.getUserById(id);
		if(user!=null) {
			return new ResponseEntity<>(new Response(HttpStatus.OK.value(),"User found",user),HttpStatus.OK);
		}
		return new ResponseEntity<>(new Response(HttpStatus.BAD_REQUEST.value(),"User not found",user),HttpStatus.BAD_REQUEST);

	}
    
	@ApiOperation(value = "View a list of available User", response =User.class)
	@GetMapping("/users")
	public ResponseEntity<Response> getAllUser(){
		List<User> userList=userService.getAllUser();
		if(userList.isEmpty()) {
			return new ResponseEntity<>(new Response(HttpStatus.BAD_REQUEST.value(),"User not found",userList),HttpStatus.BAD_REQUEST);
		}
	    return new ResponseEntity<>(new Response(HttpStatus.OK.value(),"User found",userList),HttpStatus.OK);
			
	}
    
	
	 @GetMapping("users/verify/{token}")
     public Long getUserId(@PathVariable String token) {
		System.out.println("fgfgfhfgfg");
		return userService.getUserId(token);
		
     }
	


}
