package com.rpgproject.infrastructure.adapter;

import com.rpgproject.domain.bean.Campaign;
import com.rpgproject.domain.exception.CannotCreateCampaignException;
import com.rpgproject.domain.exception.CannotCreateUserException;
import com.rpgproject.domain.exception.NoResultException;
import com.rpgproject.infrastructure.dao.CampaignDao;
import com.rpgproject.infrastructure.dto.CampaignDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.internal.matchers.Null;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

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
	@DisplayName("Given a campaign name and a username When campaign exists Then it gets returned")
	void givenACampaignNameAndAUsername_whenCampaignExists_thenItGetsReturned() {
		// Given
		String name = "campagne 1";
		String username = "alvin.h";
		CampaignDTO campaignDTO = new CampaignDTO(name, username);

		when(jdbcTemplate.query(any(String.class), any(Map.class), any(ResultSetExtractor.class))).thenReturn(campaignDTO);

		// When
		Campaign actualCampaign = campaignH2Adapter.getCampaignByNameAndUsername(name, username);

		// Then
		Campaign expectedCampaign = new Campaign(name, username);

		assertThat(actualCampaign).isEqualTo(expectedCampaign);
	}

	@Test
	@DisplayName("Given a campaign name and a username When campaign does not exist Then an exception is thrown")
	void givenACampaignNameAndAUsername_whenCampaignDoesNotExist_thenAnExceptionIsThrown() {
		// Given
		String name = "campagne 1";
		String username = "alvin.h";
		CampaignDTO campaignDTO = new CampaignDTO(null, null);

		when(jdbcTemplate.query(any(String.class), any(Map.class), any(ResultSetExtractor.class))).thenReturn(campaignDTO);

		// When & Then
		assertThatCode(() -> campaignH2Adapter.getCampaignByNameAndUsername(name, username)).isInstanceOf(NoResultException.class);
	}

	@Test
	@DisplayName("Given a username When campaigns exist Then they get returned")
	void givenAUsername_whenCampaignsExist_thenTheyGetReturned() {
		// Given
		String username = "alvin.h";
		List<CampaignDTO> campaignDTOs = List.of(
			new CampaignDTO("Campagne 1", username),
			new CampaignDTO("Campagne 2", username),
			new CampaignDTO("Campagne 3", username)
		);

		when(jdbcTemplate.query(any(String.class), any(Map.class), any(RowMapper.class))).thenReturn(campaignDTOs);

		// When
		List<Campaign> actualCampaigns = campaignH2Adapter.getCampaignsByUsername(username);

		// Then
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
		// Given
		String name = "campagne 1";
		String username = "alvin.h";
		Campaign campaign = new Campaign(name, username);

		// When & Then
		assertThatCode(() -> campaignH2Adapter.createCampaign(campaign)).doesNotThrowAnyException();
	}

	@Test
	@DisplayName("Given a campaign When information is invalid Then an exception is thrown")
	void givenACampaign_whenInformationIsInvalid_thenAnExceptionIsThrown() {
		// Given
		String name = "campagne";
		String username = "user does not exist";
		Campaign campaign = new Campaign(name, username);

		when(jdbcTemplate.update(any(String.class), any(Map.class))).thenThrow(new DataIntegrityViolationException("cannot create"));

		// When & Then
		assertThatCode(() -> campaignH2Adapter.createCampaign(campaign)).isInstanceOf(CannotCreateCampaignException.class);
	}

	@ParameterizedTest
	@MethodSource("getExceptionTestParameters")
	@DisplayName("Given a campaign When information is invalid Then an exception is thrown")
	void givenACampaign_whenInformationIsInvalid_thenAnExceptionIsThrown(String name, String username, Class<Exception> expectedException) {
		// Given
		Campaign campaign = new Campaign(name, username);

		// When & Then
		assertThatCode(() -> campaignH2Adapter.createCampaign(campaign)).isInstanceOf(expectedException);
	}

	private static Stream<Arguments> getExceptionTestParameters() {
		return Stream.of(
			Arguments.of(null, "alvin.h", CannotCreateCampaignException.class),
			Arguments.of(null, null, CannotCreateCampaignException.class),
			Arguments.of("Campagne", null, CannotCreateCampaignException.class)
		);
	}

}