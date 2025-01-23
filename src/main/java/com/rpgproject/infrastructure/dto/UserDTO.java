package com.rpgproject.infrastructure.dto;

import com.rpgproject.domain.bean.User;
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

	public UserDTO(User user) {
		username = user.getUsername();
		firstName = user.getFullName().split(" ")[0];
		lastName = user.getFullName().split(" ")[1];
	}

}
