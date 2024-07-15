package com.bizzy.skillbridge.entity;

import java.util.List;

public class Category {
    private String id;
    private String name;
    private String description;
    private List<String> postIds; 
    
    public Category() {
    }

    public Category(String id, String name, String description, List<String> postIds) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.postIds = postIds;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getPostIds() {
        return postIds;
    }

    public void setPostIds(List<String> postIds) {
        this.postIds = postIds;
    }

    public void addPostId(String postId) {
        this.postIds.add(postId);
    }

    public void removePostId(String postId) {
        this.postIds.remove(postId);
    }

    @Override
    public String toString() {
        return "Category{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", postIds=" + postIds +
                '}';
    }
}
