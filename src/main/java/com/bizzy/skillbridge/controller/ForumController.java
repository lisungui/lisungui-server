package com.bizzy.skillbridge.controller;

import com.bizzy.skillbridge.entity.*;
import com.bizzy.skillbridge.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/forum")
public class ForumController {

    private final CategoryService categoryService;
    private final PostService postService;
    private final CommentService commentService;
    private final SearchService searchService;

    @Autowired
    public ForumController(CategoryService categoryService, PostService postService, CommentService commentService, SearchService searchService) {
        this.categoryService = categoryService;
        this.postService = postService;
        this.commentService = commentService;
        this.searchService = searchService;
    }

    // Category Endpoints
    @PostMapping("/categories")
    @ResponseStatus(HttpStatus.CREATED)
    public Category createCategory(@RequestBody Category category) {
        return categoryService.createCategory(category);
    }

    @GetMapping("/categories/{id}")
    public Category getCategory(@PathVariable String id) {
        return categoryService.getCategory(id);
    }

    @GetMapping("/categories")
    public List<Category> getAllCategories() {
        return categoryService.getAllCategories();
    }

    @PutMapping("/categories/{id}")
    public Category updateCategory(@PathVariable String id, @RequestBody Category category) {
        return categoryService.updateCategory(id, category);
    }

    @DeleteMapping("/categories/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategory(@PathVariable String id) {
        categoryService.deleteCategory(id);
    }

    // Post Endpoints
    @PostMapping("/posts")
    @ResponseStatus(HttpStatus.CREATED)
    public Post createPost(@RequestBody Post post) {
        return postService.createPost(post);
    }

    @GetMapping("/posts/{id}")
    public Post getPost(@PathVariable String id) {
        return postService.getPost(id);
    }

    @GetMapping("/posts")
    public List<Post> getAllPosts() {
        return postService.getAllPosts();
    }

    @PutMapping("/posts/{id}")
    public Post updatePost(@PathVariable String id, @RequestBody Post post) {
        return postService.updatePost(id, post);
    }

    @DeleteMapping("/posts/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePost(@PathVariable String id) {
        postService.deletePost(id);
    }

    @PostMapping("/posts/{postId}/upvote")
    public Post upvotePost(@PathVariable String postId, @RequestParam String userId) {
        return postService.upvotePost(postId, userId);
    }

    @PostMapping("/posts/{postId}/downvote")
    public Post downvotePost(@PathVariable String postId, @RequestParam String userId) {
        return postService.downvotePost(postId, userId);
    }

    @PostMapping("/posts/{postId}/moderate")
    public void moderatePost(@PathVariable String postId, @RequestParam boolean isApproved) {
        postService.moderatePost(postId, isApproved);
    }

    // Comment Endpoints
    @PostMapping("/comments")
    @ResponseStatus(HttpStatus.CREATED)
    public Comment createComment(@RequestBody Comment comment) {
        return commentService.createComment(comment);
    }

    @GetMapping("/comments/{id}")
    public Comment getComment(@PathVariable String id) {
        return commentService.getComment(id);
    }

    @GetMapping("/comments")
    public List<Comment> getAllComments() {
        return commentService.getAllComments();
    }

    @PutMapping("/comments/{id}")
    public Comment updateComment(@PathVariable String id, @RequestBody Comment comment) {
        return commentService.updateComment(id, comment);
    }

    @DeleteMapping("/comments/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteComment(@PathVariable String id) {
        commentService.deleteComment(id);
    }

    // Search Endpoint
    @GetMapping("/search/posts")
    public List<Post> searchPosts(@RequestParam String query) {
        return searchService.searchPosts(query);
    }

    @GetMapping("/search/comments")
    public List<Comment> searchComments(@RequestParam String query) {
        return searchService.searchComments(query);
    }

    @GetMapping("/search/categories")
    public List<Category> searchCategories(@RequestParam String query) {
        return searchService.searchCategories(query);
    }
}
