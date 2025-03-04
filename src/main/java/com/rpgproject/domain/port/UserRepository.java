package com.rpgproject.domain.port;

import com.rpgproject.domain.entity.User;

public interface UserRepository {

	void register(User user);

	User getUserByUniqueName(String uniqueName);

}
