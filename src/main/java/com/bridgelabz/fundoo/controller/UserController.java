package com.bridgelabz.fundoo.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
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
import com.bridgelabz.fundoo.service.IUserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;

@RestController
@PropertySource("classpath:messages.properties")
@CrossOrigin(allowedHeaders = "*", origins = "*", exposedHeaders = { "jwtToken" })
@RequestMapping("/api")
public class UserController {

    @Autowired
    private IUserService userService;

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Operation(summary = "Register a new user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User registered successfully", 
                content = @Content(schema = @Schema(implementation = RegistrationDTO.class))),
            @ApiResponse(responseCode = "400", description = "Validation error", content = @Content)
    })
    @PostMapping("/user/register")
    public ResponseEntity<Response> register(
            @Valid @RequestBody RegistrationDTO user) {

        logger.debug("Register API called with email={}", user.getEmailId());

        RegistrationDTO response = userService.registerUser(user);

        logger.info("User registration successful for email={}", user.getEmailId());

        return ResponseEntity.ok(new Response(HttpStatus.OK.value(), "Successfully", response));
    }

    @Operation(summary = "Login a user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login successful", content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "400", description = "Login failed", content = @Content)
    })
    @PostMapping("/user/login")
    public ResponseEntity<Response> login(@Valid @RequestBody LoginDTO loginDTO) {

        logger.debug("Login API called for email={}", loginDTO.getEmailId());

        String token = userService.login(loginDTO);

        logger.info("Login successful for email={}", loginDTO.getEmailId());

        return ResponseEntity.ok(new Response(HttpStatus.OK.value(), "Login successful", token));
    }

    @Operation(summary = "Reset user password")
    @PostMapping("/user/resetpassword/{token}")
    public ResponseEntity<Response> resetPassword(@PathVariable String token, @RequestBody User user) {
        if (userService.resetPassword(token, user.getPassword())) {
            return ResponseEntity.ok(new Response(HttpStatus.OK.value(), "Password reset successful", null));
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new Response(HttpStatus.BAD_REQUEST.value(), "Password reset failed", null));
    }

    @Operation(summary = "Send password reset email")
    @PostMapping("/user/forgetpassword")
    public ResponseEntity<Response> forgetPassword(@RequestBody User user) {
        if (userService.forgetPassword(user.getEmailId())) {
            return ResponseEntity.ok(new Response(HttpStatus.OK.value(), "Password reset email sent", null));
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new Response(HttpStatus.BAD_REQUEST.value(), "Failed to send password reset email", null));
    }

    @Operation(summary = "Get a user by ID")
    @GetMapping("/user/{id}")
    public ResponseEntity<Response> getUserById(@PathVariable Long id) {
        User user = userService.getUserById(id);
        if (user != null) {
            return ResponseEntity.ok(new Response(HttpStatus.OK.value(), "User found", user));
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new Response(HttpStatus.BAD_REQUEST.value(), "User not found", null));
    }

    @Operation(summary = "Get all users")
    @GetMapping("/users")
    public ResponseEntity<Response> getAllUsers() {
        List<User> userList = userService.getAllUser();
        if (userList.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new Response(HttpStatus.BAD_REQUEST.value(), "No users found", userList));
        }
        return ResponseEntity.ok(new Response(HttpStatus.OK.value(), "Users found", userList));
    }

    @Operation(summary = "Get user ID from token")
    @GetMapping("/user/verify/{token}")
    public ResponseEntity<Response> getUserId(@PathVariable String token) {
        Long userId = userService.getUserId(token);
        return ResponseEntity.ok(new Response(HttpStatus.OK.value(), "User ID retrieved", userId));
    }
}
