package com.example.demo.service;

import com.example.demo.controller.user.UserUpdateDTO;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;

import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class UserService{
    private final UserRepository userRepository;

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





//    public ResponseEntity<?> register(LoginRequest signUpRequest) { //A new user needs to be registered with all the fields
//        if (userRepository.existsByUsername(signUpRequest.getPrincipal())) {
//            return ResponseEntity
//                    .badRequest()
//                    .body("Error: Username is already taken!");
//        }
//
//        if (userRepository.existsByEmail(signUpRequest.getPrincipal())) {
//            return ResponseEntity
//                    .badRequest()
//                    .body("Error: Email is already in use!");
//        }
//
//        // Create new user's account
//        User user = new User(signUpRequest.getPrincipal(), //All fields are required. Throws SQL Error
//                signUpRequest.getCredential());
//
//        userRepository.save(user);
//
//        return authenticate(signUpRequest);
//    }
}
