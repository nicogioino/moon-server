package com.example.demo.service;

import com.example.demo.dto.user.UserCreationDTO;
import com.example.demo.dto.user.UserUpdateDTO;
import com.example.demo.model.PasswordResetToken;
import com.example.demo.model.Post;
import com.example.demo.model.Tag;
import com.example.demo.model.User;
import com.example.demo.repository.PasswordResetTokenRepository;
import com.example.demo.repository.TagRepository;
import com.example.demo.repository.UserRepository;

import com.example.demo.validators.UserInputValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Optional;
import java.util.Random;
import java.util.Set;


@Service
public class UserService{
    private final UserRepository userRepository;
    private final TagRepository tagRepository;
    private final PasswordResetTokenRepository passwordResetTokenRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, TagRepository tagRepository, PasswordResetTokenRepository passwordResetTokenRepository) {
        this.userRepository = userRepository;
        this.tagRepository = tagRepository;
        this.passwordResetTokenRepository = passwordResetTokenRepository;
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

    public String createPasswordResetTokenForUser(final User user) {
        final PasswordResetToken myToken = new PasswordResetToken(getRandomNumberToken(), user);
        passwordResetTokenRepository.save(myToken);
        return myToken.getToken();
    }
    public PasswordResetToken getPasswordResetToken(final String token) {
        return passwordResetTokenRepository.findByToken(token);
    }
    private String getRandomNumberToken(){
        Random rnd = new Random();
        int number = rnd.nextInt(999999);
        return  String.format("%06d", number);
    }
    public Optional<User> getUserByPasswordResetToken(final String token) {
        return Optional.ofNullable(passwordResetTokenRepository.findByToken(token) .getUser());
    }
    public void changeUserPassword(final User user, final String password) throws Exception {
        UserInputValidator validator = new UserInputValidator();
        boolean valid = validator.checkPassword(password);
        if (valid) {
            user.setPassword(passwordEncoder.encode(password));
            userRepository.save(user);
        } else {
            throw new Exception("La contraseña no cumple con los requisitos");
        }
    }

    public boolean checkIfValidOldPassword(final User user, final String oldPassword) {
        return passwordEncoder.matches(oldPassword, user.getPassword());
    }
    public String validatePasswordResetToken(String token) {
        final PasswordResetToken passToken = passwordResetTokenRepository.findByToken(token);

        return !isTokenFound(passToken) ? "invalidToken"
                : isTokenExpired(passToken) ? "expired"
                : "valid";
    }

    private boolean isTokenFound(PasswordResetToken passToken) {
        return passToken != null;
    }

    private boolean isTokenExpired(PasswordResetToken passToken) {
        final Calendar cal = Calendar.getInstance();
        return passToken.getExpiryDate().before(cal.getTime());
    }

    public User findUserByEmail(String email) {
        return userRepository.getUserByEmail(email).orElseThrow(() -> new Error(
                "user with email " + email + " does not exists"
        ));
    }

    public void save(User user) {
        userRepository.save(user);
    }

    public Set<Post> getBookmarkedPosts(User user) {
        Set<Post> posts = user.getBookmarkedPosts();
        posts.removeIf(Post::isDeleted);
        return posts;
    }
    public Tag followTag(User user, Tag tag) {
        tag.getFollowers().add(user);
        tagRepository.save(tag);
        return tag;
    }
    public Tag unfollowTag(User user, Tag tag) {
        tag.getFollowers().remove(user);
        tagRepository.save(tag);
        return tag;
    }
    public User findUserById(String userId) {
        return userRepository.findById(Long.parseLong(userId)).orElseThrow(() -> new IllegalStateException(
                "user with id " + userId + " does not exists"
        ));
    }
}
