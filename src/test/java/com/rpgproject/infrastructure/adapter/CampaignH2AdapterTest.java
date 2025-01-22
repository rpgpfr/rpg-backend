package com.rpgproject.infrastructure.adapter;

import com.rpgproject.domain.bean.Campaign;
import com.rpgproject.domain.exception.CannotCreateCampaignException;
import com.rpgproject.infrastructure.dao.CampaignDao;
import com.rpgproject.infrastructure.dto.CampaignDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CampaignH2AdapterTest {

	private CampaignH2Adapter campaignH2Adapter;

	@Mock
	private NamedParameterJdbcTemplate jdbcTemplate;

	@BeforeEach
	public void setUp() {
		CampaignDao campaignDao = new CampaignDao(jdbcTemplate);
		campaignH2Adapter = new CampaignH2Adapter(campaignDao);
	}

	@Test
	@DisplayName("Should get all campaigns")
	void shouldGetAllCampaigns() {
		// Arrange
		List<CampaignDTO> campaignDTOs = List.of(
			new CampaignDTO("campagne 1", "alvin.h"),
			new CampaignDTO("campagne 2", "alvin.h")
		);

		when(jdbcTemplate.query(any(String.class), any(RowMapper.class))).thenReturn(campaignDTOs);

		// Act
		List<Campaign> actualCampaigns = campaignH2Adapter.getAllCampaigns();

		// Assert
		List<Campaign> expectedCampaigns = List.of(
			new Campaign("campagne 1", "alvin.h"),
			new Campaign("campagne 2", "alvin.h")
		);

		assertThat(actualCampaigns).isEqualTo(expectedCampaigns);
	}

	@Test
	@DisplayName("Should get campaign by name and username")
	void shouldGetCampaignByNameAndUsername() {
		// Arrange
		String name = "campagne 1";
		String username = "alvin.h";
		CampaignDTO campaignDTO = new CampaignDTO(name, username);

		when(jdbcTemplate.query(any(String.class), any(Map.class), any(ResultSetExtractor.class))).thenReturn(campaignDTO);

		// Act
		Campaign actualCampaign = campaignH2Adapter.getCampaignByNameAndUsername(name, username);

		// Assert
		Campaign expectedCampaign = new Campaign(name, username);

		assertThat(actualCampaign).isEqualTo(expectedCampaign);
	}

	@Test
	@DisplayName("Given a campaign When inserting it Then it gets saved in database")
	void givenACampaign_whenInsertingIt_thenItGetsSavedInDatabase() {
		// Arrange
		String name = "campagne 1";
		String username = "alvin.h";
		Campaign campaign = new Campaign(name, username);

		// Act & Assert
		assertThatCode(() -> campaignH2Adapter.insertCampaign(campaign)).doesNotThrowAnyException();
	}

	@Test
	@DisplayName("Given a campaign with invalid information When inserting it Then it throws an exception")
	void shouldInsertCampaignInDatabase() {
		// Arrange
		String name = "campagne 1";
		String username = "wrong username";
		Campaign campaign = new Campaign(name, username);

		when(jdbcTemplate.update(any(String.class), any(Map.class))).thenThrow(new DataIntegrityViolationException("error"));

		// Act & Assert
		assertThatCode(() -> campaignH2Adapter.insertCampaign(campaign)).isInstanceOf(CannotCreateCampaignException.class);
	}

}