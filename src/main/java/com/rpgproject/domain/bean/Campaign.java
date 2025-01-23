package com.rpgproject.domain.bean;

import com.rpgproject.utils.IgnoreCoverage;

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
	@IgnoreCoverage
	public boolean equals(Object o) {
		if (o == null || getClass() != o.getClass()) return false;
		Campaign campaign = (Campaign) o;
		return Objects.equals(getName(), campaign.getName()) && Objects.equals(getUserName(), campaign.getUserName());
	}

	@Override
	@IgnoreCoverage
	public int hashCode() {
		return Objects.hash(getName(), getUserName());
	}

	@Override
	@IgnoreCoverage
	public String toString() {
		return "Campaign{" +
			"name='" + name + '\'' +
			", userName='" + userName + '\'' +
			'}';
	}

}
