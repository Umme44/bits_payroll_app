package com.bits.hr.service.dto;

public class UserDTONew {
    private String username;
    private int continuousFailedAttempts;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getContinuousFailedAttempts() {
        return continuousFailedAttempts;
    }

    public void setContinuousFailedAttempts(int continuousFailedAttempts) {
        this.continuousFailedAttempts = continuousFailedAttempts;
    }

    public UserDTONew(String username, int continuousFailedAttempts) {
        this.username = username;
        this.continuousFailedAttempts = continuousFailedAttempts;
    }
}
