package com.example.demo.validators;

import com.example.demo.dto.comment.CommentDTO;
import com.example.demo.dto.post.PostDTO;

public class CommentInputValidator {
    public boolean checkCreateComment(CommentDTO comment) throws Exception {
        if(comment.getText() == null)
            throw new Exception("Missing fields");
        else{
            if(comment.getTags().length > 5) throw new Exception("Too many tags");
            if(comment.getText().length() < 1 || comment.getText().length() > 200)
                throw new Exception("Comment requires text to have 1 word or less than 200");
        }

        return true;
    }

}
