package com.example.demo.validators;

import com.example.demo.dto.user.UserCreationDTO;
import com.example.demo.dto.user.UserUpdateDTO;
import com.example.demo.utils.Utils;

public class UserInputValidator {
    public boolean checkUpdateInput(UserUpdateDTO userUpdateDTO) throws Exception {
        final Utils utils = new Utils();
        if(userUpdateDTO.getUsername() != null){
            if( ! utils.checkString(userUpdateDTO.getUsername(),3, 16))
                throw new Exception("Invalid username: should be min length 3 and max length 16");

        }
        if(userUpdateDTO.getBiography() != null){
            if(! utils.checkString(userUpdateDTO.getBiography(),0, 100))
                throw new Exception("Invalid biography : should be max length 100");
        }
        if(userUpdateDTO.getPassword() != null){
            if( !utils.checkString(userUpdateDTO.getPassword(),8, 20))
                throw new Exception("Invalid password: should be min length 8 and max length 20");
        }
        return true;
    }

    public boolean checkCreationInput(UserCreationDTO user) throws Exception {
        if (user.getUsername() == null || user.getPassword() == null || user.getEmail() == null)
            throw new Exception("Missing fields");
        else {
            if (!Utils.checkString(user.getUsername(), 3, 16))
                throw new Exception("Invalid username: should be min length 3 and max length 16");
            if(user.getBio() != null){
                if (!Utils.checkString(user.getBio(), 0, 100))
                    throw new Exception("Invalid biography : should be max length 100");
            }
            if (!Utils.checkString(user.getPassword(), 8, 20))
                throw new Exception("Invalid password: should be min length 8 and max length 20");
            if (!Utils.isEmail(user.getEmail())) throw new IllegalArgumentException("Email format invalid");
            return true;
        }
    }
}
