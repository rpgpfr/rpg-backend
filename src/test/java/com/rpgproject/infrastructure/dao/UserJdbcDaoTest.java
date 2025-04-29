package com.rpgproject.infrastructure.dao;

import com.rpgproject.infrastructure.dto.UserDTO;
import com.rpgproject.infrastructure.exception.userjdbc.DuplicateUserCredentialsException;
import com.rpgproject.infrastructure.exception.userjdbc.UserNotFoundException;
import com.rpgproject.utils.BasicDatabaseExtension;
import com.rpgproject.utils.EzDatabase;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.util.ReflectionTestUtils;

import java.nio.file.Paths;
import java.sql.SQLDataException;
import java.time.LocalDate;
import java.util.HashMap;

import static com.rpgproject.infrastructure.DTOCreationTestUtils.createUserDTO;
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
	@DisplayName("Given a username, when user exists, then it is returned")
	void givenAUsername_whenUserExists_thenItIsReturned() {
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
			null,
			LocalDate.of(2025, 1, 1)
		);

		assertThat(actualUserDTO).isEqualTo(expectedUserDTO);
	}

	@Test
	@DisplayName("Given an email, when user exists, then it is returned")
	void givenAnEmail_whenUserExists_thenItIsReturned() {
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
			null,
			LocalDate.of(2025, 1, 1)
		);

		assertThat(actualUserDTO).isEqualTo(expectedUserDTO);
	}

	@Test
	@DisplayName("Given a username, when user is not found, then an exception is thrown")
	void givenAUsername_whenUserIsNotFound_thenAnExceptionIsThrown() {
		// Given
		String username = "usernaaaaame";

		// When & Then
		assertThatCode(() -> userJdbcDao.getUserByIdentifier(username)).isInstanceOf(UserNotFoundException.class);
	}

	@Test
	@DisplayName("Given a UserDTO, when user does not exist, then user is saved")
	void givenAUserDTO_whenUserDoesNotExist_thenUserIsSaved() {
		// Given
		UserDTO userDTO = createUserDTO("firstName", "lastName", null, null, null);

		// When & Then
		assertThatCode(() -> userJdbcDao.register(userDTO)).doesNotThrowAnyException();
	}

	@Test
	@DisplayName("Given a UserDTO with a password, when user does not exist, then user is saved")
	void givenAUserDTOWithAPassword_whenUserDoesNotExist_thenUserIsSaved() {
		// Given
		UserDTO userDTO = createUserDTO("firstName", "lastName", "password", null, null);

		// When & Then
		assertThatCode(() -> userJdbcDao.register(userDTO)).doesNotThrowAnyException();
	}

	@Test
	@DisplayName("Given a UserDTO, when register fails because it already exists, then an exception is thrown")
	void givenAUserDTO_whenRegisterFailsBecauseItAlreadyExist_thenAnExceptionIsThrown() {
		// Given
		UserDTO userDTO = createUserDTO("firstName", "lastName", null, null, null);
		NamedParameterJdbcTemplate mockJdbcTemplate = mock(NamedParameterJdbcTemplate.class);

		ReflectionTestUtils.setField(userJdbcDao, "jdbcTemplate", mockJdbcTemplate);

		doThrow(new DataIntegrityViolationException("already exists")).when(mockJdbcTemplate).update(anyString(), anyMap());

		// When & Then
		assertThatCode(() -> userJdbcDao.register(userDTO)).isInstanceOf(DuplicateUserCredentialsException.class);
	}

	@Test
	@DisplayName("Given a UserDTO, when register fails, then an exception is thrown")
	void givenAUserDTO_whenRegisterFails_thenAnExceptionIsThrown() {
		// Given
		UserDTO userDTO = createUserDTO("firstName", "lastName", null, null, null);
		NamedParameterJdbcTemplate mockJdbcTemplate = mock(NamedParameterJdbcTemplate.class);

		ReflectionTestUtils.setField(userJdbcDao, "jdbcTemplate", mockJdbcTemplate);

		doThrow(new BadSqlGrammarException("error", "", new SQLDataException())).when(mockJdbcTemplate).update(anyString(), anyMap());

		// When & Then
		assertThatCode(() -> userJdbcDao.register(userDTO))
			.isInstanceOf(RuntimeException.class)
			.hasMessage("Une erreur est survenue lors de la création du compte.");
	}

	@Test
	@DisplayName("Given a UserDTO, when updating it, then updates are saved")
	void givenAUserDTO_whenUpdatingIt_thenUpdatesAreSaved() {
		// Given
		UserDTO userDTO = new UserDTO("alvin", "mail@example.com", "goulou", "Hamaide", "password", null, null, LocalDate.of(2025, 1, 1));

		// When & Then
		assertThatCode(() -> userJdbcDao.update(userDTO)).doesNotThrowAnyException();
	}

	@Test
	@DisplayName("Given a UserDTO with description and rpgKnowledge, when updating it, then updates are saved")
	void givenAUserDTOWithDescriptionAndRpgKnowledge_whenUpdatingIt_thenUpdatesAreSaved() {
		// Given
		UserDTO userDTO = new UserDTO("alvin", "mail@example.com", "goulou", "Hamaide", "password", "description", "knowledge", LocalDate.of(2025, 1, 1));

		// When
		assertThatCode(() -> userJdbcDao.update(userDTO)).doesNotThrowAnyException();
	}

	@Test
	@DisplayName("Given a UserDTO, when update fails because it already exists, then an exception is thrown")
	void givenAUserDTO_whenUpdateFailsBecauseItAlreadyExists_thenAnExceptionIsThrown() {
		// Given
		UserDTO userDTO = createUserDTO("firstName", "lastName", null, null, null);
		NamedParameterJdbcTemplate mockJdbcTemplate = mock(NamedParameterJdbcTemplate.class);

		ReflectionTestUtils.setField(userJdbcDao, "jdbcTemplate", mockJdbcTemplate);

		doThrow(new DataIntegrityViolationException("error")).when(mockJdbcTemplate).update(anyString(), anyMap());

		// When & Then
		assertThatCode(() -> userJdbcDao.update(userDTO)).isInstanceOf(DuplicateUserCredentialsException.class);
	}

	@Test
	@DisplayName("Given a UserDTO, when update fails, then an exception is thrown")
	void givenAUserDTO_whenUpdateFails_thenAnExceptionIsThrown() {
		// Given
		UserDTO userDTO = createUserDTO("firstName", "lastName", null, null, null);
		NamedParameterJdbcTemplate mockJdbcTemplate = mock(NamedParameterJdbcTemplate.class);

		ReflectionTestUtils.setField(userJdbcDao, "jdbcTemplate", mockJdbcTemplate);

		doThrow(new BadSqlGrammarException("error", "", new SQLDataException())).when(mockJdbcTemplate).update(anyString(), anyMap());

		// When & Then
		assertThatCode(() -> userJdbcDao.update(userDTO))
			.isInstanceOf(RuntimeException.class)
			.hasMessage("Une erreur est survenue lors de la mise à jour des informations.");
	}

	@SneakyThrows
	private void initTables() {
		jdbcTemplate.update(
			new String(readAllBytes(Paths.get("src/test/resources/initUsers.sql"))),
			new HashMap<>()
		);
	}

}
