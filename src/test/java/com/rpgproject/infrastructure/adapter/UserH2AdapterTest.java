package com.rpgproject.infrastructure.adapter;

import com.rpgproject.domain.bean.User;
import com.rpgproject.domain.exception.CannotCreateUserException;
import com.rpgproject.infrastructure.dao.UserDao;
import com.rpgproject.infrastructure.dto.UserDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserH2AdapterTest {

	private UserH2Adapter userH2Adapter;

	@Mock
	private NamedParameterJdbcTemplate jdbcTemplate;

	@BeforeEach
	public void setUp() {
		UserDao userDao = new UserDao(jdbcTemplate);
		userH2Adapter = new UserH2Adapter(userDao);
	}

	@Test
	@DisplayName("Should get all users")
	void shouldGetAllUsers() {
		// Arrange
		List<UserDTO> userDTOs = List.of(
			new UserDTO("alvin.h", "Alvin", "Hamaide"),
			new UserDTO("tom.e", "Tom", "Engelibert"),
			new UserDTO("philippe.p", "Philippe", "Plaia")
		);

		when(jdbcTemplate.query(any(String.class), any(RowMapper.class))).thenReturn(userDTOs);

		// Act
		List<User> actualUsers = userH2Adapter.getAllUsers();

		// Assert
		List<User> expectedUsers = List.of(
			new User("alvin.h", "Alvin Hamaide"),
			new User("tom.e", "Tom Engelibert"),
			new User("philippe.p", "Philippe Plaia")
		);

		assertThat(actualUsers).isEqualTo(expectedUsers);
	}

	@Test
	@DisplayName("Should get user by username")
	void shouldGetUserByUsername() {
		// Arrange
		UserDTO userDTO = new UserDTO("alvin.h", "Alvin", "Hamaide");

		when(jdbcTemplate.query(any(String.class), any(Map.class), any(ResultSetExtractor.class))).thenReturn(userDTO);

		// Act
		User actualUser = userH2Adapter.getUserByUsername("alvin.h");

		// Assert
		User expectedUser = new User("alvin.h", "Alvin Hamaide");

		assertThat(actualUser).isEqualTo(expectedUser);
	}

	@Test
	@DisplayName("Given a user When username does not exist Then it gets inserted")
	void givenAUser_whenUsernameDoesNotExist_thenItGetsInserted() {
		// Arrange
		User user = new User("alvin.h", "Alvin Hamaide");

		// Act & Assert
		assertThatCode(() -> userH2Adapter.insertUser(user)).doesNotThrowAnyException();
	}

	@Test
	@DisplayName("Given a user When username exists Then an exception is thrown")
	void givenAUser_whenUsernameExists_thenAnExceptionIsThrown() {
		// Arrange
		User user = new User("alvin.h", "Alvin Hamaide");

		when(jdbcTemplate.update(any(String.class), any(Map.class))).thenThrow(new DataIntegrityViolationException("error"));

		// Act & Assert
		assertThatCode(() -> userH2Adapter.insertUser(user)).isInstanceOf(CannotCreateUserException.class);
	}

}