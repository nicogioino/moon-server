package com.example.demo.controller.comment;

import com.example.demo.dto.comment.*;
import com.example.demo.dto.error.ErrorDTO;
import com.example.demo.model.*;
import com.example.demo.security.util.JwtUtil;
import com.example.demo.service.*;
import com.example.demo.validators.CommentInputValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@CrossOrigin(origins= "*")
@RequestMapping(path= "/comment")
public class CommentController {
    private final CommentInputValidator commentInputValidator = new CommentInputValidator();
    private  final TagService tagService;
    private final CommentService commentService;
    private final VoteService voteService;
    @Autowired
    private JwtUtil jwtUtil;
    private PostService postService;
    private UserService userService;

    public CommentController(TagService tagService, CommentService commentService, VoteService voteService, PostService postService, UserService userService) {
        this.tagService = tagService;
        this.commentService = commentService;
        this.voteService = voteService;
        this.postService = postService;
        this.userService = userService;
    }

    @PostMapping("/{postId}")
    public ResponseEntity<?> createComment(@PathVariable("postId") Long postId, @RequestBody CommentDTO possibleComment, @RequestHeader String Authorization ){
        try{
            String email = jwtUtil.extractEmail(Authorization);
            User user = userService.findUserByEmail(email);
            List<Tag> tags = tagService.createTags(possibleComment.getTags(), user);
            Post post = postService.getPostById(postId);
            Comment comment = commentService.create(possibleComment, user, tags, post);
            return new ResponseEntity<>(CommentDTO.fromComment(comment, voteService.countVotesByCommentId(comment.getId())), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(ErrorDTO.fromMessage(e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{postId}")
    public ResponseEntity<?> getAllComments(@PathVariable("postId") Long postId, @RequestHeader String Authorization){
        try{
            String email = jwtUtil.extractEmail(Authorization);
            User user = userService.findUserByEmail(email);
            Post post = postService.getPostById(postId);
            List<Comment> comments = commentService.getAllComments(post.getId());
            List<CommentWithType> commentsWithVotes = voteService.getVotesForComments(comments, user);
            return new ResponseEntity<>(CommentListingTypeDTO.fromComments(commentsWithVotes), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(ErrorDTO.fromMessage(e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<?> deleteComment(@PathVariable("commentId") Long commentId, @RequestHeader String Authorization ){
        try{
            String email = jwtUtil.extractEmail(Authorization);
            User user = userService.findUserByEmail(email);
            Comment comment = commentService.getCommentById(commentId);
            if(!comment.getUser().getId().equals(user.getId())){
                throw new Exception("You are not the owner of this comment");
            }
            commentService.deleteComment(comment);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(ErrorDTO.fromMessage(e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }
    @PostMapping(path = "/upvote/{commentId}")
    public ResponseEntity<?> upvote(@RequestHeader String Authorization, @PathVariable Long commentId) {
        try {
            String email = jwtUtil.extractEmail(Authorization);
            User user = userService.findUserByEmail(email);
            Vote vote = voteService.upvote(commentId, user);
            return new ResponseEntity<>(vote,HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(ErrorDTO.fromMessage(e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }
    @PostMapping(path = "/downvote/{commentId}")
    public ResponseEntity<?> downvote(@RequestHeader String Authorization, @PathVariable Long commentId) {
        try {
            String email = jwtUtil.extractEmail(Authorization);
            User user = userService.findUserByEmail(email);

            Vote vote = voteService.downvote(commentId, user);
            return new ResponseEntity<>(vote,HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(ErrorDTO.fromMessage(e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping(path = "/votes/{commentId}")
    public ResponseEntity<?> getVotesCount(@PathVariable Long commentId) { //devuelve un voteDTO con los counts
        try {
            return new ResponseEntity<>(voteService.countVotesByCommentId(commentId), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(ErrorDTO.fromMessage(e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping(path = "/votes/type/{commentId}")
    public ResponseEntity<?> getVotesCountByType(@PathVariable Long commentId, @RequestBody VoteType voteType) {
        try {
            return new ResponseEntity<>(voteService.countVotesByType(commentId,voteType), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(ErrorDTO.fromMessage(e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping(path = "/upvoted")
    public ResponseEntity<?> getUpvotedCommentsByUser(@RequestHeader String Authorization) {
        try {
            String email = jwtUtil.extractEmail(Authorization);
            User user = userService.findUserByEmail(email);
            return new ResponseEntity<>(voteService.getCommentsByVoteType(user,VoteType.UPVOTE), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(ErrorDTO.fromMessage(e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping(path = "/downvoted")
    public ResponseEntity<?> getDownVotedCommentsByUser(@RequestHeader String Authorization) {
        try {
            String email = jwtUtil.extractEmail(Authorization);
            User user = userService.findUserByEmail(email);
            return new ResponseEntity<>(voteService.getCommentsByVoteType(user,VoteType.DOWNVOTE), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(ErrorDTO.fromMessage(e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }
}
