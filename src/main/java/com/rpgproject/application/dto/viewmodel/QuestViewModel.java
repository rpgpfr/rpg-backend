package com.rpgproject.application.dto.viewmodel;

import java.util.List;

public record QuestViewModel(String title, String type, String description, List<GoalViewModel> goals) {
}
