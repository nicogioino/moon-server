package com.example.demo.dto.comment;

import com.example.demo.model.Comment;
import com.example.demo.model.VoteType;

public class CommentWithType {
    VoteType vote;
    Comment comment;

    public VoteType getVote() {
        return vote;
    }

    public Comment getComment() {
        return comment;
    }

    public CommentWithType(VoteType vote, Comment comment) {
        this.vote = vote;
        this.comment = comment;
    }
}
