package com.example.demo.service;

import com.example.demo.dto.tag.TagListingDTO;
import com.example.demo.dto.tag.TagNameDTO;
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
                respondedTags.add(tagRepository.save(newTag));
                userService.followTag(user, newTag);
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

    public Tag getTagByName(String name) {
        return tagRepository.getTagByName(name);
    }
    public Tag getTagById(Long id) {
        return tagRepository.getTagById(id);
    }

    public List<Tag> getTagsFollowedByUser(User user) {
        List<Tag> allTags = tagRepository.findAll();
        List<Tag> followedTags = new ArrayList<>();
        for(Tag tag : allTags) {
            if(tag.getFollowers().contains(user)) {
                followedTags.add(tag);
            }
        }
        return followedTags;
    }

    public List<TagNameDTO> getTagsFollowedByUserDTO(User user) {//For Reacts. Returns only tag name
        List<Tag> followedTags = getTagsFollowedByUser(user);
        List<TagNameDTO> followedTagsDTO = new ArrayList<>();
        for(Tag tag : followedTags) {
            followedTagsDTO.add(TagNameDTO.fromTag(tag.getName()));
        }
        return followedTagsDTO;
    }

    public List<TagListingDTO> getFullTagsFollowedByUserDTO(User user) {//When the whole Tag object is needed
        List<Tag> followedTags = getTagsFollowedByUser(user);
        List<TagListingDTO> followedTagsDTO = new ArrayList<>();
        for(Tag tag : followedTags) {
            followedTagsDTO.add(TagListingDTO.fromTag(tag));
        }
        return followedTagsDTO;
    }
    public List<TagListingDTO> getFullTagsCreatedByUserDTO(User user) {//When the whole Tag object is needed
        List<Tag> userTags = getUserTags(user);
        List<TagListingDTO> createdTagsDTO = new ArrayList<>();
        for(Tag tag : userTags) {
            createdTagsDTO.add(TagListingDTO.fromTag(tag));
        }
        return createdTagsDTO;
    }




}
