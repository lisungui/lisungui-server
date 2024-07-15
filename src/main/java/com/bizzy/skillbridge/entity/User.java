package com.bizzy.skillbridge.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bizzy.skillbridge.constant.UserStatus;

public class User {
    private String username;
    private String id;
    private String email;
    private boolean verifiedEmail=false;
    private String firstName;
    private String lastName;
    private String fullName;
    private String phone;
    private String userToken;
    private UserStatus status;
    private Date createdDate;
    private String picture;
    private Date birthDay;
    private List<String> hobbies = new ArrayList<>();
    private String gender;
    private String role="customer";
    
    private List<String> languagesSpoken = new ArrayList<>();
    private Map<String, List<Message>> messages = new HashMap<>();
    // private Map<String, Address> address = new HashMap<>();

    public User() {
    }

    public User(String username, String email){
        this.username = username;
        this.email = email;
    }

    // Getters and setters for the fields

    public String getId() {
        return id;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isVerifiedEmail() {
        return verifiedEmail;
    }

    public void setVerifiedEmail(boolean verifiedEmail) {
        this.verifiedEmail = verifiedEmail;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender){
        this.gender = gender;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUserToken() {
        return userToken;
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }

    public UserStatus getStatus() {
        return status;
    }

    public void setStatus(UserStatus status) {
        this.status = status;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getBirthDay() {
        return birthDay;
    }

    public void setBirthDay(Date birthDay) {
        this.birthDay = birthDay;
    }

    public List<String> getHobbies() {
        return hobbies;
    }

    public void setHobbies(List<String> hobbies) {
        this.hobbies = hobbies;
    }

    public List<String> getLanguagesSpoken() {
        return languagesSpoken;
    }

    public void setLanguagesSpoken(List<String> languagesSpoken) {
        this.languagesSpoken = languagesSpoken;
    }

    // Update the methods for messages to use Map

    public Map<String, List<Message>> getMessages() {
        return messages;
    }

    public void setMessages(Map<String, List<Message>> messages) {
        this.messages = messages;
    }

    // public Map<String, Address> getAddress() {
    //     return address;
    // }

    // public void setAddress(Map<String, Address> address) {
    //     this.address = address;
    // }

    public void addMessage(String contactId, Message message) {
        List<Message> contactMessages = this.messages.computeIfAbsent(contactId, k -> new ArrayList<>());
        contactMessages.add(message);
    }
}
