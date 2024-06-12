package com.bizzy.skillbridge.service;

import com.google.cloud.firestore.Firestore;

import java.util.UUID;
import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.bizzy.skillbridge.constant.UserStatus;
import com.bizzy.skillbridge.entity.Gig;
import com.bizzy.skillbridge.entity.Message;
import com.bizzy.skillbridge.entity.User;
import com.bizzy.skillbridge.rest.dto.GigPostDTO;
import com.bizzy.skillbridge.rest.dto.MessageDTO;
import com.bizzy.skillbridge.rest.dto.UserPostDTO;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserRecord;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.Timestamp;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
            updatedUser.setCity(userPostDTO.getCity());

            if (userPostDTO.getCountry() != null && !userPostDTO.getCountry().isEmpty()) {
                if (!userPostDTO.getCountry().matches("^[a-zA-Z]+(?:[\\s-][a-zA-Z]+)*$")) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid country format. Please use a valid country.");
                }
            }
            updatedUser.setCountry(userPostDTO.getCountry());
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
            updatedUser.setLanguages(userPostDTO.getLanguages());

            if (userPostDTO.getInterests() != null && !userPostDTO.getInterests().isEmpty()){
                updatedUser.setInterests(userPostDTO.getInterests());
            }
            if (userPostDTO.getAddress() != null && !userPostDTO.getAddress().isEmpty()){
                updatedUser.setAddress(userPostDTO.getAddress());
            }
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

    public User createFreelanceUser(String uid){
        try {
            User user = new User();
            User userRecord = getUserRecord(uid);
            String email = userRecord.getEmail();
            String fullname = userRecord.getFullName();
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
            user.setPicture(userRecord.getPicture());
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
            User userRecord = getUserRecord(uid);
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

    public User getUserRecord(String uid) {
        try {
            UserRecord userRecord = FirebaseAuth.getInstance().getUser(uid);
            boolean userExists = userExists(userRecord.getEmail());
            if (!userExists) {
                User user = new User();
                user.setEmail(userRecord.getEmail());
                user.setPicture(userRecord.getPhotoUrl());
                user.setId(uid);
                user.setFullName(userRecord.getDisplayName());
                user.setUserToken(UUID.randomUUID().toString());
                DocumentReference newUserRef = db.collection("users").document(uid);
                WriteResult writeResult = newUserRef.set(user).get();
                return user;
            } else {
                User user = db.collection("users").document(uid).get().get().toObject(User.class);
                return user;
            }

        } catch (Exception e) {
            throw new RuntimeException("Failed to get user record", e);
        }
    }

    public void sendMessage(MessageDTO messageDTO) {
        try {
            // Validate sender and recipient existence
            User sender = getUserRecord(messageDTO.getSenderId());
            User recipient = getUserRecord(messageDTO.getRecipientId());

            // Create a new message object
            Message newMessage = new Message();
            newMessage.setId(messageDTO.getId());
            newMessage.setSender(messageDTO.getSenderId());
            newMessage.setContent(messageDTO.getContent());
            newMessage.setTimestamp(messageDTO.getTimestamp());

            // Update the sender's messages dictionary
            DocumentReference senderRef = db.collection("users").document(sender.getId());
            updateMessagesForUser(senderRef, messageDTO.getRecipientId(), newMessage);

            // Update the recipient's messages dictionary
            DocumentReference recipientRef = db.collection("users").document(recipient.getId());
            updateMessagesForUser(recipientRef, messageDTO.getSenderId(), newMessage);

        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException("Failed to send message", e);
        }
    }

    private void updateMessagesForUser(DocumentReference userRef, String contactId, Message newMessage) throws ExecutionException, InterruptedException {
        DocumentSnapshot userSnapshot = userRef.get().get();

        if (userSnapshot.exists()) {
            // Initialize the messages map
            Map<String, List<Message>> messagesMap = new HashMap<>();

            // Retrieve existing messages or initialize the structure if absent
            Object messagesField = userSnapshot.get("messages");

            if (messagesField instanceof Map) {
                // It's already a Map, so we can cast and use it directly
                messagesMap = (Map<String, List<Message>>) messagesField;
            } else if (messagesField instanceof List) {
                // It's a List, we need to convert it to a Map under a general key
                List<Message> messagesList = (List<Message>) messagesField;
                messagesMap.put("general", messagesList);
            }

            // Add the new message to the list for the specific contactId
            List<Message> contactMessages = messagesMap.computeIfAbsent(contactId, k -> new ArrayList<>());
            contactMessages.add(newMessage);

            // Save the updated messages map back to Firestore
            userRef.update("messages", messagesMap).get();
        } else {
            // If the document doesn't exist, create a new one with the messages structure
            Map<String, List<Message>> messagesMap = new HashMap<>();
            List<Message> contactMessages = new ArrayList<>();
            contactMessages.add(newMessage);
            messagesMap.put(contactId, contactMessages);

            Map<String, Object> data = new HashMap<>();
            data.put("messages", messagesMap);

            userRef.set(data).get();
        }
    }

    public Map<String, List<Message>> getMessages(String uid) {
        try {
            // Reference to the user's document in the 'users' collection
            DocumentReference userMessageRef = db.collection("users").document(uid);

            // Fetch the document
            ApiFuture<DocumentSnapshot> future = userMessageRef.get();
            DocumentSnapshot document = future.get();

            // Initialize an empty map to collect messages by contact ID
            Map<String, List<Message>> messagesByContact = new HashMap<>();

            if (document.exists() && document.contains("messages")) {
                // Retrieve the messages map from Firestore
                Map<String, List<Map<String, Object>>> messagesMap = (Map<String, List<Map<String, Object>>>) document.get("messages");

                if (messagesMap != null) {
                    // Iterate through the map entries
                    for (Map.Entry<String, List<Map<String, Object>>> entry : messagesMap.entrySet()) {
                        String contactId = entry.getKey();
                        List<Map<String, Object>> messageMaps = entry.getValue();

                        // Initialize a list to hold Message objects for this contact
                        List<Message> messages = new ArrayList<>();

                        // Convert each message map to a Message object
                        for (Map<String, Object> messageMap : messageMaps) {
                            Message message = new Message();
                            message.setId(((Number) messageMap.get("id")).intValue());
                            message.setSender((String) messageMap.get("sender"));
                            message.setContent((String) messageMap.get("content"));
                            message.setTimestamp((String) messageMap.get("timestamp"));
                            // Add the message to the list for this contact
                            messages.add(message);
                        }

                        // Add the list of messages to the map under the contact ID
                        messagesByContact.put(contactId, messages);
                    }
                }
            }
            return messagesByContact;

        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException("Failed to get messages", e);
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
