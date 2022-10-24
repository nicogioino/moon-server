package com.example.demo.dto.tag;

public class TagNameDTO {
    String name;

    public static TagNameDTO fromTag(String name) {
        TagNameDTO tagNameDTO = new TagNameDTO();
        tagNameDTO.setName(name);
        return tagNameDTO;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
