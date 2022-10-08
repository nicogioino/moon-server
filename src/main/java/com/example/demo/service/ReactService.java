package com.example.demo.service;

import com.example.demo.model.Post;
import com.example.demo.model.React;
import com.example.demo.model.ReactType;
import com.example.demo.model.User;
import com.example.demo.repository.PostRepository;
import com.example.demo.repository.ReactRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ReactService {

    private final ReactRepository reactRepository;

    @Autowired
    public ReactService(ReactRepository reactRepository) {
        this.reactRepository = reactRepository;
    }

    public React createIfNotExists(User user, Post post, ReactType reactType) {
        Optional<React> existingReact = reactRepository.findByPostIdAndReactType(post.getId(),reactType);

        if (existingReact.isEmpty()) {
            React react = new React(user,post , reactType);
            return reactRepository.save(react);
        } else {
            return react(user,post,reactType);
        }
    }

    public React react(User user, Post post, ReactType reactType) { //Assumes a user can only give one react to a post, regardless of type
        React existingReact = reactRepository.findByUserIdAndPostId(user.getId(),post.getId());
        if (existingReact == null) {
            React newReact = new React(user, post, reactType);
            return reactRepository.save(newReact);
        } else {
            return existingReact;
        }
    }

    public void unReact(User user, Post post) {
        React existingReact = reactRepository.findByUserIdAndPostId(user.getId(),post.getId());
        if (existingReact != null) {
            reactRepository.delete(existingReact);
        }
    }
    public Long countReactsByType(Long postId, ReactType type){return reactRepository.countReactsByPostIdAndReactType(postId, type);}
}
