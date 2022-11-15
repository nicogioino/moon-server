package com.example.demo;

import com.example.demo.dto.comment.CommentDTO;
import com.example.demo.model.*;
import com.example.demo.repository.CommentRepository;
import com.example.demo.service.CommentService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CommentTest {

    @Mock
    private CommentRepository commentRepository;

    @InjectMocks
    private CommentService commentService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        commentService = new CommentService(commentRepository);

    }

    public User loaderUser() {
        return new User(2L, "testUsername","mail@test.com", "aPassword123");
    }
    public com.example.demo.model.Post loaderPost() {
        return new Post( 1L,"test-post-title", "test-post-text", loaderUser() );
    }

    @Test
    public void createCommentTest() throws Exception {
        Comment comment = new Comment("test-comment-text", loaderUser(), loaderPost());
        commentService.create(new CommentDTO(comment.getText(), new String[2]), loaderUser(), Collections.EMPTY_LIST, loaderPost());
    }

    @Test
    public void createCommentFailTest(){
        Comment comment = new Comment(generateLargeText(), loaderUser(), loaderPost());
        try {
            commentService.create(new CommentDTO(comment.getText(), new String[2]), loaderUser(), Collections.EMPTY_LIST, loaderPost());
        } catch (Exception e) {
            Assert.assertEquals("Comment requires text to have 1 word or less than 200", e.getMessage());
        }
    }

    @Test
    public void deleteComment() throws Exception{
        Comment comment = new Comment("test-comment-text", loaderUser(), loaderPost());
        try {
            Comment comment1 = commentService.create(new CommentDTO(comment.getText(), new String[2]), loaderUser(), Collections.EMPTY_LIST, loaderPost());
            Long id = comment1.getId();
        } catch (Exception e) {
            Assert.assertEquals("Comment is deleted", e.getMessage());
        }
    }

    @Test
    public void testCreateComment() throws Exception {
        Comment comment = new Comment("test-comment-text", loaderUser(), loaderPost());
        when(commentRepository.save(any(Comment.class))).then(returnsFirstArg());
        when(commentRepository.findById(any(Long.class))).thenReturn(Optional.of(comment));
        Comment comment1 = commentService.create(new CommentDTO(comment.getText(), new String[2]), loaderUser(), Collections.EMPTY_LIST, loaderPost());
        assertNotNull(comment1);
    }
    @Test
    public void testDeleteComment() throws Exception {
        Comment comment = new Comment("test-comment-text", loaderUser(), loaderPost());
        when(commentRepository.save(any(Comment.class))).then(returnsFirstArg());
        when(commentRepository.findById(any(Long.class))).thenReturn(Optional.of(comment));
        Comment comment1 = commentService.createWithId(5L, new CommentDTO(comment.getText(), new String[2]), loaderUser(), Collections.EMPTY_LIST, loaderPost());
        assertNotNull(comment1);
        commentService.deleteComment(comment1);
        try {
            commentService.getCommentById(comment1.getId());
        } catch (Exception e) {
            Assert.assertEquals("Comment is deleted", e.getMessage());
        }
    }

    public com.example.demo.model.Comment loaderComment() {
        return new Comment("Comment de prueba", loaderUser(), loaderPost());
    }
    @Test
    public void testGetComment() throws Exception {
        when(commentRepository.save(any(Comment.class))).then(returnsFirstArg());
        when(commentRepository.findById(any(Long.class))).thenReturn(Optional.of(loaderComment()));
        Comment comment1 = commentService.createWithId(3L, new CommentDTO(loaderComment().getText(), new String[2]), loaderUser(), Collections.EMPTY_LIST, loaderPost());
        when(commentRepository.findById(any(Long.class))).thenReturn(Optional.of(loaderComment()));
        assertNotNull(comment1);
        Comment comment2 = commentService.createWithId(4L, new CommentDTO(loaderComment().getText(), new String[2]), loaderUser(), Collections.EMPTY_LIST, loaderPost());
        assertNotNull(comment2);
    }


    private String generateLargeText() {
        StringBuilder sb = new StringBuilder();
        sb.append("a".repeat(300));
        return sb.toString();
    }

}
