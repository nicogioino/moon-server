package com.example.demo.controller.post;

import com.example.demo.dto.error.ErrorDTO;
import com.example.demo.dto.post.PostDTO;
import com.example.demo.dto.post.PostListingDTO;
import com.example.demo.dto.react.ReactsListingDTO;
import com.example.demo.model.Post;
import com.example.demo.model.Tag;
import com.example.demo.model.User;
import com.example.demo.security.util.JwtUtil;
import com.example.demo.service.*;
import com.example.demo.validators.PostInputValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@RestController
@CrossOrigin(origins= "*")
@RequestMapping(path= "/post")
public class PostController {
    private final PostInputValidator postInputValidator = new PostInputValidator();
    private final PostService postService;
    private final FollowService followService;
    private final UserService userService;
    private  final TagService tagService;
    private final ReactService reactService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    public PostController(PostService postService, UserService userService, TagService tagService, FollowService followService, ReactService reactService) {
        this.postService = postService;
        this.userService = userService;
        this.tagService = tagService;
        this.followService = followService;
        this.reactService = reactService;
    }

    @PostMapping
    public ResponseEntity<?> createPost(@RequestBody PostDTO possiblePost, @RequestHeader String Authorization ){
        try{
            String email = jwtUtil.extractEmail(Authorization);
            User user = userService.findUserByEmail(email);
            postInputValidator.checkCreatePost(possiblePost);
            ArrayList<Tag> tags = tagService.createTags(possiblePost.getTags(), user);
            Post post = postService.create(possiblePost, user, tags);
            return new ResponseEntity<>(PostListingDTO.fromPost(post), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(ErrorDTO.fromMessage(e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/feed")
    public ResponseEntity<?> getAllPosts(@RequestHeader String Authorization ){
        try{
            String email = jwtUtil.extractEmail(Authorization);
            User user = userService.findUserByEmail(email);
            Long[] usersId = followService.findUsersId(user);
            List<Tag> followedTags = tagService.getTagsFollowedByUser(user);
            Post[] posts = postService.getAllPosts(usersId, user.getId(),followedTags);
            ReactsListingDTO[] reacts = reactService.getReacts(posts);
            return new ResponseEntity<>(PostListingDTO.fromPosts(posts,reacts), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(ErrorDTO.fromMessage(e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping()
    public ResponseEntity<?> getPosts(@RequestHeader String Authorization ){
        try{
            String email = jwtUtil.extractEmail(Authorization);
            User user = userService.findUserByEmail(email);
            Post[] posts = postService.getPostsFrom(user.getId());
            ReactsListingDTO[] reacts = reactService.getReacts(posts);
            return new ResponseEntity<>(PostListingDTO.fromPosts(posts,reacts), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(ErrorDTO.fromMessage(e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping("/post-info/{postId}")
    public ResponseEntity<?> getPost(@PathVariable Long postId, @RequestHeader String Authorization){
        try{
            Post post = postService.getPostById(postId);
            return new ResponseEntity<>(PostListingDTO.fromPost(post), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(ErrorDTO.fromMessage(e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{userId}")
    public ResponseEntity<?> getPostsFrom(@PathVariable("userId") Long userId, @RequestHeader String Authorization ){
        try{
            Post[] posts = postService.getPostsFrom(userId);
            ReactsListingDTO[] reacts = reactService.getReacts(posts);
            return new ResponseEntity<>(PostListingDTO.fromPosts(posts,reacts), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(ErrorDTO.fromMessage(e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @PatchMapping("/{postId}")
    public ResponseEntity<?> editPost(@PathVariable("postId") Long postId, @RequestBody PostDTO possiblePost, @RequestHeader String Authorization){
        try{
            String email = jwtUtil.extractEmail(Authorization);
            User user = userService.findUserByEmail(email);
            postInputValidator.checkCreatePost(possiblePost);
            ArrayList<Tag> tags = tagService.createTags(possiblePost.getTags(), user);
            Post post = postService.editPost(postId, possiblePost, user, tags);
            return new ResponseEntity<>(PostListingDTO.fromPost(post), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(ErrorDTO.fromMessage(e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<?> deletePost(@PathVariable("postId") Long postId, @RequestHeader String Authorization){
        try{
            String email = jwtUtil.extractEmail(Authorization);
            User user = userService.findUserByEmail(email);
            postService.deletePost(postId, user);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }catch (Exception e){
            return new ResponseEntity<>(ErrorDTO.fromMessage(e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(method = RequestMethod.GET, path = "/bookmarks")
    public ResponseEntity<?> getAllBookmarks(@RequestHeader String Authorization) {
        try {
            String email = jwtUtil.extractEmail(Authorization);
            User user = userService.findUserByEmail(email);
            Set<Post> bookmarks = userService.getBookmarkedPosts(user);
            Post[] posts = bookmarks.toArray(new Post[bookmarks.size()]);
            ReactsListingDTO[] reacts = reactService.getReacts(posts);
            return new ResponseEntity<>(PostListingDTO.fromPosts(posts,reacts), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(method= RequestMethod.POST, path= "/bookmarks/{postId}")
    public ResponseEntity<?> bookmarkPost(@PathVariable("postId") Long postId, @RequestHeader String Authorization){
        try{
            String email = jwtUtil.extractEmail(Authorization);
            User user = userService.findUserByEmail(email);
            postService.bookmarkPost(postId, user, userService);
            return new ResponseEntity<>(HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(method= RequestMethod.DELETE, path= "/bookmarks/{postId}")
    public ResponseEntity<?> deleteBookmark(@PathVariable("postId") Long postId, @RequestHeader String Authorization){
        try{
            String email = jwtUtil.extractEmail(Authorization);
            User user = userService.findUserByEmail(email);
            postService.deleteBookmark(postId, user, userService);
            return new ResponseEntity<>(HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

}
