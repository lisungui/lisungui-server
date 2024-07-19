package com.bizzy.skillbridge.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.bizzy.skillbridge.entity.Freelancer;
import com.bizzy.skillbridge.entity.User;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;


@Service
public class FreelancerService {

    private final Firestore db;
    private final UserService userService;

    @Autowired
    public FreelancerService(Firestore db, UserService userService) {
        this.db = db;
        this.userService = userService;
    }

    public Freelancer createFreelanceUser(String uid, Freelancer freelancerDTO) {
        try {
            // Fetch user details from Firebase
            User userRecord = userService.getUserRecord(uid);

            // Create a new Freelancer instance using the DTO
            Freelancer freelancer = new Freelancer(userRecord);
            freelancer.setSummary(freelancerDTO.getSummary());
            freelancer.setExpertise(freelancerDTO.getExpertise());
            freelancer.setSkills(freelancerDTO.getSkills());
            freelancer.setInterests(freelancerDTO.getInterests());
            freelancer.setLanguages(freelancerDTO.getLanguages());
            freelancer.setCertifications(freelancerDTO.getCertifications());
            freelancer.setEducation(freelancerDTO.getEducation());
            freelancer.setExperience(freelancerDTO.getExperience());
            freelancer.setProjects(freelancerDTO.getProjects());
            freelancer.setSocialLinks(freelancerDTO.getSocialLinks());
            freelancer.setReferences(freelancerDTO.getReferences());
            freelancer.setAwards(freelancerDTO.getAwards());
            freelancer.setPublications(freelancerDTO.getPublications());
            freelancer.setPatents(freelancerDTO.getPatents());
            freelancer.setCourses(freelancerDTO.getCourses());
            freelancer.setOrganizations(freelancerDTO.getOrganizations());
            freelancer.setVolunteer(freelancerDTO.getVolunteer());
            freelancer.setRole("freelancer");
            freelancer.setRating(0);

            // Save the Freelancer to Firestore
            DocumentReference newUserRef = db.collection("users").document(uid);
            WriteResult writeResult = newUserRef.set(freelancer).get();

            System.out.println("Freelancer created with write result: " + writeResult);
            return freelancer;

        } catch (ResponseStatusException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Could not create Freelancer: " + e.getMessage());
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException("Failed to create freelance user", e);
        }
    }

    public Freelancer getFreelancer(String uid) {
        try {
            DocumentReference userRef = db.collection("users").document(uid);
            ApiFuture<DocumentSnapshot> future = userRef.get();
            DocumentSnapshot document = future.get();
    
            if (document.exists()) {
                Freelancer freelancer = document.toObject(Freelancer.class);
                if (freelancer != null && "freelancer".equals(freelancer.getRole())) {
                    return freelancer;
                } else {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User is not a freelancer");
                }
            } else {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Freelancer not found");
            }
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException("Failed to fetch freelancer", e);
        }
    }
    

    public List<Freelancer> getFreelancers() {
        // Fetch all freelancers from Firestore
        try {
            ApiFuture<QuerySnapshot> query = db.collection("users").whereEqualTo("role", "freelancer").get();
            QuerySnapshot querySnapshot = query.get();
            List<Freelancer> freelancers = new ArrayList<>();
            for (QueryDocumentSnapshot document : querySnapshot) {
                freelancers.add(document.toObject(Freelancer.class));
            }
            return freelancers;
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException("Failed to fetch freelancers", e);
        }
    }

    // public Freelancer updateFreelanceUser(String uid, Freelancer freelancer) {
    //     try {
    //         // Fetch the existing user record from Firestore
    //         User existingUser = userService.getUserRecord(uid);
    //         if (existingUser == null) {
    //             throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
    //         }

    //         // Validate and update fields
    //         if (freelancer.getFirstName() == null || freelancer.getFirstName().isEmpty()) {
    //             throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "First name is required");
    //         }
    //         if (freelancer.getLastName() == null || freelancer.getLastName().isEmpty()) {
    //             throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Last name is required");
    //         }

    //         // Update fields for the freelancer
    //         if (freelancer.getPhone() != null && !freelancer.getPhone().isEmpty()) {
    //             if (!freelancer.getPhone().matches("^[0-9]{10}$")) {
    //                 throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid phone number format. Please use a valid phone number.");
    //             }
    //             existingUser.setPhone(freelancer.getPhone());
    //         }

    //         if (freelancer.getCity() != null && !freelancer.getCity().isEmpty()) {
    //             if (!freelancer.getCity().matches("^[a-zA-Z]+(?:[\\s-][a-zA-Z]+)*$")) {
    //                 throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid city format. Please use a valid city.");
    //             }
    //             existingUser.setCity(freelancer.getCity());
    //         }

    //         if (freelancer.getSkills() != null && !freelancer.getSkills().isEmpty()) {
    //             existingUser.setSkills(freelancer.getSkills());
    //         }

    //         if (freelancer.getLanguages() != null && !freelancer.getLanguages().isEmpty()) {
    //             existingUser.setLanguages(freelancer.getLanguages());
    //         }

    //         if (freelancer.getCourses() != null && !freelancer.getCourses().isEmpty()) {
    //             existingUser.setCourses(freelancer.getCourses());
    //         }

    //         existingUser.setFirstName(freelancer.getFirstName());
    //         existingUser.setLastName(freelancer.getLastName());
    //         existingUser.setRating(0);
    //         existingUser.setStatus(UserStatus.ONLINE);

    //         // Save the updated user to Firestore
    //         DocumentReference userRef = db.collection("users").document(uid);
    //         WriteResult writeResult = userRef.set(existingUser).get();

    //         System.out.println("Freelancer updated with write result: " + writeResult);
    //         return (Freelancer) existingUser;

    //     } catch (ResponseStatusException e) {
    //         throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Could not update Freelancer: " + e.getMessage());
    //     } catch (Exception e) {
    //         throw new RuntimeException("Failed to update freelance user", e);
    //     }
    // }

    // Dummy method to check if the user exists based on email
    private boolean userExists(String email) {
        // This is a placeholder. Implement the actual logic to check if a user exists by email.
        return false;
    }
}
