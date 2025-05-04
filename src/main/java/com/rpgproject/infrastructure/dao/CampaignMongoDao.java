package com.rpgproject.infrastructure.dao;

import com.mongodb.client.result.DeleteResult;
import com.rpgproject.infrastructure.dto.CampaignDTO;
import com.rpgproject.infrastructure.exception.campaignmongo.CampaignNotFoundException;
import com.rpgproject.infrastructure.exception.campaignmongo.DuplicateCampaignNameException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

@Slf4j
@Component
public class CampaignMongoDao {

	private static final String OWNER_FIELD = "owner";

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
		Query query = query(where(OWNER_FIELD).is(owner));
		query.fields().include("name", "slug", "createdAt");

		return query;
	}

	public CampaignDTO findCampaignBySlugAndOwner(String slug, String owner) {
		Query query = buildCampaignBySlugAndOwnerQuery(slug, owner);

		CampaignDTO campaignDTO = mongoTemplate.findOne(query, CampaignDTO.class);

		if (campaignDTO == null) {
			log.error("Campaign not found for slug: {} and owner: {}", slug, owner);
			throw new CampaignNotFoundException();
		}

		return campaignDTO;
	}

	public String findCampaignIdBySlugAndOwner(String slug, String owner) {
		return findCampaignBySlugAndOwner(slug, owner).getId();
	}

	public long getCountByOwner(String owner) {
		return mongoTemplate
			.query(CampaignDTO.class)
			.matching(query(where(OWNER_FIELD).is(owner)))
			.count();
	}

	public void save(CampaignDTO campaignDTO) {
		try {
			mongoTemplate.insert(campaignDTO);
		} catch (DuplicateKeyException e) {
			log.error(e.getMessage());

			throw new DuplicateCampaignNameException();
		} catch (RuntimeException e) {
			log.error(e.getMessage());

			throw new RuntimeException("Une erreur est survenue lors de la création de la campagne.");
		}
	}

	public void update(CampaignDTO campaignDTO, String slug) {
		try {
			updateToDatabase(campaignDTO, slug);
		} catch (CampaignNotFoundException e) {
			log.error(e.getMessage());

			throw new CampaignNotFoundException();
		} catch (RuntimeException e) {
			log.error(e.getMessage());

			throw new RuntimeException("Une erreur est survenue lors de la mise à jour des informations.");
		}
	}

	private void updateToDatabase(CampaignDTO campaignDTO, String slug) {
		Query query = buildCampaignBySlugAndOwnerQuery(slug, campaignDTO.getOwner());
		Update update = buildUpdate(campaignDTO);

		CampaignDTO updatedCampaignDTO = mongoTemplate.findAndModify(query, update, CampaignDTO.class);

		if (updatedCampaignDTO == null) {
			throw new CampaignNotFoundException();
		}
	}

	private Query buildCampaignBySlugAndOwnerQuery(String slug, String owner) {
		return query(
			where("slug")
				.is(slug)
				.and(OWNER_FIELD)
				.is(owner)
		);
	}

	private Update buildUpdate(CampaignDTO campaignDTO) {
		return new Update()
			.set("description", campaignDTO.getDescription())
			.set("type", campaignDTO.getType())
			.set("mood", campaignDTO.getMood());
	}

	public void delete(CampaignDTO campaignDTO) {
		try {
			deleteToDatabase(campaignDTO);
		} catch (CampaignNotFoundException e) {
			log.error(e.getMessage());

			throw new CampaignNotFoundException();
		} catch (RuntimeException e) {
			log.error(e.getMessage());

			throw new RuntimeException("Une erreur est survenue lors de la suppression de la campagne.");
		}
	}

	private void deleteToDatabase(CampaignDTO campaignDTO) {
		DeleteResult removeResult = mongoTemplate.remove(campaignDTO);

		if (removeResult.getDeletedCount() == 0) {
			throw new CampaignNotFoundException();
		}
	}

}
