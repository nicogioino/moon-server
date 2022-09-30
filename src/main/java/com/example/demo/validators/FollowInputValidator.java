package com.example.demo.validators;

import com.example.demo.dto.follow.FollowDTO;
import com.example.demo.model.User;
import com.example.demo.service.FollowService;
import com.example.demo.service.UserService;

public class FollowInputValidator {

    public User userExists(UserService userService, FollowDTO followDTO ){
        try {
            if(followDTO.getEmail() != null && followDTO.getEmail() != " "){
                return userService.findUserByEmail(followDTO.getEmail());
            }else{
                throw new Error("Invalid follow, missing email field");
            }
        }catch (Error e){
            throw new Error(e.getMessage());
        }

    }
}
