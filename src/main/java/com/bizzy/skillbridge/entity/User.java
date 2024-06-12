package com.bizzy.skillbridge.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bizzy.skillbridge.constant.UserStatus;

public class User {
    private String username;
    private String password;
    private String id;
    private String email;
    private boolean verifiedEmail=false;
    private int rating;
    private String firstName;
    private String lastName;
    private String fullName;
    private String phone;
    private String address;
    private String city;
    private String state;
    private String zip;
    private String country;
    private String userToken;
    private String role;
    private UserStatus status;
    private String createdDate;
    private String picture;
    private Date birthDay;
    private List<String> skills = new ArrayList<>();
    private List<String> interests = new ArrayList<>();
    private List<String> languages = new ArrayList<>();
    private List<String> certifications = new ArrayList<>();
    private List<String> education = new ArrayList<>();
    private List<String> experience = new ArrayList<>();
    private List<String> projects = new ArrayList<>();
    private List<String> socialLinks = new ArrayList<>();
    private List<String> references = new ArrayList<>();
    private List<String> awards = new ArrayList<>();
    private List<String> publications = new ArrayList<>();
    private List<String> patents = new ArrayList<>();
    private List<String> courses = new ArrayList<>();
    private List<String> hobbies = new ArrayList<>();
    private List<String> organizations = new ArrayList<>();
    private List<String> volunteer = new ArrayList<>();
    private List<String> languagesSpoken = new ArrayList<>();
    private Map<String, List<Message>> messages = new HashMap<>(); // Updated to Map

    public User() {
    }

    public User(String username, String email, String password){
        this.username = username;
        this.email = email;
        this.password = password;
    }

    // Getters and setters for the fields

    public String getId() {
        return id;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getUserToken() {
        return userToken;
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public UserStatus getStatus() {
        return status;
    }

    public void setStatus(UserStatus status) {
        this.status = status;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public Date getBirthDay() {
        return birthDay;
    }

    public void setBirthDay(Date birthDay) {
        this.birthDay = birthDay;
    }

    public List<String> getSkills() {
        return skills;
    }

    public void setSkills(List<String> skills) {
        this.skills = skills;
    }

    public List<String> getInterests() {
        return interests;
    }

    public void setInterests(List<String> interests) {
        this.interests = interests;
    }

    public List<String> getLanguages() {
        return languages;
    }

    public void setLanguages(List<String> languages) {
        this.languages = languages;
    }

    public List<String> getCertifications() {
        return certifications;
    }

    public void setCertifications(List<String> certifications) {
        this.certifications = certifications;
    }

    public List<String> getEducation() {
        return education;
    }

    public void setEducation(List<String> education) {
        this.education = education;
    }

    public List<String> getExperience() {
        return experience;
    }

    public void setExperience(List<String> experience) {
        this.experience = experience;
    }

    public List<String> getProjects() {
        return projects;
    }

    public void setProjects(List<String> projects) {
        this.projects = projects;
    }

    public List<String> getSocialLinks() {
        return socialLinks;
    }

    public void setSocialLinks(List<String> socialLinks) {
        this.socialLinks = socialLinks;
    }

    public List<String> getReferences() {
        return references;
    }

    public void setReferences(List<String> references) {
        this.references = references;
    }

    public List<String> getAwards() {
        return awards;
    }

    public void setAwards(List<String> awards) {
        this.awards = awards;
    }

    public List<String> getPublications() {
        return publications;
    }

    public void setPublications(List<String> publications) {
        this.publications = publications;
    }

    public List<String> getPatents() {
        return patents;
    }

    public void setPatents(List<String> patents) {
        this.patents = patents;
    }

    public List<String> getCourses() {
        return courses;
    }

    public void setCourses(List<String> courses) {
        this.courses = courses;
    }

    public List<String> getHobbies() {
        return hobbies;
    }

    public void setHobbies(List<String> hobbies) {
        this.hobbies = hobbies;
    }

    public List<String> getOrganizations() {
        return organizations;
    }

    public void setOrganizations(List<String> organizations) {
        this.organizations = organizations;
    }

    public List<String> getVolunteer() {
        return volunteer;
    }

    public void setVolunteer(List<String> volunteer) {
        this.volunteer = volunteer;
    }

    public List<String> getLanguagesSpoken() {
        return languagesSpoken;
    }

    public void setLanguagesSpoken(List<String> languagesSpoken) {
        this.languagesSpoken = languagesSpoken;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public void addSkills(List<String> skills) {
        this.skills.addAll(skills);
    }

    public void addInterests(List<String> interests) {
        this.interests.addAll(interests);
    }

    public void addLanguages(List<String> languages) {
        this.languages.addAll(languages);
    }

    public void addCertifications(List<String> certifications) {
        this.certifications.addAll(certifications);
    }

    public void addEducation(List<String> education) {
        this.education.addAll(education);
    }

    public void addExperience(List<String> experience) {
        this.experience.addAll(experience);
    }

    public void addProjects(List<String> projects) {
        this.projects.addAll(projects);
    }

    public void addSocialLinks(List<String> socialLinks) {
        this.socialLinks.addAll(socialLinks);
    }

    // Update the methods for messages to use Map

    public Map<String, List<Message>> getMessages() {
        return messages;
    }

    public void setMessages(Map<String, List<Message>> messages) {
        this.messages = messages;
    }

    public void addMessage(String contactId, Message message) {
        List<Message> contactMessages = this.messages.computeIfAbsent(contactId, k -> new ArrayList<>());
        contactMessages.add(message);
    }
}
