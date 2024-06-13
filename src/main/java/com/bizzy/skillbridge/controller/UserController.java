package com.bizzy.skillbridge.controller;

import java.util.List;
import java.util.Map;

import org.apache.catalina.mapper.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.bizzy.skillbridge.entity.Gig;
import com.bizzy.skillbridge.entity.Message;
import com.bizzy.skillbridge.entity.User;
import com.bizzy.skillbridge.rest.dto.GigGetDTO;
import com.bizzy.skillbridge.rest.dto.GigPostDTO;
import com.bizzy.skillbridge.rest.dto.MessageDTO;
import com.bizzy.skillbridge.rest.dto.UserPostDTO;
import com.bizzy.skillbridge.rest.mapper.DTOMapper;
import com.bizzy.skillbridge.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.firebase.auth.UserRecord;

@RestController
public class UserController {

    private final UserService userService;

    UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/users/register")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public ResponseEntity<User> registerUser(@RequestBody UserPostDTO userPostDTO) throws Exception{
        User userInput = DTOMapper.INSTANCE.convertUserPostDTOtoEntity(userPostDTO);
        User newUser = userService.registerUser(userInput);
        if (newUser == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email address already exists");
        } else {
            return ResponseEntity.ok(newUser);
        }
    }

    @PostMapping("/users/login")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public ResponseEntity<User> loginUser(@RequestBody UserPostDTO userPostDTO) throws Exception{
        User userInput = DTOMapper.INSTANCE.convertUserPostDTOtoEntity(userPostDTO);
        User user = userService.loginUser(userInput);
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid email or password");
        } else {
            return ResponseEntity.ok(user);
        }
    }

    @PostMapping("/createfreelance")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public ResponseEntity<User> createFreelanceUser(@RequestBody String uid) throws JsonMappingException, JsonProcessingException{
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, String> map = objectMapper.readValue(uid, Map.class);
        String mappedUid = map.get("uid");
        User freelancer = userService.createFreelanceUser(mappedUid);
        if (freelancer == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Failed to create freelancer user");
        } else {
            return ResponseEntity.ok(freelancer);
        }
    }


    @PutMapping("/users/freelance/{uid}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public ResponseEntity<User> createFreelanceUser(@PathVariable String uid, @RequestBody UserPostDTO userPostDTO) throws Exception{
        User userInput = DTOMapper.INSTANCE.convertUserPostDTOtoEntity(userPostDTO);
        User freelancer = userService.updateFreelanceUser(uid, userInput);
        if (freelancer == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Failed to create freelancer user");
        } else {
            return ResponseEntity.ok(freelancer);
        }
    }

    @PutMapping("/users/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public ResponseEntity<User> updateUser(@PathVariable String id, @RequestBody UserPostDTO userPostDTO) throws Exception{
        User user = userService.updateUser(id, userPostDTO);
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Failed to update user");
        } else {
            return ResponseEntity.ok(user);
        }
    }

    @GetMapping("/users/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public ResponseEntity<User> getUser(@PathVariable String id) throws Exception{
        User user = userService.getUserRecord(id);
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        } else {
            return ResponseEntity.ok(user);
        }
    }

    @GetMapping("/users/details/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public ResponseEntity<User> getUserDetails(@PathVariable String id) throws Exception{
        User user = userService.getUser(id);
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        } else {
            return ResponseEntity.ok(user);
        }
    }

    @GetMapping("/users")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public ResponseEntity<List<User>> getAllUsers() throws Exception{
        List<User> users = userService.getAllUsers();
        if (users == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No users found");
        } else {
            return ResponseEntity.ok(users);
        }
    }

    @PostMapping("/sendmessage")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public void sendMessage(@RequestBody MessageDTO messageDTO) throws Exception{
        userService.sendMessage(messageDTO);
    }

    @GetMapping("/messages/{uid}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public ResponseEntity<Map<String, List<Message>>> getMessages(@PathVariable String uid) {
        try {
            Map<String, List<Message>> messages = userService.getMessages(uid);
            return new ResponseEntity<>(messages, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR); // Return 500 for unexpected errors
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No messages found or could not retrieve messages", e);
        }
    }

    @GetMapping("/messageconversation/{userId}/{otherUid}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public ResponseEntity<List<Message>> getMessageConversation(@PathVariable String userId, @PathVariable String otherUid) {
        try {
            List<Message> messages = userService.getMessageConversation(userId, otherUid);
            return new ResponseEntity<>(messages, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR); // Return 500 for unexpected errors
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No messages found or could not retrieve messages", e);
        }
    }

    
}
