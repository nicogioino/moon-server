package com.example.demo.utils;

import com.example.demo.dto.user.UserCreationDTO;
import com.example.demo.dto.user.UserUpdateDTO;
import com.example.demo.utils.Utils;

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

    public boolean checkCreationInput(UserCreationDTO user) throws Exception {
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

//    public void checkLoginInput(LoginRequest req) {
//        if (req.getPrincipal() == null || req.getCredential() == null)
//            throw new IllegalArgumentException("Missing fields");
//        else {
//            if (!Utils.checkString(req.getPrincipal(), 3, 16))
//                throw new IllegalArgumentException("Invalid fields");
//            if (!Utils.checkString(req.getCredential(), 8, 20))
//                throw new IllegalArgumentException("Invalid fields");
//        }
//    }
}
