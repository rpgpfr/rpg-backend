package com.rpgproject.infrastructure.repository;

import com.rpgproject.domain.entity.User;
import com.rpgproject.domain.exception.user.UserLoginFailedException;
import com.rpgproject.domain.exception.user.UserNotFoundException;
import com.rpgproject.domain.exception.user.UserRegistrationFailedException;
import com.rpgproject.domain.exception.user.UserUpdateFailedException;
import com.rpgproject.infrastructure.dao.UserJdbcDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static com.rpgproject.domain.EntityCreationTestUtils.createUser;
import static com.rpgproject.infrastructure.DTOCreationTestUtils.createUserDTO;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserJdbcRepositoryTest {

	private UserJdbcRepository userJdbcRepository;

	@Mock
	private NamedParameterJdbcTemplate jdbcTemplate;

	@Mock
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@BeforeEach
	public void setUp() {
		UserJdbcDao userJdbcDao = new UserJdbcDao(jdbcTemplate);
		userJdbcRepository = new UserJdbcRepository(userJdbcDao, bCryptPasswordEncoder);
	}

	@Test
	@DisplayName("Given a username, when user exists, then user is returned")
	void givenAUsername_whenUserExists_thenUserIsReturned() {
		// Given
		String username = "username";

		when(jdbcTemplate.queryForObject(anyString(), anyMap(), any(BeanPropertyRowMapper.class))).thenReturn(createUserDTO("firstName", "lastName", "password", null, null));

		// When
		User actualUser = userJdbcRepository.getUserByIdentifier(username);

		// Then
		User expectedUser = createUser();

		assertThat(actualUser).isEqualTo(expectedUser);
	}

	@Test
	@DisplayName("Given a username, when user does not exist, then an error is thrown")
	void givenAUsername_whenUserDoesNotExist_thenAnErrorIsThrown() {
		// Given
		String username = "usernaaaame";

		when(jdbcTemplate.queryForObject(anyString(), anyMap(), any(BeanPropertyRowMapper.class))).thenThrow(new EmptyResultDataAccessException(1));

		// When
		assertThatCode(() -> userJdbcRepository.getUserByIdentifier(username)).isInstanceOf(UserNotFoundException.class);
	}

	@Test
	@DisplayName("Given a user, when registering, then user is saved")
	void givenAUser_whenRegistering_thenUserIsSaved() {
		// Given
		User user = createUser();

		when(jdbcTemplate.update(anyString(), anyMap())).thenReturn(1);

		// When & Then
		assertDoesNotThrow(() -> userJdbcRepository.register(user));
	}

	@Test
	@DisplayName("Given a user, when register fails, then exception is thrown")
	void givenAUser_whenRegisterFails_thenExceptionIsThrown() {
		// Given
		User user = createUser();

		doThrow(new DataIntegrityViolationException("error")).when(jdbcTemplate).update(anyString(), anyMap());

		// When & Then
		assertThatCode(() -> userJdbcRepository.register(user)).isInstanceOf(UserRegistrationFailedException.class);
	}

	@Test
	@DisplayName("Given an identifier and a password, when logging is successfull, then user is returned")
	void givenAnIdentifierAndAPassword_whenLoggingIsSuccessfull_thenUserIsReturned() {
		// Given
		String username = "username";
		String password = "password";

		when(jdbcTemplate.queryForObject(anyString(), anyMap(), any(BeanPropertyRowMapper.class))).thenReturn(createUserDTO("firstName", "lastName", "password", null, null));
		when(bCryptPasswordEncoder.matches(anyString(), anyString())).thenReturn(true);

		// When
		User actualUser = userJdbcRepository.logIn(username, password);

		// Then
		User expectedUser = createUser();

		assertThat(actualUser).isEqualTo(expectedUser);
	}

	@Test
	@DisplayName("Given an identifier and a password, when password does not match, then login error is thrown")
	void givenAnIdentifierAndAPassword_whenPasswordDoesNotMatch_thenLoginErrorIsThrown() {
		// Given
		String username = "username";
		String password = "password";

		when(jdbcTemplate.queryForObject(anyString(), anyMap(), any(BeanPropertyRowMapper.class))).thenReturn(createUserDTO("firstName", "lastName", "password", null, null));
		when(bCryptPasswordEncoder.matches(anyString(), anyString())).thenReturn(false);

		// When & Then
		assertThatCode(() -> userJdbcRepository.logIn(username, password)).isInstanceOf(UserLoginFailedException.class);
	}

	@Test
	@DisplayName("Given an identifier and a password, when user is not found on login, then login error is thrown")
	void givenAnIdentifierAndAPassword_whenUserIsNotFoundOnLogin_ThenLoginErrorIsThrown() {
		// Given
		String username = "username";
		String password = "password";

		when(jdbcTemplate.queryForObject(anyString(), anyMap(), any(BeanPropertyRowMapper.class))).thenThrow(new EmptyResultDataAccessException(1));

		// When & Then
		assertThatCode(() -> userJdbcRepository.logIn(username, password)).isInstanceOf(UserLoginFailedException.class);
	}

	@Test
	@DisplayName("Given a user, when updating, then updates are saved")
	void givenAUser_whenUpdating_thenUpdatesAreSaved() {
		// Given
		User user = new User("alvin", "mail@example.com", "goulou", "lastName", null);

		when(jdbcTemplate.update(anyString(), anyMap())).thenReturn(1);

		// When & Then
		assertDoesNotThrow(() -> userJdbcRepository.update(user));
	}

	@Test
	@DisplayName("Given a user, when update fails, then exception is thrown")
	void givenAUser_whenUpdateFails_thenExceptionIsThrown() {
		// Given
		User user = new User("alvin", "mail@example.com", "goulou", "lastName", null);

		doThrow(new DataIntegrityViolationException("error")).when(jdbcTemplate).update(anyString(), anyMap());

		// When & Then
		assertThatCode(() -> userJdbcRepository.update(user)).isInstanceOf(UserUpdateFailedException.class);
	}

}