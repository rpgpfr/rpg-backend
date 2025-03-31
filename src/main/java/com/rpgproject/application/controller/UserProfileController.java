package com.rpgproject.application.controller;

import com.rpgproject.application.annotation.CurrentOwner;
import com.rpgproject.application.dto.requestbody.UserUpdateRequestBody;
import com.rpgproject.application.dto.responsebody.ResponseViewModel;
import com.rpgproject.application.dto.viewmodel.UserProfileViewModel;
import com.rpgproject.application.dto.viewmodel.UserViewModel;
import com.rpgproject.application.presenter.UserProfileRestPresenter;
import com.rpgproject.application.presenter.UserRestPresenter;
import com.rpgproject.domain.entity.User;
import com.rpgproject.domain.port.CampaignRepository;
import com.rpgproject.domain.port.CharacterRepository;
import com.rpgproject.domain.port.MapRepository;
import com.rpgproject.domain.port.UserRepository;
import com.rpgproject.domain.usecase.user.GetUserProfile;
import com.rpgproject.domain.usecase.user.UpdateUser;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/profile")
public class UserProfileController {

	private final GetUserProfile<ResponseEntity<ResponseViewModel<UserProfileViewModel>>> getUserProfile;
	private final UpdateUser<ResponseEntity<ResponseViewModel<UserViewModel>>> updateUser;

	public UserProfileController(UserRepository userRepository, CampaignRepository campaignRepository, MapRepository mapRepository, CharacterRepository characterRepository, UserProfileRestPresenter userProfileRestPresenter, UserRestPresenter userRestPresenter) {
		getUserProfile = new GetUserProfile<>(userRepository, campaignRepository, mapRepository, characterRepository, userProfileRestPresenter);
		updateUser = new UpdateUser<>(userRepository, userRestPresenter);
	}

	@GetMapping("")
	@CrossOrigin(origins = "*")
	public @ResponseBody ResponseEntity<ResponseViewModel<UserProfileViewModel>> getUserProfile(@CurrentOwner String username) {
		return getUserProfile.execute(username);
	}

	@PatchMapping("")
	@CrossOrigin(origins = "*")
	public @ResponseBody ResponseEntity<ResponseViewModel<UserViewModel>> update(@CurrentOwner String username, @RequestBody UserUpdateRequestBody userUpdateRequestBody) {
		User user = new User(username, null, userUpdateRequestBody.firstName(), userUpdateRequestBody.lastName(), "password", userUpdateRequestBody.description(), userUpdateRequestBody.rpgKnowledge(), null);

		return updateUser.execute(user);
	}

}
