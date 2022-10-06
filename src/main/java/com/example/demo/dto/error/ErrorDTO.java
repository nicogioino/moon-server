package com.example.demo.dto.error;

import com.example.demo.dto.tag.TagListingDTO;

public class ErrorDTO {
    String message;
    public static ErrorDTO fromMessage(String message) {
        ErrorDTO errorDTO = new ErrorDTO();
        errorDTO.setMessage(message);
        return errorDTO;
    }
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
