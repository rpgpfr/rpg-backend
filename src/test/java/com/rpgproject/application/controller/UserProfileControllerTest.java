package com.rpgproject.application.controller;

import com.rpgproject.application.dto.responsebody.ResponseViewModel;
import com.rpgproject.application.dto.viewmodel.UserProfileViewModel;
import com.rpgproject.application.presenter.UserProfileRestPresenter;
import com.rpgproject.config.ApplicationConfig;
import com.rpgproject.domain.port.CharacterRepository;
import com.rpgproject.domain.port.UserRepository;
import com.rpgproject.infrastructure.dao.CampaignMongoDao;
import com.rpgproject.infrastructure.dao.CharacterMongoDao;
import com.rpgproject.infrastructure.dao.MapMongoDao;
import com.rpgproject.infrastructure.dao.UserJdbcDao;
import com.rpgproject.infrastructure.repository.CampaignMongoRepository;
import com.rpgproject.infrastructure.repository.CharacterMongoRepository;
import com.rpgproject.infrastructure.repository.MapMongoRepository;
import com.rpgproject.infrastructure.repository.UserJdbcRepository;
import com.rpgproject.utils.BasicDatabaseExtension;
import com.rpgproject.utils.EzDatabase;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.ActiveProfiles;

import java.nio.file.Paths;
import java.util.HashMap;

import static com.rpgproject.utils.CreationTestUtils.createCampaignDTOs;
import static java.nio.file.Files.readAllBytes;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@ExtendWith(BasicDatabaseExtension.class)
@Import({
	ApplicationConfig.class,
	UserProfileController.class,
	UserJdbcRepository.class,
	UserJdbcDao.class,
	CampaignMongoRepository.class,
	CampaignMongoDao.class,
	MapMongoRepository.class,
	MapMongoDao.class,
	CharacterMongoRepository.class,
	CharacterMongoDao.class
})
class UserProfileControllerTest {

	private UserProfileController userProfileController;

	@Autowired
	private MongoTemplate mongoTemplate;

	@EzDatabase
	private NamedParameterJdbcTemplate jdbcTemplate;

	@BeforeEach
	public void setUp() {
		UserJdbcDao userJdbcDao = new UserJdbcDao(jdbcTemplate);
		UserRepository userRepository = new UserJdbcRepository(userJdbcDao);
		CampaignMongoDao campaignMongoDao = new CampaignMongoDao(mongoTemplate);
		CampaignMongoRepository campaignRepository = new CampaignMongoRepository(campaignMongoDao);
		MapMongoDao mapMongoDao = new MapMongoDao(mongoTemplate);
		MapMongoRepository mapRepository = new MapMongoRepository(mapMongoDao);
		CharacterMongoDao characterMongoDao = new CharacterMongoDao(mongoTemplate);
		CharacterMongoRepository characterRepository = new CharacterMongoRepository(characterMongoDao);
		UserProfileRestPresenter userProfileRestPresenter = new UserProfileRestPresenter();

		userProfileController = new UserProfileController(userRepository, campaignRepository, mapRepository, characterRepository, userProfileRestPresenter);

		initTables();
		mongoTemplate.insert(createCampaignDTOs(), "Campaign");
	}

	@AfterEach
	public void tearDown() {
		mongoTemplate.dropCollection("Campaign");
	}

	@Test
	@DisplayName("Given a unique name, when user exists, then user profile is returned")
	void givenAUniqueName_whenUserExists_thenUserProfileIsReturned() {
		// Given
		String uniqueName = "ID123";

		// When
		ResponseEntity<ResponseViewModel<UserProfileViewModel>> actualResponseEntity = userProfileController.test(uniqueName);

		// Then
		ResponseEntity<ResponseViewModel<UserProfileViewModel>> expectedResponseEntity = ResponseEntity.ok(
			new ResponseViewModel<>(new UserProfileViewModel(
				"alvin",
				"Alvin",
				"Alvinson",
				null,
				null,
				0,
				0,
				0,
				0
			), null)
		);

		assertThat(actualResponseEntity).isEqualTo(expectedResponseEntity);
	}

	@SneakyThrows
	private void initTables() {
		jdbcTemplate.update(
			new String(readAllBytes(Paths.get("src/test/resources/initUsers.sql"))),
			new HashMap<>()
		);
	}

}