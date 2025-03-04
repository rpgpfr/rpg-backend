package com.rpgproject.domain.usecase;

import com.rpgproject.domain.entity.User;
import com.rpgproject.domain.entity.UserProfile;
import com.rpgproject.domain.exception.UserNotFoundException;
import com.rpgproject.domain.port.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.rpgproject.utils.CreationTestUtils.createUser;
import static com.rpgproject.utils.CreationTestUtils.createUserProfile;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GetUserProfileTest {

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
	public void setUp() {
		getUserProfile = new GetUserProfile<>(userRepository, campaignRepository, mapRepository, characterRepository, presenter);
	}

	@Test
	@DisplayName("Given a user's uniquename, when accessing the user's profile, then all the profile's information are sent")
	void givenAUsername_whenAccessingTheUsersProfile_thenAllTheProfilesInformationAreSent() {
		// Given
		String uniqueName = "uniqueName";
		User user = createUser();

		when(userRepository.getUserByUniqueName(uniqueName)).thenReturn(user);
		when(campaignRepository.getCountByOwner(uniqueName)).thenReturn((long) 1);
		when(mapRepository.getCountByOwner(uniqueName)).thenReturn((long) 1);
		when(characterRepository.getCountByOwner(uniqueName)).thenReturn((long) 1);

		// When
		getUserProfile.execute(uniqueName);

		// Then
		String expectedUniqueName = "uniqueName";
		UserProfile expectedUserProfile = createUserProfile();

		verify(userRepository).getUserByUniqueName(expectedUniqueName);
		verify(campaignRepository).getCountByOwner(expectedUniqueName);
		verify(mapRepository).getCountByOwner(expectedUniqueName);
		verify(characterRepository).getCountByOwner(expectedUniqueName);
		verify(presenter).ok(expectedUserProfile);
	}

	@Test
	@DisplayName("Given a user's uniquename, when the user does not exist, then an error is sent")
	void givenAUsername_whenTheUserDoesNotExist_thenAnErrorIsSent() {
		// Given
		String uniqueName = "uniqueName";
		UserNotFoundException exception = new UserNotFoundException();

		doThrow(exception).when(userRepository).getUserByUniqueName(uniqueName);

		// When
		getUserProfile.execute(uniqueName);

		// Then

		verify(presenter).error(exception);
	}

}
