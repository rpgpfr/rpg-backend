package com.rpgproject.infrastructure.dao;

import com.rpgproject.infrastructure.dto.CampaignDTO;
import com.rpgproject.utils.BasicDatabaseExtension;
import com.rpgproject.utils.EzDatabase;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;

import static java.nio.file.Files.readAllBytes;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

@ExtendWith(BasicDatabaseExtension.class)
class CampaignDaoTest {

	private CampaignDao campaignDao;

	@EzDatabase
	private NamedParameterJdbcTemplate jdbcTemplate;

	@BeforeEach
	public void setUp() {
		campaignDao = new CampaignDao(jdbcTemplate);

		initTables();
	}

	@SneakyThrows
	private void initTables() {
		jdbcTemplate.update(
			new String(readAllBytes(Paths.get("src/test/resources/initCampaign.sql"))),
			new HashMap<>()
		);
	}

	@Test
	@DisplayName("Given a username When campaigns exist with this username Then they get returned")
	void givenAUsername_whenCampaignsExistWithThisUsername_thenTheyGetReturned() {
		// Given
		String username = "alvin.h";

		// When
		List<CampaignDTO> actualCampaigns = campaignDao.getCampaignsByUsername(username);

		// Then
		List<CampaignDTO> expectedCampaigns = List.of(
			new CampaignDTO("Campagne 1", "alvin.h"),
			new CampaignDTO("Campagne 2", "alvin.h"),
			new CampaignDTO("Campagne 3", "alvin.h")
		);

		assertThat(actualCampaigns).isEqualTo(expectedCampaigns);
	}

	@Test
	@DisplayName("Given a campaign name and a username When campaign exists Then it gets returned")
	void givenACampaignNameAndAUsername_whenCampaignExists_thenItGetsReturned() {
		// Given
		String name = "Campagne 1";
		String username = "alvin.h";

		// When
		CampaignDTO actualCampaign = campaignDao.getCampaignByNameAndUsername(name, username);

		// Then
		CampaignDTO expectedCampaign = new CampaignDTO("Campagne 1", "alvin.h");

		assertThat(actualCampaign).isEqualTo(expectedCampaign);
	}

	@Test
	@DisplayName("Given a campaign When username exists Then it gets inserted")
	void givenACampaign_whenUsernameExists_thenItGetsInserted() {
		// Given
		String name = "insertedCampaign";
		String username = "alvin.h";
		CampaignDTO campaignDTO = new CampaignDTO(name, username);

		// When
		campaignDao.createCampaign(campaignDTO);

		// Then
		CampaignDTO actualCampaignDTO = campaignDao.getCampaignByNameAndUsername(name, username);
		CampaignDTO expectedCampaignDTO = new CampaignDTO(name, username);

		assertThat(actualCampaignDTO).isEqualTo(expectedCampaignDTO);
	}

	@Test
	@DisplayName("Given a campaign When username does not exist Then exception is thrown")
	void givenACampaign_whenUsernameDoesNotExist_thenExceptionIsThrown() {
		// Given
		String name = "insertedCampaign";
		CampaignDTO campaignDTO = new CampaignDTO(name, null);

		// When & Then
		assertThatCode(() -> campaignDao.createCampaign(campaignDTO)).isInstanceOf(Exception.class);
	}

}