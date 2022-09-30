package com.example.demo.service;

import com.example.demo.dto.user.UserCreationDTO;
import com.example.demo.dto.user.UserUpdateDTO;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;

import com.example.demo.security.util.JwtUtil;
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


    public User create(UserCreationDTO userDto) {
        if (usernameAlreadyExists(userDto)) {
            throw new IllegalStateException("Nombre de usuario no disponible");
        }
        if (emailAlreadyExists(userDto)) {
            throw new IllegalStateException("Email no disponible");
        }
        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setEmail(userDto.getEmail());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setBio(userDto.getBio());
        return userRepository.save(user);
    }

    private boolean usernameAlreadyExists(UserCreationDTO user) {
        return userRepository.existsByUsername(user.getUsername());
    }

    private boolean emailAlreadyExists(UserCreationDTO user) {
        return userRepository.existsByEmail(user.getEmail());
    }

    public User findUserByEmail(String email) {
        return userRepository.getUserByEmail(email).orElseThrow(() -> new IllegalStateException(
                "user with email " + email + " does not exists"
        ));
    }

}
