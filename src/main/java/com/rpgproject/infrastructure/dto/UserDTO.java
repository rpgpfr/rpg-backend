package com.rpgproject.infrastructure.dto;

import lombok.*;

import java.time.LocalDate;

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
	private String description;
	private String rpgKnowledge;
	private LocalDate signedUpAt;

}
