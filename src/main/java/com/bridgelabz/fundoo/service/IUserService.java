package com.bridgelabz.fundoo.service;

import java.util.List;

import com.bridgelabz.fundoo.dto.LoginDTO;
import com.bridgelabz.fundoo.dto.RegistrationDTO;
import com.bridgelabz.fundoo.model.User;

public interface IUserService {
    RegistrationDTO registerUser(RegistrationDTO user);

    String login(LoginDTO loginDTO);

    void parseToken(String token);

    boolean resetPassword(String emailId, String password);

    boolean forgetPassword(String emailId);

    User getUserById(Long id);

    List<User> getAllUser();

    Long getUserId(String token);

}
