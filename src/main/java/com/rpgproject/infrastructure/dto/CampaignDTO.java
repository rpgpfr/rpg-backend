package com.rpgproject.infrastructure.dto;

import lombok.*;
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

	private String userId;
	private String name;

	public CampaignDTO(String userId, String name) {
		this.userId = userId;
		this.name = name;
	}

	@Override
	public boolean equals(Object o) {
		if (o == null || getClass() != o.getClass()) return false;
		CampaignDTO that = (CampaignDTO) o;
		return Objects.equals(getUserId(), that.getUserId()) && Objects.equals(getName(), that.getName());
	}

	@Override
	public int hashCode() {
		return Objects.hash(getUserId(), getName());
	}

}
