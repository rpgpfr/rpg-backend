package com.rpgproject.infrastructure.repository;

import com.rpgproject.domain.bean.User;
import com.rpgproject.domain.exception.CannotRegisterUserException;
import com.rpgproject.domain.port.UserRepository;
import com.rpgproject.infrastructure.dao.UserJdbcDao;
import com.rpgproject.infrastructure.dto.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class UserJdbcRepository implements UserRepository {

	private final UserJdbcDao userJdbcDao;

	@Autowired
	public UserJdbcRepository(UserJdbcDao userJdbcDao) {
		this.userJdbcDao = userJdbcDao;
	}

	@Override
	public void register(User user) {
		UserDTO userDTO = new UserDTO(user);

		try {
			userJdbcDao.register(userDTO);
		} catch (RuntimeException e) {
			throw new CannotRegisterUserException();
		}
	}

}
