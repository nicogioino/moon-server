package com.example.demo.controller.user;

import com.example.demo.model.User;
import com.example.demo.service.UserService;
import com.example.demo.validators.UserInputValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins= "*")
@RequestMapping(path= "/user")
public class UserController {
    private final UserService userService;
    private final UserInputValidator userInputValidator = new UserInputValidator();
    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> createUser(@RequestBody User poosibleBody){
        try{
            userInputValidator.checkCreationInput(poosibleBody);
            User user = userService.create(poosibleBody);
            return new ResponseEntity<>(user, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }


    @RequestMapping( method = RequestMethod.PUT, path = "{userId}" )
    public   ResponseEntity<?> updateUser(@PathVariable("userId") Long userId,
                                                @RequestBody(required = false) UserUpdateDTO userUpdateDTO
                              ) {
        try {
            userInputValidator.checkUpdateInput(userUpdateDTO);
            User user = userService.updateUser(userId,userUpdateDTO);
            return new ResponseEntity<>(user, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }


    }

}