package com.example.demo.dto.user;

import com.example.demo.model.User;

public class UserListingDTO {
    Long id;
    String username;
    String email;
    String bio;



    public static UserListingDTO fromUser(User user) {
        UserListingDTO userListingDto = new UserListingDTO();
        userListingDto.setUsername(user.getUsername());
        userListingDto.setEmail(user.getEmail());
        userListingDto.setBio(user.getBio());
        userListingDto.setId(user.getId());
        return userListingDto;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBio() {
        return bio;
    }
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }
}
