package com.rpgproject.domain.usecase;

import com.rpgproject.domain.entity.User;
import com.rpgproject.domain.exception.UserUpdateFailedException;
import com.rpgproject.domain.port.Presenter;
import com.rpgproject.domain.port.UserRepository;

public class UpdateUser<T> {

	private UserRepository userRepository;
	private Presenter<User, T> presenter;

	public UpdateUser(UserRepository userRepository, Presenter<User, T> presenter) {
		this.userRepository = userRepository;
		this.presenter = presenter;
	}

	public T execute(User user) {
		try {
			userRepository.update(user);

			return presenter.ok(user);
		} catch (UserUpdateFailedException e) {
			return presenter.error(e);
		}
	}

}
