package com.bizzy.skillbridge.service;

import com.bizzy.skillbridge.entity.Category;
import com.bizzy.skillbridge.entity.Comment;
import com.bizzy.skillbridge.entity.Post;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.Query;
import com.google.cloud.firestore.QuerySnapshot;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
public class SearchService {

    private final Firestore db;

    public SearchService(Firestore db) {
        this.db = db;
    }

    public List<Post> searchPosts(String query) {
        Query searchQuery = db.collection("posts")
                .orderBy("content")
                .startAt(query)
                .endAt(query + "\uf8ff");

        ApiFuture<QuerySnapshot> querySnapshot = searchQuery.get();
        try {
            return querySnapshot.get().toObjects(Post.class);
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException("Failed to search posts", e);
        }
    }

    public List<Comment> searchComments(String query) {
        Query searchQuery = db.collection("comments")
                .orderBy("content")
                .startAt(query)
                .endAt(query + "\uf8ff");

        ApiFuture<QuerySnapshot> querySnapshot = searchQuery.get();
        try {
            return querySnapshot.get().toObjects(Comment.class);
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException("Failed to search comments", e);
        }
    }

    public List<Category> searchCategories(String query) {
        Query searchQuery = db.collection("categories")
                .orderBy("name")
                .startAt(query)
                .endAt(query + "\uf8ff");

        ApiFuture<QuerySnapshot> querySnapshot = searchQuery.get();
        try {
            return querySnapshot.get().toObjects(Category.class);
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException("Failed to search categories", e);
        }
    }
}