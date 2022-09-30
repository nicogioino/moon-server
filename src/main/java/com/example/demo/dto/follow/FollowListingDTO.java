package com.example.demo.dto.follow;

import com.example.demo.dto.user.UserListingDTO;
import com.example.demo.model.Follow;
import com.example.demo.model.User;

public class FollowListingDTO {
    UserListingDTO follower;
    UserListingDTO follows;



    public static FollowListingDTO fromFollow(Follow follow) {
        FollowListingDTO followListingDTO = new FollowListingDTO();
        followListingDTO.setFollower(UserListingDTO.fromUser(follow.getFollower()));
        followListingDTO.setFollows(UserListingDTO.fromUser(follow.getFollowed()));
        return followListingDTO;
    }

    public UserListingDTO getFollower() {
        return follower;
    }

    public UserListingDTO getFollows() {
        return follows;
    }

    public void setFollower(UserListingDTO follower) {
        this.follower = follower;
    }

    public void setFollows(UserListingDTO follows) {
        this.follows = follows;
    }

}
