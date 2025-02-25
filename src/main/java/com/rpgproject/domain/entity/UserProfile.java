package com.rpgproject.domain.entity;

import com.rpgproject.utils.IgnoreCoverage;

public class UserProfile {
    
    private final User user;

    public UserProfile(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
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

        UserProfile that = (UserProfile) o;
        return user.equals(that.user);
    }

    @Override
    @IgnoreCoverage
    public int hashCode() {
        return user.hashCode();
    }

    @Override
    @IgnoreCoverage
    public String toString() {
        return "UserProfile{" +
            "user=" + user +
            '}';
    }

}
