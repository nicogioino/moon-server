package com.example.demo.dto.comment;

import com.example.demo.dto.tag.TagListingDTO;
import com.example.demo.model.Comment;
import com.example.demo.model.Tag;

import java.util.ArrayList;
import java.util.Set;

public class CommentListingDTO {
    private Long id;
    private String text;
    private Long postId;

    private TagListingDTO[] tags;

    public CommentListingDTO(Long id, String text, Long postId, TagListingDTO[] tags) {
        this.id = id;
        this.text = text;
        this.postId = postId;
        this.tags = tags;
    }

    public static CommentListingDTO fromComment(Comment comment) {
        return new CommentListingDTO(comment.getId(), comment.getText(), comment.getPost().getId(), generateTags(comment.getTags()));
    }

    public TagListingDTO[] getTags() {
        return tags;
    }

    public void setTags(TagListingDTO[] tags) {
        this.tags = tags;
    }

    public Long getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public Long getPostId() {
        return postId;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
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
