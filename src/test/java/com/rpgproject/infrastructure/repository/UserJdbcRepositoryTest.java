package com.rpgproject.infrastructure.repository;

import com.rpgproject.domain.entity.User;
import com.rpgproject.domain.exception.CannotRegisterUserException;
import com.rpgproject.domain.exception.UserLoginFailedException;
import com.rpgproject.domain.exception.UserNotFoundException;
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

import static com.rpgproject.utils.CreationTestUtils.createUser;
import static com.rpgproject.utils.CreationTestUtils.createUserDTO;
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
	@DisplayName("Given a username, when user does not exist, then user an error is thrown")
	void givenAUsername_whenUserDoesNotExist_thenUserIsReturned() {
		// Given
		String username = "usernaaaame";

		when(jdbcTemplate.queryForObject(anyString(), anyMap(), any(BeanPropertyRowMapper.class))).thenThrow(new EmptyResultDataAccessException(1));

		// When
		assertThatCode(() -> userJdbcRepository.getUserByIdentifier(username)).isInstanceOf(UserNotFoundException.class);
	}

	@Test
	@DisplayName("Given a user, when user is registered, then nothing happens")
	void givenAUser_whenUserIsRegistered_thenNothingHappens() {
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
		assertThatCode(() -> userJdbcRepository.register(user)).isInstanceOf(CannotRegisterUserException.class);
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

}