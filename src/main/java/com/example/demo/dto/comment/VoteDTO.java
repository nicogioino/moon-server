package com.example.demo.dto.comment;

public class VoteDTO {

    private Long upvoteCount;
    private Long downvoteCount;

    public VoteDTO() {
    }

    public VoteDTO(Long upvoteCount, Long downvoteCount) {
        this.upvoteCount = upvoteCount;
        this.downvoteCount = downvoteCount;
    }

    public void setUpvoteCount(Long upvoteCount) {this.upvoteCount = upvoteCount;}

    public void setDownvoteCount(Long downvoteCount) {this.downvoteCount = downvoteCount;}

    public Long getUpvoteCount() {return upvoteCount;}

    public Long getDownvoteCount() {return downvoteCount;}

    public String toString() {
        return "{" +
                "UPVOTE: " + upvoteCount +
                ", DOWNVOTE: " + downvoteCount +
                '}';
    }
}
