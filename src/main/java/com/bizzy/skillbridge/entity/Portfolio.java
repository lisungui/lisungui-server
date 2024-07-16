package com.bizzy.skillbridge.entity;

import java.util.ArrayList;
import java.util.List;

public class Portfolio {
    private String id;
    private String freelancerId;
    private List<PortfolioItem> items = new ArrayList<>();

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFreelancerId() {
        return freelancerId;
    }

    public void setFreelancerId(String freelancerId) {
        this.freelancerId = freelancerId;
    }

    public List<PortfolioItem> getItems() {
        return items;
    }

    public void setItems(List<PortfolioItem> items) {
        this.items = items;
    }
}

