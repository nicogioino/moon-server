package com.example.demo.model;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "comments")
public class Comment extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "text", nullable = false)
    @Size(min=1, max =200)
    private String text;

    @Column(name = "deleted", nullable = false, columnDefinition = "boolean default false")
    private Boolean deleted = false;

    @ManyToMany(cascade = {
            CascadeType.ALL
    })
    @JoinTable(
            name = "tags_for_comments",
            joinColumns = {
                    @JoinColumn(name = "comment_id", nullable = false)
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "tag_id", nullable = false)
            }
    )
    private Set<Tag> tags = new HashSet<>();

    @ManyToOne
    @JoinColumn(name="user_id", nullable=false)
    private User user;

    @ManyToOne
    @JoinColumn(name="post_id", nullable=false)
    private Post post;

    public Comment(String text, User user, Post post) {
        this.id = id;
        this.text = text;
        this.deleted = false;
        this.tags = tags;
        this.user = user;
        this.post = post;
    }

    public Comment(String text, Set<Tag> tags, User user, Post post) {
        this.text = text;
        this.deleted = false;
        this.tags = tags;
        this.user = user;
        this.post = post;
    }

    public Comment() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public Set<Tag> getTags() {
        return tags;
    }

    public void setTags(Set<Tag> tags) {
        this.tags = tags;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }
}
