package com.example.demo.service;

import com.example.demo.controller.user.UserUpdateDTO;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Modifying
    public User updateUser(Long userId, UserUpdateDTO userUpdateDTO) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalStateException(
                        "user with id " + userId + " does not exists"
                ));
        if(userUpdateDTO.getUsername() != null){
            user.setUsername(userUpdateDTO.getUsername());
        }
        if(userUpdateDTO.getBiography() != null){
            user.setBio(userUpdateDTO.getBiography());
        }
        if(userUpdateDTO.getPassword() != null){
            user.setPassword(userUpdateDTO.getPassword());
        }
        userRepository.save(user);
        return user;
    }
    public List<User> findAll(){
        return userRepository.findAll();
    }
    public User createUser(User user){
        return userRepository.save(user);
    }
}
