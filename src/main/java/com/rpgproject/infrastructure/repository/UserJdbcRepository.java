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
	public User getUserByIdentifier(String identifier) {
		try {
			UserDTO userDTO = userJdbcDao.getUserByIdentifier(identifier);

			return mapToUser(userDTO);
		} catch (RuntimeException e) {
			throw new UserNotFoundException();
		}
	}

	private User mapToUser(UserDTO userDTO) {
		return new User(
			userDTO.getUsername(),
			userDTO.getEmail(),
			userDTO.getFirstName(),
			userDTO.getLastName(),
			userDTO.getPassword(),
			userDTO.getIntroduction(),
			userDTO.getRpgKnowledge()
		);
	}

	@Override
	public void register(User user) {
		try {
			UserDTO userDTO = mapToUserDTO(user);

			userJdbcDao.register(userDTO);
		} catch (RuntimeException e) {
			throw new CannotRegisterUserException();
		}
	}

	private static UserDTO mapToUserDTO(User user) {
		return new UserDTO(
			user.username(),
			user.email(),
			user.firstName(),
			user.lastName(),
			user.password(),
			user.description(),
			user.rpgKnowledge()
		);
	}

}
