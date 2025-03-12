package com.rpgproject.infrastructure.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class UserDTO {

	private String username;
	private String email;
	private String firstName;
	private String lastName;
	private String password;
	private String introduction;
	private String rpgKnowledge;

}
