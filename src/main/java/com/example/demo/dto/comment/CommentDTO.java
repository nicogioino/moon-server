package com.example.demo.dto.comment;

import com.example.demo.dto.tag.TagListingDTO;
import com.example.demo.model.Comment;
import com.example.demo.model.Tag;

import java.util.ArrayList;
import java.util.Set;

public class CommentDTO {
    private String text;
    private String[] tags;

    public CommentDTO(String text, String[] tags) {
        this.text = text;
        this.tags = tags;
    }

    public static CommentListingDTO fromComment(Comment comment) {
        return new CommentListingDTO(comment.getId(), comment.getText(), comment.getPost().getId(), comment.getUser().getId(),generateTags(comment.getTags()));
    }


    public String[] getTags() {
        return tags;
    }

    public void setTags(String[] tags) {
        this.tags = tags;
    }



    public String getText() {
        return text;
    }




    public void setText(String text) {
        this.text = text;
    }


    private static TagListingDTO[] generateTags(Set<Tag> tags) {
        ArrayList<TagListingDTO> arrayedTags = new ArrayList<>();
        for(Tag tag : tags){
            arrayedTags.add(TagListingDTO.fromTag(tag));
        }
        TagListingDTO[] returned = new TagListingDTO[arrayedTags.size()];
        return arrayedTags.toArray(returned);
    }

}
