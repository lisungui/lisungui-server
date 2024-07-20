// package com.bizzy.skillbridge.controller;

// import com.bizzy.skillbridge.entity.Message;
// import com.bizzy.skillbridge.rest.dto.MarkAsReadRequest;
// import com.bizzy.skillbridge.rest.dto.MessageDTO;
// import com.bizzy.skillbridge.service.MessagingService;
// import com.google.cloud.firestore.Firestore;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.http.HttpStatus;
// import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.*;

// import java.util.List;
// import java.util.Map;

// @RestController
// @RequestMapping("/api/messages")
// public class MessagingController {

//     private final MessagingService messagingService;

//     @Autowired
//     public MessagingController(MessagingService messagingService, Firestore db) {
//         this.messagingService = messagingService;
//     }

//     @PostMapping("/send")
//     @ResponseStatus(HttpStatus.CREATED)
//     public ResponseEntity<String> sendMessage(@RequestBody MessageDTO messageDTO) {
//         try {
//             messagingService.sendMessage(messageDTO);
//             return new ResponseEntity<>(HttpStatus.CREATED);
//         } catch (Exception e) {
//             return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
//         }
//     }

//     @GetMapping("/{userId}")
//     public ResponseEntity<Map<String, List<Message>>> getMessages(@PathVariable String userId) {
//         try {
//             Map<String, List<Message>> messages = messagingService.getMessages(userId);
//             return new ResponseEntity<>(messages, HttpStatus.OK);
//         } catch (Exception e) {
//             return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
//         }
//     }

//     @GetMapping("/conversation/{userId}/{contactId}")
//     public ResponseEntity<List<Message>> getMessageConversation(@PathVariable String userId, @PathVariable String contactId) {
//         try {
//             List<Message> messages = messagingService.getMessageConversation(userId, contactId);
//             return new ResponseEntity<>(messages, HttpStatus.OK);
//         } catch (Exception e) {
//             return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
//         }
//     }

//     @PostMapping("/like")
//     public ResponseEntity<Message> likeMessage(@RequestParam int messageId, @RequestParam String userId) {
//         try {
//             Message message = messagingService.likeMessage(messageId, userId);
//             return new ResponseEntity<>(message, HttpStatus.OK);
//         } catch (Exception e) {
//             return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
//         }
//     }

//     @PostMapping("/markAsRead")
//     public ResponseEntity<?> markMessagesAsRead(@RequestBody MarkAsReadRequest request) {
//         try {
//             messagingService.markMessagesAsRead(request.getUserId(), request.getContactId());
//             return ResponseEntity.ok().build();
//         } catch (Exception e) {
//             return ResponseEntity.status(500).body("Failed to mark messages as read");
//         }
//     }
// }
