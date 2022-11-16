package com.example.demo.service;

import com.example.demo.dto.comment.CommentListingDTO;
import com.example.demo.dto.comment.CommentWithType;
import com.example.demo.dto.comment.CommentWithVotes;
import com.example.demo.dto.comment.VoteDTO;
import com.example.demo.model.Comment;
import com.example.demo.model.User;
import com.example.demo.model.Vote;
import com.example.demo.model.VoteType;
import com.example.demo.repository.VoteRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class VoteService {

    private final VoteRepository voteRepository;
    private final CommentService commentService;

    public VoteService(VoteRepository voteRepository, CommentService commentService) {
        this.voteRepository = voteRepository;
        this.commentService = commentService;
    }

    public Vote upvote(Long commentId, User user) throws Exception {
        Comment comment = commentService.getCommentById(commentId);
        Vote existingVote = voteRepository.findVoteByUserIdAndVoteTypeAAndCommentId(user.getId(), commentId);
        if (existingVote != null && existingVote.getVoteType().equals(VoteType.UPVOTE)) {
            throw new Exception("You have already upvoted this comment");
        }
        else if (existingVote != null && existingVote.getVoteType().equals(VoteType.DOWNVOTE)) {
            voteRepository.delete(existingVote);
            Vote upvote = new Vote(user, comment, VoteType.UPVOTE);
            voteRepository.save(upvote);
            return upvote;
        }
        else {
            Vote vote = new Vote(user, comment, VoteType.UPVOTE);
            voteRepository.save(vote);
            return vote;
        }
    }
    public Vote downvote(Long commentId, User user) throws Exception {
        Comment comment = commentService.getCommentById(commentId);
        Vote existingVote = voteRepository.findVoteByUserIdAndVoteTypeAAndCommentId(user.getId(), commentId);
        if (existingVote != null && existingVote.getVoteType().equals(VoteType.DOWNVOTE)) {
            throw new Exception("You have already downvoted this comment");
        }
        else if (existingVote != null && existingVote.getVoteType().equals(VoteType.UPVOTE)) {
            voteRepository.delete(existingVote);
            Vote downVote = new Vote(user, comment, VoteType.DOWNVOTE);
            voteRepository.save(downVote);
            return downVote;
        }
        else {
            Vote vote = new Vote(user, comment, VoteType.DOWNVOTE);
            voteRepository.save(vote);
            return vote;
        }
    }

    public VoteDTO countVotesByCommentId(Long commentId) throws Exception {
        Long upvotes = voteRepository.countByCommentIdAndVoteType(commentId, VoteType.UPVOTE);
        Long downvotes = voteRepository.countByCommentIdAndVoteType(commentId, VoteType.DOWNVOTE);

        return new VoteDTO(upvotes, downvotes);
    }
    public VoteType countVotesByCommentIdAndUser(Long commentId, Long userId) throws Exception {
        Vote existingVote = voteRepository.findVoteByUserIdAndVoteTypeAAndCommentId(userId, commentId);
        if (existingVote != null ) {
            return existingVote.getVoteType();
        }
        return VoteType.NOTVOTED;
    }

    public Long countVotesByType(Long commentId, VoteType voteType) {
        return voteRepository.countByCommentIdAndVoteType(commentId, voteType);
    }
    public CommentListingDTO[] getCommentsByVoteType(User user, VoteType voteType) throws Exception {
        List<Comment> comments = voteRepository.findByUserIdAndVoteType(user.getId(), voteType);
        CommentListingDTO[] commentListingDTOS = new CommentListingDTO[comments.size()];
        for (int i = 0; i < comments.size(); i++) {
            commentListingDTOS[i] =  CommentListingDTO.fromComment(comments.get(i),countVotesByCommentId(comments.get(i).getId()));
        }
        return commentListingDTOS;
    }

    public List<CommentWithType> getVotesForComments(List<Comment> comments, User user) throws Exception {
        ArrayList<CommentWithType> arr = new ArrayList<>();
        for(Comment comment: comments){
            VoteType voteType = countVotesByCommentIdAndUser(comment.getId(),user.getId());
            arr.add(new CommentWithType(voteType, comment,countVotesByCommentId(comment.getId())));
        }
        return arr;
    }

    public List<CommentWithVotes> getVotesForComments(List<Comment> comments) throws Exception {
        ArrayList<CommentWithVotes> arr = new ArrayList<>();
        for(Comment comment: comments){
            arr.add(new CommentWithVotes(countVotesByCommentId(comment.getId()), comment));
        }
        return arr;
    }
}
