package com.bizzy.skillbridge.service;

import com.google.cloud.firestore.Firestore;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.bizzy.skillbridge.constant.UserStatus;
import com.bizzy.skillbridge.entity.User;
import com.bizzy.skillbridge.rest.dto.UserPostDTO;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserRecord;
import com.google.cloud.firestore.DocumentReference;
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

    public User loginUser(User user) {

        if (userEmailAndPasswordMatch(user.getEmail(), user.getPassword())) {
            User loggedInUser = getUserByEmail(user.getEmail());
            System.out.println("User logged in");
            return loggedInUser;
        } else {
            System.out.println("Invalid email or password");
            return null;
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

    public User createFreelanceUser(String uid){
        try {
            User user = new User();
            UserRecord userRecord = getUserRecord(uid);
            String email = userRecord.getEmail();
            String fullname = userRecord.getDisplayName();
            if (userExists(email)) {
                user.setEmail(email);
                return user;
            }
            if (fullname != null && !fullname.isEmpty()) {
                int firstSpaceIndex = fullname.indexOf(' ');
                if (firstSpaceIndex != -1) {
                    user.setFirstName(fullname.substring(0, firstSpaceIndex));
                    user.setLastName(fullname.substring(firstSpaceIndex + 1));
                }
            } 
            user.setUserToken(UUID.randomUUID().toString());
            user.setEmail(email);
            user.setRole("freelancer");
            user.setId(uid);
            DocumentReference newUserRef = db.collection("users").document(uid);
            WriteResult writeResult = newUserRef.set(user).get();
            return user;
        } catch (ResponseStatusException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Could not create: "+ e.getMessage());  // Rethrow ResponseStatusException directly
        } catch (Exception e) {
            throw new RuntimeException("Failed to create freelance user", e);
        }
    }

    public User updateFreelanceUser(String uid, User user){
        try {
            UserRecord userRecord = getUserRecord(uid);
            User updatedUser = getUserByEmail(userRecord.getEmail());
            if (user.getLastName().isEmpty() || user.getFirstName() == null ) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "First name is required");
            }
            if (user.getLastName().isEmpty() || user.getLastName() == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Last name is required");
            }
            if (user.getPhone() != null && !user.getPhone().isEmpty()) {
                if (!user.getPhone().matches("^[0-9]{10}$")) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid phone number format. Please use a valid phone number.");
                }
                updatedUser.setPhone(user.getPhone());
            }
            if (user.getCity() != null && !user.getCity().isEmpty()) {
                if (!user.getCity().matches("^[a-zA-Z]+(?:[\\s-][a-zA-Z]+)*$")) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid city format. Please use a valid city.");
                }
                updatedUser.setCity(user.getCity());
            }
            if (user.getSkills() != null && !user.getSkills().isEmpty()){
                // for (String skill : user.getSkills()) {
                //     if (!skill.matches("^[a-zA-Z]+(?:[\\s-][a-zA-Z]+)*$")) {
                //         throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid skill format. Please use a valid skill.");
                //     }
                // }
                updatedUser.setSkills(user.getSkills());
            }
            if (user.getLanguages() != null && !user.getLanguages().isEmpty()){
                for (String language : user.getLanguages()) {
                    if (!language.matches("^[a-zA-Z]+(?:[\\s-][a-zA-Z]+)*$")) {
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid language format. Please use a valid language.");
                    }
                }
                updatedUser.setLanguages(user.getLanguages());
            }
            if (user.getCourses() != null && !user.getCourses().isEmpty()){
                updatedUser.setCourses(user.getCourses());
            }
            updatedUser.setFirstName(user.getFirstName());
            updatedUser.setLastName(user.getLastName());
            updatedUser.setCity(user.getCity());
            updatedUser.setRating(0);
            DocumentReference newUserRef = db.collection("users").document(uid);
            updatedUser.setId(uid);
            updatedUser.setStatus(UserStatus.ONLINE);
            WriteResult writeResult = newUserRef.set(updatedUser).get();
            return updatedUser;

        } catch (ResponseStatusException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Could not create: "+ e.getMessage());  // Rethrow ResponseStatusException directly
        } catch (Exception e) {
            throw new RuntimeException("Failed to create freelance user", e);
        }
    }

    public UserRecord getUserRecord(String uid) {
        try {
            UserRecord userRecord = FirebaseAuth.getInstance().getUser(uid);
            return userRecord;
        } catch (Exception e) {
            throw new RuntimeException("Failed to get user record", e);
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
        try {
            QuerySnapshot querySnapshot = db.collection("users").get().get();
            return querySnapshot.toObjects(User.class);
        } catch (Exception e) {
            throw new RuntimeException("Failed to get all users", e);
        }
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
