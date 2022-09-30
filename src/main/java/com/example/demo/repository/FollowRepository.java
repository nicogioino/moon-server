package com.example.demo.repository;

import com.example.demo.model.Follow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface FollowRepository extends JpaRepository<Follow, Long> {
    @Query("SELECT f FROM Follow f WHERE  f.follower.id = :followerId and f.followed.id = :followedId and f.deleted = false")
    Optional<Follow> ckeckIfFollowExists(Long followerId, Long followedId);
}
