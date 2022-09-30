package com.example.demo.dto.tag;

import com.example.demo.dto.post.PostListingDTO;

public class TagListingDTO {
    String name;

    public static TagListingDTO fromTag(String name) {
        TagListingDTO tagListingDTO = new TagListingDTO();
        tagListingDTO.setName(name);
        return tagListingDTO;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }




}
