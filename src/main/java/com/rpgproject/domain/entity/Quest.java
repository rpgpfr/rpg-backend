package com.rpgproject.domain.entity;

import java.util.List;

public record Quest(String title, String type, String description, List<Goal> goals) {
}
