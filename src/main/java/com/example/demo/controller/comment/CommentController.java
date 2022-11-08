package com.example.demo.controller.comment;

import com.example.demo.dto.comment.CommentDTO;
import com.example.demo.dto.comment.CommentListingDTO;
import com.example.demo.dto.error.ErrorDTO;
import com.example.demo.dto.post.PostDTO;
import com.example.demo.dto.post.PostListingDTO;
import com.example.demo.model.Comment;
import com.example.demo.model.Post;
import com.example.demo.model.Tag;
import com.example.demo.model.User;
import com.example.demo.security.util.JwtUtil;
import com.example.demo.service.*;
import com.example.demo.validators.CommentInputValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;


@RestController
@CrossOrigin(origins= "*")
@RequestMapping(path= "/comment")
public class CommentController {
    private final CommentInputValidator commentInputValidator = new CommentInputValidator();
    private  final TagService tagService;
    private final CommentService commentService;
    @Autowired
    private JwtUtil jwtUtil;
    private PostService postService;
    private UserService userService;

    public CommentController(TagService tagService, CommentService commentService, PostService postService, UserService userService) {
        this.tagService = tagService;
        this.commentService = commentService;
        this.postService = postService;
        this.userService = userService;
    }

    @PostMapping("/{postId}")
    public ResponseEntity<?> createComment(@PathVariable("postId") Long postId, @RequestBody CommentDTO possibleComment, @RequestHeader String Authorization ){
        try{
            String email = jwtUtil.extractEmail(Authorization);
            User user = userService.findUserByEmail(email);
            commentInputValidator.checkCreateComment(possibleComment);
            List<Tag> tags = tagService.createTags(possibleComment.getTags(), user);
            Post post = postService.getPostById(postId);
            Comment comment = commentService.create(possibleComment, user, tags, post);
            return new ResponseEntity<>(CommentDTO.fromComment(comment), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(ErrorDTO.fromMessage(e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{postId}")
    public ResponseEntity<?> getAllComments(@PathVariable("postId") Long postId, @RequestHeader String Authorization ){
        try{
            String email = jwtUtil.extractEmail(Authorization);
            User user = userService.findUserByEmail(email);
            Post post = postService.getPostById(postId);
            List<Comment> comments = commentService.getAllComments(post);
            return new ResponseEntity<>(CommentListingDTO.fromComments(comments), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(ErrorDTO.fromMessage(e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }







}
