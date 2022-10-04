package com.example.demo.validators;

import com.example.demo.dto.user.UserCreationDTO;
import com.example.demo.dto.user.UserUpdateDTO;
import com.example.demo.utils.Utils;

public class UserInputValidator {
    private Utils utils = new Utils();
    public boolean checkUpdateInput(UserUpdateDTO userUpdateDTO) throws Exception {
        return checkUsername(userUpdateDTO.getUsername()) && checkBiography(userUpdateDTO.getBiography()) && checkPassword(userUpdateDTO.getPassword());
    }

    public boolean checkCreationInput(UserCreationDTO user) throws Exception {
        if (user.getUsername() == null || user.getPassword() == null || user.getEmail() == null)
            throw new Exception("Campos faltantes");
        else {
            if (!Utils.isEmail(user.getEmail())) throw new IllegalArgumentException("Invalido formato de email");
            return checkUsername(user.getUsername()) && checkBiography(user.getBio()) && checkPassword(user.getPassword());
        }
    }

    private boolean checkUsername(String username) throws Exception {
        if(username != null){
            if( ! utils.checkString(username,3, 16))
                throw new Exception("El nombre de usuario debe tener entre 3 y 16 caracteres");
        }
        return true;
    }

    private boolean checkBiography(String biography) throws Exception {
        if(biography != null){
            if(!utils.checkString(biography,0, 100))
                throw new Exception("Largo maximo de la Bio 100 caracteres");
        }
        return true;
    }

    private boolean checkPassword(String password) throws Exception {
        if(password != null){
            if( !utils.checkString(password,8, 20))
                throw new Exception("La contraseña debe tener entre 8 y 20 caracteres");
        }
        return true;
    }
}
