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
	@DisplayName("Should get campaigns by username")
	void shouldGetCampaignsByUsername() {
		// Arrange
		String username = "alvin.h";
		List<CampaignDTO> campaignDTOs = List.of(
			new CampaignDTO("Campagne 1", username),
			new CampaignDTO("Campagne 2", username),
			new CampaignDTO("Campagne 3", username)
		);

		when(jdbcTemplate.query(any(String.class), any(Map.class), any(RowMapper.class))).thenReturn(campaignDTOs);

		// Act
		List<Campaign> actualCampaigns = campaignH2Adapter.getCampaignsByUsername(username);

		// Assert
		List<Campaign> expectedCampaigns = List.of(
			new Campaign("Campagne 1", username),
			new Campaign("Campagne 2", username),
			new Campaign("Campagne 3", username)
		);

		assertThat(actualCampaigns).isEqualTo(expectedCampaigns);
	}

	@Test
	@DisplayName("Given a campaign When information is valid Then it gets inserted")
	void givenACampaign_whenInformationIsValid_thenItGetsInserted() {
		// Arrange
		String name = "campagne 1";
		String username = "alvin.h";
		Campaign campaign = new Campaign(name, username);

		// Act & Assert
		assertThatCode(() -> campaignH2Adapter.createCampaign(campaign)).doesNotThrowAnyException();
	}

	@Test
	@DisplayName("Given a campaign When information is invalid Then an exception is thrown")
	void givenACampaign_whenInformationIsInvalid_thenAnExceptionIsThrown() {
		// Arrange
		String name = "campagne 1";
		String username = "wrong username";
		Campaign campaign = new Campaign(name, username);

		when(jdbcTemplate.update(any(String.class), any(Map.class))).thenThrow(new DataIntegrityViolationException("error"));

		// Act & Assert
		assertThatCode(() -> campaignH2Adapter.createCampaign(campaign)).isInstanceOf(CannotCreateCampaignException.class);
	}

}