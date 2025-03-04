package com.rpgproject.infrastructure.dao;

import com.rpgproject.infrastructure.dto.UserDTO;
import com.rpgproject.utils.BasicDatabaseExtension;
import com.rpgproject.utils.CreationTestUtils;
import com.rpgproject.utils.EzDatabase;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.util.ReflectionTestUtils;

import java.nio.file.Paths;
import java.util.HashMap;

import static java.nio.file.Files.readAllBytes;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;

@ExtendWith(BasicDatabaseExtension.class)
class UserJdbcDaoTest {

	private UserJdbcDao userJdbcDao;

	@EzDatabase
	private NamedParameterJdbcTemplate jdbcTemplate;

	@BeforeEach
	public void setUp() {
		userJdbcDao = new UserJdbcDao(jdbcTemplate);

		initTables();
	}

	@Test
	@DisplayName("Given a username, when user exists, then user is returned")
	void givenAUserName_whenUserExists_thenUserIsReturned() {
		// Given
		String id = "ID123";

		// When
		UserDTO actualUserDTO = userJdbcDao.getUserById(id);

		// Then
		UserDTO expectedUserDTO = new UserDTO(
			"ID123",
			"alvin",
			"Alvin",
			"Alvinson",
			null,
			null
		);

		assertThat(actualUserDTO).isEqualTo(expectedUserDTO);
	}

	@Test
	@DisplayName("Given a username, when user does not exist then an error is thrown")
	void givenAUserName_whenGettingUserByUsername_thenUserIsReturned() {
		// Given
		String id = "ID124";

		// When & Then
		assertThatCode(() -> userJdbcDao.getUserById(id)).isInstanceOf(RuntimeException.class);
	}

	@Test
	@DisplayName("Given an id and a username, when user is registered, then nothing happens")
	void givenAnIdAndAUsernameWithNoIntroductionOrRpgKnowledge_whenUserIsRegistered_thenNothingHappens() {
		// Given
		String id = "uniqueName";
		String username = "username";

		// When
		userJdbcDao.register(id, username);

		// Then
		UserDTO expectedUserDTO = CreationTestUtils.createUserDTO(null, null, null, null);

		assertThat(userJdbcDao.getUserById("uniqueName")).isEqualTo(expectedUserDTO);
	}

	@Test
	@DisplayName("Given a UserDTO, when register fails, then RuntimeException is thrown")
	void givenAUserDTO_whenRegisterFails_thenRuntimeExceptionIsThrown() {
		// Given
		String id = "id";
		String username = "username";
		NamedParameterJdbcTemplate mockJdbcTemplate = mock(NamedParameterJdbcTemplate.class);

		ReflectionTestUtils.setField(userJdbcDao, "jdbcTemplate", mockJdbcTemplate);

		doThrow(new DataIntegrityViolationException("error")).when(mockJdbcTemplate).update(anyString(), anyMap());

		// When & Then
		assertThatCode(() -> userJdbcDao.register(id, username)).isInstanceOf(RuntimeException.class);
	}

	@SneakyThrows
	private void initTables() {
		jdbcTemplate.update(
			new String(readAllBytes(Paths.get("src/test/resources/initUsers.sql"))),
			new HashMap<>()
		);
	}

}