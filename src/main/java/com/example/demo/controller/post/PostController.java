package com.example.demo.controller.post;

import com.example.demo.dto.post.PostDTO;
import com.example.demo.model.Post;
import com.example.demo.model.Tag;
import com.example.demo.model.User;
import com.example.demo.service.PostService;
import com.example.demo.service.TagService;
import com.example.demo.service.UserService;
import com.example.demo.validators.PostInputValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins= "*")
@RequestMapping(path= "/post")
public class PostController {
    private final PostInputValidator postInputValidator = new PostInputValidator();
    private final PostService postService;
    private final UserService userService;
    private  final TagService tagService;

    @Autowired
    public PostController(PostService postService, UserService userService, TagService tagService) {
        this.postService = postService;
        this.userService = userService;
        this.tagService = tagService;
    }

    @PostMapping
    public ResponseEntity<?> createPost(@RequestBody PostDTO possiblePost, @RequestHeader String Authorization ){
        try{
            User user = userService.findUserByUsername(Authorization);
            postInputValidator.checkCreatePost(possiblePost);
            Tag[] tags = tagService.createTags(possiblePost.getTags(), user);
            Post post = postService.create(possiblePost, user, tags);
            return new ResponseEntity<>(post, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(path = "/edit/{postId}", method = RequestMethod.PATCH)
    public ResponseEntity<?> editPost(@PathVariable("postId") Long postId, @RequestBody PostDTO possiblePost, @RequestHeader String Authorization){
        try{
            User user = userService.findUserByUsername(Authorization);
            postInputValidator.checkCreatePost(possiblePost);
            Tag[] tags = tagService.createTags(possiblePost.getTags(), user);
            Post post = postService.editPost(postId, possiblePost, user, tags);
            return new ResponseEntity<>(post, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
