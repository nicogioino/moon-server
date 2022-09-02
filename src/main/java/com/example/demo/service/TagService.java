package com.example.demo.service;



import com.example.demo.model.Tag;
import com.example.demo.model.User;
import com.example.demo.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class TagService {
    private final TagRepository tagRepository;
    @Autowired
    public TagService(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    public Tag[] createTags(String[] tags, User user) {
        Tag[] respondedTags = new Tag[tags.length];
        for (int i = 0; i < tags.length; i++) {
            Optional<Tag> tag = tagRepository.getTagByName(tags[i]);
            if(tag.isEmpty()){
                Tag newTag = new Tag(tags[i], user);
                Tag createdTag = tagRepository.save(newTag);
                respondedTags[i] = createdTag;
            }else{
                respondedTags[i] = tag.get();
            }
        }
        return respondedTags;
    }
}
