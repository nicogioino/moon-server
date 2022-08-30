package com.example.demo.controller.user;

import com.example.demo.model.User;

public class UserInputValidator {
    public boolean checkUpdateInput(UserUpdateDTO userUpdateDTO){
        final Utils utils = new Utils();
        if(userUpdateDTO.getUsername() != null){
            if( ! utils.checkString(userUpdateDTO.getUsername(),3, 16))
                throw new Error("Invalid fields");

        }
        if(userUpdateDTO.getBiography() != null){
            if(! utils.checkString(userUpdateDTO.getBiography(),0, 100))
                throw new Error("Invalid fields");
        }
        if(userUpdateDTO.getPassword() != null){
            if( !utils.checkString(userUpdateDTO.getPassword(),8, 20))
                throw new Error("Invalid fields");
        }
        return true;
    }

    public boolean checkCreationInput(User user){
        final Utils utils = new Utils();
        if(user.getUsername() != null){
            if( ! utils.checkString(user.getUsername(),3, 16))
                throw new Error("Invalid fields");

        }
        if(user.getPassword() != null){
            if( !utils.checkString(user.getPassword(),8, 20))
                throw new Error("Invalid fields");
        }
        return true;
    }

}
