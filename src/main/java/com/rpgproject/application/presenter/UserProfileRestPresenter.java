package com.rpgproject.application.presenter;

import com.rpgproject.application.dto.responsebody.ResponseViewModel;
import com.rpgproject.application.dto.viewmodel.UserProfileViewModel;
import com.rpgproject.domain.entity.UserProfile;
import com.rpgproject.domain.port.Presenter;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class UserProfileRestPresenter implements Presenter<UserProfile, ResponseEntity<ResponseViewModel<UserProfileViewModel>>> {

	@Override
	public ResponseEntity<ResponseViewModel<UserProfileViewModel>> ok() {
		return null;
	}

	@Override
	public ResponseEntity<ResponseViewModel<UserProfileViewModel>> ok(UserProfile userProfile) {
		return ResponseEntity.ok(
			new ResponseViewModel<>(createUserProfileModel(userProfile), null)
		);
	}

	private UserProfileViewModel createUserProfileModel(UserProfile userProfile) {
		return new UserProfileViewModel(
			userProfile.user().username(),
			userProfile.user().firstName(),
			userProfile.user().lastName(),
			userProfile.user().description(),
			userProfile.campaignCount(),
			userProfile.mapCount(),
			userProfile.characterCount(),
			userProfile.resourcesCount()
		);
	}

	@Override
	public ResponseEntity<ResponseViewModel<UserProfileViewModel>> error(Exception exception) {
		return ResponseEntity.badRequest().body(new ResponseViewModel<>(null, exception.getMessage()));
	}

}
