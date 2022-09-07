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

    public Post editPost(Long postId, PostCreation possiblePost, User user, Tag[] tags) {
        return getPost(postId, possiblePost, tags);
    }

    private Post getPost(Long postId, PostCreation possiblePost, Tag[] tags) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalStateException(
                        "post with id " + postId + " does not exists"
                ));
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
        postRepository.save(post);
        return post;
    }
}
