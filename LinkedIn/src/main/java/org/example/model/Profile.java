package org.example.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a user's professional profile
 */
public class Profile {
    private String headline;
    private String summary;
    private String profilePicture;
    private String location;
    private List<String> experience;
    private List<String> education;
    private List<String> skills;
    private List<String> certifications;

    public Profile() {
        this.experience = new ArrayList<>();
        this.education = new ArrayList<>();
        this.skills = new ArrayList<>();
        this.certifications = new ArrayList<>();
    }

    // Getters and Setters
    public String getHeadline() {
        return headline;
    }

    public void setHeadline(String headline) {
        this.headline = headline;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public List<String> getExperience() {
        return new ArrayList<>(experience);
    }

    public void addExperience(String exp) {
        this.experience.add(exp);
    }

    public List<String> getEducation() {
        return new ArrayList<>(education);
    }

    public void addEducation(String edu) {
        this.education.add(edu);
    }

    public List<String> getSkills() {
        return new ArrayList<>(skills);
    }

    public void addSkill(String skill) {
        if (!this.skills.contains(skill)) {
            this.skills.add(skill);
        }
    }

    public List<String> getCertifications() {
        return new ArrayList<>(certifications);
    }

    public void addCertification(String cert) {
        this.certifications.add(cert);
    }
}

