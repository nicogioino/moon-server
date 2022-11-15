package com.example.demo.repository;

import com.example.demo.model.Comment;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    @Query("SELECT c FROM Comment c WHERE c.post.id = :postId and c.deleted = false")
    Optional<List<Comment>> findAllCommentsInPost(Long postId, Sort sort);

    @Query("SELECT c FROM Comment c WHERE c.id = :commentId and c.deleted = false")
    Optional<Comment> findCommentById(Long commentId);
}
