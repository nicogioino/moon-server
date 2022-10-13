package com.example.demo.service;

import com.example.demo.model.Tag;
import com.example.demo.model.User;
import com.example.demo.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TagService {
    private final TagRepository tagRepository;
    private final UserService userService;
    @Autowired
    public TagService(TagRepository tagRepository, UserService userService) {
        this.tagRepository = tagRepository;
        this.userService = userService;
    }

    public ArrayList<Tag> createTags(String[] tags, User user) {
        ArrayList<Tag> created_tags = tagRepository.getTagsByName(tags);
        ArrayList<Tag> respondedTags = new ArrayList<>(created_tags);
        List<String> nameOfTags = getNameOfTagsOf(created_tags);
        for(String tagName: tags){
            if(!nameOfTags.contains(tagName)){
                Tag newTag = new Tag(tagName, user);
                userService.followTag(user, newTag);
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

    private List<String> getNameOfTagsOf(ArrayList<Tag> created_tags) {
        List<String> s = new ArrayList<>();
        for(Tag tag : created_tags) {
            s.add(tag.getName());
        }
        return s;
    }
    public List<Tag> getUserTags(User user) {
        return tagRepository.findByUser(user.getId());
    }
}
