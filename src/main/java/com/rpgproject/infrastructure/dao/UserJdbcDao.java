package com.rpgproject.infrastructure.dao;

import com.rpgproject.infrastructure.dto.UserDTO;
import com.rpgproject.infrastructure.exception.userjdbc.DuplicateUserCredentialsException;
import com.rpgproject.infrastructure.exception.userjdbc.UserNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class UserJdbcDao {

	private static final String GET_BY_IDENTIFIER = "SELECT * FROM USERS WHERE USERNAME = :identifier OR EMAIL = :identifier";
	private static final String REGISTER_START = "INSERT INTO USERS (USERNAME, EMAIL, FIRST_NAME, LAST_NAME";
	private static final String REGISTER_END = "VALUES (:username, :email, :firstName, :lastName";
	private static final String UPDATE_START = "UPDATE USERS SET FIRST_NAME = :firstName, LAST_NAME = :lastName";
	private static final String UPDATE_END = " WHERE USERNAME = :username";

	private final NamedParameterJdbcTemplate jdbcTemplate;

	public UserJdbcDao(NamedParameterJdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public UserDTO getUserByIdentifier(String identifier) {
		Map<String, String> parameters = Map.of("identifier", identifier);

		try {
			return jdbcTemplate.queryForObject(GET_BY_IDENTIFIER, parameters, new BeanPropertyRowMapper<>(UserDTO.class));
		} catch (EmptyResultDataAccessException _) {
			log.error("User not found with identifier: {}", identifier);

			throw new UserNotFoundException();
		}
	}

	public void register(UserDTO userDTO) {
		try {
			saveToDatabase(userDTO);
		} catch (DataIntegrityViolationException e) {
			log.error(e.getMessage());

			throw new DuplicateUserCredentialsException();
		} catch (DataAccessException e) {
			log.error(e.getMessage());

			throw new RuntimeException("Une erreur est survenue lors de la création du compte.");
		}
	}

	private void saveToDatabase(UserDTO userDTO) {
		Map<String, String> parameters = new HashMap<>();
		parameters.put("username", userDTO.getUsername());
		parameters.put("email", userDTO.getEmail());
		parameters.put("firstName", userDTO.getFirstName());
		parameters.put("lastName", userDTO.getLastName());

		String registerQuery = buildRegisterQuery(userDTO, parameters);

		jdbcTemplate.update(registerQuery, parameters);
	}

	private String buildRegisterQuery(UserDTO userDTO, Map<String, String> parameters) {
		String registerQuery = REGISTER_START;

		if (userDTO.getPassword() != null) {
			registerQuery = addPassword(userDTO, registerQuery, parameters);
		} else {
			registerQuery += ") " + REGISTER_END + ");";
		}

		return registerQuery;
	}

	private String addPassword(UserDTO userDTO, String registerQuery, Map<String, String> parameters) {
		registerQuery += ", PASSWORD) " + REGISTER_END + ", :password);";
		parameters.put("password", userDTO.getPassword());

		return registerQuery;
	}

	public void update(UserDTO userDTO) {
		Map<String, String> parameters = new HashMap<>();
		parameters.put("username", userDTO.getUsername());
		parameters.put("firstName", userDTO.getFirstName());
		parameters.put("lastName", userDTO.getLastName());

		String updateQuery = buildUpdateQuery(userDTO, parameters);

		updateToDatabase(updateQuery, parameters);
	}

	private String buildUpdateQuery(UserDTO userDTO, Map<String, String> parameters) {
		String updateQuery = UPDATE_START;

		if (userDTO.getDescription() != null) {
			updateQuery += ", DESCRIPTION = :description";
			parameters.put("description", userDTO.getDescription());
		}

		if (userDTO.getRpgKnowledge() != null) {
			updateQuery += ", RPG_KNOWLEDGE = :rpgKnowledge";
			parameters.put("rpgKnowledge", userDTO.getRpgKnowledge());
		}

		updateQuery += UPDATE_END;
		return updateQuery;
	}

	private void updateToDatabase(String updateQuery, Map<String, String> parameters) {
		try {
			jdbcTemplate.update(updateQuery, parameters);
		} catch (DataIntegrityViolationException e) {
			log.error(e.getMessage());

			throw new DuplicateUserCredentialsException();
		} catch (DataAccessException e) {
			log.error(e.getMessage());

			throw new RuntimeException("Une erreur est survenue lors de la mise à jour des informations.");
		}
	}

}
