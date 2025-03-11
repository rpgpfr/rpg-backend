package com.rpgproject.infrastructure.dao;

import com.rpgproject.infrastructure.dto.UserDTO;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class UserJdbcDao {

	private static final String GET_BY_IDENTIFIER = "SELECT * FROM USERS WHERE USERNAME = :identifier OR EMAIL = :identifier";
	private static final String REGISTER_START = "INSERT INTO USERS (USERNAME, EMAIL, FIRST_NAME, LAST_NAME";
	private static final String REGISTER_END = "VALUES (:username, :email, :firstName, :lastName";

	private final NamedParameterJdbcTemplate jdbcTemplate;

	public UserJdbcDao(NamedParameterJdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public UserDTO getUserByIdentifier(String identifier) {
		Map<String, String> parameters = Map.of("identifier", identifier);

		try {
			return jdbcTemplate.queryForObject(GET_BY_IDENTIFIER, parameters, new BeanPropertyRowMapper<>(UserDTO.class));
		} catch (EmptyResultDataAccessException e) {
			throw new RuntimeException("User not found");
		}
	}

	public void register(UserDTO userDTO) {
		Map<String, String> parameters = new HashMap<>();
		parameters.put("username", userDTO.getUsername());
		parameters.put("email", userDTO.getEmail());
		parameters.put("firstName", userDTO.getFirstName());
		parameters.put("lastName", userDTO.getLastName());

		String registerQuery = REGISTER_START;

		if (userDTO.getPassword() != null) {
			registerQuery += ", PASSWORD) " + REGISTER_END + ", :password);";
			parameters.put("password", userDTO.getPassword());
		} else {
			registerQuery += ") " + REGISTER_END + ");";
		}

		try {
			jdbcTemplate.update(registerQuery, parameters);
		} catch (DataAccessException e) {
			System.out.println(e.getMessage());
			throw new RuntimeException("Error registering user", e);
		}
	}

}
