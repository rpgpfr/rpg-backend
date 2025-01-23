package com.rpgproject.infrastructure.dao;

import com.rpgproject.infrastructure.dto.CampaignDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class CampaignDao {

	private final static String GET_CAMPAIGNS_BY_USERNAME = "SELECT * FROM CAMPAIGN WHERE USERNAME LIKE :username;";
	private final static String GET_CAMPAIGN_BY_NAME_AND_USERNAME = "SELECT * FROM CAMPAIGN WHERE NAME LIKE :name AND USERNAME LIKE :username;";
	private final static String INSERT_CAMPAIGN = "INSERT INTO CAMPAIGN VALUES (:name, :username)";

	private final NamedParameterJdbcTemplate jdbcTemplate;

	@Autowired
	public CampaignDao(DataSource dataSource) {
		jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
	}

	public CampaignDao(NamedParameterJdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public CampaignDTO getCampaignByNameAndUsername(String name, String username) {
		Map<String, String> params = Map.of(
			"name", name,
			"username", username
		);
		ResultSetExtractor<CampaignDTO> resultSetExtractor = getResultSetExtractor();

		return jdbcTemplate.query(GET_CAMPAIGN_BY_NAME_AND_USERNAME, params, resultSetExtractor);
	}

	private ResultSetExtractor<CampaignDTO> getResultSetExtractor() {
		return rs -> {
			Map<String, String> record = new HashMap<>();

			while (rs.next()) {
				record.put("name", rs.getString("NAME"));
				record.put("username", rs.getString("USERNAME"));
			}

			return new CampaignDTO(record.get("name"), record.get("username"));
		};
	}

	public List<CampaignDTO> getCampaignsByUsername(String username) {
		Map<String, String> params = Map.of("username", username);

		return jdbcTemplate.query(GET_CAMPAIGNS_BY_USERNAME, params, new BeanPropertyRowMapper<>(CampaignDTO.class));
	}

	public void createCampaign(CampaignDTO campaignDTO) throws DataIntegrityViolationException {
		Map<String, String> params = Map.of(
			"name", campaignDTO.getName(),
			"username", campaignDTO.getUsername()
		);

		jdbcTemplate.update(INSERT_CAMPAIGN, params);
	}

}
