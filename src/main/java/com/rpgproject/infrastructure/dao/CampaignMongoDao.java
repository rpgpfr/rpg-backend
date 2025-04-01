package com.rpgproject.infrastructure.dao;

import com.rpgproject.infrastructure.dto.CampaignDTO;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
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
			.matching(buildCampaignsByOwnerQuery(owner))
			.all();
	}

	private Query buildCampaignsByOwnerQuery(String owner) {
		return query(where("owner").is(owner));
	}

	public CampaignDTO findCampaignBySlugAndOwner(String slug, String owner) {
		Query query = buildCampaignBySlugAndOwnerQuery(slug, owner);

		return mongoTemplate.findOne(query, CampaignDTO.class);
	}

	private Query buildCampaignBySlugAndOwnerQuery(String slug, String owner) {
		return query(
			where("slug")
				.is(slug)
				.and("owner")
				.is(owner)
		);
	}

	public String findCampaignIdBySlugAndOwner(String slug, String owner) {
		return findCampaignBySlugAndOwner(slug, owner).getId();
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
			Query query = buildQuery(campaignDTO, slug);
			Update update = buildUpdate(campaignDTO);

			mongoTemplate.findAndModify(query, update, CampaignDTO.class);
		} catch (RuntimeException e) {
			System.err.println(e.getMessage());

			throw new RuntimeException("Error updating campaign", e);
		}
	}

	private Query buildQuery(CampaignDTO campaignDTO, String slug) {
		return new Query(
			where("slug")
				.is(slug)
				.and("owner")
				.is(campaignDTO.getOwner())
		);
	}

	private Update buildUpdate(CampaignDTO campaignDTO) {
		return new Update()
			.set("name", campaignDTO.getName())
			.set("description", campaignDTO.getDescription())
			.set("slug", campaignDTO.getSlug())
			.set("type", campaignDTO.getType())
			.set("mood", campaignDTO.getMood());
	}

}
