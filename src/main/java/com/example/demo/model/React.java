package com.example.demo.model;


import javax.persistence.*;

@Entity
@Table(name = "react")
public class React extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name="user_id", nullable=false)
    private User user;

    @ManyToOne
    @JoinColumn(name="post_id", nullable=false)
    private Post post;

    @Column(name = "type", nullable = false)
    private ReactType reactType;

    public React() {}

    public React(User user, Post post, ReactType reactType) {
        this.user = user;
        this.post = post;
        this.reactType = reactType;
    }

    public Long getId() {return id;}

    public void setId(Long id) {this.id = id;}

    public Long getUserId() {return user.getId();}

    public ReactType getReactType() {return reactType;}

    public Long getPostId() {return post.getId();}

    public void setPost(Post post) {this.post = post;}

    public void setUser(User user) {this.user = user;}

    public void setReactType(ReactType reactType) {this.reactType = reactType;}
}

