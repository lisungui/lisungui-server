package com.bizzy.skillbridge.service;

import com.bizzy.skillbridge.entity.Comment;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
public class CommentService {

    private final Firestore db;

    public CommentService(Firestore db) {
        this.db = db;
    }

    public Comment createComment(Comment comment) {
        DocumentReference newCommentRef = db.collection("comments").document();
        comment.setId(newCommentRef.getId());
        ApiFuture<WriteResult> result = newCommentRef.set(comment);
        try {
            result.get();
            return comment;
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException("Failed to create comment", e);
        }
    }

    public Comment likeComment(String commentId, String userId) {
        DocumentReference commentRef = db.collection("comments").document(commentId);
        ApiFuture<WriteResult> result = commentRef.update("likeIds", FieldValue.arrayUnion(userId));
        try {
            result.get();
            DocumentSnapshot snapshot = commentRef.get().get();
            return snapshot.toObject(Comment.class);
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException("Failed to like comment", e);
        }
    }

    public Comment getComment(String commentId) {
        DocumentReference commentRef = db.collection("comments").document(commentId);
        ApiFuture<DocumentSnapshot> future = commentRef.get();
        try {
            DocumentSnapshot document = future.get();
            if (document.exists()) {
                return document.toObject(Comment.class);
            } else {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Comment not found");
            }
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException("Failed to get comment", e);
        }
    }

    public List<Comment> getAllComments() {
        CollectionReference commentsRef = db.collection("comments");
        ApiFuture<QuerySnapshot> future = commentsRef.get();
        try {
            List<QueryDocumentSnapshot> documents = future.get().getDocuments();
            List<Comment> comments = new ArrayList<>();
            for (DocumentSnapshot document : documents) {
                comments.add(document.toObject(Comment.class));
            }
            return comments;
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException("Failed to get comments", e);
        }
    }

    public Comment updateComment(String commentId, Comment updatedComment) {
        DocumentReference commentRef = db.collection("comments").document(commentId);
        ApiFuture<WriteResult> result = commentRef.set(updatedComment, SetOptions.merge());
        try {
            result.get();
            return updatedComment;
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException("Failed to update comment", e);
        }
    }

    public void deleteComment(String commentId) {
        DocumentReference commentRef = db.collection("comments").document(commentId);
        ApiFuture<WriteResult> writeResult = commentRef.delete();
        try {
            writeResult.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException("Failed to delete comment", e);
        }
    }

    public void moderateComment(String commentId, boolean isApproved) {
        DocumentReference commentRef = db.collection("comments").document(commentId);
        ApiFuture<WriteResult> result = commentRef.update("isApproved", isApproved);
        try {
            result.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException("Failed to moderate comment", e);
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
}
