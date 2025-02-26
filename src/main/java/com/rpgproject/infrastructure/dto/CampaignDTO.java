package com.rpgproject.infrastructure.dto;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "Campaign")
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
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

}
