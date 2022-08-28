package com.example.demo.controller.user;

public class Utils {
    public static boolean checkString(String word, int minLength, int maxLength){
        return word.length() >= minLength && word.length() <= maxLength;
    }
}
