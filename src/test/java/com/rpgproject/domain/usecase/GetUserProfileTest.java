package com.rpgproject.domain.usecase;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;

import com.rpgproject.domain.port.UserPresenter;
import com.rpgproject.domain.port.UserRepository;

@ExtendWith(MockitoExtension.class)
public class GetUserProfileTest {

    private GetUserProfile getUserProfile;

	@Mock
	private UserRepository userRepository;

	@BeforeEach
	public void setUp() {
		getUserProfile = new GetUserProfile(userRepository);
	}

}
