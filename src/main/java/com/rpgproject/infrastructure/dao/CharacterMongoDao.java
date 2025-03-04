package com.rpgproject.infrastructure.dao;

import com.rpgproject.infrastructure.dto.CharacterDTO;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

@Component
public class CharacterMongoDao {

	private final MongoTemplate mongoTemplate;

	public CharacterMongoDao(MongoTemplate mongoTemplate) {
		this.mongoTemplate = mongoTemplate;
	}

	public long getCountByOwner(String uniqueName) {
		return mongoTemplate.count(query(where("owner").is(uniqueName)), CharacterDTO.class);
	}

}
