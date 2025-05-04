package com.rpgproject.domain.port;

import com.rpgproject.domain.entity.User;

public interface UserRepository {

	User getUserByIdentifier(String identifier);

	User logIn(String identifier, String password);

	void register(User user);

	void update(User user);

}
