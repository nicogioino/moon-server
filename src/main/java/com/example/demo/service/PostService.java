package com.example.demo.service;

import com.example.demo.controller.post.PostCreation;
import com.example.demo.model.Post;
import com.example.demo.model.Tag;
import com.example.demo.model.User;
import com.example.demo.repository.PostRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class PostService {
    private final PostRepository postRepository;

    @Autowired
    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }
    public Post create(PostCreation postWithTags, User user, Tag[] tags) {
        Post post = new Post(postWithTags.getTitle(), postWithTags.getText(), user);
        for (int i = 0; i < tags.length; i++) {
            System.out.println(tags[i]);
            post.getTags().add(tags[i]);
        }
        return postRepository.save(post);
    }
}
