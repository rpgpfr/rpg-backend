package com.rpgproject.infrastructure.repository;

import com.rpgproject.domain.entity.User;
import com.rpgproject.domain.exception.CannotRegisterUserException;
import com.rpgproject.domain.exception.UserNotFoundException;
import com.rpgproject.domain.port.UserRepository;
import com.rpgproject.infrastructure.dao.UserJdbcDao;
import com.rpgproject.infrastructure.dto.UserDTO;
import org.springframework.stereotype.Repository;

@Repository
public class UserJdbcRepository implements UserRepository {

	private final UserJdbcDao userJdbcDao;

	public UserJdbcRepository(UserJdbcDao userJdbcDao) {
		this.userJdbcDao = userJdbcDao;
	}

	@Override
	public User getUserByUniqueName(String uniqueName) {
		try {
			UserDTO userDTO = userJdbcDao.getUserById(uniqueName);

			return mapToUser(userDTO);
		} catch (RuntimeException e) {
			throw new UserNotFoundException();
		}
	}

	private User mapToUser(UserDTO userDTO) {
		return new User(
			userDTO.getId(),
			userDTO.getUsername(),
			userDTO.getFirstName(),
			userDTO.getLastName(),
			userDTO.getIntroduction(),
			userDTO.getRpgKnowledge()
		);
	}

	@Override
	public void register(User user) {
		try {
			userJdbcDao.register(user.uniqueName(), user.username());
		} catch (RuntimeException e) {
			throw new CannotRegisterUserException();
		}
	}

}
