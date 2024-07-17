package com.bizzy.skillbridge.controller;

import com.bizzy.skillbridge.entity.Portfolio;
import com.bizzy.skillbridge.entity.PortfolioItem;
import com.bizzy.skillbridge.service.PortfolioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class PortfolioControllerTest {

    @Mock
    private PortfolioService portfolioService;

    @InjectMocks
    private PortfolioController portfolioController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreatePortfolio_Valid() {
        Portfolio portfolio = new Portfolio();
        when(portfolioService.createPortfolio(anyString())).thenReturn(portfolio);

        Portfolio result = portfolioController.createPortfolio("freelancerId");
        assertEquals(portfolio, result);
        verify(portfolioService, times(1)).createPortfolio("freelancerId");
    }

    @Test
    void testCreatePortfolio_Invalid() {
        when(portfolioService.createPortfolio(anyString())).thenThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid freelancer ID"));

        Exception exception = assertThrows(ResponseStatusException.class, () -> {
            portfolioController.createPortfolio("invalidFreelancerId");
        });

        assertEquals("400 BAD_REQUEST \"Invalid freelancer ID\"", exception.getMessage());
        verify(portfolioService, times(1)).createPortfolio("invalidFreelancerId");
    }

    @Test
    void testGetPortfolio_Valid() {
        Portfolio portfolio = new Portfolio();
        when(portfolioService.getPortfolio(anyString())).thenReturn(portfolio);

        Portfolio result = portfolioController.getPortfolio("portfolioId");
        assertEquals(portfolio, result);
        verify(portfolioService, times(1)).getPortfolio("portfolioId");
    }

    @Test
    void testGetPortfolio_Invalid() {
        when(portfolioService.getPortfolio(anyString())).thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Portfolio not found"));

        Exception exception = assertThrows(ResponseStatusException.class, () -> {
            portfolioController.getPortfolio("invalidPortfolioId");
        });

        assertEquals("404 NOT_FOUND \"Portfolio not found\"", exception.getMessage());
        verify(portfolioService, times(1)).getPortfolio("invalidPortfolioId");
    }

    @Test
    void testAddPortfolioItem_Valid() {
        PortfolioItem portfolioItem = new PortfolioItem();
        when(portfolioService.addPortfolioItem(anyString(), any(PortfolioItem.class))).thenReturn(portfolioItem);

        PortfolioItem result = portfolioController.addPortfolioItem("portfolioId", portfolioItem);
        assertEquals(portfolioItem, result);
        verify(portfolioService, times(1)).addPortfolioItem(eq("portfolioId"), any(PortfolioItem.class));
    }

    @Test
    void testAddPortfolioItem_Invalid() {
        when(portfolioService.addPortfolioItem(anyString(), any(PortfolioItem.class))).thenThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid portfolio ID or item data"));

        PortfolioItem portfolioItem = new PortfolioItem();

        Exception exception = assertThrows(ResponseStatusException.class, () -> {
            portfolioController.addPortfolioItem("invalidPortfolioId", portfolioItem);
        });

        assertEquals("400 BAD_REQUEST \"Invalid portfolio ID or item data\"", exception.getMessage());
        verify(portfolioService, times(1)).addPortfolioItem(eq("invalidPortfolioId"), any(PortfolioItem.class));
    }

    @Test
    void testUpdatePortfolioItem_Valid() {
        PortfolioItem portfolioItem = new PortfolioItem();
        when(portfolioService.updatePortfolioItem(anyString(), anyString(), any(PortfolioItem.class))).thenReturn(portfolioItem);

        PortfolioItem result = portfolioController.updatePortfolioItem("portfolioId", "itemId", portfolioItem);
        assertEquals(portfolioItem, result);
        verify(portfolioService, times(1)).updatePortfolioItem(eq("portfolioId"), eq("itemId"), any(PortfolioItem.class));
    }

    @Test
    void testUpdatePortfolioItem_Invalid() {
        when(portfolioService.updatePortfolioItem(anyString(), anyString(), any(PortfolioItem.class))).thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Portfolio or item not found"));

        PortfolioItem portfolioItem = new PortfolioItem();

        Exception exception = assertThrows(ResponseStatusException.class, () -> {
            portfolioController.updatePortfolioItem("invalidPortfolioId", "invalidItemId", portfolioItem);
        });

        assertEquals("404 NOT_FOUND \"Portfolio or item not found\"", exception.getMessage());
        verify(portfolioService, times(1)).updatePortfolioItem(eq("invalidPortfolioId"), eq("invalidItemId"), any(PortfolioItem.class));
    }

    @Test
    void testDeletePortfolioItem_Valid() {
        doNothing().when(portfolioService).deletePortfolioItem(anyString(), anyString());

        portfolioController.deletePortfolioItem("portfolioId", "itemId");
        verify(portfolioService, times(1)).deletePortfolioItem("portfolioId", "itemId");
    }

    @Test
    void testDeletePortfolioItem_Invalid() {
        doThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Portfolio or item not found")).when(portfolioService).deletePortfolioItem(anyString(), anyString());

        Exception exception = assertThrows(ResponseStatusException.class, () -> {
            portfolioController.deletePortfolioItem("invalidPortfolioId", "invalidItemId");
        });

        assertEquals("404 NOT_FOUND \"Portfolio or item not found\"", exception.getMessage());
        verify(portfolioService, times(1)).deletePortfolioItem("invalidPortfolioId", "invalidItemId");
    }

    @Test
    void testGenerateShareableLink_Valid() {
        String shareableLink = "https://example.com/portfolio/portfolioId/item/itemId";
        when(portfolioService.generateShareableLink(anyString(), anyString())).thenReturn(shareableLink);

        String result = portfolioController.generateShareableLink("portfolioId", "itemId");
        assertEquals(shareableLink, result);
        verify(portfolioService, times(1)).generateShareableLink("portfolioId", "itemId");
    }

    @Test
    void testGenerateShareableLink_Invalid() {
        when(portfolioService.generateShareableLink(anyString(), anyString())).thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Portfolio or item not found"));

        Exception exception = assertThrows(ResponseStatusException.class, () -> {
            portfolioController.generateShareableLink("invalidPortfolioId", "invalidItemId");
        });

        assertEquals("404 NOT_FOUND \"Portfolio or item not found\"", exception.getMessage());
        verify(portfolioService, times(1)).generateShareableLink("invalidPortfolioId", "invalidItemId");
    }
}
