package com.bizzy.skillbridge.service;

import com.bizzy.skillbridge.entity.Portfolio;
import com.bizzy.skillbridge.entity.PortfolioItem;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
public class PortfolioService {

    private final Firestore db;

    @Autowired
    public PortfolioService(Firestore db) {
        this.db = db;
    }

    public Portfolio createPortfolio(String freelancerId) {
        Portfolio portfolio = new Portfolio();
        portfolio.setFreelancerId(freelancerId);
        DocumentReference newPortfolioRef = db.collection("portfolios").document();
        portfolio.setId(newPortfolioRef.getId());
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
            DocumentSnapshot document = db.collection("portfolios").document(portfolioId).get().get();
            if (document.exists()) {
                return document.toObject(Portfolio.class);
            } else {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Portfolio not found");
            }
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException("Failed to get portfolio", e);
        }
    }

    public Portfolio addPortfolioItem(String portfolioId, PortfolioItem item) {
        try {
            Portfolio portfolio = getPortfolio(portfolioId);
            item.setId(db.collection("portfolioItems").document().getId());
            portfolio.getItems().add(item);
            db.collection("portfolios").document(portfolioId).set(portfolio).get();
            return portfolio;
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException("Failed to add item to portfolio", e);
        }
    }

    public Portfolio updatePortfolioItem(String portfolioId, PortfolioItem item) {
        try {
            Portfolio portfolio = getPortfolio(portfolioId);
            List<PortfolioItem> items = portfolio.getItems();
            for (int i = 0; i < items.size(); i++) {
                if (items.get(i).getId().equals(item.getId())) {
                    items.set(i, item);
                    break;
                }
            }
            db.collection("portfolios").document(portfolioId).set(portfolio).get();
            return portfolio;
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException("Failed to update item in portfolio", e);
        }
    }

    public Portfolio deletePortfolioItem(String portfolioId, String itemId) {
        try {
            Portfolio portfolio = getPortfolio(portfolioId);
            List<PortfolioItem> items = portfolio.getItems();
            items.removeIf(item -> item.getId().equals(itemId));
            db.collection("portfolios").document(portfolioId).set(portfolio).get();
            return portfolio;
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException("Failed to delete item from portfolio", e);
        }
    }

    public String generateShareableLink(String portfolioId, String itemId) {
        // Implement logic for generating a secure shareable link
        // This is a placeholder implementation
        return "https://example.com/portfolio/" + portfolioId + "/item/" + itemId;
    }
}
