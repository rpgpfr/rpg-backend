package com.rpgproject.application.dto.requestbody;

import java.util.List;

public record QuestUpdateRequestBody(String title, String type, String description, List<GoalUpdateRequestBody> goals) {

}
