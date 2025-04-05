package com.rpgproject.infrastructure.repository;

import com.rpgproject.domain.entity.User;
import com.rpgproject.domain.exception.user.UserLoginFailedException;
import com.rpgproject.domain.exception.user.UserNotFoundException;
import com.rpgproject.domain.exception.user.UserRegistrationFailedException;
import com.rpgproject.domain.exception.user.UserUpdateFailedException;
import com.rpgproject.domain.port.UserRepository;
import com.rpgproject.infrastructure.dao.UserJdbcDao;
import com.rpgproject.infrastructure.dto.UserDTO;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;

@Repository
public class UserJdbcRepository implements UserRepository {

	private final UserJdbcDao userJdbcDao;
	private final BCryptPasswordEncoder bCryptPasswordEncoder;

	public UserJdbcRepository(UserJdbcDao userJdbcDao, BCryptPasswordEncoder bCryptPasswordEncoder) {
		this.userJdbcDao = userJdbcDao;
		this.bCryptPasswordEncoder = bCryptPasswordEncoder;
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

	@Override
	public User logIn(String identifier, String password) {
		try {
			UserDTO userDTO = userJdbcDao.getUserByIdentifier(identifier);

			if (bCryptPasswordEncoder.matches(password, userDTO.getPassword())) {
				return mapToUser(userDTO);
			}

			throw new UserLoginFailedException();
		} catch (RuntimeException e) {
			throw new UserLoginFailedException();
		}
	}

	private User mapToUser(UserDTO userDTO) {
		return new User(
			userDTO.getUsername(),
			userDTO.getEmail(),
			userDTO.getFirstName(),
			userDTO.getLastName(),
			userDTO.getPassword(),
			userDTO.getDescription(),
			userDTO.getRpgKnowledge(),
			userDTO.getSignedUpAt()
		);
	}

	@Override
	public void register(User user) {
		try {
			UserDTO userDTO = mapToUserDTO(user);

			userJdbcDao.register(userDTO);
		} catch (RuntimeException e) {
			throw new UserRegistrationFailedException("Le nom d'utilisateur ou le mail associé est déjà utilisé.");
		}
	}

	@Override
	public void update(User user) {
		try {
			UserDTO userDTO = mapToUserDTO(user);

			userJdbcDao.update(userDTO);
		} catch (RuntimeException e) {
			throw new UserUpdateFailedException();
		}
	}

	private UserDTO mapToUserDTO(User user) {
		return new UserDTO(
			user.username(),
			user.email(),
			user.firstName(),
			user.lastName(),
			bCryptPasswordEncoder.encode(user.password()),
			user.description(),
			user.rpgKnowledge(),
			null
		);
	}

}
