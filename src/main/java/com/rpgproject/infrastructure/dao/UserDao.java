package com.rpgproject.infrastructure.dao;

import com.rpgproject.infrastructure.dto.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class UserDao {

	private final static String GET_ALL_USERS = "SELECT * FROM USERS;";
	private final static String GET_USER_BY_USERNAME = "SELECT * FROM USERS WHERE USERNAME LIKE :username;";
	private final static String INSERT_USER = "INSERT INTO USERS VALUES (:username, :firstName, :lastName);";

	private final NamedParameterJdbcTemplate jdbcTemplate;

	@Autowired
	public UserDao(DataSource dataSource) {
		jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
	}

	public UserDao(NamedParameterJdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public List<UserDTO> getAllUsers() {
		return jdbcTemplate.query(GET_ALL_USERS, new BeanPropertyRowMapper<>(UserDTO.class));
	}

	public UserDTO getUserByUsername(String username) {
		Map<String, String> params = Map.of("username", username);
		ResultSetExtractor<UserDTO> resultSetExtractor = getResultSetExtractor();

		return jdbcTemplate.query(GET_USER_BY_USERNAME, params, resultSetExtractor);
	}

	private ResultSetExtractor<UserDTO> getResultSetExtractor() {
		return rs -> {
			Map<String, String> record = new HashMap<>();

			while (rs.next()) {
				record.put("username", rs.getString("USERNAME"));
				record.put("firstName", rs.getString("FIRST_NAME"));
				record.put("lastName", rs.getString("LAST_NAME"));
			}

			return new UserDTO(record.get("username"), record.get("firstName"), record.get("lastName"));
		};
	}

	public void insertUser(UserDTO userDTO) {
		Map<String, String> params = Map.of(
			"username", userDTO.getUsername(),
			"firstName", userDTO.getFirstName(),
			"lastName", userDTO.getLastName()
		);

		jdbcTemplate.update(INSERT_USER, params);
	}

}
