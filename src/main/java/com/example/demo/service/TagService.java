package com.example.demo.service;



import com.example.demo.model.Tag;
import com.example.demo.model.User;
import com.example.demo.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Optional;

@Component
public class TagService {
    private final TagRepository tagRepository;
    @Autowired
    public TagService(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    public ArrayList<Tag> createTags(String[] tags, User user) {
        ArrayList<Tag> respondedTags = new ArrayList<>();
        ArrayList<Tag> created_tags = tagRepository.getTagsByName(tags);
        respondedTags.addAll(created_tags);
        ArrayList<String> nameOfTags = this.getNameOfTagsOf(created_tags);
        for (int i = 0; i < tags.length; i++) {
            if (!nameOfTags.contains(tags[i])) {
                Tag newTag = new Tag(tags[i], user);
                respondedTags.add(tagRepository.save(newTag));
            }
        }
        return respondedTags;
    }
    public String[] getNameOfTags(){
        Optional<String[]> tags= tagRepository.getAllTagsNames();
        if(tags.isEmpty()){
            return new String[0];
        }else{
            return tags.get();
        }
    }

    private ArrayList<String> getNameOfTagsOf(ArrayList<Tag> created_tags) {
        ArrayList<String> s = new ArrayList<>();
        for (int i = 0; i < created_tags.size(); i++) {
            s.add(created_tags.get(i).getName());
        }
        return s;
    }

}
