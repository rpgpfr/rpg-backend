package com.rpgproject.utils;

import com.rpgproject.domain.entity.User;
import com.rpgproject.infrastructure.dto.UserDTO;

public class CreationTestUtils {

	public static User createUser() {
		return new User(
			"uniqueName",
			"username"
		);
	}

	public static UserDTO createUserDTO(String firstName, String lastName, String introduction, String rpgKnowledge) {
		return new UserDTO(
			"uniqueName",
			"username",
			firstName,
			lastName,
			introduction,
			rpgKnowledge
		);
	}

}
