package com.rpgproject.infrastructure.repository;

import com.rpgproject.domain.entity.User;
import com.rpgproject.domain.exception.CannotRegisterUserException;
import com.rpgproject.domain.exception.UserLoginFailedException;
import com.rpgproject.domain.exception.UserNotFoundException;
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
			throw new CannotRegisterUserException("Le nom d'utilisateur ou le mail associé est déjà utilisé.");
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
			user.rpgKnowledge()
		);
	}

}
