package com.example.demo.dto.react;

public class ReactsListingDTO {
    private Long likes;
    private Long applause;
    private Long love;

    public ReactsListingDTO(Long likes, Long applause, Long love) {
        this.likes = likes;
        this.applause = applause;
        this.love = love;
    }
    public ReactsListingDTO fromReacts(Long likes, Long applause, Long love) {
        return new ReactsListingDTO(likes, applause, love);
    }

    public Long getLikes() {
        return likes;
    }

    public void setLikes(Long likes) {
        this.likes = likes;
    }

    public Long getApplause() {
        return applause;
    }

    public void setApplause(Long applause) {
        this.applause = applause;
    }

    public Long getLove() {
        return love;
    }

    public void setLove(Long love) {
        this.love = love;
    }
}
