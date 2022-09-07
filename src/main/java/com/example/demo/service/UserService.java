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

import java.util.Optional;


@Service
public class UserService{
    private final UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

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
        if (accountAlreadyExists(userDto)) {
            throw new IllegalStateException("User already exists");
        }
        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setEmail(userDto.getEmail());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setBio(userDto.getBio());
        return userRepository.save(user);
    }

    private boolean accountAlreadyExists(UserCreationDTO user) {
        return userRepository.existsByEmail(user.getEmail()) || userRepository.existsByUsername(user.getUsername());
    }

    public User getUserById(Long id) {
        Optional<User> user = userRepository.findById(id);
        if(user.isEmpty()){
            throw new IllegalStateException("User does not exists");
        }else{
            return user.get();
        }
    }
    public User findUserByUsername(String header) {
        String token = header.substring(7);
        String username = jwtUtil.extractUsername(token);
        return userRepository.getUserByUsername(username).orElseThrow(() -> new IllegalStateException(
                "user with username " + username + " does not exists"
        ));
    }

}
