package com.example.demo.service;


import com.example.demo.dto.react.ReactDTO;
import com.example.demo.dto.react.ReactsListingDTO;
import com.example.demo.model.Post;
import com.example.demo.model.React;
import com.example.demo.model.ReactType;
import com.example.demo.model.User;
import com.example.demo.repository.ReactRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReactService {

    private final ReactRepository reactRepository;

    @Autowired
    public ReactService(ReactRepository reactRepository) {
        this.reactRepository = reactRepository;
    }

    public React react(User user, Post post, ReactType reactType) { //A user can react once per reactType
        List<React> existingReact = reactRepository.findByUserIdAndPostId(user.getId(),post.getId());
        if (existingReact == null) {
            React newReact = new React(user, post, reactType);
            return reactRepository.save(newReact);
        } else if(existingReact.stream().noneMatch(react -> react.getReactType() == reactType)){
            React newReact = new React(user, post, reactType);
            return reactRepository.save(newReact);
        }
        return existingReact.stream().filter(react -> react.getReactType() == reactType).findFirst().get();
    }

    public React unReact(User user, Post post,ReactType reactType) {
        List<React> existingReact = reactRepository.findByUserIdAndPostId(user.getId(),post.getId());
        React toUnreact = existingReact.stream().filter(react -> react.getReactType() == reactType).findFirst().orElse(null);
            if (toUnreact != null) {
                reactRepository.delete(toUnreact);
                return toUnreact;
            }
        return toUnreact;
    }
    public Long countReactsByType(Long postId, ReactType reactType){return reactRepository.countReactsByPostIdAndReactType(postId, reactType);}

    public ReactDTO countReactsByPostId(Long postId){
        Long applauseCount = reactRepository.countReactsByPostIdAndReactType(postId, ReactType.APPLAUSE);
        Long likeCount = reactRepository.countReactsByPostIdAndReactType(postId, ReactType.LIKE);
        Long loveCount = reactRepository.countReactsByPostIdAndReactType(postId, ReactType.LOVE);
        return new ReactDTO(applauseCount, likeCount, loveCount);
    }

    public List<React> getReactsByUserAndPost(User user, Long postId) {
        return reactRepository.findByUserIdAndPostId(user.getId(),postId);
    }

    public ReactsListingDTO[] getReacts(Post[] posts) {
        ReactsListingDTO[] reactsListingDTOS = new ReactsListingDTO[posts.length];
        for (int i = 0; i < posts.length; i++) {
            ReactDTO reactDTO = countReactsByPostId(posts[i].getId());
            ReactsListingDTO reactsListingDTO = new ReactsListingDTO(reactDTO.getLikeCount(), reactDTO.getApplauseCount(), reactDTO.getLoveCount());
            reactsListingDTOS[i] = reactsListingDTO;
        }
        return reactsListingDTOS;
    }
}
