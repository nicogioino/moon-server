package com.example.demo.service;

import com.example.demo.controller.user.UserUpdateDTO;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Component;

import java.util.Optional;

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

    public User create(User user) {
        Optional<User> search = userRepository.getUserByEmailOrUsername(user.getEmail(), user.getUsername());
        if(search.isEmpty()) return userRepository.save(user);
        else throw new IllegalStateException("User already exists");
    }

    public User getUserById(Long id ){
        Optional<User> user =  userRepository.findById(id);
        if(!user.isEmpty()) return user.get();
        else throw new IllegalStateException("User does not exist");
    }
}
