package com.bizzy.skillbridge.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.bizzy.skillbridge.entity.Gig;
import com.bizzy.skillbridge.rest.dto.GigPostDTO;
import com.bizzy.skillbridge.service.GigService;

@RestController
public class GigController {

    private final GigService gigService;

    public GigController(GigService gigService) {
        this.gigService = gigService;
    }

    @PostMapping("/gigs/{uid}")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public void createFreelanceUser(@PathVariable String uid, @RequestBody GigPostDTO gigPostDTO) throws Exception{
        gigService.createGig(uid, gigPostDTO);
    }

    @GetMapping("/listgigs/{uid}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public ResponseEntity<List<Gig>> getGigs(@PathVariable String uid) {
        try {
            List<Gig> gigs = gigService.getGigs(uid);
            return new ResponseEntity<>(gigs, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR); // Return 500 for unexpected errors
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No gigs found or could not retrieve gigs", e);
        }
    }

    @GetMapping("/gigs/{uid}/{gigId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public ResponseEntity<Gig> getGig(@PathVariable String uid, @PathVariable String gigId) {
        try {
            Gig gig = gigService.getGig(uid, gigId);
            return new ResponseEntity<>(gig, HttpStatus.OK);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Gig not found or could not retrieve gig: " + e.getMessage());
        }
    }

    @GetMapping("/gigs/category/{category}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public ResponseEntity<List<Gig>> getGigsByCategory(@PathVariable String category) {
        try {
            List<Gig> gigs = gigService.getGigsByCategory(category);
            return new ResponseEntity<>(gigs, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR); // Return 500 for unexpected errors
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No gigs found or could not retrieve gigs", e);
        }
    }

    @PutMapping("/gigs/updategig/{userId}/{gigId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public ResponseEntity<String> updateGig(@PathVariable String userId, @PathVariable String gigId, @RequestBody GigPostDTO gigPostDTO) throws Exception{
        try {
            gigService.updateGig(userId, gigId, gigPostDTO);
            return new ResponseEntity<>("Gig updated successfully", HttpStatus.OK);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND.value(), "Gig not found or could not update gig: " + e.getMessage(), e);
        }
    }
}

