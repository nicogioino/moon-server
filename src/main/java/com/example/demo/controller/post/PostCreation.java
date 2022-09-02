package com.example.demo.controller.post;

public class PostCreation {
    private String text;
    private String title;
    private String[] tags;

    public PostCreation(String text, String title, String[] tags) {
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
