package com.bizzy.skillbridge.service;

import com.bizzy.skillbridge.entity.Category;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
public class CategoryService {

    private final Firestore db;

    public CategoryService(Firestore db) {
        this.db = db;
    }

    public Category createCategory(Category category) {
        DocumentReference newCategoryRef = db.collection("categories").document();
        category.setId(newCategoryRef.getId());
        ApiFuture<WriteResult> result = newCategoryRef.set(category);
        try {
            result.get();
            return category;
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException("Failed to create category", e);
        }
    }

    public Category getCategory(String categoryId) {
        DocumentReference categoryRef = db.collection("categories").document(categoryId);
        ApiFuture<DocumentSnapshot> future = categoryRef.get();
        try {
            DocumentSnapshot document = future.get();
            if (document.exists()) {
                return document.toObject(Category.class);
            } else {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found");
            }
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException("Failed to get category", e);
        }
    }

    public List<Category> getAllCategories() {
        CollectionReference categoriesRef = db.collection("categories");
        ApiFuture<QuerySnapshot> future = categoriesRef.get();
        try {
            List<QueryDocumentSnapshot> documents = future.get().getDocuments();
            List<Category> categories = new ArrayList<>();
            for (DocumentSnapshot document : documents) {
                categories.add(document.toObject(Category.class));
            }
            return categories;
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException("Failed to get categories", e);
        }
    }

    public Category updateCategory(String categoryId, Category updatedCategory) {
        DocumentReference categoryRef = db.collection("categories").document(categoryId);
        ApiFuture<WriteResult> result = categoryRef.set(updatedCategory, SetOptions.merge());
        try {
            result.get();
            return updatedCategory;
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException("Failed to update category", e);
        }
    }

    public void deleteCategory(String categoryId) {
        DocumentReference categoryRef = db.collection("categories").document(categoryId);
        ApiFuture<WriteResult> writeResult = categoryRef.delete();
        try {
            writeResult.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException("Failed to delete category", e);
        }
    }
}
