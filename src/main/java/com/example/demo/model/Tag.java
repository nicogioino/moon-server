package com.example.demo.model;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "tags")
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name", nullable = false, unique = true)
    @Size(min=1, max =20)
    private String name;

    @ManyToMany(mappedBy = "tags", cascade = { CascadeType.ALL })
    private Set<Post> posts = new HashSet<>();

    @ManyToOne
    @JoinColumn(name="user_id", nullable=false)
    private User user;

    public Tag(Long id, String name) {
        this.id = id;
        this.name = name;
    }
    public Tag(String name, User user) {
        this.name = name;
        this.user = user;
    }
    public Tag(String name){
        this.name = name;
    }

    public Tag() {
    }

    public Tag(Long id, String name, Set<Post> posts) {
        this.id = id;
        this.name = name;
        this.posts = posts;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPosts(Set<Post> posts) {
        this.posts = posts;
    }

    @Override
    public String toString() {
        return "Tag{" +
                "name='" + name + '\'' +
                '}';
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

}
