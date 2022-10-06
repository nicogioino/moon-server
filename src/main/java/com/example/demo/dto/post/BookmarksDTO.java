package com.example.demo.dto.post;

import com.example.demo.model.Post;

import java.util.Set;

public class BookmarksDTO {
    private Set<Post> posts;

    public void setPosts(Set<Post> posts) {
        this.posts = posts;
    }

    public Set<Post> getPosts() {
        return posts;
    }

    public static BookmarksDTO fromPosts(Set<Post> posts) {
        BookmarksDTO bookmarksDTO = new BookmarksDTO();
        bookmarksDTO.setPosts(posts);
        return bookmarksDTO;
    }
}
