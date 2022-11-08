package com.example.demo.service;

import com.example.demo.dto.comment.CommentDTO;
import com.example.demo.dto.post.PostDTO;
import com.example.demo.model.Comment;
import com.example.demo.model.Post;
import com.example.demo.model.Tag;
import com.example.demo.model.User;
import com.example.demo.repository.CommentRepository;
import com.example.demo.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
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

    public Comment create(CommentDTO commentDTO, User user, List<Tag> tags, Post post) {
        Comment comment = new Comment(commentDTO.getText(), user, post);
        comment.getTags().addAll(tags);
        return commentRepository.save(comment);
    }

}
