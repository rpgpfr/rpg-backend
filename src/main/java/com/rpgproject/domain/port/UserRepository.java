package com.rpgproject.domain.port;

import com.rpgproject.domain.entity.User;

public interface UserRepository {

	User getUserByIdentifier(String identifier);

	User logIn(User user);

	void register(User user);

}
