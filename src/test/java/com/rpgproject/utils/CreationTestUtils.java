package com.rpgproject.utils;

import com.rpgproject.infrastructure.dto.UserDTO;

public class CreationTestUtils {

	public static UserDTO createUserDTO(String firstName, String lastName, String introduction, String rpgKnowledge) {
		return new UserDTO(
			"username",
			firstName,
			lastName,
			introduction,
			rpgKnowledge
		);
	}

}
