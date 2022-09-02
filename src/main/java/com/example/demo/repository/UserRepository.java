package com.example.demo.repository;

import com.example.demo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String mail);

    @Query("SELECT u FROM User u WHERE u.id = :id")
    Optional<User> getUserById(Long id);

    @Query("SELECT u FROM User u WHERE u.email = :email or u.username = :username")
    Optional<User> getUserByEmailOrUsername(String email, String username);

    boolean existsByUsername(String principal);

    boolean existsByEmail(String principal);
}
