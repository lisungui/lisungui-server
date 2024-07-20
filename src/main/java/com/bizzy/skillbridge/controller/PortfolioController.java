// package com.bizzy.skillbridge.controller;

// import com.bizzy.skillbridge.entity.*;
// import com.bizzy.skillbridge.service.*;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.http.HttpStatus;
// import org.springframework.web.bind.annotation.*;


// @RestController
// @RequestMapping("/api/portfolios")
// public class PortfolioController {

//     private final PortfolioService portfolioService;

//     @Autowired
//     public PortfolioController(PortfolioService portfolioService) {
//         this.portfolioService = portfolioService;
//     }

//     @PostMapping("/create")
//     @ResponseStatus(HttpStatus.CREATED)
//     public Portfolio createPortfolio(@RequestParam String freelancerId) {
//         return portfolioService.createPortfolio(freelancerId);
//     }

//     // @GetMapping("/{portfolioId}")
//     // public Portfolio getPortfolio(@PathVariable String portfolioId) {
//     //     return portfolioService.getPortfolio(portfolioId);
//     // }

//     @GetMapping("/{freelancerId}")
//     public Portfolio getPortfolioByFreelancerId(@PathVariable String freelancerId) {
//         return portfolioService. getPortfolioByFreelancerId(freelancerId);
//     }

//     @PostMapping("/{portfolioId}/items")
//     @ResponseStatus(HttpStatus.CREATED)
//     public PortfolioItem addPortfolioItem(@PathVariable String portfolioId, @RequestBody PortfolioItem portfolioItem) {
//         return portfolioService.addPortfolioItem(portfolioId, portfolioItem);
//     }

//     @PutMapping("/{portfolioId}/items/{itemId}")
//     public PortfolioItem updatePortfolioItem(@PathVariable String portfolioId, @PathVariable String itemId, @RequestBody PortfolioItem portfolioItem) {
//         return portfolioService.updatePortfolioItem(portfolioId, itemId, portfolioItem);
//     }

//     @DeleteMapping("/{portfolioId}/items/{itemId}")
//     @ResponseStatus(HttpStatus.NO_CONTENT)
//     public void deletePortfolioItem(@PathVariable String portfolioId, @PathVariable String itemId) {
//         portfolioService.deletePortfolioItem(portfolioId, itemId);
//     }

//     @GetMapping("/{portfolioId}/items/{itemId}/share")
//     public String generateShareableLink(@PathVariable String portfolioId, @PathVariable String itemId) {
//         return portfolioService.generateShareableLink(portfolioId, itemId);
//     }
// }

