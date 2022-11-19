package com.example.demo.service;

import com.example.demo.dto.post.PostDTO;
import com.example.demo.model.BaseEntity;
import com.example.demo.model.Post;
import com.example.demo.model.Tag;
import com.example.demo.model.User;
import com.example.demo.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
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
        post.deletePost();
        postRepository.save(post);
        return post;
    }

    public Post editPost(Long postId, PostDTO possiblePost, User user, ArrayList<Tag> tags) {
        return edit(postId, possiblePost, tags, user);
    }

    private Post edit(Long postId, PostDTO possiblePost, ArrayList<Tag> tags, User user) {
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
                post.getTags().addAll(tags);

            }
            return postRepository.save(post);
        } else {
            throw new IllegalStateException("post does not belong to user");
        }
    }

    public Post[] getAllPosts(Long[] usersId, Long userId){
        return postRepository.allPostsFrom(usersId, userId, Sort.by(Sort.Direction.DESC, "createdAt") );
    }

    public Post[] getPostsFrom(Long userId){
        return postRepository.postsFrom(userId, Sort.by(Sort.Direction.DESC, "createdAt") );
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
        if(post.isDeleted()) throw new IllegalStateException("Post is deleted");

        Set<Post> bookmarkedPosts = user.getBookmarkedPosts();
        if(!bookmarkedPosts.contains(post)) {
            bookmarkedPosts.add(post);
            userService.save(user);
        } else throw new IllegalArgumentException("Post already bookmarked");
    }

    public void deleteBookmark(Long postId, User user, UserService userService) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalStateException(
                        "Post with id " + postId + " does not exist"
                ));
        if(post.isDeleted()) throw new IllegalStateException("Post is deleted");
        Set<Post> bookmarkedPosts = user.getBookmarkedPosts();
        if(bookmarkedPosts.contains(post)) {
            bookmarkedPosts.remove(post);
            userService.save(user);
            postRepository.save(post);
        } else throw new IllegalArgumentException("Post not bookmarked");
    }

    public Post getPostById(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalStateException(
                        "Post with id " + postId + " does not exist"
                ));

        if(post.isDeleted()) throw new IllegalStateException("Post is deleted");
        return post;

    }

    public List<Post> getPostsWithTags(Long[] tagsIds){
        List<Post> posts = postRepository.findAll();
        List<Post> postsWithTags = new ArrayList<>();
        for (Post post : posts) {
            for (Long tagId : tagsIds) {
                if(!post.isDeleted() && isTagIdInPostTags(post.getTags(), tagId)){
                    postsWithTags.add(post);
                }
            }
        }
        return postsWithTags;
    }

    private boolean isTagIdInPostTags(Set<Tag> tags, Long tagId) {
        for (Tag tag : tags) {
            if(tag.getId() == tagId) return true;
        }
        return false;
    }

    public Post[] getPostsFromTags(Long[] tagsId, Post[] posts) {
        ArrayList<Post> postsFromTags = new ArrayList<>();
        for (Post post : posts) {
            for (Tag tag : post.getTags()) {
                for (Long tagId : tagsId) {
                    if (tag.getId().equals(tagId)) {
                        postsFromTags.add(post);
                    }
                }
            }
        }
        return postsFromTags.toArray(new Post[0]);
    }

    public Post[] mergePostLists(List<Post> list1, List<Post> list2){
        List<Post> list1Copy = new ArrayList<>(list1);
        for (Post x : list2){
            if (!list1Copy.contains(x))
                list1Copy.add(x);
        }
        List<Post> sortedList = sortListByDescendingDate(list1Copy);
        return sortedList.toArray(new Post[0]);
    }

    private List<Post> sortListByDescendingDate(List<Post> list){
        list.sort(Comparator.comparing(Post::getCreatedAt).reversed());
        return list;
    }

}
