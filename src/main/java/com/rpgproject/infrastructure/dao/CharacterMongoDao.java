package com.rpgproject.infrastructure.dao;

import com.mongodb.client.result.DeleteResult;
import com.rpgproject.infrastructure.dto.CharacterDTO;
import com.rpgproject.infrastructure.exception.charactermongo.CharacterNotFoundException;
import com.rpgproject.infrastructure.exception.charactermongo.DuplicateCharacterNameException;
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
public class CharacterMongoDao {

	private final MongoTemplate mongoTemplate;

	public CharacterMongoDao(MongoTemplate mongoTemplate) {
		this.mongoTemplate = mongoTemplate;
	}

	public List<CharacterDTO> getCharactersByCampaignsSlugAndOwner(String campaignSlug, String owner) {
		Query query = buildCharactersByCampaignsSlugAndOwnerQuery(campaignSlug, owner);

		return mongoTemplate.find(query, CharacterDTO.class);
	}

	public long getCountByOwner(String username) {
		return mongoTemplate.count(query(where("owner").is(username)), CharacterDTO.class);
	}

	public void save(CharacterDTO characterDTO) {
		try {
			mongoTemplate.insert(characterDTO);
		} catch (DuplicateKeyException e) {
			log.error(e.getMessage());

			throw new DuplicateCharacterNameException();
		} catch (RuntimeException e) {
			log.error(e.getMessage());

			throw new RuntimeException("Une erreur est survenue lors de la création du personnage.", e);
		}
	}

	public void update(CharacterDTO characterDTO, String oldName) {
		try {
			updateToDatabase(characterDTO, oldName);
		} catch (DuplicateKeyException e) {
			log.error(e.getMessage());

			throw new DuplicateCharacterNameException();
		} catch (CharacterNotFoundException e) {
			log.error(e.getMessage());

			throw new CharacterNotFoundException();
		} catch (RuntimeException e) {
			log.error(e.getMessage());

			throw new RuntimeException("Une erreur est survenue lors de la mise à jour des informations.", e);
		}
	}

	private void updateToDatabase(CharacterDTO characterDTO, String oldName) {
		Query query = buildCharacterByCampaignSlugAndOwnerAndOldNameQuery(characterDTO, oldName);

		Update update = buildCharacterUpdate(characterDTO);
		CharacterDTO updatedCharacterDTO = mongoTemplate.findAndModify(query, update, CharacterDTO.class);

		if (updatedCharacterDTO == null) {
			throw new CharacterNotFoundException();
		}
	}

	private Query buildCharacterByCampaignSlugAndOwnerAndOldNameQuery(CharacterDTO characterDTO, String oldName) {
		return query(
			where("campaignSlug")
				.is(characterDTO.getCampaignSlug())
				.and("owner")
				.is(characterDTO.getOwner())
				.and("name")
				.is(oldName)
		);
	}

	private Update buildCharacterUpdate(CharacterDTO characterDTO) {
		return new Update().set("name", characterDTO.getName());
	}

	public void deleteAllByCampaignSlugAndOwner(String campaignSlug, String owner) {
		Query query = buildCharactersByCampaignsSlugAndOwnerQuery(campaignSlug, owner);

		mongoTemplate.remove(query, CharacterDTO.class);
	}

	public void delete(CharacterDTO characterDTO) {
		try {
			deleteFromDatabase(characterDTO);
		} catch (CharacterNotFoundException e) {
			log.error(e.getMessage());

			throw new CharacterNotFoundException();
		} catch (RuntimeException e) {
			log.error(e.getMessage());

			throw new RuntimeException("Une erreur est survenue lors de la suppression du personnage.", e);
		}
	}

	private void deleteFromDatabase(CharacterDTO characterDTO) {
		Query query = buildCharacterByCampaignSlugAndOwnerAndNameQuery(characterDTO);

		DeleteResult deleteResult = mongoTemplate.remove(query, CharacterDTO.class);

		if (deleteResult.getDeletedCount() == 0) {
			throw new CharacterNotFoundException();
		}
	}

	private Query buildCharacterByCampaignSlugAndOwnerAndNameQuery(CharacterDTO characterDTO) {
		return query(
			where("campaignSlug")
				.is(characterDTO.getCampaignSlug())
				.and("owner")
				.is(characterDTO.getOwner())
				.and("name")
				.is(characterDTO.getName())
		);
	}

	private Query buildCharactersByCampaignsSlugAndOwnerQuery(String campaignSlug, String owner) {
		return query(
			where("campaignSlug")
				.is(campaignSlug)
				.and("owner")
				.is(owner)
		);
	}

}
