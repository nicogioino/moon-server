package com.example.demo.repository;

import com.example.demo.model.Post;
import com.example.demo.model.Tag;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    @Query("SELECT p FROM Post p where p.deleted = false and (p.user.id in :usersId or p.user.id = :userId or p.tags in :followedTags)")
    Post[] allPostsFrom(Long[] usersId, Long userId, List<Tag> followedTags, Sort sort);

    @Query("SELECT p FROM Post p where p.user.id = :userId and p.deleted = false")
    Post[] postsFrom(Long userId, Sort sort);


}
