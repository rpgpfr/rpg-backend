package com.rpgproject.domain.usecase.user;

import com.rpgproject.domain.entity.User;
import com.rpgproject.domain.exception.DuplicateException;
import com.rpgproject.domain.exception.InternalException;
import com.rpgproject.domain.port.Presenter;
import com.rpgproject.domain.port.UserRepository;

public class RegisterUser<T> {

	private final UserRepository userRepository;
	private final Presenter<User, T> userPresenter;

	public RegisterUser(UserRepository userRepository, Presenter<User, T> presenter) {
		this.userRepository = userRepository;
		this.userPresenter = presenter;
	}

	public T execute(User user) {
		try {
			userRepository.register(user);

			return userPresenter.ok();
		} catch (DuplicateException | InternalException e) {
			return userPresenter.error(e);
		}
	}
}
