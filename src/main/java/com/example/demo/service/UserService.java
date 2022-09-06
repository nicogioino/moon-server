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
    private JwtUtil jwtUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

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

    public User findUserByUsername(String header) {
        String token = header.substring(7);
        String username = jwtUtil.extractUsername(token);
        return userRepository.getUserByUsername(username).orElseThrow(() -> new IllegalStateException(
                "user with username " + username + " does not exists"
        ));
    }

    public User create(UserCreationDTO userDto) {
//        Optional<User> search = userRepository.getUserByEmailOrUsername(userDto.getEmail(), userDto.getUsername());
//        if(search.isEmpty()) return userRepository.save(user);
//        else throw new IllegalStateException("User already exists");


        if (accountAlreadyExists(userDto)) {
            throw new IllegalStateException("User already exists");
        }
        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setEmail(userDto.getEmail());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setBio(userDto.getBio());

        //user.setRole(new Role(Integer.valueOf(1), user));
        return userRepository.save(user);
    }

    private boolean accountAlreadyExists(UserCreationDTO user) {
        return userRepository.existsByEmail(user.getEmail()) || userRepository.existsByUsername(user.getUsername());
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
