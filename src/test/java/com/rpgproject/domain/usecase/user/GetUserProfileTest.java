package com.rpgproject.domain.usecase.user;

import com.rpgproject.domain.entity.User;
import com.rpgproject.domain.entity.UserProfile;
import com.rpgproject.domain.exception.NotFoundException;
import com.rpgproject.domain.port.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.rpgproject.domain.EntityCreationTestUtils.createUser;
import static com.rpgproject.domain.EntityCreationTestUtils.createUserProfile;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetUserProfileTest {

	private GetUserProfile<?> getUserProfile;

	@Mock
	private UserRepository userRepository;

	@Mock
	private CampaignRepository campaignRepository;

	@Mock
	private MapRepository mapRepository;

	@Mock
	private CharacterRepository characterRepository;

	@Mock
	private Presenter<UserProfile, ?> presenter;

	@BeforeEach
	void setUp() {
		getUserProfile = new GetUserProfile<>(userRepository, campaignRepository, mapRepository, characterRepository, presenter);
	}

	@Test
	@DisplayName("Given a username, when accessing the user's profile, then all the profile's information are presented")
	void givenAUsername_whenAccessingTheUsersProfile_thenAllTheProfilesInformationArePresented() {
		// Given
		String username = "username";
		User user = createUser();

		when(userRepository.getUserByIdentifier(username)).thenReturn(user);
		when(campaignRepository.getCountByOwner(username)).thenReturn((long) 1);
		when(mapRepository.getCountByOwner(username)).thenReturn((long) 1);
		when(characterRepository.getCountByOwner(username)).thenReturn((long) 1);

		// When
		getUserProfile.execute(username);

		// Then
		String expectedUsername = "username";
		UserProfile expectedUserProfile = createUserProfile();

		verify(userRepository).getUserByIdentifier(expectedUsername);
		verify(campaignRepository).getCountByOwner(expectedUsername);
		verify(mapRepository).getCountByOwner(expectedUsername);
		verify(characterRepository).getCountByOwner(expectedUsername);
		verify(presenter).ok(expectedUserProfile);
	}

	@Test
	@DisplayName("Given a username, when the user is not found, then an exception is presented")
	void givenAUsername_whenTheUserIsNotFound_thenAnExceptionIsPresented() {
		// Given
		String username = "username";
		NotFoundException exception = new NotFoundException("error");

		when(userRepository.getUserByIdentifier(username)).thenThrow(exception);

		// When
		getUserProfile.execute(username);

		// Then
		verify(presenter).error(exception);
	}

}
