package com.rpgproject.domain.port;

import com.rpgproject.domain.entity.Character;

import java.util.List;

public interface CharacterRepository {

	List<Character> getCharactersByCampaignsSlugAndOwner(String campaignSlug, String owner);

	long getCountByOwner(String owner);

	void save(Character character);

	void update(Character character, String oldName);

	void deleteAllByCampaignSlugAndOwner(String campaignSlug, String owner);

	void delete(Character character);

}
