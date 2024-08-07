package com.bizzy.skillbridge.service;

import com.bizzy.skillbridge.entity.Post;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
public class PostService {

    private final Firestore db;

    public PostService(Firestore db) {
        this.db = db;
    }

    public Post createPost(Post post) {
        DocumentReference newPostRef = db.collection("posts").document();
        post.setId(newPostRef.getId());
        ApiFuture<WriteResult> result = newPostRef.set(post);
        try {
            result.get();
            return post;
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException("Failed to create post", e);
        }
    }

    public Post upvotePost(String postId, String userId) {
        DocumentReference postRef = db.collection("posts").document(postId);
        try {
            // Remove userId from downVoteIds if present
            postRef.update("downVoteIds", FieldValue.arrayRemove(userId)).get();
            
            // Toggle upvote
            DocumentSnapshot snapshot = postRef.get().get();
            Post post = snapshot.toObject(Post.class);
            if (post.getUpVoteIds().contains(userId)) {
                postRef.update("upVoteIds", FieldValue.arrayRemove(userId)).get();
            } else {
                postRef.update("upVoteIds", FieldValue.arrayUnion(userId)).get();
            }
            return postRef.get().get().toObject(Post.class);
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException("Failed to upvote post", e);
        }
    }

    public Post downvotePost(String postId, String userId) {
        DocumentReference postRef = db.collection("posts").document(postId);
        try {
            // Remove userId from upVoteIds if present
            postRef.update("upVoteIds", FieldValue.arrayRemove(userId)).get();

            // Toggle downvote
            DocumentSnapshot snapshot = postRef.get().get();
            Post post = snapshot.toObject(Post.class);
            if (post.getDownVoteIds().contains(userId)) {
                postRef.update("downVoteIds", FieldValue.arrayRemove(userId)).get();
            } else {
                postRef.update("downVoteIds", FieldValue.arrayUnion(userId)).get();
            }
            return postRef.get().get().toObject(Post.class);
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException("Failed to downvote post", e);
        }
    }

    public Post replyToPost(String postId, Post replyPost) {
        DocumentReference replyPostRef = db.collection("posts").document();
        replyPost.setId(replyPostRef.getId());
        ApiFuture<WriteResult> result = replyPostRef.set(replyPost);
        DocumentReference postRef = db.collection("posts").document(postId);
        try {
            result.get();
            postRef.update("postReplies", FieldValue.arrayUnion(replyPost.getId())).get();
            return postRef.get().get().toObject(Post.class);
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException("Failed to reply to post", e);
        }
    }

    public Post getPost(String postId) {
        DocumentReference postRef = db.collection("posts").document(postId);
        ApiFuture<DocumentSnapshot> future = postRef.get();
        try {
            DocumentSnapshot document = future.get();
            if (document.exists()) {
                return document.toObject(Post.class);
            } else {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found");
            }
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException("Failed to get post", e);
        }
    }

    public List<Post> getAllPosts() {
        CollectionReference postsRef = db.collection("posts");
        ApiFuture<QuerySnapshot> future = postsRef.get();
        try {
            List<QueryDocumentSnapshot> documents = future.get().getDocuments();
            List<Post> posts = new ArrayList<>();
            for (DocumentSnapshot document : documents) {
                posts.add(document.toObject(Post.class));
            }
            return posts;
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException("Failed to get posts", e);
        }
    }

    public Post updatePost(String postId, Post updatedPost) {
        DocumentReference postRef = db.collection("posts").document(postId);
        ApiFuture<WriteResult> result = postRef.set(updatedPost, SetOptions.merge());
        try {
            result.get();
            return updatedPost;
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException("Failed to update post", e);
        }
    }

    public void deletePost(String postId) {
        DocumentReference postRef = db.collection("posts").document(postId);
        ApiFuture<WriteResult> writeResult = postRef.delete();
        try {
            writeResult.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException("Failed to delete post", e);
        }
    }

    public void moderatePost(String postId, boolean isApproved) {
        DocumentReference postRef = db.collection("posts").document(postId);
        ApiFuture<WriteResult> result = postRef.update("isApproved", isApproved);
        try {
            result.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException("Failed to moderate post", e);
        }
    }

    public List<Post> searchPosts(String query) {
        CollectionReference postsRef = db.collection("posts");
        ApiFuture<QuerySnapshot> future = postsRef.whereArrayContains("content", query).get();
        try {
            List<QueryDocumentSnapshot> documents = future.get().getDocuments();
            List<Post> posts = new ArrayList<>();
            for (DocumentSnapshot document : documents) {
                posts.add(document.toObject(Post.class));
            }
            return posts;
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException("Failed to search posts", e);
        }
    }
}