package com.rpgproject.infrastructure.repository;

import com.rpgproject.domain.port.MapRepository;
import com.rpgproject.infrastructure.dao.MapMongoDao;
import org.springframework.stereotype.Repository;

@Repository
public class MapMongoRepository implements MapRepository {

	private final MapMongoDao mapMongoDao;

	public MapMongoRepository(MapMongoDao mapMongoDao) {
		this.mapMongoDao = mapMongoDao;
	}

	@Override
	public long getCountByOwner(String owner) {
		return mapMongoDao.getCountByOwner(owner);
	}

}
