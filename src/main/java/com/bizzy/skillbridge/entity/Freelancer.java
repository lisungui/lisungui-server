package com.bizzy.skillbridge.entity;

import java.util.ArrayList;
import java.util.List;

public class Freelancer extends User {
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
    private List<String> organizations = new ArrayList<>();
    private List<String> volunteer = new ArrayList<>();
    private int rating;
    private String role;

    public Freelancer() {
        super();
    }

    public Freelancer(User user) {
        this.setId(user.getId());
        this.setEmail(user.getEmail());
        this.setUsername(user.getUsername());
        this.setFullName(user.getFullName());
        this.setFirstName(user.getFirstName());
        this.setLastName(user.getLastName());
        this.setPicture(user.getPicture());
        this.setUserToken(user.getUserToken());
        this.setCreatedDate(user.getCreatedDate());
        this.setPhone(user.getPhone());
        this.setHobbies(user.getHobbies());
        this.setLanguagesSpoken(user.getLanguagesSpoken());
        this.setMessages(user.getMessages());
        this.setBirthDay(user.getBirthDay());
        this.setGender(user.getGender());
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

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "Freelancer{" +
                "skills=" + skills +
                ", interests=" + interests +
                ", languages=" + languages +
                ", certifications=" + certifications +
                ", education=" + education +
                ", experience=" + experience +
                ", projects=" + projects +
                ", socialLinks=" + socialLinks +
                ", references=" + references +
                ", awards=" + awards +
                ", publications=" + publications +
                ", patents=" + patents +
                ", courses=" + courses +
                ", organizations=" + organizations +
                ", volunteer=" + volunteer +
                ", rating=" + rating +
                ", role='" + role + '\'' +
                '}' + super.toString(); // Include User's toString for inherited fields
    }
}
