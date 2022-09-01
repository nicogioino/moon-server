package com.example.demo.controller.user;

import com.example.demo.model.User;

public class UserInputValidator {
    public boolean checkUpdateInput(UserUpdateDTO userUpdateDTO) throws Exception {
        final Utils utils = new Utils();
        if(userUpdateDTO.getUsername() != null){
            if( ! utils.checkString(userUpdateDTO.getUsername(),3, 16))
                throw new Exception("Invalid fields");

        }
        if(userUpdateDTO.getBiography() != null){
            if(! utils.checkString(userUpdateDTO.getBiography(),0, 100))
                throw new Exception("Invalid fields");
        }
        if(userUpdateDTO.getPassword() != null){
            if( !utils.checkString(userUpdateDTO.getPassword(),8, 20))
                throw new Exception("Invalid fields");
        }
        return true;
    }

    public boolean checkCreationInput(User user) throws Exception {
        if (user.getUsername() == null || user.getPassword() == null || user.getEmail() == null)
            throw new Exception("Missing fields");
        else {
            if (!Utils.checkString(user.getUsername(), 3, 16))
                throw new Exception("Invalid fields");
            if(user.getBio() != null){
                if (!Utils.checkString(user.getBio(), 0, 100))
                    throw new Exception("Invalid fields");
            }
            if (!Utils.checkString(user.getPassword(), 8, 20))
                throw new Exception("Invalid fields");
            if (!Utils.isEmail(user.getEmail())) throw new IllegalArgumentException("Input is not an email");
            return true;
        }
    }

}
