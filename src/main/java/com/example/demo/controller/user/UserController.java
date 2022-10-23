package com.example.demo.controller.user;

import com.example.demo.dto.error.ErrorDTO;
import com.example.demo.dto.follow.FollowDTO;
import com.example.demo.dto.follow.FollowListingDTO;
import com.example.demo.dto.tag.TagListingDTO;
import com.example.demo.dto.user.*;
import com.example.demo.model.Follow;
import com.example.demo.model.Post;
import com.example.demo.model.Tag;
import com.example.demo.model.User;
import com.example.demo.security.util.JwtUtil;
import com.example.demo.service.FollowService;
import com.example.demo.service.TagService;
import com.example.demo.service.PostService;
import com.example.demo.service.UserService;
import com.example.demo.validators.UserInputValidator;
import com.example.demo.validators.FollowInputValidator;
import org.apache.tomcat.util.http.parser.Authorization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = "/user")
public class UserController {
    @Autowired
    private JwtUtil jwtUtil;
    private final UserService userService;
    private final TagService tagService;
    private final UserInputValidator userInputValidator = new UserInputValidator();

    private final FollowInputValidator followInputValidator = new FollowInputValidator();
    private final FollowService followService;

    private final PostService postService;

    @Autowired
    public UserController(UserService userService, TagService tagService, FollowService followService, PostService postService) {
        this.userService = userService;
        this.tagService = tagService;
        this.followService = followService;
        this.postService = postService;
    }

    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody UserCreationDTO possibleBody) {
        try {
            userInputValidator.checkCreationInput(possibleBody);
            User user = userService.create(possibleBody);
            return new ResponseEntity<>(UserListingDTO.fromUser(user), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(ErrorDTO.fromMessage(e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(method = RequestMethod.PUT)
    public ResponseEntity<?> updateUser(@RequestBody(required = false) UserUpdateDTO userUpdateDTO, @RequestHeader String Authorization) {
        try {
            String email = jwtUtil.extractEmail(Authorization);
            User user = userService.findUserByEmail(email);
            userInputValidator.checkUpdateInput(userUpdateDTO);
            User response = userService.updateUser(user, userUpdateDTO);
            return new ResponseEntity<>(UserListingDTO.fromUser(response), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(ErrorDTO.fromMessage(e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping
    public ResponseEntity<?> getUserProfile(@RequestHeader String Authorization) {
        try {
            String email = jwtUtil.extractEmail(Authorization);
            User user = userService.findUserByEmail(email);
            return new ResponseEntity<>(UserListingDTO.fromUser(user), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(ErrorDTO.fromMessage(e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(path = "/follow", method = RequestMethod.POST)
    public ResponseEntity<?> follow(@RequestHeader String Authorization, @RequestBody FollowDTO followDTO) {
        try {
            User possibleFollowed = followInputValidator.userExists(userService, followDTO);
            String email = jwtUtil.extractEmail(Authorization);
            User follower = userService.findUserByEmail(email);
            Follow follow = followService.follow(follower, possibleFollowed);
            return new ResponseEntity<>(FollowListingDTO.fromFollow(follow), HttpStatus.OK);
        } catch (Error e) {
            return new ResponseEntity<>(ErrorDTO.fromMessage(e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }
    @RequestMapping(path = "/unfollow", method = RequestMethod.POST)
    public ResponseEntity<?> unfollow(@RequestHeader String Authorization, @RequestBody FollowDTO followDTO) {
        try {
            User possibleFollowed = followInputValidator.userExists(userService, followDTO);
            String email = jwtUtil.extractEmail(Authorization);
            User follower = userService.findUserByEmail(email);
            Follow follow = followService.unfollow(follower, possibleFollowed);
            return new ResponseEntity<>(FollowListingDTO.fromFollow(follow), HttpStatus.OK);
        } catch (Error e) {
            return new ResponseEntity<>(ErrorDTO.fromMessage(e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(path = "/profile/{userId}", method = RequestMethod.GET)
    public ResponseEntity<?> getUserProfile(@PathVariable String userId, @RequestHeader String Authorization) {
        try {
            String email = jwtUtil.extractEmail(Authorization);
            User user = userService.findUserById(userId);
            Post[] posts = postService.getPostsFrom(user.getId());
            Integer followers = followService.findFollowersId(user).length;
            Integer following = followService.findUsersId(user).length; // This returns the ids this user is following
            Integer tagsFollowed = tagService.getTagsFollowedByUser(user).size();
            return new ResponseEntity<>(UserProfileDTO.fromUser(user, posts, followers, following, tagsFollowed), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(ErrorDTO.fromMessage(e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(path = "/my-profile", method = RequestMethod.GET)
    public ResponseEntity<?> getMyProfile(@RequestHeader String Authorization){
        try {
            String email = jwtUtil.extractEmail(Authorization);
            User user = userService.findUserByEmail(email);
            Post[] posts = postService.getPostsFrom(user.getId());
            Integer followers = followService.findFollowersId(user).length;
            Integer following = followService.findUsersId(user).length; // This returns the ids this user is following
            List<Tag> tagsFollowed = tagService.getTagsFollowedByUser(user);
            return new ResponseEntity<>(MyProfileDTO.fromUser(user, posts, followers, following, tagsFollowed), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(ErrorDTO.fromMessage(e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(path = "/hello")
    public Long[] hello(@RequestHeader String Authorization) {
        String email = jwtUtil.extractEmail(Authorization);
        User user = userService.findUserByEmail(email);

        return followService.findFollowersId(user);
    }

    @GetMapping(path = "/followed")
    public ResponseEntity<?> getFollowedUsers(@RequestHeader String Authorization) {
        try {
            String email = jwtUtil.extractEmail(Authorization);
            User user = userService.findUserByEmail(email);
            return new ResponseEntity<>(followService.getFollowedUsers(user), HttpStatus.OK);
        } catch (Error e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping(path = "/followers")
    public ResponseEntity<?> getFollowers(@RequestHeader String Authorization) {
        try {
            String email = jwtUtil.extractEmail(Authorization);
            User user = userService.findUserByEmail(email);
            return new ResponseEntity<>(followService.getFollowers(user), HttpStatus.OK);
        } catch (Error e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping(path = "/tags")
    public ResponseEntity<?> getFollowedTags(@RequestHeader String Authorization) {
        try {
            String email = jwtUtil.extractEmail(Authorization);
            User user = userService.findUserByEmail(email);
            return new ResponseEntity<>(tagService.getTagsFollowedByUser(user), HttpStatus.OK);
        } catch (Error e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping(path = "/tags/created")
    public ResponseEntity<?> getCreatedTags(@RequestHeader String Authorization) {
        try {
            String email = jwtUtil.extractEmail(Authorization);
            User user = userService.findUserByEmail(email);
            return new ResponseEntity<>(tagService.getUserTags(user), HttpStatus.OK);
        } catch (Error e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    @PostMapping(path = "/follow/tag/{tagId}")
    public ResponseEntity<?> followTag(@RequestHeader String Authorization, @PathVariable Long tagId) {
        try {
            String email = jwtUtil.extractEmail(Authorization);
            User user = userService.findUserByEmail(email);
            Tag tag = tagService.getTagById(tagId);
            return new ResponseEntity<>(TagListingDTO.fromTag(userService.followTag(user, tag)), HttpStatus.OK);
        } catch (Error e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    @PostMapping(path = "/unfollow/tag/{tagId}")
    public ResponseEntity<?> unfollowTag(@RequestHeader String Authorization, @PathVariable Long tagId) {
        try {
            String email = jwtUtil.extractEmail(Authorization);
            User user = userService.findUserByEmail(email);
            Tag tag = tagService.getTagById(tagId);
            return new ResponseEntity<>(userService.unfollowTag(user, tag), HttpStatus.OK);
        } catch (Error e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}