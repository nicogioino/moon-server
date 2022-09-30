package com.example.demo.dto.post;
import com.example.demo.dto.tag.TagListingDTO;
import com.example.demo.dto.user.UserListingDTO;
import com.example.demo.model.Post;
import com.example.demo.model.Tag;

import java.util.ArrayList;
import java.util.Set;

public class PostListingDTO {


    String title;
    String text;
    TagListingDTO[] tags;
    UserListingDTO user;

    public static PostListingDTO fromPost(Post post) {
        PostListingDTO postListingDTO = new PostListingDTO();
        postListingDTO.setText(post.getText());
        postListingDTO.setTitle(post.getTitle());
        postListingDTO.setTags(generateTags(post.getTags()));
        postListingDTO.setUser(UserListingDTO.fromUser(post.getUser()));
        return postListingDTO;
    }

    public TagListingDTO[] getTags() {
        return tags;
    }

    public UserListingDTO getUser() {
        return user;
    }

    private static TagListingDTO[] generateTags(Set<Tag> tags) {
        ArrayList<TagListingDTO> arrayedTags = new ArrayList<>();
        for(Tag tag : tags){
            arrayedTags.add(TagListingDTO.fromTag(tag.getName()));
        }
        TagListingDTO[] returned = new TagListingDTO[arrayedTags.size()];
        return arrayedTags.toArray(returned);
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setText(String text) {
        this.text = text;
    }
    public String getTitle() {
        return title;
    }

    public String getText() {
        return text;
    }

    public void setTags(TagListingDTO[] tags) {
        this.tags = tags;
    }

    public void setUser(UserListingDTO user) {
        this.user = user;
    }
}
