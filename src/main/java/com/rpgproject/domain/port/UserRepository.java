package com.rpgproject.domain.port;

import com.rpgproject.domain.entity.User;

public interface UserRepository {

	User getUserByUniqueName(String uniqueName);

	void register(User user);

}
