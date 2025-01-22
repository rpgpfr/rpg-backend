package com.rpgproject.infrastructure.adapter;

import com.rpgproject.domain.bean.User;
import com.rpgproject.domain.exception.CannotCreateUserException;
import com.rpgproject.domain.port.UserRepository;

import java.util.List;

import static java.util.Collections.emptyList;

public class UserH2Adapter implements UserRepository {
	@Override
	public List<User> getAllUsers() {
		return emptyList();
	}

	@Override
	public User getUserByUsername(String username) {
		return null;
	}

	@Override
	public void insertUser(User user) throws CannotCreateUserException {

	}
}
