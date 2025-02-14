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
	@DisplayName("Given a username When getting user by username Then user is returned")
	void givenAUserName_whenGettingUserByUsername_thenUserIsReturned() {
		// Given
		String username = "alvin";

		// When
		UserDTO actualUserDTO = userJdbcDao.getUserByUsername(username);

		// Then
		UserDTO expectedUserDTO = new UserDTO(
			"alvin",
			"alvin@test.com",
			"Alvin",
			"Alvinson",
			null,
			null,
			"password"
		);

		assertThat(actualUserDTO).isEqualTo(expectedUserDTO);
	}

	@Test
	@DisplayName("Given a UserDTO, when user is registered, then nothing happens")
	void givenAUserDTOWithNoIntroductionOrRpgKnowledge_whenUserIsRegistered_thenNothingHappens() {
		// Given
		UserDTO userDTO = CreationTestUtils.createUserDTO(null, null);

		// When
		userJdbcDao.register(userDTO);

		// Then
		UserDTO expectedUserDTO = CreationTestUtils.createUserDTO(null, null);

		assertThat(userJdbcDao.getUserByUsername("username")).isEqualTo(expectedUserDTO);
	}

	@Test
	@DisplayName("Given a UserDTO, when user is registered, then nothing happens")
	void givenAUserDTOWithIntroductionAndRpgKnowledge_whenUserIsRegistered_thenNothingHappens() {
		// Given
		UserDTO userDTO = CreationTestUtils.createUserDTO("introduction", "rpgKnowledge");

		// When
		userJdbcDao.register(userDTO);

		// Then
		UserDTO expectedUserDTO = CreationTestUtils.createUserDTO("introduction", "rpgKnowledge");

		assertThat(userJdbcDao.getUserByUsername("username")).isEqualTo(expectedUserDTO);
	}

	@Test
	@DisplayName("Given a UserDTO When register fails Then RuntimeException is thrown")
	void givenAUserDTO_whenRegisterFails_thenRuntimeExceptionIsThrown() {
		// Given
		UserDTO userDTO = CreationTestUtils.createUserDTO(null, null);
		NamedParameterJdbcTemplate mockJdbcTemplate = mock(NamedParameterJdbcTemplate.class);

		ReflectionTestUtils.setField(userJdbcDao, "jdbcTemplate", mockJdbcTemplate);

		doThrow(new DataIntegrityViolationException("error")).when(mockJdbcTemplate).update(anyString(), anyMap());

		// When & Then
		assertThatCode(() -> userJdbcDao.register(userDTO)).isInstanceOf(RuntimeException.class);
	}

	@SneakyThrows
	private void initTables() {
		jdbcTemplate.update(
			new String(readAllBytes(Paths.get("src/test/resources/initUsers.sql"))),
			new HashMap<>()
		);
	}

}