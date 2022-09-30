package com.example.demo.dto.follow;

public class FollowDTO {
    String email;

    public FollowDTO(String email) {
        this.email = email;
    }

    public FollowDTO() {}


    public String getEmail(){
        return this.email;
    };

    public void setEmail(String email) {
        this.email = email;
    }
}

