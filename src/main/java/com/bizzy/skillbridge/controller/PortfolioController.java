package com.bizzy.skillbridge.controller;

import com.bizzy.skillbridge.entity.Portfolio;
import com.bizzy.skillbridge.entity.PortfolioItem;
import com.bizzy.skillbridge.service.PortfolioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/portfolios")
public class PortfolioController {

    private final PortfolioService portfolioService;

    @Autowired
    public PortfolioController(PortfolioService portfolioService) {
        this.portfolioService = portfolioService;
    }

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public Portfolio createPortfolio(@RequestParam String freelancerId) {
        return portfolioService.createPortfolio(freelancerId);
    }

    @GetMapping("/{portfolioId}")
    public Portfolio getPortfolio(@PathVariable String portfolioId) {
        return portfolioService.getPortfolio(portfolioId);
    }

    @PostMapping("/{portfolioId}/items")
    @ResponseStatus(HttpStatus.CREATED)
    public Portfolio addPortfolioItem(@PathVariable String portfolioId, @RequestBody PortfolioItem item) {
        return portfolioService.addPortfolioItem(portfolioId, item);
    }

    @PutMapping("/{portfolioId}/items/{itemId}")
    public Portfolio updatePortfolioItem(@PathVariable String portfolioId, @PathVariable String itemId, @RequestBody PortfolioItem item) {
        item.setId(itemId);
        return portfolioService.updatePortfolioItem(portfolioId, item);
    }

    @DeleteMapping("/{portfolioId}/items/{itemId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePortfolioItem(@PathVariable String portfolioId, @PathVariable String itemId) {
        portfolioService.deletePortfolioItem(portfolioId, itemId);
    }

    @GetMapping("/{portfolioId}/items/{itemId}/share")
    public String generateShareableLink(@PathVariable String portfolioId, @PathVariable String itemId) {
        return portfolioService.generateShareableLink(portfolioId, itemId);
    }
}
