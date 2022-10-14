package com.example.demo.repository;

import com.example.demo.model.React;
import com.example.demo.model.ReactType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ReactRepository extends JpaRepository<React, Long> {
    @Query("SELECT r FROM React r WHERE r.user.id = :userId AND r.post.id = :postId")
    React findByUserIdAndPostId(Long userId, Long postId);

    Optional<React> findByPostIdAndReactType(Long postId, ReactType reactType);


    Long countReactsByPostIdAndReactType(Long postId, ReactType type);



}
