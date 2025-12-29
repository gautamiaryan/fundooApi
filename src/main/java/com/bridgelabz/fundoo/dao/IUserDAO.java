package com.bridgelabz.fundoo.dao;

import java.util.List;

import com.bridgelabz.fundoo.model.User;

public interface IUserDAO {

    User persistUser(User user);

    List<User> getAllUser();

    User getUserById(Long id);

    User getUserByEmail(String emailId);
}
