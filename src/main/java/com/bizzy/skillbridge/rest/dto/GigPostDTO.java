package com.bizzy.skillbridge.rest.dto;

import java.util.Date;

public class GigPostDTO {
    
    private String id;
    private String title;
    private String description;
    private String category;
    private float price;
    private String status;
    private int duration;
    private String userCreator;
    private Date deadline;
    private Date updateDeadline;

    public GigPostDTO() {
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCategory() {
        return category;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public float getPrice() {
        return price;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getDuration() {
        return duration;
    }

    public void setUserCreator(String userCreator) {
        this.userCreator = userCreator;
    }

    public String getUserCreator() {
        return userCreator;
    }

    public void setDeadline(Date deadline) {
        this.deadline = deadline;
    }

    public Date getDeadline() {
        return deadline;
    }

    public void setUpdateDeadline(Date updateDeadline) {
        this.updateDeadline = updateDeadline;
    }

    public Date getUpdateDeadline() {
        return updateDeadline;
    }

    @Override
    public String toString() {
        return "GigPostDTO{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", category='" + category + '\'' +
                ", price=" + price +
                ", status='" + status + '\'' +
                ", duration='" + duration + '\'' +
                '}';
    }
}
