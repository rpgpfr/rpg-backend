package com.rpgproject.infrastructure.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class UserDTO {

	private String username;
	private String firstName;
	private String lastName;

}
