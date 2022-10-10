package com.example.demo.service;

import com.example.demo.dto.user.UserCreationDTO;
import com.example.demo.dto.user.UserUpdateDTO;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;



@Service
public class UserService{
    private final UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Modifying
    public User updateUser(User user, UserUpdateDTO userUpdateDTO) {
        if(userUpdateDTO.getUsername() != null){
            if (usernameAlreadyExists(userUpdateDTO.getUsername())) {
                throw new IllegalStateException("El nombre de usuario ya esta en uso");
            }
            user.setUsername(userUpdateDTO.getUsername());
        }
        if(userUpdateDTO.getBiography() != null){
            user.setBio(userUpdateDTO.getBiography());
        }
        if(userUpdateDTO.getPassword() != null){
            user.setPassword(passwordEncoder.encode(userUpdateDTO.getPassword()));
        }
        userRepository.save(user);
        return user;
    }


    public User create(UserCreationDTO userDto) {
        if (usernameAlreadyExists(userDto.getUsername())) {
            throw new IllegalStateException("El nombre de usuario ya esta en uso");
        }
        if (emailAlreadyExists(userDto.getEmail())) {
            throw new IllegalStateException("El email ya esta en uso");
        }
        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setEmail(userDto.getEmail());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setBio(userDto.getBio());
        return userRepository.save(user);
    }

    private boolean usernameAlreadyExists(String username) {
        return userRepository.existsByUsername(username);
    }

    private boolean emailAlreadyExists(String email) {
        return userRepository.existsByEmail(email);
    }

    public User findUserByEmail(String email) {
        return userRepository.getUserByEmail(email).orElseThrow(() -> new IllegalStateException(
                "user with email " + email + " does not exists"
        ));
    }

    public User findUserById(String userId) {
        return userRepository.findById(Long.parseLong(userId)).orElseThrow(() -> new IllegalStateException(
                "user with id " + userId + " does not exists"
        ));
    }
}
