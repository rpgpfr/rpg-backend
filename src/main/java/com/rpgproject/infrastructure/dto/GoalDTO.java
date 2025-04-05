package com.rpgproject.infrastructure.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@EqualsAndHashCode
@ToString
public class GoalDTO {

	private String name;
	private boolean completed;

}
