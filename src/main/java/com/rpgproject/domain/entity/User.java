package com.rpgproject.domain.entity;

import com.rpgproject.utils.IgnoreCoverage;

import java.util.Objects;

public class User {

	private final String uniqueName;
	private final String username;

	public User(String uniqueName, String username) {
		this.uniqueName = uniqueName;
		this.username = username;
	}

	public String getUniqueName() {
		return uniqueName;
	}

	public String getUsername() {
		return username;
	}

	@Override
	@IgnoreCoverage
	public boolean equals(Object o) {
		if (o == null || getClass() != o.getClass()) return false;
		User user = (User) o;
		return Objects.equals(getUniqueName(), user.getUniqueName()) && Objects.equals(getUsername(), user.getUsername());
	}

	@Override
	@IgnoreCoverage
	public int hashCode() {
		return Objects.hash(getUniqueName(), getUsername());
	}

	@Override
	@IgnoreCoverage
	public String toString() {
		return "User{" +
			"id='" + uniqueName + '\'' +
			", username='" + username + '\'' +
			'}';
	}

}
