package org.example.model;

import org.example.enums.ExperienceLevel;
import org.example.enums.JobType;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Represents a job posting
 * Uses Builder pattern for flexible construction
 */
public class Job {
    private final String jobId;
    private final String employerId;
    private final String title;
    private final String description;
    private final String company;
    private String location;
    private JobType jobType;
    private ExperienceLevel experienceLevel;
    private List<String> requiredSkills;
    private String salaryRange;
    private final LocalDateTime postedAt;
    private int applicationCount;

    private Job(Builder builder) {
        this.jobId = UUID.randomUUID().toString();
        this.employerId = builder.employerId;
        this.title = builder.title;
        this.description = builder.description;
        this.company = builder.company;
        this.location = builder.location;
        this.jobType = builder.jobType;
        this.experienceLevel = builder.experienceLevel;
        this.requiredSkills = builder.requiredSkills != null ? builder.requiredSkills : new ArrayList<>();
        this.salaryRange = builder.salaryRange;
        this.postedAt = LocalDateTime.now();
        this.applicationCount = 0;
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

    public int getApplicationCount() {
        return applicationCount;
    }

    public void incrementApplicationCount() {
        this.applicationCount++;
    }

    /**
     * Builder class for Job
     */
    public static class Builder {
        private final String employerId;
        private final String title;
        private final String description;
        private final String company;
        private String location;
        private JobType jobType;
        private ExperienceLevel experienceLevel;
        private List<String> requiredSkills;
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
            if (employerId == null || employerId.trim().isEmpty()) {
                throw new IllegalArgumentException("Employer ID cannot be null or empty");
            }
            if (title == null || title.trim().isEmpty()) {
                throw new IllegalArgumentException("Job title cannot be null or empty");
            }
            if (description == null || description.trim().isEmpty()) {
                throw new IllegalArgumentException("Job description cannot be null or empty");
            }
            return new Job(this);
        }
    }
}

