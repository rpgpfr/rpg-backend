package com.rpgproject.infrastructure.adapter;

import com.rpgproject.domain.bean.User;
import com.rpgproject.domain.exception.CannotCreateUserException;
import com.rpgproject.domain.port.repository.UserRepository;
import com.rpgproject.infrastructure.dao.UserDao;
import com.rpgproject.infrastructure.dto.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserH2Adapter implements UserRepository {

	private final UserDao userDao;

	@Autowired
	public UserH2Adapter(UserDao userDao) {
		this.userDao = userDao;
	}

	@Override
	public List<User> getAllUsers() {
		List<UserDTO> userDTOs = userDao.getAllUsers();

		return mapToUsers(userDTOs);
	}

	@Override
	public User getUserByUsername(String username) {
		UserDTO userDTO = userDao.getUserByUsername(username);

		return mapToUser(userDTO);
	}

	@Override
	public void insertUser(User user) throws CannotCreateUserException {
		UserDTO userDTO = new UserDTO(user);

		try {
			userDao.insertUser(userDTO);
		} catch (DataIntegrityViolationException | NullPointerException e) {
			throw new CannotCreateUserException();
		}
	}

	private List<User> mapToUsers(List<UserDTO> userDTOs) {
		return userDTOs
			.stream()
			.map(this::mapToUser)
			.toList();
	}

	private User mapToUser(UserDTO userDTO) {
		String fullName = userDTO.getFirstName() + " " + userDTO.getLastName();

		return new User(userDTO.getUsername(), fullName);
	}

}
