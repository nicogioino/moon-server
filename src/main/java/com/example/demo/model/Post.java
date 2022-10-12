package com.example.demo.model;


import javax.persistence.*;

import javax.validation.constraints.Size;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "posts")
public class Post extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "title", nullable = false)
    @Size(min=1, max =50)
    private String title;

    public void setUser(User user) {
        this.user = user;
    }

    @Column(name = "text", nullable = false)
    @Size(min=1, max =500)
    private String text;

    @ManyToMany(mappedBy = "bookmarkedPosts")
    Set<User> bookmarkedByUsers;

    @Column(name = "deleted", nullable = false, columnDefinition = "boolean default false")
    private Boolean deleted = false;

    public Boolean isDeleted() {
        return deleted;
    }
    public void deletePost(){
        this.getTags().clear();
        this.deleted = true;
    }

    @ManyToMany(cascade = {
            CascadeType.ALL
    })
    @JoinTable(
            name = "tags_for_posts",
            joinColumns = {
                    @JoinColumn(name = "post_id", nullable = false)
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "tag_id", nullable = false)
            }
    )
    private Set<Tag> tags = new HashSet<>();

    @ManyToOne
    @JoinColumn(name="user_id", nullable=false)
    private User user;

    public Post() {
    }

    public Post(Long id, String title, String text, User user) {
        this.id = id;
        this.title = title;
        this.text = text;
    }
    public Post( String title, String text, User user) {
        this.user = user;
        this.title = title;
        this.text = text;
    }

    public Post(Long id, String title, String text, Set<Tag> tags) {
        this.id = id;
        this.title = title;
        this.text = text;
        this.tags = tags;
    }

    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }
    public String getTitle() {
        return title;
    }

    public String getText() {
        return text;
    }

    public Set<Tag> getTags() {
        return tags;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setTags(Set<Tag> tags) {
        this.tags = tags;
    }



    public Set<User> getBookmarkedByUsers() {
        return bookmarkedByUsers;
    }

    @Override
    public String toString() {
        return "Post{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", text='" + text + '\'' +
                ", tags=" + tags.toString() +
                ", user=" + user.toString() +
                '}';
    }
}
