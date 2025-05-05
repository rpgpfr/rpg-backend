package com.rpgproject.infrastructure.dto;

import com.rpgproject.utils.IgnoreCoverage;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.Objects;

@Document(collection = "Quest")
@NoArgsConstructor
@Getter
@Setter
@ToString
public class QuestDTO {

	@Id
	private String id;
	private String owner;
	private String campaignSlug;
	private String type;
	private String title;
	private String description;
	private List<GoalDTO> goals;

	public QuestDTO(String owner, String campaignSlug, String type, String title, String description, List<GoalDTO> goals) {
		this.owner = owner;
		this.campaignSlug = campaignSlug;
		this.type = type;
		this.title = title;
		this.description = description;
		this.goals = goals;
	}

	@Override
	@IgnoreCoverage
	public boolean equals(Object o) {
		if (o == null || getClass() != o.getClass()) return false;
		QuestDTO questDTO = (QuestDTO) o;
		return Objects.equals(getOwner(), questDTO.getOwner()) && Objects.equals(getCampaignSlug(), questDTO.getCampaignSlug()) && Objects.equals(getType(), questDTO.getType()) && Objects.equals(getTitle(), questDTO.getTitle()) && Objects.equals(getDescription(), questDTO.getDescription()) && Objects.equals(getGoals(), questDTO.getGoals());
	}

	@Override
	@IgnoreCoverage
	public int hashCode() {
		return Objects.hash(getOwner(), getCampaignSlug(), getType(), getTitle(), getDescription(), getGoals());
	}

}
