package com.example.demo.repository;

import com.example.demo.model.Comment;
import com.example.demo.model.Vote;
import com.example.demo.model.VoteType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface VoteRepository extends JpaRepository<Vote, Long> {

    @Query("SELECT v FROM Vote v WHERE v.user.id = ?1 AND v.comment.id = ?2")
    Vote findVoteByUserIdAndVoteTypeAAndCommentId(Long userId, Long commentId);
    @Query("SELECT v.comment FROM Vote v WHERE v.user.id = :userId AND v.voteType = :voteType")
    List<Comment> findByUserIdAndVoteType(Long userId, VoteType voteType);
    @Query("SELECT COUNT(v) FROM Vote v WHERE v.comment.id = :commentId AND v.voteType = :voteType")
    Long countByCommentIdAndVoteType(Long commentId, VoteType voteType);




}
