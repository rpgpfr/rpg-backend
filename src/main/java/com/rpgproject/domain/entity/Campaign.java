package com.rpgproject.domain.entity;

import com.rpgproject.utils.IgnoreCoverage;

import java.util.Objects;

public class Campaign {

	private final String name;

	public Campaign(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	@Override
	@IgnoreCoverage
	public boolean equals(Object o) {
		if (o == null || getClass() != o.getClass()) return false;
		Campaign campaign = (Campaign) o;
		return Objects.equals(getName(), campaign.getName());
	}

	@Override
	@IgnoreCoverage
	public int hashCode() {
		return Objects.hashCode(getName());
	}

	@Override
	@IgnoreCoverage
	public String toString() {
		return "Campaign{" +
			"name='" + name + '\'' +
			'}';
	}

}
