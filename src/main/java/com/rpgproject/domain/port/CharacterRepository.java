package com.rpgproject.domain.port;

public interface CharacterRepository {

	long getCountByUserId(String userId);

}
