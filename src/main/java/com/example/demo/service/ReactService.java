package com.example.demo.service;


import com.example.demo.dto.react.ReactDTO;
import com.example.demo.model.Post;
import com.example.demo.model.React;
import com.example.demo.model.ReactType;
import com.example.demo.model.User;
import com.example.demo.repository.ReactRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReactService {

    private final ReactRepository reactRepository;

    @Autowired
    public ReactService(ReactRepository reactRepository) {
        this.reactRepository = reactRepository;
    }

    public React react(User user, Post post, ReactType reactType) { //Assumes a user can only give one react to a post, regardless of type
        React existingReact = reactRepository.findByUserIdAndPostId(user.getId(),post.getId());
        if (existingReact == null) {
            React newReact = new React(user, post, reactType);
            return reactRepository.save(newReact);
        } else { //Overrides the existing React with a new React (Changes ReactType)
            unReact(user,post);
            React newReact = new React(user, post, reactType);
            return reactRepository.save(newReact);
        }
    }

    public React unReact(User user, Post post) {
        React existingReact = reactRepository.findByUserIdAndPostId(user.getId(),post.getId());
        if (existingReact != null) {
            reactRepository.delete(existingReact);
        }
        return existingReact;
    }
    public Long countReactsByType(Long postId, ReactType reactType){return reactRepository.countReactsByPostIdAndReactType(postId, reactType);}

    public ReactDTO countReactsByPostId(Long postId){
        Long applauseCount = reactRepository.countReactsByPostIdAndReactType(postId, ReactType.APPLAUSE);
        Long likeCount = reactRepository.countReactsByPostIdAndReactType(postId, ReactType.LIKE);
        Long loveCount = reactRepository.countReactsByPostIdAndReactType(postId, ReactType.LOVE);
        return new ReactDTO(applauseCount, likeCount, loveCount);
    }

    public React getReactsByUserAndPost(User user, Long postId) {
        return reactRepository.findByUserIdAndPostId(user.getId(),postId);
    }
}
