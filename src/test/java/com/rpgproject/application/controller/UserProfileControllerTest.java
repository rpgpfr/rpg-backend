package com.rpgproject.application.controller;

import com.rpgproject.application.dto.requestbody.UserUpdateRequestBody;
import com.rpgproject.application.dto.responsebody.ResponseViewModel;
import com.rpgproject.application.dto.viewmodel.UserProfileViewModel;
import com.rpgproject.application.dto.viewmodel.UserViewModel;
import com.rpgproject.application.presenter.UserProfileRestPresenter;
import com.rpgproject.application.presenter.UserRestPresenter;
import com.rpgproject.application.service.ExceptionHTTPStatusService;
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
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import java.nio.file.Paths;
import java.util.HashMap;

import static com.rpgproject.infrastructure.DTOCreationTestUtils.createCampaignDTOs;
import static java.nio.file.Files.readAllBytes;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@ExtendWith(BasicDatabaseExtension.class)
class UserProfileControllerTest {

	private UserProfileController userProfileController;

	@Autowired
	private MongoTemplate mongoTemplate;

	@EzDatabase
	private NamedParameterJdbcTemplate jdbcTemplate;

	@BeforeEach
	void setUp() {
		UserJdbcDao userJdbcDao = new UserJdbcDao(jdbcTemplate);
		BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
		UserRepository userRepository = new UserJdbcRepository(userJdbcDao, bCryptPasswordEncoder);
		CampaignMongoDao campaignMongoDao = new CampaignMongoDao(mongoTemplate);
		CampaignMongoRepository campaignRepository = new CampaignMongoRepository(campaignMongoDao);
		MapMongoDao mapMongoDao = new MapMongoDao(mongoTemplate);
		MapMongoRepository mapRepository = new MapMongoRepository(mapMongoDao);
		CharacterMongoDao characterMongoDao = new CharacterMongoDao(mongoTemplate);
		CharacterMongoRepository characterRepository = new CharacterMongoRepository(characterMongoDao);
		ExceptionHTTPStatusService exceptionHTTPStatusService = new ExceptionHTTPStatusService();
		UserProfileRestPresenter userProfileRestPresenter = new UserProfileRestPresenter(exceptionHTTPStatusService);
		UserRestPresenter userRestPresenter = new UserRestPresenter(exceptionHTTPStatusService);

		userProfileController = new UserProfileController(userRepository, campaignRepository, mapRepository, characterRepository, userProfileRestPresenter, userRestPresenter);

		initTables();
		mongoTemplate.insert(createCampaignDTOs(), "Campaign");
	}

	@AfterEach
	void tearDown() {
		mongoTemplate.dropCollection("Campaign");
	}

	@Test
	@DisplayName("Given a username, when the user exists, then the user profile is returned")
	void givenAUsername_whenTheUserExists_thenTheUserProfileIsReturned() {
		// Given
		String username = "alvin";

		// When
		ResponseEntity<ResponseViewModel<UserProfileViewModel>> actualResponseEntity = userProfileController.getUserProfile(username);

		// Then
		ResponseEntity<ResponseViewModel<UserProfileViewModel>> expectedResponseEntity = ResponseEntity.ok(
			new ResponseViewModel<>(new UserProfileViewModel(
				"alvin",
				"mail@example.com",
				"Alvin",
				"Hamaide",
				"01/01/2025",
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

	@Test
	@DisplayName("Given a username and a userUpdateRequestBody, when the user exists, then the user is updated")
	void givenAUsernameAndAUserUpdateRequestBody_whenTheUserExists_thenTheUserIsUpdated() {
		// Given
		String username = "alvin";
		UserUpdateRequestBody userUpdateRequestBody = new UserUpdateRequestBody("Goulou", "Hamaide", "Description", "RPG Knowledge");

		// When
		ResponseEntity<ResponseViewModel<UserViewModel>> actualResponseEntity = userProfileController.update(username, userUpdateRequestBody);

		// Then
		ResponseEntity<ResponseViewModel<UserViewModel>> expectedResponseEntity = ResponseEntity.ok(
			new ResponseViewModel<>(new UserViewModel(
				"alvin",
				null,
				"Goulou",
				"Hamaide",
				"Description",
				"RPG Knowledge"
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