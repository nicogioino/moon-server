package com.example.demo.service;

import com.example.demo.dto.post.PostDTO;
import com.example.demo.model.Post;
import com.example.demo.model.Tag;
import com.example.demo.model.User;
import com.example.demo.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PostService {
    private final PostRepository postRepository;

    @Autowired
    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }
    public Post create(PostDTO postWithTags, User user, Tag[] tags) {
        Post post = new Post(postWithTags.getTitle(), postWithTags.getText(), user);
        for (int i = 0; i < tags.length; i++) {
            post.getTags().add(tags[i]);
        }
        return postRepository.save(post);
    }

    public Post editPost(Long postId, PostDTO possiblePost, User user, Tag[] tags) {
        return getPost(postId, possiblePost, tags, user);
    }

    private Post getPost(Long postId, PostDTO possiblePost, Tag[] tags, User user) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalStateException(
                        "post with id " + postId + " does not exists"
                ));
        if (belongsToUser(user, post)) {
            if(possiblePost.getTitle() != null){
                post.setTitle(possiblePost.getTitle());
            }
            if(possiblePost.getText() != null){
                post.setText(possiblePost.getText());
            }
            if(tags != null){
                post.getTags().clear();
                for (Tag tag : tags) {
                    post.getTags().add(tag);
                }
            }
            return postRepository.save(post);
        } else {
            throw new IllegalStateException("post does not belong to user");
        }
    }

    private boolean belongsToUser(User user, Post post) {return user.getId().equals(post.getUser().getId());}
}