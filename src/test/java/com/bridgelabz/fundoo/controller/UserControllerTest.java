package com.bridgelabz.fundoo.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.Collections;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.bridgelabz.fundoo.dto.LoginDTO;
import com.bridgelabz.fundoo.dto.RegistrationDTO;
import com.bridgelabz.fundoo.model.User;
import com.bridgelabz.fundoo.service.IUserService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false ) // disable security filters
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IUserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testRegisterUser() throws Exception {
	RegistrationDTO request = new RegistrationDTO("Gautam", "Singh", "test@example.com", "password", "7254060097");
	when(userService.registerUser(any(RegistrationDTO.class))).thenReturn(request);

	mockMvc.perform(post("/api/user/register").contentType(MediaType.APPLICATION_JSON)
		.content(objectMapper.writeValueAsString(request))).andExpect(status().isOk())
		.andExpect(jsonPath("$.message").value("Successfully"))
		.andExpect(jsonPath("$.data.emailId").value("test@example.com"));
    }

    @Test
    void testLoginUser() throws Exception {
	LoginDTO loginDTO = new LoginDTO("test@example.com", "password");
	when(userService.login(any(LoginDTO.class))).thenReturn("fake-jwt-token");

	mockMvc.perform(post("/api/user/login").contentType(MediaType.APPLICATION_JSON)
		.content(objectMapper.writeValueAsString(loginDTO))).andExpect(status().isOk())
		.andExpect(jsonPath("$.message").value("Login successful"))
		.andExpect(jsonPath("$.data").value("fake-jwt-token"));
    }

    @Test
    void testVerifyUserToken() throws Exception {
	String token = "dummy-token";

	mockMvc.perform(get("/api/user/verify/{token}", token)).andExpect(status().isOk())
		.andExpect(jsonPath("$.message").value("User is verified"));
    }

    @Test
    void testResetPasswordSuccess() throws Exception {
	String token = "dummy-token";
	User user = new User();
	user.setPassword("newpassword");

	when(userService.resetPassword(eq(token), eq(user.getPassword()))).thenReturn(true);

	mockMvc.perform(post("/api/user/resetpassword/{token}", token).contentType(MediaType.APPLICATION_JSON)
		.content(objectMapper.writeValueAsString(user))).andExpect(status().isOk())
		.andExpect(jsonPath("$.message").value("Password reset successful"));
    }

    @Test
    void testResetPasswordFailure() throws Exception {
	String token = "dummy-token";
	User user = new User();
	user.setPassword("newpassword");

	when(userService.resetPassword(eq(token), eq(user.getPassword()))).thenReturn(false);

	mockMvc.perform(post("/api/user/resetpassword/{token}", token).contentType(MediaType.APPLICATION_JSON)
		.content(objectMapper.writeValueAsString(user))).andExpect(status().isBadRequest())
		.andExpect(jsonPath("$.message").value("Password reset failed"));
    }

    @Test
    void testForgetPasswordSuccess() throws Exception {
	User user = new User();
	user.setEmailId("test@example.com");

	when(userService.forgetPassword(eq(user.getEmailId()))).thenReturn(true);

	mockMvc.perform(post("/api/user/forgetpassword").contentType(MediaType.APPLICATION_JSON)
		.content(objectMapper.writeValueAsString(user))).andExpect(status().isOk())
		.andExpect(jsonPath("$.message").value("Password reset email sent"));
    }

    @Test
    void testForgetPasswordFailure() throws Exception {
	User user = new User();
	user.setEmailId("test@example.com");

	when(userService.forgetPassword(eq(user.getEmailId()))).thenReturn(false);

	mockMvc.perform(post("/api/user/forgetpassword").contentType(MediaType.APPLICATION_JSON)
		.content(objectMapper.writeValueAsString(user))).andExpect(status().isBadRequest())
		.andExpect(jsonPath("$.message").value("Failed to send password reset email"));
    }

    @Test
    void testGetUserByIdFound() throws Exception {
	User user = new User();
	user.setUserId(1L);
	user.setEmailId("test@example.com");

	when(userService.getUserById(1L)).thenReturn(user);

	mockMvc.perform(get("/api/user/{id}", 1L)).andExpect(status().isOk())
		.andExpect(jsonPath("$.message").value("User found"))
		.andExpect(jsonPath("$.data.emailId").value("test@example.com"));
    }

    @Test
    void testGetUserByIdNotFound() throws Exception {
        when(userService.getUserById(1L)).thenReturn(null);

        mockMvc.perform(get("/api/user/{id}", 1L))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("User not found"));
    }

    @Test
    void testGetAllUsersFound() throws Exception {
	User user = new User();
	user.setUserId(1L);
	user.setEmailId("test@example.com");

	when(userService.getAllUser()).thenReturn(Arrays.asList(user));

	mockMvc.perform(get("/api/users")).andExpect(status().isOk())
		.andExpect(jsonPath("$.message").value("Users found"))
		.andExpect(jsonPath("$.data[0].emailId").value("test@example.com"));
    }

    @Test
    void testGetAllUsersEmpty() throws Exception {
        when(userService.getAllUser()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("No users found"));
    }

    @Test
    void testGetUserIdFromToken() throws Exception {
	String token = "dummy-token";
	when(userService.getUserId(token)).thenReturn(100L);

	mockMvc.perform(get("/api/user/verify/{token}", token)).andExpect(status().isOk())
		.andExpect(jsonPath("$.message").value("User ID retrieved")).andExpect(jsonPath("$.data").value(100));
    }
}
