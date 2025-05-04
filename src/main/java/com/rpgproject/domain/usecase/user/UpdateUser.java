package com.rpgproject.domain.usecase.user;

import com.rpgproject.domain.entity.User;
import com.rpgproject.domain.exception.DuplicateException;
import com.rpgproject.domain.exception.InternalException;
import com.rpgproject.domain.port.Presenter;
import com.rpgproject.domain.port.UserRepository;

public class UpdateUser<T> {

	private final UserRepository userRepository;
	private final Presenter<User, T> presenter;

	public UpdateUser(UserRepository userRepository, Presenter<User, T> presenter) {
		this.userRepository = userRepository;
		this.presenter = presenter;
	}

	public T execute(User user) {
		try {
			userRepository.update(user);

			return presenter.ok(user);
		} catch (DuplicateException | InternalException e) {
			return presenter.error(e);
		}
	}

}
