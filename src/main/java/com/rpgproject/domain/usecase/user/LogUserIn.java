package com.rpgproject.domain.usecase.user;

import com.rpgproject.domain.entity.User;
import com.rpgproject.domain.exception.UserLoginFailedException;
import com.rpgproject.domain.port.Presenter;
import com.rpgproject.domain.port.UserRepository;

public class LogUserIn<T> {

	private final UserRepository userRepository;
	private final Presenter<User, T> presenter;

	public LogUserIn(UserRepository userRepository, Presenter<User, T> presenter) {
		this.userRepository = userRepository;
		this.presenter = presenter;
	}

	public T execute(String identifier, String password) {
		try {
			User loggedUser = userRepository.logIn(identifier, password);
			return presenter.ok(loggedUser);
		} catch (UserLoginFailedException e) {
			return presenter.error(e);
		}
	}

}
