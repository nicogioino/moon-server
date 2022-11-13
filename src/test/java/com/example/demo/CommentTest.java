package com.example.demo;

import com.example.demo.dto.comment.CommentDTO;
import com.example.demo.model.Comment;
import com.example.demo.model.Post;
import com.example.demo.model.User;
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
        return new User( "test-username","mail@test.com", "password");
    }
    public Post loaderPost() {
        return new Post( "test-post-title", "test-post-text", loaderUser() );
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

    private String generateLargeText() {
        StringBuilder sb = new StringBuilder();
        sb.append("a".repeat(300));
        return sb.toString();
    }

}
