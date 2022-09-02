package com.example.demo.validators;

import com.example.demo.controller.post.PostCreation;
import com.example.demo.model.Post;

public class PostInputValidator {

    public boolean checkCreatePost(PostCreation post, Integer userId) throws Exception {
        if (post.getText() == null || post.getTitle() == null || userId == null)
            throw new Exception("Missing fields");
        else{
            if(post.getTags().length > 5) throw new Exception("To many tags");
            if(post.getText().length() < 1 || post.getText().length() > 500)
                throw new Exception("Post requires text to have 1 word or less than 500");
            if(post.getTitle().length() < 1 || post.getTitle().length() > 50)
                throw new Exception("Post requires title to have 1 word or less than 50");
        }
        return true;
    }
}
