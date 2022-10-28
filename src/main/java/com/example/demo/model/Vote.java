package com.example.demo.model;

import javax.persistence.*;

@Entity
public class Vote extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;
    @ManyToOne
    @JoinColumn(name="user_id", nullable=false)
    private User user;

    @ManyToOne
    @JoinColumn(name="comment_id", nullable=false)
    private Comment comment;
    private VoteType voteType;


    public Vote() {}

    public Vote(User user, Comment comment, VoteType voteType) {
        this.user = user;
        this.comment = post;
        this.voteType = voteType;
    }

    public Long getId() {return id;}

    public void setId(Long id) {this.id = id;}

    public Long getUserId() {return user.getId();}

    public VoteType getVoteType() {return voteType;}

    public Long getCommentId() {return comment.getId();}

    public void setPost(Comment post) {this.comment = post;}

    public void setUser(User user) {this.user = user;}

    public void setVoteType(VoteType voteType) {this.voteType = voteType;}
}
}
