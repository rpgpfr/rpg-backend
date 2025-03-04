package com.rpgproject.infrastructure.repository;

import com.rpgproject.domain.entity.User;
import com.rpgproject.domain.exception.CannotRegisterUserException;
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

	@BeforeEach
	public void setUp() {
		UserJdbcDao userJdbcDao = new UserJdbcDao(jdbcTemplate);
		userJdbcRepository = new UserJdbcRepository(userJdbcDao);
	}

	@Test
	@DisplayName("Given a uniqueName, when user exists, then user is returned")
	void givenAUniqueName_whenUserExists_thenUserIsReturned() {
		// Given
		String uniqueName = "ID123";

		when(jdbcTemplate.queryForObject(anyString(), anyMap(), any(BeanPropertyRowMapper.class))).thenReturn(createUserDTO(null, null, null, null));

		// When
		User actualUser = userJdbcRepository.getUserByUniqueName(uniqueName);

		// Then
		User expectedUser = createUser();

		assertThat(actualUser).isEqualTo(expectedUser);
	}

	@Test
	@DisplayName("Given a uniqueName, when user does not exist, then user an error is thrown")
	void givenAUniqueName_whenUserDoesNotExist_thenUserIsReturned() {
		// Given
		String uniqueName = "ID124";

		doThrow(new EmptyResultDataAccessException(1)).when(jdbcTemplate).queryForObject(anyString(), anyMap(), any(BeanPropertyRowMapper.class));

		// When
		assertThatCode(() -> userJdbcRepository.getUserByUniqueName(uniqueName)).isInstanceOf(UserNotFoundException.class);
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

}