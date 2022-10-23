package com.example.demo.dto.user;

import com.example.demo.dto.post.PostDTO;
import com.example.demo.dto.post.PostListingDTO;
import com.example.demo.dto.react.ReactsListingDTO;
import com.example.demo.model.Post;
import com.example.demo.model.User;

import java.util.List;

public class UserProfileDTO {
    private Long id;
    private String username;
    private String bio;
    private PostListingDTO[] posts;
    private Integer following;
    private Integer followers;
    private Integer followedTags;

    public static UserProfileDTO fromUser(User user, Post[] posts, ReactsListingDTO[] reacts, Integer followers, Integer following, Integer tagsFollowed) {
        UserProfileDTO userProfileDTO = new UserProfileDTO();
        userProfileDTO.setId(user.getId());
        userProfileDTO.setUsername(user.getUsername());
        userProfileDTO.setBio(user.getBio());
        userProfileDTO.setPosts(PostListingDTO.fromPosts(posts, reacts));
        userProfileDTO.setFollowers(followers);
        userProfileDTO.setFollowing(following);

        userProfileDTO.setFollowedTags(tagsFollowed);
        return userProfileDTO;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public PostListingDTO[] getPosts() {
        return posts;
    }

    public void setPosts(PostListingDTO[] posts) {
        this.posts = posts;
    }

    public Integer getFollowing() {
        return following;
    }

    public void setFollowing(Integer following) {
        this.following = following;
    }

    public Integer getFollowers() {
        return followers;
    }

    public void setFollowers(Integer followers) {
        this.followers = followers;
    }

    public Integer getFollowedTags() {
        return followedTags;
    }

    public void setFollowedTags(Integer followedTags) {
        this.followedTags = followedTags;
    }
}
