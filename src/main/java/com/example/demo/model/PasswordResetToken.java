package com.example.demo.model;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class PasswordResetToken extends BaseEntity{
    private static final int EXPIRATION = 60 * 24;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String token;

    @OneToOne(targetEntity = User.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "user_id")
    private User user;

    private LocalDateTime expiryDate;

    public PasswordResetToken(String token, User user) {
        this.token = token;
        this.user = user;
        this.expiryDate = calculateExpiryDate();
    }

    private LocalDateTime calculateExpiryDate() {
        return LocalDateTime.now().plusMinutes(PasswordResetToken.EXPIRATION);
    }

    public PasswordResetToken() {}

    public LocalDateTime getExpiryDate() {return expiryDate;}

    public User getUser() {return user;}
}

