package com.rpgproject.infrastructure.dao;

import com.rpgproject.infrastructure.dto.MapDTO;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

@Component
public class MapMongoDao {

	private final MongoTemplate mongoTemplate;

	public MapMongoDao(MongoTemplate mongoTemplate) {
		this.mongoTemplate = mongoTemplate;
	}

	public long getCountByUserId(String userId) {
		return mongoTemplate
			.query(MapDTO.class)
			.matching(query(where("userId").is(userId)))
			.count();
	}

}
