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

    @Test
    @DisplayName("Should get all campaigns")
    void shouldGetAllCampaigns() {
        // Act
        List<CampaignDTO> actualCampaigns = campaignDao.getAllCampaigns();

        // Assert
        List<CampaignDTO> expectedCampaigns = List.of(
            new CampaignDTO("Campagne 1", "alvin.h"),
            new CampaignDTO("Campagne 2", "alvin.h"),
            new CampaignDTO("Campagne 3", "alvin.h"),
            new CampaignDTO("Campagne 4", "tom.e"),
            new CampaignDTO("Campagne 5", "tom.e")
        );

        assertThat(actualCampaigns).isEqualTo(expectedCampaigns);
    }

    @Test
    @DisplayName("Should get campaign by name and username")
    void shouldGetCampaignByNameAndUsername() {
        // Arrange
        String name = "Campagne 1";
        String username = "alvin.h";

        // Act
        CampaignDTO actualCampaign = campaignDao.getCampaignByNameAndUsername(name, username);

        // Assert
        CampaignDTO expectedCampaign = new CampaignDTO("Campagne 1", "alvin.h");

        assertThat(actualCampaign).isEqualTo(expectedCampaign);
    }

    @Test
    @DisplayName("Should insert campaign When username exists in database")
    void shouldInsertCampaign_whenUserExistsInDatabase() {
        // Arrange
        String name = "insertedCampaign";
        String username = "alvin.h";
        CampaignDTO campaignDTO = new CampaignDTO(name, username);

        // Act
        campaignDao.insertCampaign(campaignDTO);

        // Assert
        CampaignDTO actualCampaignDTO = campaignDao.getCampaignByNameAndUsername(name, username);
        CampaignDTO expectedCampaignDTO = new CampaignDTO("insertedCampaign", "alvin.h");

        assertThat(actualCampaignDTO).isEqualTo(expectedCampaignDTO);
    }

    @Test
    @DisplayName("Should not insert campaign When username does not exist in database")
    void shouldNotInsertCampaign_whenUserDoesNotExistInDatabase() {
        // Arrange
        String name = "insertedCampaign";
        String username = "nonExistent";
        CampaignDTO campaignDTO = new CampaignDTO(name, username);

        // Act & Assert
        assertThatCode(() -> campaignDao.insertCampaign(campaignDTO));
    }

    @SneakyThrows
    private void initTables() {
        jdbcTemplate.update(
            new String(readAllBytes(Paths.get("src/test/resources/initCampaign.sql"))),
            new HashMap<>()
        );
    }

}