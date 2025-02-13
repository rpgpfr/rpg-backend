package com.rpgproject.domain.usecase;

import com.rpgproject.domain.bean.User;
import com.rpgproject.domain.port.UserPresenter;
import com.rpgproject.domain.port.UserRepository;

public class RegisterUser<T> {

	private final UserRepository userRepository;
	private final UserPresenter<T> userPresenter;

	public RegisterUser(UserRepository userRepository, UserPresenter<T> userPresenter) {
		this.userRepository = userRepository;
		this.userPresenter = userPresenter;
	}

	public T execute(User user) {
		try {
			userRepository.signUp(user);
			return userPresenter.ok();
		} catch (Exception e) {
			return userPresenter.error(e);
		}
	}
}
