package com.example.demo.controller.user;

public class UserUpdateDTO {
    private String username;
    private String password;
    private String biography;
    public UserUpdateDTO(){}

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getBiography() {
        return biography;
    }
}
