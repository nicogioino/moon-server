package com.example.demo.repository;

import com.example.demo.model.Tag;
import com.example.demo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByUsername(String principal);

    boolean existsByEmail(String principal);

    Optional<User> getUserByUsername(String username);
    Optional<User> getUserByEmail(String username);
    @Query("SELECT u.email FROM User u WHERE  u.username = :username")
    String getEmailByUsername(String username);
    @Query("SELECT u FROM User u WHERE  u.username = :username or u.email = :username")
    Optional<User> getUserByEmailOrUsername(String username);

}
