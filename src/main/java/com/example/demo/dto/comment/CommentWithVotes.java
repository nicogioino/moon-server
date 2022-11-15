package com.example.demo.dto.comment;

import com.example.demo.model.Comment;

public class CommentWithVotes {
    VoteDTO voteDTO;
    Comment comment;

    public VoteDTO getVoteDTO() {
        return voteDTO;
    }

    public Comment getComment() {
        return comment;
    }

    public CommentWithVotes(VoteDTO voteDTO, Comment comment) {
        this.voteDTO = voteDTO;
        this.comment = comment;
    }
}
