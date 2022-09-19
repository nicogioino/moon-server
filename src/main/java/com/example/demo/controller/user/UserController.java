package com.example.demo.controller.user;

import com.example.demo.dto.user.UserCreationDTO;
import com.example.demo.dto.user.UserListingDTO;
import com.example.demo.dto.user.UserUpdateDTO;
import com.example.demo.model.User;
import com.example.demo.service.UserService;
import com.example.demo.utils.UserInputValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = "/user")
public class UserController {
    private final UserService userService;
    private final UserInputValidator userInputValidator = new UserInputValidator();

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
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
            User user = userService.findUserByUsername(Authorization);
            userInputValidator.checkUpdateInput(userUpdateDTO);
            User response = userService.updateUser(user, userUpdateDTO);
            return new ResponseEntity<>(UserListingDTO.fromUser(response), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping
    public ResponseEntity<?> getUserProfile(@RequestHeader String Authorization) {
        try {
            User user = userService.findUserByUsername(Authorization);
            return new ResponseEntity<>(user, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}