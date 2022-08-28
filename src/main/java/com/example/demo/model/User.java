package com.example.demo.model;

import javax.persistence.*;
import javax.validation.constraints.Size;

@Entity
@Table(name = "users")
public class User {
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
    @Size(min=8, max =20)
    private String password;

    public User(String username, String mail, String password) {
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
}