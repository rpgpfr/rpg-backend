package com.rpgproject.infrastructure.dao;

import com.mongodb.client.result.DeleteResult;
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
			Query query = buildCampaignBySlugAndOwnerQuery(slug, campaignDTO.getOwner());
			Update update = buildUpdate(campaignDTO);

			CampaignDTO updatedCampaignDTO = mongoTemplate.findAndModify(query, update, CampaignDTO.class);

			if (updatedCampaignDTO == null) {
				throw new RuntimeException();
			}
		} catch (RuntimeException e) {
			System.err.println(e.getMessage());

			throw new RuntimeException("Error updating campaign", e);
		}
	}

	private Query buildCampaignBySlugAndOwnerQuery(String slug, String owner) {
		return query(
			where("slug")
				.is(slug)
				.and("owner")
				.is(owner)
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

	public void delete(CampaignDTO campaignDTO) {
		try {
			DeleteResult removeResult = mongoTemplate.remove(campaignDTO);

			if (removeResult.getDeletedCount() == 0) {
				throw new RuntimeException();
			}
		} catch (RuntimeException e) {
			System.err.println(e.getMessage());

			throw new RuntimeException("Error deleting campaign", e);
		}
	}

}
