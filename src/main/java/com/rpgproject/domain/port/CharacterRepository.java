package com.rpgproject.domain.port;

public interface CharacterRepository {

	long getCountByOwner(String owner);

}
