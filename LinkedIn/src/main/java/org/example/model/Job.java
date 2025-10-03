package org.example.model;

import org.example.enums.ExperienceLevel;
import org.example.enums.JobType;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Represents a job posting in the system.
 */
public class Job {
    private final String jobId;
    private final String employerId; // User ID of the employer who posted the job
    private String title;
    private String description;
    private String company;
    private String location;
    private JobType jobType;
    private ExperienceLevel experienceLevel;
    private List<String> requiredSkills;
    private String salaryRange;
    private final LocalDateTime postedAt;
    private LocalDateTime expiresAt;
    private boolean isActive;
    private int applicationCount;

    public Job(String employerId, String title, String description, String company, String location) {
        this.jobId = UUID.randomUUID().toString();
        this.employerId = employerId;
        this.title = title;
        this.description = description;
        this.company = company;
        this.location = location;
        this.requiredSkills = new ArrayList<>();
        this.postedAt = LocalDateTime.now();
        this.isActive = true;
        this.applicationCount = 0;
    }

    // Builder pattern for flexible job creation
    public static class Builder {
        private final String employerId;
        private final String title;
        private final String description;
        private final String company;
        private String location;
        private JobType jobType;
        private ExperienceLevel experienceLevel;
        private List<String> requiredSkills = new ArrayList<>();
        private String salaryRange;

        public Builder(String employerId, String title, String description, String company) {
            this.employerId = employerId;
            this.title = title;
            this.description = description;
            this.company = company;
        }

        public Builder location(String location) {
            this.location = location;
            return this;
        }

        public Builder jobType(JobType jobType) {
            this.jobType = jobType;
            return this;
        }

        public Builder experienceLevel(ExperienceLevel experienceLevel) {
            this.experienceLevel = experienceLevel;
            return this;
        }

        public Builder requiredSkills(List<String> requiredSkills) {
            this.requiredSkills = requiredSkills;
            return this;
        }

        public Builder salaryRange(String salaryRange) {
            this.salaryRange = salaryRange;
            return this;
        }

        public Job build() {
            Job job = new Job(employerId, title, description, company, location);
            job.jobType = this.jobType;
            job.experienceLevel = this.experienceLevel;
            job.requiredSkills = this.requiredSkills;
            job.salaryRange = this.salaryRange;
            return job;
        }
    }

    // Getters
    public String getJobId() {
        return jobId;
    }

    public String getEmployerId() {
        return employerId;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getCompany() {
        return company;
    }

    public String getLocation() {
        return location;
    }

    public JobType getJobType() {
        return jobType;
    }

    public ExperienceLevel getExperienceLevel() {
        return experienceLevel;
    }

    public List<String> getRequiredSkills() {
        return new ArrayList<>(requiredSkills);
    }

    public String getSalaryRange() {
        return salaryRange;
    }

    public LocalDateTime getPostedAt() {
        return postedAt;
    }

    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }

    public boolean isActive() {
        return isActive;
    }

    public int getApplicationCount() {
        return applicationCount;
    }

    // Setters
    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setJobType(JobType jobType) {
        this.jobType = jobType;
    }

    public void setExperienceLevel(ExperienceLevel experienceLevel) {
        this.experienceLevel = experienceLevel;
    }

    public void setSalaryRange(String salaryRange) {
        this.salaryRange = salaryRange;
    }

    public void setExpiresAt(LocalDateTime expiresAt) {
        this.expiresAt = expiresAt;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public void addRequiredSkill(String skill) {
        if (!requiredSkills.contains(skill)) {
            requiredSkills.add(skill);
        }
    }

    public void incrementApplicationCount() {
        this.applicationCount++;
    }

    @Override
    public String toString() {
        return "Job{" +
                "jobId='" + jobId + '\'' +
                ", title='" + title + '\'' +
                ", company='" + company + '\'' +
                ", location='" + location + '\'' +
                ", jobType=" + jobType +
                ", isActive=" + isActive +
                ", applications=" + applicationCount +
                '}';
    }
}

