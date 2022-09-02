package com.example.demo.validators;

import java.util.regex.Pattern;

public class Utils {
    private static final String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+
            "[a-zA-Z0-9_+&*-]+)*@" +
            "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
            "A-Z]{2,7}$";

    public static boolean checkString(String word, int minLength, int maxLength){
        return word.length() >= minLength && word.length() <= maxLength;
    }


    public static boolean isEmail(String email) {
        Pattern pat = Pattern.compile(emailRegex);
        if (email == null)
            return false;
        return pat.matcher(email).matches();
    }
}
