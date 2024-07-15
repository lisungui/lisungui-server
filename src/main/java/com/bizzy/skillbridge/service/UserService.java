package com.bizzy.skillbridge.service;

import com.google.cloud.firestore.Firestore;

import java.util.UUID;
import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.bizzy.skillbridge.constant.UserStatus;
import com.bizzy.skillbridge.entity.User;
import com.bizzy.skillbridge.rest.dto.UserPostDTO;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserRecord;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class UserService {

    private final Firestore db;

    @Autowired
    public UserService(Firestore db) {
        this.db = db;
    }

    public User registerUser(User user) {
        if (!checkIfEmailValid(user.getEmail())) {
            return null;
        }
        if (userExists(user.getEmail())) {
            System.out.println("User already exists");
            return null;
        }
        return createUser(user);
    }

    // public User loginUser(User user) {

    //     if (userEmailAndPasswordMatch(user.getEmail(), user.getPassword())) {
    //         User loggedInUser = getUserByEmail(user.getEmail());
    //         System.out.println("User logged in");
    //         return loggedInUser;
    //     } else {
    //         System.out.println("Invalid email or password");
    //         return null;
    //     }
    // }

    
    public User updateUser(String uid, UserPostDTO userPostDTO) {
        try {
            // Validate the user existence (assuming getUserRecord validates this)
            User userRecord = getUserRecord(uid);
            User updatedUser = getUserByEmail(userRecord.getEmail());
    
            // Validate the user data
            // if (userPostDTO.getFirstName() == null || userPostDTO.getFirstName().isEmpty()) {
            //     throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "First name is required");
            // }
            // if (userPostDTO.getLastName() == null || userPostDTO.getLastName().isEmpty()) {
            //     throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Last name is required");
            // }
            if (userPostDTO.getUsername() == null || userPostDTO.getUsername().isEmpty()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username is required");
            }
            updatedUser.setUsername(userPostDTO.getUsername());
            if (userPostDTO.getPhone() != null && !userPostDTO.getPhone().isEmpty()) {
                if (!userPostDTO.getPhone().matches("^[0-9]{10}$")) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid phone number format. Please use a valid phone number.");
                }
            }
            updatedUser.setPhone(userPostDTO.getPhone());

            if (userPostDTO.getCity() != null && !userPostDTO.getCity().isEmpty()) {
                if (!userPostDTO.getCity().matches("^[a-zA-Z]+(?:[\\s-][a-zA-Z]+)*$")) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid city format. Please use a valid city.");
                }
            }
            // updatedUser.setCity(userPostDTO.getCity());

            // if (userPostDTO.getCountry() != null && !userPostDTO.getCountry().isEmpty()) {
            //     if (!userPostDTO.getCountry().matches("^[a-zA-Z]+(?:[\\s-][a-zA-Z]+)*$")) {
            //         throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid country format. Please use a valid country.");
            //     }
            // }
            // updatedUser.setCountry(userPostDTO.getCountry());
            // if (userPostDTO.getSkills() != null && !userPostDTO.getSkills().isEmpty()){
            //     for (String skill : userPostDTO.getSkills()) {
            //         if (!skill.matches("^[a-zA-Z]+(?:[\\s-][a-zA-Z]+)*$")) {
            //             throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid skill format. Please use a valid skill.");
            //         }
            //     }
            // }

            if (userPostDTO.getLanguages() != null && !userPostDTO.getLanguages().isEmpty()){
                for (String language : userPostDTO.getLanguages()) {
                    if (!language.matches("^[a-zA-Z]+(?:[\\s-][a-zA-Z]+)*$")) {
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid language format. Please use a valid language.");
                    }
                }
            }
            // updatedUser.setLanguages(userPostDTO.getLanguages());

            // if (userPostDTO.getInterests() != null && !userPostDTO.getInterests().isEmpty()){
            //     updatedUser.setInterests(userPostDTO.getInterests());
            // }
            // if (userPostDTO.getAddress() != null && !userPostDTO.getAddress().isEmpty()){
            //     updatedUser.setAddress(userPostDTO.getAddress());
            // }
            // if (userPostDTO.getCourses() != null && !userPostDTO.getCourses().isEmpty()){
            //     user.setCourses(userPostDTO.getCourses());
            // }
    
            // Update the user object with the new data
            // updatedUser.setFirstName(userPostDTO.getFirstName());
            // user.setLastName(userPostDTO.getLastName());

            // if (userPostDTO.getCity() != null && !userPostDTO.getCity().isEmpty()) {
            //     user.setCity(userPostDTO.getCity());
            // }

            // if (userPostDTO.getSkills() != null && !userPostDTO.getSkills().isEmpty()) {
            //     user.setSkills(userPostDTO.getSkills());
            // }

            // if (userPostDTO.getLanguages() != null && !userPostDTO.getLanguages().isEmpty()) {
            //     user.setLanguages(userPostDTO.getLanguages());
            // }

            // Update the user in the database
            DocumentReference userRef = db.collection("users").document(uid);
            userRef.set(updatedUser).get();

            return updatedUser;
        } catch (ResponseStatusException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Could not update user: " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException("Failed to update user", e);
        }
    }
    

    public User getUser(String id) {
        try {
            QuerySnapshot querySnapshot = db.collection("users")
                                            .whereEqualTo("id", id)
                                            .get().get();
            if (querySnapshot.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
            }
            return querySnapshot.toObjects(User.class).get(0);
        } catch (Exception e) {
            throw new RuntimeException("Failed to get user", e);
        }
    }

    public User getUserRecord(String uid) {
        try {
            UserRecord userRecord = FirebaseAuth.getInstance().getUser(uid);
            boolean userExists = userExists(userRecord.getEmail());
            if (!userExists) {
                User user = new User();
                user.setEmail(userRecord.getEmail());
                user.setPicture(userRecord.getPhotoUrl());
                user.setId(uid);
                String fullName = userRecord.getDisplayName();
                if (fullName != null) {
                    user.setFullName(fullName);
                    String[] names = splitFullName(fullName);
                    String firstName = names[0];
                    user.setFirstName(firstName);
                    user.setLastName(names[1]);
                    user.setUsername(firstName);

                }
                user.setUserToken(UUID.randomUUID().toString());
                Date date = new Date();
                user.setCreatedDate(date);
                DocumentReference newUserRef = db.collection("users").document(uid);
                WriteResult writeResult = newUserRef.set(user).get();
                return user;
            } else {
                User user = db.collection("users").document(uid).get().get().toObject(User.class);
                return user;
            }

        } catch (Exception e) {
            throw new RuntimeException("Failed to get user record: " + e.getMessage());
        }
    }


    private User getUserByEmail(String email) {
        try {
            QuerySnapshot querySnapshot = db.collection("users")
                                            .whereEqualTo("email", email)
                                            .get().get();
            if (querySnapshot.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
            }
            return querySnapshot.toObjects(User.class).get(0);
        } catch (Exception e) {
            throw new RuntimeException("Failed to get user", e);
        }
    }

    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        try {
            // Fetch all documents in the 'users' collection to get their IDs
            ApiFuture<QuerySnapshot> future = db.collection("users").get();
            QuerySnapshot querySnapshot = future.get();

            // Iterate through each document in the collection to get its ID
            for (DocumentSnapshot document : querySnapshot.getDocuments()) {
                if (document.exists()) {
                    String uid = document.getId(); // Get the document ID (uid)
                    
                    User user = getUserRecord(uid);
                    users.add(user);
                }
            }
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException("Failed to get all users", e);
        }
        return users;
    }

    private boolean userExists(String email) {
        try {
            QuerySnapshot querySnapshot = db.collection("users")
                                            .whereEqualTo("email", email)
                                            .get().get();
            return !querySnapshot.isEmpty();
        } catch (Exception e) {
            throw new RuntimeException("Failed to check if user exists", e);
        }
    }

    private boolean userEmailAndPasswordMatch(String email, String password) {
        try {
            QuerySnapshot querySnapshot = db.collection("users")
                                            .whereEqualTo("email", email)
                                            .whereEqualTo("password", password)
                                            .get().get();
            return !querySnapshot.isEmpty();
        } catch (Exception e) {
            throw new RuntimeException("Failed to check if user exists", e);
        }
    }

    private User createUser(User user) {
        try {
            DocumentReference newUserRef = db.collection("users").document();
            String id = newUserRef.getId();
            user.setId(id);
            user.setUserToken(UUID.randomUUID().toString());
            user.setStatus(UserStatus.ONLINE);
            WriteResult writeResult = newUserRef.set(user).get();
            return user;
        } catch (Exception e) {
            throw new RuntimeException("Failed to create user", e);
        }
    }

    private String[] splitFullName(String fullName) {
        int lastSpaceIndex = fullName.lastIndexOf(" ");
        
        if (lastSpaceIndex == -1) {
            return new String[]{fullName, ""};
        }
        
        String firstPart = fullName.substring(0, lastSpaceIndex);
        String lastPart = fullName.substring(lastSpaceIndex + 1);
        
        return new String[]{firstPart, lastPart};
    }

    private boolean checkIfEmailValid(String email) {
        if (email == null) {
            return false;
        }  else if (!email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid email format. Please use a valid email.");
        } else {
            return true;
        }
    }
    
}
