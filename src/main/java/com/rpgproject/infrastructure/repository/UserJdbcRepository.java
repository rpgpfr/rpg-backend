package com.rpgproject.infrastructure.repository;

import com.rpgproject.domain.entity.User;
import com.rpgproject.domain.exception.*;
import com.rpgproject.domain.port.UserRepository;
import com.rpgproject.infrastructure.dao.UserJdbcDao;
import com.rpgproject.infrastructure.dto.UserDTO;
import com.rpgproject.infrastructure.exception.userjdbc.DuplicateUserCredentialsException;
import com.rpgproject.infrastructure.exception.userjdbc.UserNotFoundException;
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
		} catch (UserNotFoundException e) {
			throw new NotFoundException(e.getMessage());
		}
	}

	@Override
	public User logIn(String identifier, String password) {
		try {
			UserDTO userDTO = userJdbcDao.getUserByIdentifier(identifier);

			if (bCryptPasswordEncoder.matches(password, userDTO.getPassword())) {
				return mapToUser(userDTO);
			}

			throw new RuntimeException();
		} catch (RuntimeException e) {
			throw new InvalidCredentialsException("Le mot de passe ou l'identifiant est incorrect.");
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
		} catch (DuplicateUserCredentialsException e) {
			throw new DuplicateException(e.getMessage());
		} catch (RuntimeException e) {
			throw new InternalException(e.getMessage());
		}
	}

	@Override
	public void update(User user) {
		try {
			UserDTO userDTO = mapToUserDTO(user);

			userJdbcDao.update(userDTO);
		} catch (DuplicateUserCredentialsException e) {
			throw new DuplicateException(e.getMessage());
		} catch (RuntimeException e) {
			throw new InternalException(e.getMessage());
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
