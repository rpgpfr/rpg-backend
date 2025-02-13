package com.rpgproject.domain.bean;

public record User(String username, String email, String firstName, String lastName, Profile profile, String password) {

}
