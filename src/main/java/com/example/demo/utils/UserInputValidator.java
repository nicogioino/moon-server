package com.example.demo.utils;

import com.example.demo.dto.user.UserCreationDTO;
import com.example.demo.dto.user.UserUpdateDTO;

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
            throw new Exception("Campos faltantes");
        else {
            if (!Utils.checkString(user.getUsername(), 3, 16))
                throw new Exception("El nombre de usuario debe tener entre 3 y 16 caracteres");
            if(user.getBio() != null){
                if (!Utils.checkString(user.getBio(), 0, 100))
                    throw new Exception("Largo maximo de la Bio 100 caracteres");
            }
            if (!Utils.checkString(user.getPassword(), 8, 20))
                throw new Exception("La contraseña debe tener entre 8 y 20 caracteres");
            if (!Utils.isEmail(user.getEmail())) throw new IllegalArgumentException("Formato de email invalido");
            return true;
        }
    }
}
