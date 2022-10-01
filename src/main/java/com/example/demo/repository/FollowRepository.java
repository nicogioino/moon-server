package com.example.demo.repository;

import com.example.demo.model.Follow;
import com.example.demo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface FollowRepository extends JpaRepository<Follow, Long> {
    @Query("SELECT f FROM Follow f WHERE  f.follower.id = :followerId and f.followed.id = :followedId and f.deleted = false")
    Optional<Follow> ckeckIfFollowExists(Long followerId, Long followedId);
    @Query("SELECT f.followed.id FROM Follow f WHERE  f.follower.id = :id and f.deleted = false")
    Long[] findFollows(Long id);

    @Query("SELECT f.followed FROM Follow f WHERE  f.follower.id = :id and f.deleted = false")
    Optional<List<User>> findFollowedUsersByFollowerId(Long id);
}
