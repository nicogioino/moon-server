package com.example.demo.dto.follow;

public class FollowersDTO {
    Long id;
    String username;

    public FollowersDTO(Long id, String username) {
        this.id = id;
        this.username = username;
    }

    public Long getId() {return id;}

    public String getUsername() {return username;}

    public void setId(Long id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
