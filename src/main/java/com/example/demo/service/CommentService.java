package com.example.demo.service;

import com.example.demo.dto.comment.CommentDTO;
import com.example.demo.dto.post.PostDTO;
import com.example.demo.model.Comment;
import com.example.demo.model.Post;
import com.example.demo.model.Tag;
import com.example.demo.model.User;
import com.example.demo.repository.CommentRepository;
import com.example.demo.repository.PostRepository;
import com.example.demo.validators.CommentInputValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class CommentService {
    private final CommentRepository commentRepository;

    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    public Comment create(CommentDTO commentDTO, User user, List<Tag> tags, Post post) throws Exception {
        CommentInputValidator validator = new CommentInputValidator();
        validator.checkCreateComment(commentDTO);
        Comment comment = new Comment(commentDTO.getText(), user, post);
        comment.getTags().addAll(tags);
        commentRepository.save(comment);
        return comment;
    }
    public Comment createWithId(Long id, CommentDTO commentDTO, User user, List<Tag> tags, Post post) throws Exception {
        CommentInputValidator validator = new CommentInputValidator();
        validator.checkCreateComment(commentDTO);
        Comment comment = new Comment(id,commentDTO.getText(), user, post);
        comment.getTags().addAll(tags);
        commentRepository.save(comment);
        return comment;
    }
    public List<Comment> getAllComments(Long id) {
        return commentRepository.findAllCommentsInPost(id, Sort.by(Sort.Direction.DESC, "createdAt"))
                .orElseThrow(() -> new RuntimeException("No comments found"));
    }

    public Comment getCommentById(Long commentId) throws Exception {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new Exception("No comment found"));

        if(comment.getDeleted()) throw new Exception("Comment is deleted");
        return comment;

    }

    public void deleteComment(Comment comment) {
        comment.getTags().clear();
        comment.setDeleted(true);
        commentRepository.save(comment);
    }
}
