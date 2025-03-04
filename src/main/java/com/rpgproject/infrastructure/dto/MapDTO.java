package com.rpgproject.infrastructure.dto;

import com.rpgproject.utils.IgnoreCoverage;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Objects;

@Document(collection = "Map")
@NoArgsConstructor
@Getter
@ToString
public class MapDTO {

	@Id
	private String id;

	private String userId;
	private String url;

	public MapDTO(String userId, String url) {
		this.userId = userId;
		this.url = url;
	}

	@Override
	@IgnoreCoverage
	public boolean equals(Object o) {
		if (o == null || getClass() != o.getClass()) return false;
		MapDTO mapDTO = (MapDTO) o;
		return Objects.equals(getUserId(), mapDTO.getUserId()) && Objects.equals(getUrl(), mapDTO.getUrl());
	}

	@Override
	@IgnoreCoverage
	public int hashCode() {
		return Objects.hash(getUserId(), getUrl());
	}

}
