package com.rpgproject.domain.entity;

public record UserProfile(User user, long campaignCount, long mapCount, long characterCount, long resourcesCount) {

}
