package com.rpgproject.application.controller;

import com.rpgproject.application.annotation.CurrentOwner;
import com.rpgproject.application.dto.responsebody.ResponseViewModel;
import com.rpgproject.application.dto.viewmodel.UserProfileViewModel;
import com.rpgproject.application.presenter.UserProfileRestPresenter;
import com.rpgproject.domain.port.CampaignRepository;
import com.rpgproject.domain.port.CharacterRepository;
import com.rpgproject.domain.port.MapRepository;
import com.rpgproject.domain.port.UserRepository;
import com.rpgproject.domain.usecase.GetUserProfile;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/profile")
public class UserProfileController {

	private final GetUserProfile<ResponseEntity<ResponseViewModel<UserProfileViewModel>>> getUserProfile;

	public UserProfileController(UserRepository userRepository, CampaignRepository campaignRepository, MapRepository mapRepository, CharacterRepository characterRepository, UserProfileRestPresenter userProfileRestPresenter) {
		getUserProfile = new GetUserProfile<>(userRepository, campaignRepository, mapRepository, characterRepository, userProfileRestPresenter);
	}

	@GetMapping("/")
	public @ResponseBody ResponseEntity<ResponseViewModel<UserProfileViewModel>> test(@CurrentOwner String uniqueName) {
		return getUserProfile.execute(uniqueName);
	}

}
