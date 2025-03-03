package com.rpgproject.domain.port;

import com.rpgproject.domain.entity.User;
import com.rpgproject.domain.entity.UserProfile;

public interface UserRepository {

	void register(User user);

	User getUserByUniqueName(String uniqueName);

}
