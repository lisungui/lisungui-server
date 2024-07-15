package com.bizzy.skillbridge.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.springframework.stereotype.Service;

import com.bizzy.skillbridge.entity.Message;
import com.bizzy.skillbridge.entity.User;
import com.bizzy.skillbridge.rest.dto.MessageDTO;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;

@Service
public class MessagingService {

    private final Firestore db;
    private final UserService userService;

    public MessagingService(Firestore db, UserService userService) {
        this.db = db;
        this.userService = userService;
    }

    public void sendMessage(MessageDTO messageDTO) {
        try {
            User sender = userService.getUserRecord(messageDTO.getSenderId());
            User recipient = userService.getUserRecord(messageDTO.getRecipientId());

            Message newMessage = new Message();
            newMessage.setId(messageDTO.getId());
            newMessage.setSender(messageDTO.getSenderId());
            newMessage.setContent(messageDTO.getContent());
            newMessage.setTimestamp(messageDTO.getTimestamp());

            DocumentReference senderRef = db.collection("messages").document(sender.getId());
            updateMessagesForUser(senderRef, messageDTO.getRecipientId(), newMessage);

            DocumentReference recipientRef = db.collection("messages").document(recipient.getId());
            updateMessagesForUser(recipientRef, messageDTO.getSenderId(), newMessage);

        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException("Failed to send message: " + e.getMessage());
        }
    }

    private void updateMessagesForUser(DocumentReference userRef, String contactId, Message newMessage) throws ExecutionException, InterruptedException {
        DocumentSnapshot userSnapshot = userRef.get().get();
        DocumentReference messagesRef = db.collection("messages").document(userRef.getId());
        DocumentSnapshot messagesSnapshot = messagesRef.get().get();

        Map<String, List<Message>> messagesMap;

        if (messagesSnapshot.exists()) {
            Object messagesField = messagesSnapshot.get("messages");

            if (messagesField instanceof Map) {
                messagesMap = (Map<String, List<Message>>) messagesField;
            } else {
                messagesMap = new HashMap<>();
            }
        } else {
            messagesMap = new HashMap<>();
        }

        List<Message> contactMessages = messagesMap.computeIfAbsent(contactId, k -> new ArrayList<>());
        contactMessages.add(newMessage);

        messagesRef.set(Collections.singletonMap("messages", messagesMap)).get();
    }

    public Map<String, List<Message>> getMessages(String uid) {
        try {
            DocumentReference userMessageRef = db.collection("messages").document(uid);

            ApiFuture<DocumentSnapshot> future = userMessageRef.get();
            DocumentSnapshot document = future.get();

            Map<String, List<Message>> messagesByContact = new HashMap<>();

            if (document.exists() && document.contains("messages")) {
                Map<String, List<Map<String, Object>>> messagesMap = (Map<String, List<Map<String, Object>>>) document.get("messages");

                if (messagesMap != null) {
                    for (Map.Entry<String, List<Map<String, Object>>> entry : messagesMap.entrySet()) {
                        String contactId = entry.getKey();
                        List<Map<String, Object>> messageMaps = entry.getValue();

                        List<Message> messages = new ArrayList<>();

                        for (Map<String, Object> messageMap : messageMaps) {
                            Message message = new Message();
                            message.setId(((Number) messageMap.get("id")).intValue());
                            message.setSender((String) messageMap.get("sender"));
                            message.setContent((String) messageMap.get("content"));
                            message.setTimestamp((String) messageMap.get("timestamp"));
                            // Add the message to the list for this contact
                            messages.add(message);
                        }

                        messagesByContact.put(contactId, messages);
                    }
                }
            }
            return messagesByContact;

        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException("Failed to get messages", e);
        }
    }

    public List<Message> getMessageConversation(String uid, String contactId) {
        try {
            DocumentReference userMessageRef = db.collection("messages").document(uid);

            ApiFuture<DocumentSnapshot> future = userMessageRef.get();
            DocumentSnapshot document = future.get();

            List<Message> messages = new ArrayList<>();

            if (document.exists() && document.contains("messages")) {
                Map<String, List<Map<String, Object>>> messagesMap = (Map<String, List<Map<String, Object>>>) document.get("messages");

                if (messagesMap != null && messagesMap.containsKey(contactId)) {
                    List<Map<String, Object>> messageMaps = messagesMap.get(contactId);

                    for (Map<String, Object> messageMap : messageMaps) {
                        Message message = new Message();
                        message.setId(((Number) messageMap.get("id")).intValue());
                        message.setSender((String) messageMap.get("sender"));
                        message.setContent((String) messageMap.get("content"));
                        message.setTimestamp((String) messageMap.get("timestamp"));
                        messages.add(message);
                    }
                }
            }
            return messages;

        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException("Failed to get message conversation", e);
        }
    }

    public Message likeMessage(int messageId, String userId) {
        try {
            DocumentReference userMessagesRef = db.collection("messages").document(userId);
            DocumentSnapshot userMessagesSnapshot = userMessagesRef.get().get();
    
            if (userMessagesSnapshot.exists()) {
                Map<String, List<Map<String, Object>>> messagesMap = (Map<String, List<Map<String, Object>>>) userMessagesSnapshot.get("messages");
    
                if (messagesMap != null) {
                    for (Map.Entry<String, List<Map<String, Object>>> entry : messagesMap.entrySet()) {
                        List<Map<String, Object>> messageMaps = entry.getValue();
    
                        for (Map<String, Object> messageMap : messageMaps) {
                            if (messageId == ((Number) messageMap.get("id")).intValue()) {
                                Message message = new Message();
                                message.setId(((Number) messageMap.get("id")).intValue());
                                message.setSender((String) messageMap.get("sender"));
                                message.setContent((String) messageMap.get("content"));
                                message.setTimestamp((String) messageMap.get("timestamp"));
    
                                List<String> likes = (List<String>) messageMap.get("likes");
                                if (likes == null) {
                                    likes = new ArrayList<>();
                                }
    
                                if (likes.contains(userId)) {
                                    likes.remove(userId);
                                } else {
                                    likes.add(userId);
                                }
                                messageMap.put("likes", likes);
                                message.setLikes(likes);
    
                                userMessagesRef.set(Collections.singletonMap("messages", messagesMap)).get();
                                return message;
                            }
                        }
                    }
                }
            }
            throw new RuntimeException("Message not found");
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException("Failed to like message", e);
        }
    }

    public void markMessagesAsRead(String userId, String contactId) throws Exception {
        DocumentReference userMessageRef = db.collection("messages").document(userId);
        ApiFuture<DocumentSnapshot> future = userMessageRef.get();
        DocumentSnapshot document = future.get();

        if (document.exists() && document.contains("messages")) {
            Map<String, List<Map<String, Object>>> messagesMap = (Map<String, List<Map<String, Object>>>) document.get("messages");

            if (messagesMap != null && messagesMap.containsKey(contactId)) {
                List<Map<String, Object>> messageMaps = messagesMap.get(contactId);

                for (Map<String, Object> messageMap : messageMaps) {
                    messageMap.put("read", true);
                }

                userMessageRef.set(Collections.singletonMap("messages", messagesMap)).get();
            }
        }
    }
    
}
