package com.example.demo.service;

import com.example.demo.dto.post.PostDTO;
import com.example.demo.model.Post;
import com.example.demo.model.Tag;
import com.example.demo.model.User;
import com.example.demo.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Array;
import java.util.ArrayList;

@Component
public class PostService {
    private final PostRepository postRepository;

    @Autowired
    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }
    public Post create(PostDTO postWithTags, User user, ArrayList<Tag> tags) {
        Post post = new Post(postWithTags.getTitle(), postWithTags.getText(), user);
        post.getTags().addAll(tags);
        return postRepository.save(post);
    }
}
