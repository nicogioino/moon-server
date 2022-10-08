package com.example.demo.controller.user;

import com.example.demo.dto.follow.FollowDTO;
import com.example.demo.dto.follow.FollowListingDTO;
import com.example.demo.dto.user.UserCreationDTO;
import com.example.demo.dto.user.UserListingDTO;
import com.example.demo.dto.user.UserUpdateDTO;
import com.example.demo.dto.user.UserUpdatePasswordDTO;
import com.example.demo.model.Follow;
import com.example.demo.model.PasswordResetToken;
import com.example.demo.model.User;
import com.example.demo.repository.PasswordTokenRepository;
import com.example.demo.security.util.JwtUtil;
import com.example.demo.service.FollowService;
import com.example.demo.service.TagService;
import com.example.demo.service.UserService;
import com.example.demo.validators.UserInputValidator;
import com.example.demo.validators.FollowInputValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Calendar;
import java.util.Locale;
import java.util.Optional;
import java.util.UUID;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = "/user")
public class UserController {
    @Autowired
    private JwtUtil jwtUtil;
    private final UserService userService;
    private final UserInputValidator userInputValidator = new UserInputValidator();

    private final FollowInputValidator followInputValidator = new FollowInputValidator();

    private final PasswordTokenRepository passwordTokenRepository;
    private final FollowService followService;

    private final TagService tagService;

    @Autowired
    public UserController(UserService userService, FollowService followService, TagService tagService, PasswordTokenRepository passwordTokenRepository) {
        this.userService = userService;
        this.followService = followService;
        this.tagService = tagService;
        this.passwordTokenRepository = passwordTokenRepository;
    }

    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody UserCreationDTO possibleBody) {
        try {
            userInputValidator.checkCreationInput(possibleBody);
            User user = userService.create(possibleBody);
            return new ResponseEntity<>(UserListingDTO.fromUser(user), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(method = RequestMethod.PUT)
    public ResponseEntity<?> updateUser(@RequestBody(required = false) UserUpdateDTO userUpdateDTO, @RequestHeader String Authorization) {
        try {
            String email = jwtUtil.extractEmail(Authorization);
            User user = userService.findUserByEmail(email);
            userInputValidator.checkUpdateInput(userUpdateDTO);
            User response = userService.updateUser(user, userUpdateDTO);
            return new ResponseEntity<>(UserListingDTO.fromUser(response), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping
    public ResponseEntity<?> getUserProfile(@RequestBody String Authorization) {
        try {
            String email = jwtUtil.extractEmail(Authorization);
            User user = userService.findUserByEmail(email);
            return new ResponseEntity<>(user, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(path = "/follow", method = RequestMethod.POST)
    public ResponseEntity<?> follow(@RequestHeader String Authorization, @RequestBody FollowDTO followDTO) {
        try {
            User possibleFollowed = followInputValidator.userExists(userService, followDTO);
            String email = jwtUtil.extractEmail(Authorization);
            User follower = userService.findUserByEmail(email);
            Follow follow = followService.follow(follower, possibleFollowed);
            return new ResponseEntity<>(FollowListingDTO.fromFollow(follow), HttpStatus.OK);
        } catch (Error e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    @RequestMapping(path = "/unfollow", method = RequestMethod.POST)
    public ResponseEntity<?> unfollow(@RequestHeader String Authorization, @RequestBody FollowDTO followDTO) {
        try {
            User possibleFollowed = followInputValidator.userExists(userService, followDTO);
            String email = jwtUtil.extractEmail(Authorization);
            User follower = userService.findUserByEmail(email);
            Follow follow = followService.unfollow(follower, possibleFollowed);
            return new ResponseEntity<>(FollowListingDTO.fromFollow(follow), HttpStatus.OK);
        } catch (Error e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    @PostMapping("/user/resetPassword")
    public ResponseEntity<?> resetPassword(HttpServletRequest request, @RequestParam("email") String userEmail) {
        User user = userService.findUserByEmail(userEmail);
        if (user == null) {
            return new ResponseEntity<>("Email not found", HttpStatus.BAD_REQUEST);
        }
        String token = UUID.randomUUID().toString();
        userService.createPasswordResetTokenForUser(user, token);
        mailSender.send(constructResetTokenEmail(getAppUrl(request),
                request.getLocale(), token, user));
        return new GenericResponse(
                messages.getMessage("message.resetPasswordEmail", null,
                        request.getLocale()));
    }
    @GetMapping
    public ResponseEntity<?> getFollowedUsers(@RequestHeader String Authorization) {
        try {
            String email = jwtUtil.extractEmail(Authorization);
            User user = userService.findUserByEmail(email);
            return new ResponseEntity<>(followService.getFollowedUsers(user), HttpStatus.OK);
        } catch (Error e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping
    public ResponseEntity<?> getFollowedTags(@RequestHeader String Authorization) {
        try {
            String email = jwtUtil.extractEmail(Authorization);
            User user = userService.findUserByEmail(email);
            return new ResponseEntity<>(tagService.getUserTags(user), HttpStatus.OK);
        } catch (Error e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping("/user/changePassword")
    public String showChangePasswordPage(Locale locale, Model model, @RequestParam("token") String token) {
        String result = securityService.validatePasswordResetToken(token);
        if(result != null) {
            String message = messages.getMessage("auth.message." + result, null, locale);
            return "redirect:/login.html?lang="
                    + locale.getLanguage() + "&message=" + message;
        } else {
            model.addAttribute("token", token);
            return "redirect:/updatePassword.html?lang=" + locale.getLanguage();
        }
    }
    public String validatePasswordResetToken(String token) {
        final PasswordResetToken passToken = passwordTokenRepository.findByToken(token);

        return !isTokenFound(passToken) ? "invalidToken"
                : isTokenExpired(passToken) ? "expired"
                : null;
    }
    @PostMapping("/user/savePassword")
    public ResponseEntity<?> savePassword(final Locale locale, @Valid UserUpdatePasswordDTO passwordDto) {

        String result = securityUserService.validatePasswordResetToken(passwordDto.getToken());

        if(result != null) {
            return new ResponseEntity<>(messages.getMessage(
                    "auth.message." + result, null, locale));
        }

        Optional<User> user = Optional.ofNullable(userService.getUserByPasswordResetToken(passwordDto.getToken()));
        if(user.isPresent()) {
            userService.changeUserPassword(user.get(), passwordDto.getNewPassword());
            return new ResponseEntity<>(messages.getMessage(
                    "message.resetPasswordSuc", null, locale));
        } else {
            return new ResponseEntity<>(messages.getMessage(
                    "auth.message.invalid", null, locale));
        }
    }

    private boolean isTokenFound(PasswordResetToken passToken) {
        return passToken != null;
    }

    private boolean isTokenExpired(PasswordResetToken passToken) {
        final Calendar cal = Calendar.getInstance();
        return passToken.getExpiryDate().before(cal.getTime());
    }


}