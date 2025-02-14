package com.rpgproject.utils;

import com.rpgproject.domain.bean.Profile;
import com.rpgproject.domain.bean.User;
import com.rpgproject.infrastructure.dto.UserDTO;

public class CreationTestUtils {

	public static User createUser() {
		return new User(
			"username",
			"email@domain.com",
			"firstName",
			"lastName",
			new Profile(null, null),
			"password"
		);
	}

	public static UserDTO createUserDTO(String introduction, String rpgKnowledge) {
		return new UserDTO(
			"username",
			"email@domain.com",
			"firstName",
			"lastName",
			introduction,
			rpgKnowledge,
			"password"
		);
	}

}
