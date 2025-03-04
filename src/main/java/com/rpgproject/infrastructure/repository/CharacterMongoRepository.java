package com.rpgproject.infrastructure.repository;

import com.rpgproject.domain.port.CharacterRepository;
import com.rpgproject.infrastructure.dao.CharacterMongoDao;
import org.springframework.stereotype.Repository;

@Repository
public class CharacterMongoRepository implements CharacterRepository {

	private final CharacterMongoDao characterMongoDao;

	public CharacterMongoRepository(CharacterMongoDao characterMongoDao) {
		this.characterMongoDao = characterMongoDao;
	}

	@Override
	public long getCountByOwner(String userId) {
		return characterMongoDao.getCountByOwner(userId);
	}

}
