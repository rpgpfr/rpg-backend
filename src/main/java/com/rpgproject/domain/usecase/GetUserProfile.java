package com.rpgproject.domain.usecase;

import com.rpgproject.domain.entity.User;
import com.rpgproject.domain.entity.UserProfile;
import com.rpgproject.domain.port.*;

public class GetUserProfile<T> {

	private final UserRepository userRepository;
	private final CampaignRepository campaignRepository;
	private final MapRepository mapRepository;
	private final CharacterRepository characterRepository;
	private final Presenter<UserProfile, T> presenter;

	public GetUserProfile(UserRepository userRepository, CampaignRepository campaignRepository, MapRepository mapRepository, CharacterRepository characterRepository, Presenter<UserProfile, T> presenter) {
		this.userRepository = userRepository;
		this.campaignRepository = campaignRepository;
		this.mapRepository = mapRepository;
		this.characterRepository = characterRepository;
		this.presenter = presenter;
	}

	public T execute(String uniqueName) {
		User user = userRepository.getUserByUniqueName(uniqueName);
		long campaignsCount = campaignRepository.getCountByUserId(uniqueName);
		long mapsCount = mapRepository.getCountByUserId(uniqueName);
		long charactersCount = characterRepository.getCountByUserId(uniqueName);
		long resourcesCount = campaignsCount + mapsCount + charactersCount;

		UserProfile userProfile = new UserProfile(user, campaignsCount, mapsCount, charactersCount, resourcesCount);

		return presenter.ok(userProfile);
	}

}
