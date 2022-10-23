package com.example.demo.dto.tag;

import com.example.demo.dto.post.PostListingDTO;
import com.example.demo.model.Tag;

import java.util.ArrayList;
import java.util.List;

public class TagListingDTO {
    String name;

    public static TagListingDTO fromTag(String name) {
        TagListingDTO tagListingDTO = new TagListingDTO();
        tagListingDTO.setName(name);
        return tagListingDTO;
    }

    public static TagListingDTO fromTag(Tag tag) {
        TagListingDTO tagListingDTO = new TagListingDTO();
        tagListingDTO.setName(tag.getName());
        return tagListingDTO;
    }

    public static List<TagListingDTO> fromTags(List<Tag> tags) {
        List<TagListingDTO> toReturn = new ArrayList<>();
        for (Tag tag : tags) {
            toReturn.add(TagListingDTO.fromTag(tag.getName()));
        }
        return toReturn;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }




}
