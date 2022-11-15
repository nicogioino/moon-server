package com.example.demo.model;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.*;

@Entity
@Table(name = "users")
public class User extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;
    @Column(name = "username", nullable = false, unique = true)
    @Size(min= 3, max= 16)
    private String username;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "biography", nullable = true)
    @Size( max= 100)
    private String bio;

    @Column(name = "password", nullable = false)
    @Size(min=8, max =255)
    private String password;

    @ManyToMany
    @JoinTable(
            name = "bookmarks",
            joinColumns = @JoinColumn(name = "user_id", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "post_id", nullable = false)

    )
    Set<Post> bookmarkedPosts;


    public User(String username, String mail, String password) {
        this.username = username;
        this.email = mail;
        this.password = password;
    }
    public User(Long id, String username, String mail, String password) {
        this.id = id;
        this.username = username;
        this.email = mail;
        this.password = password;
    }
    public User(String mail, String password) {
        this.email = mail;
        this.password = password;
    }
    public User() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {return email;}

    public String getPassword() {return password;}

    public void setPassword(String newPassword) { this.password = newPassword;}

    public String getUsername() {return username;}

    public void setUsername(String username) {this.username = username;}

    public void setEmail(String email) {this.email = email;}

    public String getBio() {return bio;}

    public void setBio(String bio) {this.bio = bio;}

    public Set<Post> getBookmarkedPosts() {
        return bookmarkedPosts;
    }

}