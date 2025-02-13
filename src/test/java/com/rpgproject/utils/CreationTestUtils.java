package com.rpgproject.utils;

import com.rpgproject.domain.bean.User;

public class CreationTestUtils {

	public static User createUser() {
		return new User(
			"username",
			"email@domain.com",
			"firstName",
			"lastName",
			null,
			"password"
		);
	}

}
