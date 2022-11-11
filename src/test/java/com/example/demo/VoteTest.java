package com.example.demo;

import com.example.demo.model.*;
import com.example.demo.repository.CommentRepository;
import com.example.demo.repository.VoteRepository;
import com.example.demo.service.CommentService;
import com.example.demo.service.VoteService;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class VoteTest {
    @InjectMocks
    private VoteService voteService;
    @InjectMocks
    private CommentService commentService;
    @Mock
    private VoteRepository voteRepository;

    @Mock
    private CommentRepository commentRepository;
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        voteService = new VoteService(voteRepository, commentService);
        commentService = new CommentService(commentRepository);
    }
    public User loaderUser() {
        return new User( "testUsername","mail@test.com", "aPassword123");
    }
    public Post loaderPost() {
        return new Post( "Post de Prueba", "Body de prueba", loaderUser() );
    }
    public com.example.demo.model.Comment loaderComment() {
        return new Comment("Comment de prueba", loaderUser(), loaderPost());
    }

    @Test
    public void testCreateVote() {
        Vote vote = new Vote(loaderUser(), loaderComment(), VoteType.UPVOTE);
        when(voteRepository.save(any(Vote.class))).then(returnsFirstArg());
    }
    @Test
    public void testUpvoteComment() throws Exception {
        Vote vote = new Vote(loaderUser(), loaderComment(), VoteType.UPVOTE);
        when(voteRepository.save(any(Vote.class))).then(returnsFirstArg());
        when(voteRepository.findVoteByUserIdAndVoteTypeAAndCommentId(any(Long.class), any(Long.class))).thenReturn(null);
        when(commentRepository.findById(any(Long.class))).thenReturn(Optional.of(loaderComment()));
        Vote upvote = voteService.upvote(1L, loaderUser());
        assertNotNull(upvote);
        assertEquals(VoteType.UPVOTE, upvote.getVoteType());
    }
    @Test
    public void testDownvoteComment() throws Exception {
        Vote vote = new Vote(loaderUser(), loaderComment(), VoteType.DOWNVOTE);
        when(voteRepository.save(any(Vote.class))).then(returnsFirstArg());
        when(voteRepository.findVoteByUserIdAndVoteTypeAAndCommentId(any(Long.class), any(Long.class))).thenReturn(null);
        when(commentRepository.findById(any(Long.class))).thenReturn(Optional.of(loaderComment()));
        Vote downvote = voteService.downvote(1L, loaderUser());
        assertNotNull(downvote);
        assertEquals(VoteType.DOWNVOTE, downvote.getVoteType());
    }
    @Test
    public void assertionsInVotes() throws Exception {
        Vote vote = new Vote(loaderUser(), loaderComment(), VoteType.UPVOTE);
        when(voteRepository.save(any(Vote.class))).then(returnsFirstArg());
        when(voteRepository.findVoteByUserIdAndVoteTypeAAndCommentId(any(Long.class), any(Long.class))).thenReturn(null);
        when(commentRepository.findById(any(Long.class))).thenReturn(Optional.of(loaderComment()));
        Vote upvote = voteService.upvote(1L, loaderUser());
        assertNotNull(upvote);
        assertEquals(VoteType.UPVOTE, upvote.getVoteType());
        Vote downvote = voteService.downvote(1L, loaderUser());
        assertNotNull(downvote);
        assertEquals(VoteType.DOWNVOTE, downvote.getVoteType());
        assertEquals(0, voteService.countVotesByType(1L, VoteType.UPVOTE));

    }

    @Test
    public void testUpvote() {
        User user = loaderUser();
        Comment comment = loaderComment();
        Vote vote = new Vote(user, comment, VoteType.UPVOTE);
        assertEquals(vote.getVoteType(),VoteType.UPVOTE);

        }
    @Test
    public void testDownvote() {
        User user = loaderUser();
        Comment comment = loaderComment();
        Vote vote = new Vote(user, comment, VoteType.DOWNVOTE);
        assertEquals(vote.getVoteType(),VoteType.DOWNVOTE);

    }
}
