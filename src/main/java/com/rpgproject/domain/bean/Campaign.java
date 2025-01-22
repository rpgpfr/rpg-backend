package com.rpgproject.domain.bean;

import java.util.Objects;

public class Campaign {

	private final String name;
	private final String userName;

	public Campaign(String name, String userName) {
		this.name = name;
		this.userName = userName;
	}

	public String getName() {
		return name;
	}

	public String getUserName() {
		return userName;
	}

	@Override
	public boolean equals(Object o) {
		if (o == null || getClass() != o.getClass()) return false;
		Campaign campaign = (Campaign) o;
		return Objects.equals(getName(), campaign.getName()) && Objects.equals(getUserName(), campaign.getUserName());
	}

	@Override
	public int hashCode() {
		return Objects.hash(getName(), getUserName());
	}

	@Override
	public String toString() {
		return "Campaign{" +
			"name='" + name + '\'' +
			", userName='" + userName + '\'' +
			'}';
	}

}
