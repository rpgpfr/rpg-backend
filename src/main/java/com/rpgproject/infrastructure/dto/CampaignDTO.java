package com.rpgproject.infrastructure.dto;

import com.rpgproject.domain.bean.Campaign;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class CampaignDTO {

	private String name;
	private String username;

	public CampaignDTO(Campaign campaign) {
		name = campaign.getName();
		username = campaign.getUserName();
	}
}
