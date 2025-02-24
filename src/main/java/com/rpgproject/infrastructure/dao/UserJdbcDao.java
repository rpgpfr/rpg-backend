package com.rpgproject.infrastructure.dao;

import com.rpgproject.infrastructure.dto.UserDTO;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class UserJdbcDao {

	private static final String GET_BY_USERNAME = "SELECT * FROM USERS WHERE USERNAME = :username";
	private static final String REGISTER = "INSERT INTO USERS (USERNAME) VALUES (:username);";

	private final NamedParameterJdbcTemplate jdbcTemplate;

	public UserJdbcDao(NamedParameterJdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public UserDTO getUserByUsername(String username) {
		Map<String, String> parameters = Map.of("username", username);

		return jdbcTemplate.queryForObject(GET_BY_USERNAME, parameters, new BeanPropertyRowMapper<>(UserDTO.class));
	}

	public void register(String username) {
		Map<String, String> parameters = Map.of("username", username);

		try {
			jdbcTemplate.update(REGISTER, parameters);
		} catch (DataAccessException e) {
			throw new RuntimeException("Error registering user", e);
		}
	}

}
