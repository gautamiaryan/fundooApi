package com.bridgelabz.fundoo.controller;

import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import org.springframework.web.bind.annotation.RestController;

import com.bridgelabz.fundoo.dto.LoginDTO;
import com.bridgelabz.fundoo.dto.RegistrationDTO;
import com.bridgelabz.fundoo.exception.Response;
import com.bridgelabz.fundoo.model.User;
import com.bridgelabz.fundoo.service.IUserService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@PropertySource("classpath:messages.properties")
@CrossOrigin(allowedHeaders = "*", origins = "*", exposedHeaders = { "jwtToken" })
@Api(value = "Fundoo Notes")
public class UserController {

    @Autowired
    private IUserService userService;

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @PostMapping("user/register")
    @Cacheable(value = "list1")
    public ResponseEntity<Response> register(@Valid @RequestBody RegistrationDTO user) {

	logger.debug("Register API called with email={}", user.getEmailId());

	RegistrationDTO response = userService.registerUser(user);

	logger.info("User registration successful for email={}", user.getEmailId());

	return ResponseEntity.ok(new Response(HttpStatus.OK.value(), "Successfully", response));
    }

    @PostMapping("user/login")
    public ResponseEntity<Response> login(@Valid @RequestBody LoginDTO loginDTO) {

	logger.debug("Login API called for email={}", loginDTO.getEmailId());

	String token = userService.login(loginDTO);

	logger.info("Login successful for email={}", loginDTO.getEmailId());

	return ResponseEntity.ok(new Response(HttpStatus.OK.value(), "Login successful", token));
    }

    @GetMapping("user/varify/{token}")
    public ResponseEntity<Response> getToken(@PathVariable(name = "token") String token) {
	userService.parseToken(token);
	return ResponseEntity.ok(new Response(HttpStatus.OK.value(), "User is verified", token));
    }

    @PostMapping("user/resetpassword/{token}")
    public ResponseEntity<Response> resetPassword(@PathVariable(name = "token") String token, @RequestBody User user) {
	if (userService.resetPassword(token, user.getPassword())) {
	    return ResponseEntity.ok(new Response(HttpStatus.OK.value(), "Password reset successful", null));
	}
	return ResponseEntity.status(HttpStatus.BAD_REQUEST)
		.body(new Response(HttpStatus.BAD_REQUEST.value(), "Password reset failed", null));
    }

    @PostMapping("user/forgetpassword")
    public ResponseEntity<Response> forgetPassword(@RequestBody User user) {
	if (userService.forgetPassword(user.getEmailId())) {
	    return ResponseEntity.ok(new Response(HttpStatus.OK.value(), "Password reset email sent", null));
	}
	return ResponseEntity.status(HttpStatus.BAD_REQUEST)
		.body(new Response(HttpStatus.BAD_REQUEST.value(), "Failed to send password reset email", null));
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<Response> getUserById(@PathVariable Long id) {
	User user = userService.getUserById(id);
	if (user != null) {
	    return ResponseEntity.ok(new Response(HttpStatus.OK.value(), "User found", user));
	}
	return ResponseEntity.status(HttpStatus.BAD_REQUEST)
		.body(new Response(HttpStatus.BAD_REQUEST.value(), "User not found", null));
    }

    @ApiOperation(value = "View a list of available users", response = User.class)
    @GetMapping("/users")
    public ResponseEntity<Response> getAllUsers() {
	List<User> userList = userService.getAllUser();
	if (userList.isEmpty()) {
	    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
		    .body(new Response(HttpStatus.BAD_REQUEST.value(), "No users found", userList));
	}
	return ResponseEntity.ok(new Response(HttpStatus.OK.value(), "Users found", userList));
    }

    @GetMapping("users/verify/{token}")
    public ResponseEntity<Response> getUserId(@PathVariable String token) {
	Long userId = userService.getUserId(token);
	return ResponseEntity.ok(new Response(HttpStatus.OK.value(), "User ID retrieved", userId));
    }
}
