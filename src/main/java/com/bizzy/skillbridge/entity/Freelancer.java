package com.bizzy.skillbridge.entity;

import java.util.ArrayList;
import java.util.List;

public class Freelancer extends User {
    private String summary;
    private List<Skill> skills = new ArrayList<>();
    private List<String> interests = new ArrayList<>();
    private List<Language> languages = new ArrayList<>();
    private List<String> certifications = new ArrayList<>();
    private List<String> education = new ArrayList<>();
    private List<Experience> experience = new ArrayList<>();
    private List<String> projects = new ArrayList<>();
    private List<String> socialLinks = new ArrayList<>();
    private List<Reference> references = new ArrayList<>();
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

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public List<Skill> getSkills() {
        return skills;
    }

    public void setSkills(List<Skill> skills) {
        this.skills = skills;
    }

    public List<String> getInterests() {
        return interests;
    }

    public void setInterests(List<String> interests) {
        this.interests = interests;
    }

    public List<Language> getLanguages() {
        return languages;
    }

    public void setLanguages(List<Language> languages) {
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

    public List<Experience> getExperience() {
        return experience;
    }

    public void setExperience(List<Experience> experience) {
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

    public List<Reference> getReferences() {
        return references;
    }

    public void setReferences(List<Reference> references) {
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
                "summary='" + summary + '\'' +
                ", skills=" + skills +
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

    public static class Skill {
        private String skill;
        private String level;

        public Skill() {
        }

        public Skill(String skill, String level) {
            this.skill = skill;
            this.level = level;
        }

        public String getSkill() {
            return skill;
        }

        public void setSkill(String skill) {
            this.skill = skill;
        }

        public String getLevel() {
            return level;
        }

        public void setLevel(String level) {
            this.level = level;
        }
    }

    public static class Language {
        private String language;
        private String level;

        public Language() {
        }

        public Language(String language, String level) {
            this.language = language;
            this.level = level;
        }

        public String getLanguage() {
            return language;
        }

        public void setLanguage(String language) {
            this.language = language;
        }

        public String getLevel() {
            return level;
        }

        public void setLevel(String level) {
            this.level = level;
        }
    }

    public static class Experience {
        private String companyName;
        private String position;
        private String startDate;
        private String endDate;
        private String responsibility;

        public Experience() {
        }

        public Experience(String companyName, String position, String startDate, String endDate, String responsibility) {
            this.companyName = companyName;
            this.position = position;
            this.startDate = startDate;
            this.endDate = endDate;
            this.responsibility = responsibility;
        }

        public String getCompanyName() {
            return companyName;
        }

        public void setCompanyName(String companyName) {
            this.companyName = companyName;
        }

        public String getPosition() {
            return position;
        }

        public void setPosition(String position) {
            this.position = position;
        }

        public String getStartDate() {
            return startDate;
        }

        public void setStartDate(String startDate) {
            this.startDate = startDate;
        }

        public String getEndDate() {
            return endDate;
        }

        public void setEndDate(String endDate) {
            this.endDate = endDate;
        }

        public String getResponsibility() {
            return responsibility;
        }

        public void setResponsibility(String responsibility) {
            this.responsibility = responsibility;
        }
    }

    public static class Reference {
        private String name;
        private String position;
        private String email;

        public Reference() {
        }

        public Reference(String name, String position, String email) {
            this.name = name;
            this.position = position;
            this.email = email;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPosition() {
            return position;
        }

        public void setPosition(String position) {
            this.position = position;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }
    }
}
