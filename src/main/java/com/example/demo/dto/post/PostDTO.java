package com.example.demo.dto.post;

public class PostDTO {
    private String text;
    private String title;
    private String[] tags;

    public PostDTO(String text, String title, String[] tags) {
        this.text = text;
        this.title = title;
        this.tags = tags;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setTags(String[] tags) {
        this.tags = tags;
    }

    public String getText() {
        return text;
    }

    public String getTitle() {
        return title;
    }

    public String[] getTags() {
        return tags;
    }
}
