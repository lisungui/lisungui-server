package com.bizzy.skillbridge.entity;

import java.util.List;

public class Post {
    private String id;
    private String categoryId;
    private String userId;
    private String title;
    private String content;
    private List<String> commentIds;
    private List<String> upVoteIds;
    private List<String> downVoteIds;
    private String timestamp;

    // Getters and Setters

    public Post() {
    }

    public Post(String id, String categoryId, String userId, String title, String content, List<String> commentIds, List<String> upVoteIds, List<String> downVoteIds, String timestamp) {
        this.id = id;
        this.categoryId = categoryId;
        this.userId = userId;
        this.title = title;
        this.content = content;
        this.commentIds = commentIds;
        this.upVoteIds = upVoteIds;
        this.downVoteIds = downVoteIds;
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

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public List<String> getUpVoteIds() {
        return upVoteIds;
    }

    public void setUpVoteIds(List<String> upVoteIds) {
        this.upVoteIds = upVoteIds;
    }

    public List<String> getDownVoteIds() {
        return downVoteIds;
    }

    public void setDownVoteIds(List<String> downVoteIds) {
        this.downVoteIds = downVoteIds;
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
                ", upVoteIds=" + upVoteIds +
                ", downVoteIds=" + downVoteIds +
                ", timestamp='" + timestamp + '\'' +
                '}';
    }
}
