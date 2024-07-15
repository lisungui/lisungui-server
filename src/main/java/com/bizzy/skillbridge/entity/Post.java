package com.bizzy.skillbridge.entity;

import java.util.List;

public class Post {
    private String id;
    private String categoryId;
    private String userId;
    private String title;
    private String content;
    private List<String> commentIds; 
    private List<String> likeIds;
    private String timestamp;

    // Getters and Setters

    public Post() {
    }

    public Post(String id, String categoryId, String userId, String title, String content, List<String> commentIds, List<String> likeIds, String timestamp) {
        this.id = id;
        this.categoryId = categoryId;
        this.userId = userId;
        this.title = title;
        this.content = content;
        this.commentIds = commentIds;
        this.likeIds = likeIds;
        this.timestamp = timestamp;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<String> getCommentIds() {
        return commentIds;
    }

    public void setCommentIds(List<String> commentIds) {
        this.commentIds = commentIds;
    }

    public List<String> getLikeIds() {
        return likeIds;
    }

    public void setLikeIds(List<String> likeIds) {
        this.likeIds = likeIds;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "Post{" +
                "id='" + id + '\'' +
                ", categoryId='" + categoryId + '\'' +
                ", userId='" + userId + '\'' +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", commentIds=" + commentIds +
                ", likeIds=" + likeIds +
                ", timestamp='" + timestamp + '\'' +
                '}';
    }
}
