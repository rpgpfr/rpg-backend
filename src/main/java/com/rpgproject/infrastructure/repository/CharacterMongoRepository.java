package com.rpgproject.infrastructure.repository;

import com.rpgproject.domain.entity.Character;
import com.rpgproject.domain.exception.DuplicateException;
import com.rpgproject.domain.exception.InternalException;
import com.rpgproject.domain.exception.NotFoundException;
import com.rpgproject.domain.port.CharacterRepository;
import com.rpgproject.infrastructure.dao.CharacterMongoDao;
import com.rpgproject.infrastructure.dto.CharacterDTO;
import com.rpgproject.infrastructure.exception.charactermongo.CharacterNotFoundException;
import com.rpgproject.infrastructure.exception.charactermongo.DuplicateCharacterNameException;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CharacterMongoRepository implements CharacterRepository {

	private final CharacterMongoDao characterMongoDao;

	public CharacterMongoRepository(CharacterMongoDao characterMongoDao) {
		this.characterMongoDao = characterMongoDao;
	}

	@Override
	public List<Character> getCharactersByCampaignsSlugAndOwner(String campaignSlug, String owner) {
		List<CharacterDTO> characterDTOs = characterMongoDao.getCharactersByCampaignsSlugAndOwner(campaignSlug, owner);

		return mapToCharacters(characterDTOs);
	}

	private List<Character> mapToCharacters(List<CharacterDTO> characterDTOs) {
		return characterDTOs.stream()
			.map(this::mapToCharacter)
			.toList();
	}

	private Character mapToCharacter(CharacterDTO characterDTO) {
		return new Character(
			characterDTO.getOwner(),
			characterDTO.getCampaignSlug(),
			characterDTO.getName()
		);
	}

	@Override
	public long getCountByOwner(String owner) {
		return characterMongoDao.getCountByOwner(owner);
	}

	@Override
	public void save(Character character) {
		try {
			CharacterDTO characterDTO = mapToCharacterDTO(character);

			characterMongoDao.save(characterDTO);
		} catch (DuplicateCharacterNameException e) {
			throw new DuplicateException(e.getMessage());
		} catch (RuntimeException e) {
			throw new InternalException(e.getMessage());
		}
	}

	private CharacterDTO mapToCharacterDTO(Character character) {
		return new CharacterDTO(
			character.owner(),
			character.campaignSlug(),
			character.name()
		);
	}

	@Override
	public void update(Character character, String oldName) {
		try {
			CharacterDTO characterDTO = mapToCharacterDTO(character);

			characterMongoDao.update(characterDTO, oldName);
		} catch (CharacterNotFoundException e) {
			throw new NotFoundException(e.getMessage());
		} catch (DuplicateCharacterNameException e) {
			throw new DuplicateException(e.getMessage());
		} catch (RuntimeException e) {
			throw new InternalException(e.getMessage());
		}
	}

	@Override
	public void deleteAllByCampaignSlugAndOwner(String campaignSlug, String owner) {
		characterMongoDao.deleteAllByCampaignSlugAndOwner(campaignSlug, owner);
	}

	@Override
	public void delete(Character character) {
		try {
			CharacterDTO characterDTO = mapToCharacterDTO(character);

			characterMongoDao.delete(characterDTO);
		} catch (CharacterNotFoundException e) {
			throw new NotFoundException(e.getMessage());
		} catch (RuntimeException e) {
			throw new InternalException(e.getMessage());
		}
	}

}
