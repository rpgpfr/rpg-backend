package com.rpgproject.infrastructure.dao;

import com.rpgproject.infrastructure.dto.CampaignDTO;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

@Component
public class CampaignMongoDao {

	private final MongoTemplate mongoTemplate;

	public CampaignMongoDao(MongoTemplate mongoTemplate) {
		this.mongoTemplate = mongoTemplate;
	}

	public List<CampaignDTO> findAllCampaignsByUserId(String userId) {
		return mongoTemplate.query(CampaignDTO.class).matching(query(where("userId").is(userId))).all();
	}

	public long getCountByUserId(String userId) {
		return mongoTemplate.query(CampaignDTO.class).matching(query(where("userId").is(userId))).count();
	}

}
