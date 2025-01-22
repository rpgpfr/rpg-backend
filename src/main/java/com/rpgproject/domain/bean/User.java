package com.rpgproject.domain.bean;

import java.util.Objects;

public class User {

	private final String username;
	private final String fullName;

	public User(String username, String fullName) {
		this.username = username;
		this.fullName = fullName;
	}

	public String getUsername() {
		return username;
	}

	public String getFullName() {
		return fullName;
	}

	@Override
	public boolean equals(Object o) {
		if (o == null || getClass() != o.getClass()) return false;
		User user = (User) o;
		return Objects.equals(getUsername(), user.getUsername()) && Objects.equals(getFullName(), user.getFullName());
	}

	@Override
	public int hashCode() {
		return Objects.hash(getUsername(), getFullName());
	}

	@Override
	public String toString() {
		return "User{" +
			"username='" + username + '\'' +
			", fullName='" + fullName + '\'' +
			'}';
	}

}
