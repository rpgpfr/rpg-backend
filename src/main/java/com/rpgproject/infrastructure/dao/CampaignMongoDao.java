package com.rpgproject.infrastructure.dao;

import com.rpgproject.infrastructure.dto.CampaignDTO;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
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

	public List<CampaignDTO> findAllCampaignsByOwner(String owner) {
		return mongoTemplate
			.query(CampaignDTO.class)
			.matching(query(where("owner").is(owner)))
			.all();
	}

	public long getCountByOwner(String owner) {
		return mongoTemplate
			.query(CampaignDTO.class)
			.matching(query(where("owner").is(owner)))
			.count();
	}

	public void save(CampaignDTO campaignDTO) {
		try {
			mongoTemplate.save(campaignDTO);
		} catch (RuntimeException e) {
			System.err.println(e.getMessage());

			throw new RuntimeException("Error saving campaign", e);
		}
	}

	public void update(CampaignDTO campaignDTO, String slug) {
		try {
			Query query = new Query(
				where("slug")
					.is(slug)
					.and("owner")
					.is(campaignDTO.getOwner())
			);

			mongoTemplate.findAndReplace(query, campaignDTO);
		} catch (RuntimeException e) {
			System.err.println(e.getMessage());

			throw new RuntimeException("Error updating campaign", e);
		}
	}

}
