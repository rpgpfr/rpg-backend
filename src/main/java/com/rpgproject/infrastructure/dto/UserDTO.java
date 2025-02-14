package com.rpgproject.infrastructure.dto;

import com.rpgproject.domain.bean.User;
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
	private String introduction;
	private String rpgKnowledge;
	private String password;

	public UserDTO(User user) {
		this.username = user.username();
		this.email = user.email();
		this.firstName = user.firstName();
		this.lastName = user.lastName();
		this.introduction = user.profile().introduction();
		this.rpgKnowledge = user.profile().rpgKnowledge();
		this.password = user.password();
	}

}
