package com.example.demo.dto.comment;

import com.example.demo.dto.tag.TagListingDTO;
import com.example.demo.dto.user.UserListingDTO;
import com.example.demo.model.Comment;
import com.example.demo.model.Tag;
import com.example.demo.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class CommentListingDTO {
    private Long id;
    private String text;
    private Long postId;
    private UserListingDTO user;
    private VoteDTO votes;


    private TagListingDTO[] tags;

    public CommentListingDTO(Long id, String text, Long postId, TagListingDTO[] tags, VoteDTO voteDTO, User user) {
        this.id = id;
        this.text = text;
        this.postId = postId;
        this.tags = tags;
        this.user = UserListingDTO.fromUser(user);
        this.votes = voteDTO;
    }

    public static CommentListingDTO fromComment(Comment comment, VoteDTO voteDTO) {
        return new CommentListingDTO(comment.getId(), comment.getText(), comment.getPost().getId(),  generateTags(comment.getTags()),voteDTO, comment.getUser());
    }


    public static List<CommentListingDTO> fromComments(List<CommentWithVotes> comments) {
        List<CommentListingDTO> commentListingDTOS = new ArrayList<>();
        for(CommentWithVotes comment : comments){
            commentListingDTOS.add(fromComment(comment.comment,comment.voteDTO));
        }
        return commentListingDTOS;
    }

    public TagListingDTO[] getTags() {
        return tags;
    }

    public void setTags(TagListingDTO[] tags) {
        this.tags = tags;
    }

    public Long getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public Long getPostId() {
        return postId;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }


    public UserListingDTO getUser() {
        return user;
    }

    public void setUser(UserListingDTO user) {
        this.user = user;
    }

    public VoteDTO getVotes() {
        return votes;
    }

    public void setVotes(VoteDTO votes) {
        this.votes = votes;
    }

    private static TagListingDTO[] generateTags(Set<Tag> tags) {
        ArrayList<TagListingDTO> arrayedTags = new ArrayList<>();
        for(Tag tag : tags){
            arrayedTags.add(TagListingDTO.fromTag(tag));
        }
        TagListingDTO[] returned = new TagListingDTO[arrayedTags.size()];
        return arrayedTags.toArray(returned);
    }
}
