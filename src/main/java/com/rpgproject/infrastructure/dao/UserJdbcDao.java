package com.rpgproject.infrastructure.dao;

import com.rpgproject.infrastructure.dto.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class UserJdbcDao {

	private static final String GET_BY_USERNAME = "SELECT * FROM USERS WHERE USERNAME = :username";
	private static final String REGISTER_START = "INSERT INTO USERS VALUES (:username, :email, :firstName, :lastName, ";
	private static final String REGISTER_END = ":password);";

	private final NamedParameterJdbcTemplate jdbcTemplate;

	@Autowired
	public UserJdbcDao(NamedParameterJdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public UserDTO getUserByUsername(String username) {
		Map<String, String> parameters = Map.of("username", username);

		return jdbcTemplate.queryForObject(GET_BY_USERNAME, parameters, new BeanPropertyRowMapper<>(UserDTO.class));
	}

	public void register(UserDTO userDTO) {
		Map<String, String> parameters = getQueryParameters(userDTO);
		String query = buildQuery(userDTO, parameters);

		try {
			jdbcTemplate.update(query, parameters);
		} catch (DataAccessException e) {
			throw new RuntimeException("Error registering user", e);
		}
	}

	private Map<String, String> getQueryParameters(UserDTO userDTO) {
		Map<String, String> parameters = new HashMap<>();
		parameters.put("username", userDTO.getUsername());
		parameters.put("email", userDTO.getEmail());
		parameters.put("firstName", userDTO.getFirstName());
		parameters.put("lastName", userDTO.getLastName());
		parameters.put("password", userDTO.getPassword());

		return parameters;
	}

	private String buildQuery(UserDTO userDTO, Map<String, String> parameters) {
		String query = REGISTER_START;
		query = updateWithIntroduction(userDTO, parameters, query);
		query = updateWithRpgKnowledge(userDTO, parameters, query);
		query += REGISTER_END;

		return query;
	}

	private String updateWithIntroduction(UserDTO userDTO, Map<String, String> parameters, String query) {
		if (userDTO.getIntroduction() != null) {
			parameters.put("introduction", userDTO.getIntroduction());
			query += ":introduction, ";
		} else {
			query += "NULL, ";
		}

		return query;
	}

	private String updateWithRpgKnowledge(UserDTO userDTO, Map<String, String> parameters, String query) {
		if (userDTO.getRpgKnowledge() != null) {
			parameters.put("rpgKnowledge", userDTO.getRpgKnowledge());
			query += ":rpgKnowledge, ";
		} else {
			query += "NULL, ";
		}

		return query;
	}

}
