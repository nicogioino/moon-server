package com.example.demo.service;

import com.example.demo.dto.post.PostDTO;
import com.example.demo.model.Post;
import com.example.demo.model.Tag;
import com.example.demo.model.User;
import com.example.demo.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Set;

@Service
public class PostService {
    private final PostRepository postRepository;

    @Autowired
    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }
    public Post create(PostDTO postWithTags, User user, ArrayList<Tag> tags) {
        Post post = new Post(postWithTags.getTitle(), postWithTags.getText(), user);
        post.getTags().addAll(tags);
        return postRepository.save(post);
    }

    public Post deletePost(Long postId, User user) {
        Post post = getPost(postId);
        if (!belongsToUser(user, post)) throw new RuntimeException("You can't delete this post");
        postRepository.delete(post);
        return post;
    }

    public Post editPost(Long postId, PostDTO possiblePost, User user, Tag[] tags) {
        return edit(postId, possiblePost, tags, user);
    }

    private Post edit(Long postId, PostDTO possiblePost, Tag[] tags, User user) {
        Post post = getPost(postId);
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

    public Post[] getAllPosts(Long[] usersId, Long userId){
        return postRepository.allPostsFrom(usersId, userId, Sort.by(Sort.Direction.DESC, "createdAt") );
    }
    private boolean belongsToUser(User user, Post post) {return user.getId().equals(post.getUser().getId());}

    private Post getPost(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new IllegalStateException(
                        "post with id " + postId + " does not exists"
                ));
    }

    public void bookmarkPost(Long postId, User user, UserService userService) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalStateException(
                        "Post with id " + postId + " does not exist"
                ));
        Set<Post> bookmarkedPosts = user.getBookmarkedPosts();
        if(!bookmarkedPosts.contains(post)) {
            bookmarkedPosts.add(post);
            post.getBookmarkedByUsers().add(user);
            userService.save(user);
            postRepository.save(post);
        } else throw new IllegalArgumentException("Post already bookmarked");
    }
}
