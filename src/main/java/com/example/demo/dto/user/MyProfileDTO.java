package com.example.demo.dto.user;

import com.example.demo.dto.post.PostListingDTO;
import com.example.demo.dto.react.ReactsListingDTO;
import com.example.demo.dto.tag.TagListingDTO;
import com.example.demo.model.Post;
import com.example.demo.model.Tag;
import com.example.demo.model.User;

import java.util.List;

public class MyProfileDTO {

    private Long id;
    private String username;
    private String bio;
    private PostListingDTO[] posts;
    private Integer following;
    private Integer followers;
    private List<TagListingDTO> followedTags;

    public static MyProfileDTO fromUser(User user, Post[] posts, ReactsListingDTO[] reacts, Integer followers, Integer following, List<Tag> tagsFollowed) {
        MyProfileDTO myProfileDTO = new MyProfileDTO();
        myProfileDTO.setId(user.getId());
        myProfileDTO.setUsername(user.getUsername());
        myProfileDTO.setBio(user.getBio());
        myProfileDTO.setPosts(PostListingDTO.fromPosts(posts, reacts));
        myProfileDTO.setFollowers(followers);
        myProfileDTO.setFollowing(following);
        List<TagListingDTO> tagsDTOs = TagListingDTO.fromTags(tagsFollowed);
        myProfileDTO.setFollowedTags(tagsDTOs);
        return myProfileDTO;
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

    public List<TagListingDTO> getFollowedTags() {
        return followedTags;
    }

    public void setFollowedTags(List<TagListingDTO> followedTags) {
        this.followedTags = followedTags;
    }
}
