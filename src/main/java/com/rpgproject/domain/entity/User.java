package com.rpgproject.domain.entity;

import com.rpgproject.utils.IgnoreCoverage;

import java.util.Objects;

public class User {

	private final String uniqueName;
	private final String username;
	private final String firstName;
	private final String lastName;
	private final String description;
	private final String rpgKnowledge;

	public User(String uniqueName, String username, String firstName, String lastName, String description, String rpgKnowledge) {
		this.uniqueName = uniqueName;
		this.username = username;
		this.firstName = firstName;
		this.lastName = lastName;
		this.description = description;
		this.rpgKnowledge = rpgKnowledge;
	}
	public User(String uniqueName, String username) {
		this.uniqueName = uniqueName;
		this.username = username;
		this.firstName = null;
		this.lastName = null;
		this.description = null;
		this.rpgKnowledge = null;
	}

	public String getUniqueName() {
		return uniqueName;
	}

	public String getUsername() {
		return username;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public String getDescription() {
		return description;
	}

	public String getRpgKnowledge() {
		return rpgKnowledge;
	}

	@Override
	@IgnoreCoverage
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}

		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		User user = (User) o;
		return Objects.equals(uniqueName, user.uniqueName) &&
			Objects.equals(username, user.username) &&
			Objects.equals(firstName, user.firstName) &&
			Objects.equals(lastName, user.lastName) &&
			Objects.equals(description, user.description) &&
			Objects.equals(rpgKnowledge, user.rpgKnowledge);
	}

	@Override
	@IgnoreCoverage
	public int hashCode() {
		return Objects.hash(uniqueName, username, firstName, lastName, description, rpgKnowledge);
	}

	@Override
	@IgnoreCoverage
	public String toString() {
		return "User{" +
			"uniqueName='" + uniqueName + '\'' +
			", username='" + username + '\'' +
			", firstName='" + firstName + '\'' +
			", lastName='" + lastName + '\'' +
			", description='" + description + '\'' +
			", rpgKnowledge='" + rpgKnowledge + '\'' +
			'}';
	}

}
