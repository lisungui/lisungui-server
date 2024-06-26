package com.bizzy.skillbridge.entity;

import java.util.Date;

public class Gig {
    
    private String id;
    private String title;
    private String description;
    private String category;
    private float price;
    private String status;
    private Date createdDate;
    private Date deadline;
    private Date updateDeadline;
    private Date updatedDate;
    private int duration;
    private String userCreator;
    private String userCreatorName;
    private String userCreatorEmail;
    private int numberOfApplicants=0;

    
    public Gig() {
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

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }

    public Date getUpdatedDate() {
        return updatedDate;
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

    public void setUserCreatorName(String userCreatorName) {
        this.userCreatorName = userCreatorName;
    }

    public String getUserCreatorName() {
        return userCreatorName;
    }

    public void setUserCreatorEmail(String userCreatorEmail) {
        this.userCreatorEmail = userCreatorEmail;
    }

    public String getUserCreatorEmail() {
        return userCreatorEmail;
    }

    public void setNumberOfApplicants(int numberOfApplicants) {
        this.numberOfApplicants = numberOfApplicants;
    }

    public int getNumberOfApplicants() {
        return numberOfApplicants;
    }
}
