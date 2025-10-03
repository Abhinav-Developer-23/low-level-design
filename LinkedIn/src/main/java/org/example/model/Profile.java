package org.example.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a user's professional profile.
 */
public class Profile {
    private String firstName;
    private String lastName;
    private String headline;
    private String summary;
    private String profilePictureUrl;
    private String location;
    private List<Experience> experiences;
    private List<Education> educations;
    private List<String> skills;
    private List<String> certifications;

    public Profile(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.experiences = new ArrayList<>();
        this.educations = new ArrayList<>();
        this.skills = new ArrayList<>();
        this.certifications = new ArrayList<>();
    }

    // Nested class for Experience
    public static class Experience {
        private String title;
        private String company;
        private String location;
        private LocalDate startDate;
        private LocalDate endDate;
        private boolean isCurrent;
        private String description;

        public Experience(String title, String company, LocalDate startDate) {
            this.title = title;
            this.company = company;
            this.startDate = startDate;
            this.isCurrent = false;
        }

        // Getters and Setters
        public String getTitle() { return title; }
        public void setTitle(String title) { this.title = title; }
        
        public String getCompany() { return company; }
        public void setCompany(String company) { this.company = company; }
        
        public String getLocation() { return location; }
        public void setLocation(String location) { this.location = location; }
        
        public LocalDate getStartDate() { return startDate; }
        public void setStartDate(LocalDate startDate) { this.startDate = startDate; }
        
        public LocalDate getEndDate() { return endDate; }
        public void setEndDate(LocalDate endDate) { this.endDate = endDate; }
        
        public boolean isCurrent() { return isCurrent; }
        public void setCurrent(boolean current) { isCurrent = current; }
        
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }

        @Override
        public String toString() {
            return title + " at " + company + (isCurrent ? " (Current)" : "");
        }
    }

    // Nested class for Education
    public static class Education {
        private String school;
        private String degree;
        private String fieldOfStudy;
        private LocalDate startDate;
        private LocalDate endDate;
        private String description;

        public Education(String school, String degree, String fieldOfStudy) {
            this.school = school;
            this.degree = degree;
            this.fieldOfStudy = fieldOfStudy;
        }

        // Getters and Setters
        public String getSchool() { return school; }
        public void setSchool(String school) { this.school = school; }
        
        public String getDegree() { return degree; }
        public void setDegree(String degree) { this.degree = degree; }
        
        public String getFieldOfStudy() { return fieldOfStudy; }
        public void setFieldOfStudy(String fieldOfStudy) { this.fieldOfStudy = fieldOfStudy; }
        
        public LocalDate getStartDate() { return startDate; }
        public void setStartDate(LocalDate startDate) { this.startDate = startDate; }
        
        public LocalDate getEndDate() { return endDate; }
        public void setEndDate(LocalDate endDate) { this.endDate = endDate; }
        
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }

        @Override
        public String toString() {
            return degree + " in " + fieldOfStudy + " from " + school;
        }
    }

    // Getters and Setters
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getFullName() { return firstName + " " + lastName; }

    public String getHeadline() { return headline; }
    public void setHeadline(String headline) { this.headline = headline; }

    public String getSummary() { return summary; }
    public void setSummary(String summary) { this.summary = summary; }

    public String getProfilePictureUrl() { return profilePictureUrl; }
    public void setProfilePictureUrl(String profilePictureUrl) { this.profilePictureUrl = profilePictureUrl; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public List<Experience> getExperiences() { return new ArrayList<>(experiences); }
    public void addExperience(Experience experience) { this.experiences.add(experience); }
    public void removeExperience(Experience experience) { this.experiences.remove(experience); }

    public List<Education> getEducations() { return new ArrayList<>(educations); }
    public void addEducation(Education education) { this.educations.add(education); }
    public void removeEducation(Education education) { this.educations.remove(education); }

    public List<String> getSkills() { return new ArrayList<>(skills); }
    public void addSkill(String skill) { 
        if (!skills.contains(skill)) {
            this.skills.add(skill); 
        }
    }
    public void removeSkill(String skill) { this.skills.remove(skill); }

    public List<String> getCertifications() { return new ArrayList<>(certifications); }
    public void addCertification(String certification) { this.certifications.add(certification); }
    public void removeCertification(String certification) { this.certifications.remove(certification); }

    @Override
    public String toString() {
        return "Profile{" +
                "name='" + getFullName() + '\'' +
                ", headline='" + headline + '\'' +
                ", location='" + location + '\'' +
                ", experiences=" + experiences.size() +
                ", skills=" + skills.size() +
                '}';
    }
}

