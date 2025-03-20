package com.rpgproject.infrastructure.dto;

import com.rpgproject.utils.IgnoreCoverage;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Objects;

@Document(collection = "Campaign")
@NoArgsConstructor
@Getter
@Setter
@ToString
public class CampaignDTO {

	@Id
	private String id;

	private String owner;
	private String name;

	public CampaignDTO(String owner, String name) {
		this.owner = owner;
		this.name = name;
	}

	@Override
	@IgnoreCoverage
	public boolean equals(Object o) {
		if (o == null || getClass() != o.getClass()) return false;
		CampaignDTO that = (CampaignDTO) o;
		return Objects.equals(getOwner(), that.getOwner()) && Objects.equals(getName(), that.getName());
	}

	@Override
	@IgnoreCoverage
	public int hashCode() {
		return Objects.hash(getOwner(), getName());
	}

}
