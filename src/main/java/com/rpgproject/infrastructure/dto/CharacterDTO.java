package com.rpgproject.infrastructure.dto;

import com.rpgproject.utils.IgnoreCoverage;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Objects;

@Document(collection = "Character")
@NoArgsConstructor
@Getter
@ToString
public class CharacterDTO {

	@Id
	private String id;

	private String owner;
	public String campaignSlug;
	private String name;

	public CharacterDTO(String owner, String campaignSlug, String name) {
		this.owner = owner;
		this.campaignSlug = campaignSlug;
		this.name = name;
	}

	@Override
	@IgnoreCoverage
	public boolean equals(Object o) {
		if (o == null || getClass() != o.getClass()) return false;
		CharacterDTO that = (CharacterDTO) o;
		return Objects.equals(getOwner(), that.getOwner()) && Objects.equals(getCampaignSlug(), that.getCampaignSlug()) && Objects.equals(getName(), that.getName());
	}

	@Override
	@IgnoreCoverage
	public int hashCode() {
		return Objects.hash(getOwner(), getCampaignSlug(), getName());
	}

}
