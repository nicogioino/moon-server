package com.example.demo.dto.react;

public class ReactDTO {

    private Long applauseCount;

    private Long likeCount;

    private Long loveCount;

    public ReactDTO() {
    }

    public ReactDTO(Long applauseCount, Long likeCount, Long loveCount) {
        this.applauseCount = applauseCount;
        this.likeCount = likeCount;
        this.loveCount = loveCount;
    }

    public void setApplauseCount(Long applauseCount) {
        this.applauseCount = applauseCount;
    }

    public void setLikeCount(Long likeCount) {
        this.likeCount = likeCount;
    }

    public void setLoveCount(Long loveCount) {
        this.loveCount = loveCount;
    }

    public Long getApplauseCount() {
        return applauseCount;
    }

    public Long getLikeCount() {return likeCount;}

    public Long getLoveCount() {return loveCount;}

    @Override
    public String toString() {
        return "{" +
                "APPLAUSE: " + applauseCount +
                ", LIKE: " + likeCount +
                ", LOVE: " + loveCount +
                '}';
    }
}
