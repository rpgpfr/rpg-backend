package com.rpgproject.infrastructure.dao;

import com.rpgproject.infrastructure.dto.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.List;

@Component
public class UserDao {

    private final static String GET_ALL_USERS = "SELECT * FROM USERS;";

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

}
