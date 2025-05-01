package com.rpgproject.infrastructure.repository;

import com.rpgproject.domain.entity.User;
import com.rpgproject.domain.exception.*;
import com.rpgproject.infrastructure.dao.UserJdbcDao;
import com.rpgproject.infrastructure.exception.userjdbc.DuplicateUserCredentialsException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static com.rpgproject.domain.EntityCreationTestUtils.createUser;
import static com.rpgproject.infrastructure.DTOCreationTestUtils.createUserDTO;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
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
	@DisplayName("Given a username, when user is not found, then an exception is thrown")
	void givenAUsername_whenUserIsNotFound_thenAnExceptionIsThrown() {
		// Given
		String username = "usernaaaame";

		when(jdbcTemplate.queryForObject(anyString(), anyMap(), any(BeanPropertyRowMapper.class))).thenThrow(new EmptyResultDataAccessException(1));

		// When
		assertThatCode(() -> userJdbcRepository.getUserByIdentifier(username)).isInstanceOf(NotFoundException.class);
	}

	@Test
	@DisplayName("Given a user, when it does not exist, then it is registered")
	void givenAUser_whenItDoesNotExist_thenItIsRegistered() {
		// Given
		User user = createUser();

		when(jdbcTemplate.update(anyString(), anyMap())).thenReturn(1);

		// When & Then
		assertThatCode(() -> userJdbcRepository.register(user)).doesNotThrowAnyException();
	}

	@Test
	@DisplayName("Given a user, when register fails because it already exists, then an exception is thrown")
	void givenAUser_whenRegisterFailsBecauseItAlreadyExists_thenAnExceptionIsThrown() {
		// Given
		User user = createUser();

		doThrow(new DuplicateUserCredentialsException()).when(jdbcTemplate).update(anyString(), anyMap());

		// When & Then
		assertThatCode(() -> userJdbcRepository.register(user)).isInstanceOf(DuplicateException.class);
	}

	@Test
	@DisplayName("Given a user, when register fails, then an exception is thrown")
	void givenAUser_whenRegisterFails_thenAnExceptionIsThrown() {
		// Given
		User user = createUser();

		doThrow(new RuntimeException()).when(jdbcTemplate).update(anyString(), anyMap());

		// When & Then
		assertThatCode(() -> userJdbcRepository.register(user)).isInstanceOf(InternalException.class);
	}

	@Test
	@DisplayName("Given an identifier and a password, when login is successful, then user is returned")
	void givenAnIdentifierAndAPassword_whenLoginIsSuccessful_thenUserIsReturned() {
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
	@DisplayName("Given an identifier and a password, when login fails because password does not match, then an exception is thrown")
	void givenAnIdentifierAndAPassword_whenLoginFailsBecausePasswordDoesNotMatch_thenAnExceptionIsThrown() {
		// Given
		String username = "username";
		String password = "password";

		when(jdbcTemplate.queryForObject(anyString(), anyMap(), any(BeanPropertyRowMapper.class))).thenReturn(createUserDTO("firstName", "lastName", "password", null, null));
		when(bCryptPasswordEncoder.matches(anyString(), anyString())).thenReturn(false);

		// When & Then
		assertThatCode(() -> userJdbcRepository.logIn(username, password)).isInstanceOf(InvalidCredentialsException.class);
	}

	@Test
	@DisplayName("Given an identifier and a password, when login fails because user is not found, then an exception is thrown")
	void givenAnIdentifierAndAPassword_whenLoginFailsBecauseUserIsNotFoundOnLogin_ThenAnExceptionIsThrown() {
		// Given
		String username = "username";
		String password = "password";

		when(jdbcTemplate.queryForObject(anyString(), anyMap(), any(BeanPropertyRowMapper.class))).thenThrow(new EmptyResultDataAccessException(1));

		// When & Then
		assertThatCode(() -> userJdbcRepository.logIn(username, password)).isInstanceOf(InvalidCredentialsException.class);
	}

	@Test
	@DisplayName("Given a user, when it exists, then it is updated")
	void givenAUser_whenItExists_thenItIsUpdated() {
		// Given
		User user = new User("alvin", "mail@example.com", "goulou", "lastName", null);

		when(jdbcTemplate.update(anyString(), anyMap())).thenReturn(1);

		// When & Then
		assertThatCode(() -> userJdbcRepository.update(user)).doesNotThrowAnyException();
	}

	@Test
	@DisplayName("Given a user, when update fails because it already exists, then an exception is thrown")
	void givenAUser_whenUpdateFailsBecauseItAlreadyExists_thenAnExceptionIsThrown() {
		// Given
		User user = new User("alvin", "mail@example.com", "goulou", "lastName", null);

		doThrow(new DuplicateUserCredentialsException()).when(jdbcTemplate).update(anyString(), anyMap());

		// When & Then
		assertThatCode(() -> userJdbcRepository.update(user)).isInstanceOf(DuplicateException.class);
	}

	@Test
	@DisplayName("Given a user, when update fails, then an exception is thrown")
	void givenAUser_whenUpdateFails_thenAnExceptionIsThrown() {
		// Given
		User user = new User("alvin", "mail@example.com", "goulou", "lastName", null);

		doThrow(new RuntimeException("exception")).when(jdbcTemplate).update(anyString(), anyMap());

		// When & Then
		assertThatCode(() -> userJdbcRepository.update(user)).isInstanceOf(InternalException.class);
	}

}