package com.example.demo.repository;

import com.example.demo.model.Post;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PostRepository  extends JpaRepository<Post, Long> {
    @Query("SELECT p FROM Post p where p.user.id in :usersId or p.user.id = :userId")
    Post[] allPostsFrom(Long[] usersId, Long userId, Sort sort);

    @Query("SELECT p FROM Post p where p.user.id = :userId")
    Post[] postsFrom(Long userId, Sort sort);
}
