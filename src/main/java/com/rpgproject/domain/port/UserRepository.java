package com.rpgproject.domain.port;

import com.rpgproject.domain.bean.User;
import com.rpgproject.domain.exception.CannotCreateUserException;

import java.util.List;

public interface UserRepository {

	List<User> getAllUsers();

	User getUserByUsername(String username);

	void insertUser(User user) throws CannotCreateUserException;

}
