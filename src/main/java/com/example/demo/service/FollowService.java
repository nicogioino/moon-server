package com.example.demo.service;

import com.example.demo.dto.follow.FollowersDTO;
import com.example.demo.model.Follow;
import com.example.demo.model.User;
import com.example.demo.repository.FollowRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class FollowService {
    private final FollowRepository followRepository;

    @Autowired
    public FollowService(FollowRepository followRepository) {
        this.followRepository = followRepository;
    }
    public Follow follow(User follower, User possibleFollowed) {
       if( checkIfFollowExists(follower, possibleFollowed).isPresent()){
           throw new Error("You can't follow a user that you already follow");
       }else{
           Follow follow = new Follow(follower, possibleFollowed);
           return followRepository.save(follow);
       }
    }

    private Optional<Follow> checkIfFollowExists(User follower, User possibleFollowed) {
        return  followRepository.ckeckIfFollowExists(follower.getId(), possibleFollowed.getId());
    }

    public Follow unfollow(User follower, User possibleFollowed) {
        Optional<Follow> follow = checkIfFollowExists(follower, possibleFollowed) ;
        if(follow.isPresent()){
           Follow realFollowed = follow.get();
           realFollowed.deleteRelation();
           return followRepository.save(realFollowed);
        }else{
            throw new Error("You can't unfollow a user that you don't follow");
        }
    }

    // Returns the IDs of users I follow.
    public Long[] findUsersId(User user) {
        Long[] ids = followRepository.findFollows(user.getId());
        return ids;
    }

    public List<FollowersDTO> getFollowedUsers(User user) {
        List<FollowersDTO> followersDTOS = new ArrayList<>();
        Optional<List<User>> followedUsers = followRepository.findFollowedUsersByFollowerId(user.getId());
        if(followedUsers.isPresent()){
            for (User followedUser : followedUsers.get()) {
                followersDTOS.add(new FollowersDTO(followedUser.getId(), followedUser.getUsername()));
            }
        }
        return followersDTOS;
    }
    public List<FollowersDTO> getFollowers(User user) {
        List<FollowersDTO> followersDTOS = new ArrayList<>();
        Optional<List<User>> followedUsers = followRepository.findFollowersByFollowedId(user.getId());
        if(followedUsers.isPresent()){
            for (User followedUser : followedUsers.get()) {
                followersDTOS.add(new FollowersDTO(followedUser.getId(), followedUser.getUsername()));
            }
        }
            return followersDTOS;
    }

    // Returns the IDs of users that follow me.
    public Long[] findFollowersId(User user) {
        Long[] ids = followRepository.findFollowers(user.getId());
        return ids;
    }
}
