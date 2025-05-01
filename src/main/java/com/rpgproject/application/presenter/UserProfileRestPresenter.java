package com.rpgproject.application.presenter;

import com.rpgproject.application.dto.responsebody.ResponseViewModel;
import com.rpgproject.application.dto.viewmodel.UserProfileViewModel;
import com.rpgproject.application.service.ExceptionHTTPStatusService;
import com.rpgproject.domain.entity.UserProfile;
import com.rpgproject.domain.port.Presenter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;

@Component
public class UserProfileRestPresenter implements Presenter<UserProfile, ResponseEntity<ResponseViewModel<UserProfileViewModel>>> {

	private final ExceptionHTTPStatusService exceptionHTTPStatusService;
	private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

	@Autowired
	public UserProfileRestPresenter(ExceptionHTTPStatusService exceptionHTTPStatusService) {
		this.exceptionHTTPStatusService = exceptionHTTPStatusService;
	}

	@Override
	public ResponseEntity<ResponseViewModel<UserProfileViewModel>> ok() {
		return ResponseEntity.ok().build();
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
			userProfile.user().email(),
			userProfile.user().firstName(),
			userProfile.user().lastName(),
			dateFormatter.format(userProfile.user().signedUpAt()),
			userProfile.user().description(),
			userProfile.user().rpgKnowledge(),
			userProfile.campaignCount(),
			userProfile.mapCount(),
			userProfile.characterCount(),
			userProfile.resourcesCount()
		);
	}

	@Override
	public ResponseEntity<ResponseViewModel<UserProfileViewModel>> error(RuntimeException exception) {
		return new ResponseEntity<>(
			new ResponseViewModel<>(
				null,
				exception.getMessage()
			),
			exceptionHTTPStatusService.getHttpStatusFromExceptionClass(exception)
		);
	}

}
