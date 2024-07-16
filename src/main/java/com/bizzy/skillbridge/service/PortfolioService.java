package com.bizzy.skillbridge.service;

import com.bizzy.skillbridge.entity.Portfolio;
import com.bizzy.skillbridge.entity.PortfolioItem;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Service
public class PortfolioService {

    private final Firestore db;
    private final ShareableLinkService shareableLinkService;

    @Autowired
    public PortfolioService(Firestore db, ShareableLinkService shareableLinkService) {
        this.db = db;
        this.shareableLinkService = shareableLinkService;
    }

    public Portfolio createPortfolio(String freelancerId) {
        DocumentReference newPortfolioRef = db.collection("portfolios").document();
        Portfolio portfolio = new Portfolio();
        portfolio.setId(newPortfolioRef.getId());
        portfolio.setFreelancerId(freelancerId);
        ApiFuture<WriteResult> result = newPortfolioRef.set(portfolio);
        try {
            result.get();
            return portfolio;
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException("Failed to create portfolio", e);
        }
    }

    public Portfolio getPortfolio(String portfolioId) {
        try {
            DocumentReference portfolioRef = db.collection("portfolios").document(portfolioId);
            ApiFuture<DocumentSnapshot> future = portfolioRef.get();
            DocumentSnapshot document = future.get();

            if (document.exists()) {
                return document.toObject(Portfolio.class);
            } else {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Portfolio not found");
            }
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException("Failed to fetch portfolio", e);
        }
    }

    public PortfolioItem addPortfolioItem(String portfolioId, PortfolioItem portfolioItem) {
        DocumentReference portfolioRef = db.collection("portfolios").document(portfolioId);
        portfolioItem.setId(db.collection("portfolioItems").document().getId());
        ApiFuture<WriteResult> result = portfolioRef.update("items", FieldValue.arrayUnion(portfolioItem));
        try {
            result.get();
            return portfolioItem;
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException("Failed to add portfolio item", e);
        }
    }

    public PortfolioItem updatePortfolioItem(String portfolioId, String itemId, PortfolioItem portfolioItem) {
        DocumentReference portfolioRef = db.collection("portfolios").document(portfolioId);
        ApiFuture<DocumentSnapshot> future = portfolioRef.get();
        try {
            DocumentSnapshot document = future.get();
            if (document.exists()) {
                Portfolio portfolio = document.toObject(Portfolio.class);
                if (portfolio != null) {
                    portfolioItem.setId(itemId);
    
                    List<PortfolioItem> items = portfolio.getItems().stream()
                            .map(item -> item.getId().equals(itemId) ? portfolioItem : item)
                            .collect(Collectors.toList());
                    portfolio.setItems(items);
                    portfolioRef.set(portfolio);
                    return portfolioItem;
                }
            }
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Portfolio not found");
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException("Failed to update portfolio item", e);
        }
    }
    

    public void deletePortfolioItem(String portfolioId, String itemId) {
        DocumentReference portfolioRef = db.collection("portfolios").document(portfolioId);
        ApiFuture<DocumentSnapshot> future = portfolioRef.get();
        try {
            DocumentSnapshot document = future.get();
            if (document.exists()) {
                Portfolio portfolio = document.toObject(Portfolio.class);
                if (portfolio != null) {
                    List<PortfolioItem> items = portfolio.getItems().stream()
                            .filter(item -> !item.getId().equals(itemId))
                            .collect(Collectors.toList());
                    portfolio.setItems(items);
                    portfolioRef.set(portfolio);
                }
            } else {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Portfolio not found");
            }
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException("Failed to delete portfolio item", e);
        }
    }

    public String generateShareableLink(String portfolioId, String itemId) {
        return shareableLinkService.generateShareableLink(portfolioId, itemId);
    }
}