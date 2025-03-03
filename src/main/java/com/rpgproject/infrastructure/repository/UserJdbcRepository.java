package com.rpgproject.infrastructure.repository;

import com.rpgproject.domain.entity.User;
import com.rpgproject.domain.entity.UserProfile;
import com.rpgproject.domain.exception.CannotRegisterUserException;
import com.rpgproject.domain.port.UserRepository;
import com.rpgproject.infrastructure.dao.UserJdbcDao;
import org.springframework.stereotype.Repository;

@Repository
public class UserJdbcRepository implements UserRepository {

	private final UserJdbcDao userJdbcDao;

	public UserJdbcRepository(UserJdbcDao userJdbcDao) {
		this.userJdbcDao = userJdbcDao;
	}

	@Override
	public void register(User user) {
		try {
			userJdbcDao.register(user.getUniqueName(), user.getUsername());
		} catch (RuntimeException e) {
			throw new CannotRegisterUserException();
		}
	}

	@Override
	public User getUserByUniqueName(String uniqueName) {
		return null;
	}

}
