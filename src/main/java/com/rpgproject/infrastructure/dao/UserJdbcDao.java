package com.rpgproject.infrastructure.dao;

import com.rpgproject.infrastructure.dto.UserDTO;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class UserJdbcDao {

	private static final String GET_BY_USERNAME = "SELECT * FROM USERS WHERE ID = :id";
	private static final String REGISTER = "INSERT INTO USERS (ID, USERNAME) VALUES (:id, :username);";

	private final NamedParameterJdbcTemplate jdbcTemplate;

	public UserJdbcDao(NamedParameterJdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public UserDTO getUserById(String id) {
		Map<String, String> parameters = Map.of("id", id);

		try {
			return jdbcTemplate.queryForObject(GET_BY_USERNAME, parameters, new BeanPropertyRowMapper<>(UserDTO.class));
		} catch (EmptyResultDataAccessException e) {
			throw new RuntimeException("User not found");
		}
	}

	public void register(String id, String username) {
		Map<String, String> parameters = Map.of(
			"id", id,
			"username", username
		);

		try {
			jdbcTemplate.update(REGISTER, parameters);
		} catch (DataAccessException e) {
			throw new RuntimeException("Error registering user", e);
		}
	}

}
