package com.rpgproject.domain.entity;

import com.rpgproject.utils.IgnoreCoverage;

import java.time.LocalDate;
import java.util.Objects;

public class Campaign {

	private final String owner;
	private final String name;
	private final String slug;
	private final String description;
	private final String type;
	private final String mood;
	private Quest mainQuest;
	private final LocalDate createdAt;

	public Campaign(String owner, String name, String slug, String description, String type, String mood, Quest mainQuest, LocalDate createdAt) {
		this.owner = owner;
		this.name = name;
		this.slug = slug;
		this.description = description;
		this.type = type;
		this.mood = mood;
		this.mainQuest = mainQuest;
		this.createdAt = createdAt;
	}

	public Campaign(String slug) {
		this(null, null, slug, null, null, null, null, null);
	}

	public Campaign(String owner, String name, String slug, LocalDate createdAt) {
		this(owner, name, slug, null, null, null, null, createdAt);
	}

	public String getOwner() {
		return owner;
	}

	public String getName() {
		return name;
	}

	public String getSlug() {
		return slug;
	}

	public String getDescription() {
		return description;
	}

	public String getType() {
		return type;
	}

	public String getMood() {
		return mood;
	}

	public Quest getMainQuest() {
		return mainQuest;
	}

	public LocalDate getCreatedAt() {
		return createdAt;
	}

	public void setMainQuest(Quest mainQuest) {
		this.mainQuest = mainQuest;
	}

	@Override
	@IgnoreCoverage
	public boolean equals(Object o) {
		if (o == null || getClass() != o.getClass()) return false;
		Campaign campaign = (Campaign) o;
		return Objects.equals(getOwner(), campaign.getOwner()) && Objects.equals(getName(), campaign.getName()) && Objects.equals(getSlug(), campaign.getSlug()) && Objects.equals(getDescription(), campaign.getDescription()) && Objects.equals(getType(), campaign.getType()) && Objects.equals(getMood(), campaign.getMood()) && Objects.equals(getMainQuest(), campaign.getMainQuest()) && Objects.equals(getCreatedAt(), campaign.getCreatedAt());
	}

	@Override
	@IgnoreCoverage
	public int hashCode() {
		return Objects.hash(getOwner(), getName(), getSlug(), getDescription(), getType(), getMood(), getMainQuest(), getCreatedAt());
	}

	@Override
	@IgnoreCoverage
	public String toString() {
		return "Campaign{" +
			"owner='" + owner + '\'' +
			", name='" + name + '\'' +
			", slug='" + slug + '\'' +
			", description='" + description + '\'' +
			", type='" + type + '\'' +
			", mood='" + mood + '\'' +
			", mainQuest=" + mainQuest +
			", createdAt=" + createdAt +
			'}';
	}

}
