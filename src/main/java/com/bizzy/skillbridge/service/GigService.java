package com.bizzy.skillbridge.service;

import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.bizzy.skillbridge.entity.Gig;
import com.bizzy.skillbridge.entity.User;
import com.bizzy.skillbridge.rest.dto.GigPostDTO;
import com.google.api.core.ApiFuture;
import com.google.cloud.Timestamp;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.WriteResult;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class GigService {

    private final Firestore db;
    private final UserService userService;

    @Autowired
    public GigService(Firestore db, UserService userService) {
        this.db = db;
        this.userService = userService;
    }

    public void createGig(String uid, GigPostDTO gigPostDTO) {
        try {
            // Validate the user existence (assuming getUserRecord validates this)
            User user = userService.getUserRecord(uid);

            // Validate the gig data
            if (gigPostDTO.getTitle() == null || gigPostDTO.getTitle().isEmpty()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Title is required");
            }
            if (gigPostDTO.getDescription() == null || gigPostDTO.getDescription().isEmpty()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Description is required");
            }
            if (gigPostDTO.getCategory() == null || gigPostDTO.getCategory().isEmpty()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Category is required");
            }
            if (gigPostDTO.getPrice() <= 0) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Price is required and must be greater than 0");
            }
            if (gigPostDTO.getStatus() == null || gigPostDTO.getStatus().isEmpty()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Status is required");
            }
            if (gigPostDTO.getDeadline() == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Deadline is required");
            }
    
            // Create a new Gig instance from the DTO
            Gig newGig = new Gig();
            newGig.setId(UUID.randomUUID().toString()); // Generate a new unique ID for the gig
            newGig.setUserCreator(user.getId());
            newGig.setTitle(gigPostDTO.getTitle());
            newGig.setDescription(gigPostDTO.getDescription());
            newGig.setCategory(gigPostDTO.getCategory());
            newGig.setPrice(gigPostDTO.getPrice());
            newGig.setStatus(gigPostDTO.getStatus());
            Date createdDate = new Date();
            newGig.setCreatedDate(createdDate);
            Date deadline = gigPostDTO.getDeadline();
            newGig.setDeadline(deadline);
            long diffInMillies = deadline.getTime() - createdDate.getTime();
            long durationInDays = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
            newGig.setDuration((int) durationInDays);
            newGig.setUserCreatorName(user.getUsername());
            newGig.setUserCreatorEmail(user.getEmail());
    
            // Reference to the 'gigs' collection
            DocumentReference userGigRef = db.collection("gigs").document(uid);

            ApiFuture<DocumentSnapshot> future = userGigRef.get();
            DocumentSnapshot document = future.get();

            // Initialize the gig list if not present
            List<Map<String, Object>> existingGigs = new ArrayList<>();
            if (document.exists() && document.contains("gigs")) {
                existingGigs = (List<Map<String, Object>>) document.get("gigs");
            }

            // Convert the new gig to a map
            Map<String, Object> newGigMap = new HashMap<>();
            newGigMap.put("id", newGig.getId());
            newGigMap.put("userCreator", newGig.getUserCreator());
            newGigMap.put("title", newGig.getTitle());
            newGigMap.put("description", newGig.getDescription());
            newGigMap.put("category", newGig.getCategory());
            newGigMap.put("price", newGig.getPrice());
            newGigMap.put("duration", newGig.getDuration());
            newGigMap.put("status", newGig.getStatus());
            newGigMap.put("createdDate", newGig.getCreatedDate());
            newGigMap.put("deadline", newGig.getDeadline());
            newGigMap.put("userCreatorName", newGig.getUserCreatorName());
            newGigMap.put("userCreatorEmail", newGig.getUserCreatorEmail());
    
            existingGigs.add(newGigMap);

            // Update the document with the new list of gigs
            Map<String, Object> updateMap = new HashMap<>();
            updateMap.put("gigs", existingGigs);

        userGigRef.set(updateMap).get();
        System.out.println("Gig created successfully");
    
        } catch (ResponseStatusException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Could not create Gig: " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException("Failed to create gig", e);
        }
    }

    public List<Gig> getGigs(String uid) {
        try {
            // Reference to the user's gig document
            User user = userService.getUserRecord(uid);
            DocumentReference userGigRef = db.collection("gigs").document(uid);
    
            // Fetch the document
            ApiFuture<DocumentSnapshot> future = userGigRef.get();
            DocumentSnapshot document = future.get();
    
            if (document.exists() && document.contains("gigs")) {
                List<Map<String, Object>> gigMaps = (List<Map<String, Object>>) document.get("gigs");
                
                // Convert the list of maps to a list of Gig objects
                List<Gig> gigs = new ArrayList<>();
                for (Map<String, Object> gigMap : gigMaps) {
                    Gig gig = convertGigMapToGigJavaObject(gigMap);
                    gigs.add(gig);
                }
                return gigs;
            } else {
                return new ArrayList<>();
            }
    
        } catch (Exception e) {
            throw new RuntimeException("Failed to get gigs", e);
        }
    }

    public List<Gig> getGigsByCategory(String category) {
        try {
            // Reference to the 'gigs' collection
            CollectionReference gigsRef = db.collection("gigs");

            // Get all documents in the 'gigs' collection
            ApiFuture<QuerySnapshot> query = gigsRef.get();

            // Get the results from the query
            QuerySnapshot querySnapshot = query.get();

            List<Gig> gigs = new ArrayList<>();

            for (QueryDocumentSnapshot document : querySnapshot.getDocuments()) {
                // Check if the document contains a list of gigs
                if (document.contains("gigs")) {

                    // Extract the list of gigs from the document
                    List<Map<String, Object>> gigMaps = (List<Map<String, Object>>) document.get("gigs");

                    for (Map<String, Object> gigMap : gigMaps) {
                        // Check if the gig matches the specified category
                        if (category.equals(gigMap.get("category"))) {

                            // Create a new Gig object and populate its fields
                            Gig gig = convertGigMapToGigJavaObject(gigMap);

                            // Add the gig to the result list
                            gigs.add(gig);
                        }
                    }
                }
            }
            return gigs;

        } catch (Exception e) {
            throw new RuntimeException("Failed to get gigs by category", e);
        }
    }

    public Gig getGig(String uid, String gigId) {
        try {
            User user = userService.getUserRecord(uid);
            DocumentReference userGigRef = db.collection("gigs").document(uid);
    
            // Fetch the document
            ApiFuture<DocumentSnapshot> future = userGigRef.get();
            DocumentSnapshot document = future.get();
    
            if (document.exists() && document.contains("gigs")) {
                List<Map<String, Object>> gigMaps = (List<Map<String, Object>>) document.get("gigs");
                
                for (Map<String, Object> gigMap : gigMaps) {
                    String currentGigId = (String) gigMap.get("id");
                    if (currentGigId.equals(gigId)) {
                        Gig gig = new Gig();
                        gig.setId(currentGigId);
                        gig.setUserCreator((String) gigMap.get("userCreator"));
                        gig.setTitle((String) gigMap.get("title"));
                        gig.setDescription((String) gigMap.get("description"));
                        gig.setCategory((String) gigMap.get("category"));
                        gig.setPrice(((Number) gigMap.get("price")).floatValue()); // Convert to float
                        gig.setDuration(((Number) gigMap.get("duration")).intValue());
                        gig.setStatus((String) gigMap.get("status"));
                        Timestamp timestamp = (Timestamp) gigMap.get("createdDate");
                        gig.setCreatedDate(timestamp.toDate()); // Convert to java.util.Date using toDate()
                        Timestamp deadline = (Timestamp) gigMap.get("deadline");
                        gig.setDeadline(deadline.toDate());
                        gig.setUpdateDeadline(null);
                        return gig;
                    }
                }
            } else {
                return new Gig();
            }

        } catch (Exception e) {
            throw new RuntimeException("Failed to get gig", e);
        }
        return null;
    }

    public void updateGig(String userId, String gigId, GigPostDTO gigPostDTO) {
        try {
            // Get the specific gig to update
            Gig gigToUpdate = getGig(userId, gigId);
            
            if (gigToUpdate == null || gigToUpdate.getId() == null || gigToUpdate.getId().isEmpty()){
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Gig not found with id: " + gigId);
            }
            // Reference to the user's gig document
            DocumentReference userGigRef = db.collection("gigs").document(userId);
    
            // Fetch the document
            ApiFuture<DocumentSnapshot> future = userGigRef.get();
            DocumentSnapshot document = future.get();
    
            if (document.exists() && document.contains("gigs")) {
                List<Map<String, Object>> gigMaps = (List<Map<String, Object>>) document.get("gigs");
    
                // Find and update the specific gig in the list
                for (int i = 0; i < gigMaps.size(); i++) {
                    Map<String, Object> gigMap = gigMaps.get(i);
                    String currentGigId = (String) gigMap.get("id");
    
                    if (currentGigId.equals(gigId)) {
                        // Update the fields of the specific gig
                        updateGigFields(gigMap, gigPostDTO);
    
                        // Replace the old gig with the updated gig in the list
                        gigMaps.set(i, gigMap);
    
                        // Update the document with the new list of gigs
                        Map<String, Object> updateMap = new HashMap<>();
                        updateMap.put("gigs", gigMaps);
    
                        ApiFuture<WriteResult> writeResult = userGigRef.set(updateMap);
                        writeResult.get();
    
                        System.out.println("Gig updated successfully");
                        return;
                    }
                }
            } else {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Gig list not found for user: " + userId);
            }
        } catch (ResponseStatusException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Could not update Gig: " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException("Failed to update gig", e);
        }
    }
    
    private void updateGigFields(Map<String, Object> gigMap, GigPostDTO gigPostDTO) {
        String newTitle = gigPostDTO.getTitle();
        if (newTitle != null && !newTitle.isEmpty()) {
            gigMap.put("title", newTitle);
        }
        String newDescription = gigPostDTO.getDescription();
        if (newDescription != null && !newDescription.isEmpty()) {
            gigMap.put("description", newDescription);
        }
        Date newDeadline = gigPostDTO.getDeadline();
        if (newDeadline != null) {
            gigMap.put("deadline", newDeadline);
    
            // Recalculate the duration in days if the deadline is updated
            Timestamp timestamp = (Timestamp) gigMap.get("createdDate");
            Date createdDate = timestamp.toDate();
            long diffInMillies = newDeadline.getTime() - createdDate.getTime();
            long durationInDays = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
            gigMap.put("duration", (int) durationInDays);
            
        }
        float newPrice = gigPostDTO.getPrice();
        if (newPrice > 0) {
            gigMap.put("price", newPrice);
        }
        // Update the updatedDate to the current date
        Date updatedDate = new Date();
        gigMap.put("updatedDate", updatedDate);
    }

    private Gig convertGigMapToGigJavaObject(Map<String, Object> gigMap) {
        Gig gig = new Gig();
        gig.setId((String) gigMap.get("id"));
        gig.setUserCreator((String) gigMap.get("userCreator"));
        gig.setTitle((String) gigMap.get("title"));
        gig.setDescription((String) gigMap.get("description"));
        gig.setCategory((String) gigMap.get("category"));
        gig.setPrice(((Number) gigMap.get("price")).floatValue()); // Convert to float
        gig.setDuration(((Number) gigMap.get("duration")).intValue());
        gig.setStatus((String) gigMap.get("status"));
        gig.setUserCreatorEmail((String) gigMap.get("userCreatorEmail"));
        String userCreatorName = (String) gigMap.get("userCreatorName");
        if (userCreatorName != null) {
            gig.setUserCreatorName(userCreatorName);
        }
        Timestamp timestamp = (Timestamp) gigMap.get("createdDate");
        gig.setCreatedDate(timestamp.toDate()); // Convert to java.util.Date using toDate()
        Timestamp deadline = (Timestamp) gigMap.get("deadline");
        gig.setDeadline(deadline.toDate());
        Timestamp updatedDate = (Timestamp) gigMap.get("updatedDate");
        if (updatedDate != null) {
            gig.setUpdatedDate(updatedDate.toDate());
        }
        return gig;
    }
    
}
