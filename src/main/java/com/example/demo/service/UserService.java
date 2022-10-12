package com.example.demo.service;

import com.example.demo.dto.user.UserCreationDTO;
import com.example.demo.dto.user.UserUpdateDTO;
import com.example.demo.model.PasswordResetToken;
import com.example.demo.model.Tag;
import com.example.demo.model.User;
import com.example.demo.repository.PasswordTokenRepository;
import com.example.demo.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;


@Service
public class UserService{
    private final UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private final PasswordTokenRepository passwordTokenRepository;

    public UserService(UserRepository userRepository, PasswordTokenRepository passwordTokenRepository) {
        this.userRepository = userRepository;
        this.passwordTokenRepository = passwordTokenRepository;
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
            user.setPassword(passwordEncoder.encode(userUpdateDTO.getPassword()));
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
    public ArrayList<Tag> getFollowedTags(User user) {
        return userRepository.findFollowedTagsById(user.getId());
    }

    public void createPasswordResetTokenForUser(User user, String token) {
        PasswordResetToken myToken = new PasswordResetToken(token, user);
        passwordTokenRepository.save(myToken);
    }

   /* private SimpleMailMessage constructResetTokenEmail(String contextPath, Locale locale, String token, User user) {
        String url = contextPath + "/user/changePassword?token=" + token;
        String message = messages.getMessage("message.resetPassword",
                null, locale);
        return constructEmail("Reset Password", message + " \r\n" + url, user);
    }

    private SimpleMailMessage constructEmail(String subject, String body, User user) {
        SimpleMailMessage email = new SimpleMailMessage();
        email.setSubject(subject);
        email.setText(body + " \r\n" + getRandomNumberString());
        email.setTo(user.getEmail());
        email.setFrom("support@moon.com"); //Get an actual email
        return email;
    }
    public User getUserByPasswordResetToken(String token) {
        return passwordTokenRepository.findByToken(token).getUser();
    }
    public void changeUserPassword(User user, String password) {
        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);
    }

    private String getRandomNumberString() {
        Random rnd = new Random();
        int number = rnd.nextInt(999999);
        return String.format("%06d", number);
    }*/
}
