package com.rpgproject.infrastructure.dao;

import com.rpgproject.infrastructure.dto.UserDTO;
import com.rpgproject.utils.BasicDatabaseExtension;
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

import static com.rpgproject.utils.CreationTestUtils.createUserDTO;
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
		String username = "alvin";

		// When
		UserDTO actualUserDTO = userJdbcDao.getUserByIdentifier(username);

		// Then
		UserDTO expectedUserDTO = new UserDTO(
			"alvin",
			"mail@example.com",
			"Alvin",
			"Hamaide",
			"password",
			null,
			null
		);

		assertThat(actualUserDTO).isEqualTo(expectedUserDTO);
	}

	@Test
	@DisplayName("Given an email, when user exists, then user is returned")
	void givenAnEmail_whenUserExists_thenUserIsReturned() {
		// Given
		String email = "mail@example.com";

		// When
		UserDTO actualUserDTO = userJdbcDao.getUserByIdentifier(email);

		// Then
		UserDTO expectedUserDTO = new UserDTO(
			"alvin",
			"mail@example.com",
			"Alvin",
			"Hamaide",
			"password",
			null,
			null
		);

		assertThat(actualUserDTO).isEqualTo(expectedUserDTO);
	}

	@Test
	@DisplayName("Given a username, when user does not exist then an error is thrown")
	void givenAUsername_whenGettingUserByUsername_thenUserIsReturned() {
		// Given
		String username = "usernaaaaame";

		// When & Then
		assertThatCode(() -> userJdbcDao.getUserByIdentifier(username)).isInstanceOf(RuntimeException.class);
	}

	@Test
	@DisplayName("Given a UserDTO, when registering, then user is saved")
	void givenAUserDTO_whenRegistering_thenUserIsSaved() {
		// Given
		UserDTO userDTO = createUserDTO("firstName", "lastName", null, null, null);

		// When
		userJdbcDao.register(userDTO);

		// Then
		UserDTO expectedUserDTO = createUserDTO("firstName", "lastName", null, null, null);

		assertThat(userJdbcDao.getUserByIdentifier("username")).isEqualTo(expectedUserDTO);
	}

	@Test
	@DisplayName("Given a UserDTO with a password, when registering, then user is saved")
	void givenAUserDTOWithAPassword_whenRegistering_thenUserIsSaved() {
		// Given
		UserDTO userDTO = createUserDTO("firstName", "lastName", "password", null, null);

		// When
		userJdbcDao.register(userDTO);

		// Then
		UserDTO expectedUserDTO = createUserDTO("firstName", "lastName", "password", null, null);

		assertThat(userJdbcDao.getUserByIdentifier("username")).isEqualTo(expectedUserDTO);
	}

	@Test
	@DisplayName("Given a UserDTO, when register fails, then RuntimeException is thrown")
	void givenAUserDTO_whenRegisterFails_thenRuntimeExceptionIsThrown() {
		// Given
		UserDTO userDTO = createUserDTO("firstName", "lastName", null, null, null);
		NamedParameterJdbcTemplate mockJdbcTemplate = mock(NamedParameterJdbcTemplate.class);

		ReflectionTestUtils.setField(userJdbcDao, "jdbcTemplate", mockJdbcTemplate);

		doThrow(new DataIntegrityViolationException("error")).when(mockJdbcTemplate).update(anyString(), anyMap());

		// When & Then
		assertThatCode(() -> userJdbcDao.register(userDTO)).isInstanceOf(RuntimeException.class);
	}

	@Test
	@DisplayName("Given a UserDTO, when updating, then updates are saved")
	void givenAUserDTO_whenUpdating_thenUpdatesAreSaved() {
		// Given
		UserDTO userDTO = new UserDTO("alvin", "mail@example.com", "goulou", "Hamaide", "password", null, null);

		// When
		userJdbcDao.update(userDTO);

		// Then
		UserDTO expectedUserDTO = new UserDTO("alvin", "mail@example.com", "goulou", "Hamaide", "password", null, null);

		assertThat(userJdbcDao.getUserByIdentifier("alvin")).isEqualTo(expectedUserDTO);
	}

	@Test
	@DisplayName("Given a UserDTO with description and rpgKnowledge, when updating, then updates are saved")
	void givenAUserDTOWithDescriptionAndRpgKnowledge_whenUpdating_thenUpdatesAreSaved() {
		// Given
		UserDTO userDTO = new UserDTO("alvin", "mail@example.com", "goulou", "Hamaide", "password", "description", "knowledge");

		// When
		userJdbcDao.update(userDTO);

		// Then
		UserDTO expectedUserDTO = new UserDTO("alvin", "mail@example.com", "goulou", "Hamaide", "password", "description", "knowledge");

		assertThat(userJdbcDao.getUserByIdentifier("alvin")).isEqualTo(expectedUserDTO);
	}

	@Test
	@DisplayName("Given a UserDTO, when update fails, then RuntimeException is thrown")
	void givenAUserDTO_whenUpdateFails_thenRuntimeExceptionIsThrown() {
		// Given
		UserDTO userDTO = createUserDTO("goulou", "lastName", null, null, null);
		NamedParameterJdbcTemplate mockJdbcTemplate = mock(NamedParameterJdbcTemplate.class);

		ReflectionTestUtils.setField(userJdbcDao, "jdbcTemplate", mockJdbcTemplate);

		doThrow(new DataIntegrityViolationException("error")).when(mockJdbcTemplate).update(anyString(), anyMap());

		// When & Then
		assertThatCode(() -> userJdbcDao.update(userDTO)).isInstanceOf(RuntimeException.class);
	}

	@SneakyThrows
	private void initTables() {
		jdbcTemplate.update(
			new String(readAllBytes(Paths.get("src/test/resources/initUsers.sql"))),
			new HashMap<>()
		);
	}

}