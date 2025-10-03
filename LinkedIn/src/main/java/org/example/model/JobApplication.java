package org.example.model;

import org.example.enums.JobApplicationStatus;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Represents a job application submitted by a user.
 */
public class JobApplication {
    private final String applicationId;
    private final String jobId;
    private final String applicantId;
    private JobApplicationStatus status;
    private String coverLetter;
    private String resumeUrl;
    private final LocalDateTime appliedAt;
    private LocalDateTime lastUpdatedAt;
    private String notes; // Notes from employer

    public JobApplication(String jobId, String applicantId) {
        this.applicationId = UUID.randomUUID().toString();
        this.jobId = jobId;
        this.applicantId = applicantId;
        this.status = JobApplicationStatus.PENDING;
        this.appliedAt = LocalDateTime.now();
        this.lastUpdatedAt = LocalDateTime.now();
    }

    public JobApplication(String jobId, String applicantId, String coverLetter, String resumeUrl) {
        this(jobId, applicantId);
        this.coverLetter = coverLetter;
        this.resumeUrl = resumeUrl;
    }

    // Getters
    public String getApplicationId() {
        return applicationId;
    }

    public String getJobId() {
        return jobId;
    }

    public String getApplicantId() {
        return applicantId;
    }

    public JobApplicationStatus getStatus() {
        return status;
    }

    public String getCoverLetter() {
        return coverLetter;
    }

    public String getResumeUrl() {
        return resumeUrl;
    }

    public LocalDateTime getAppliedAt() {
        return appliedAt;
    }

    public LocalDateTime getLastUpdatedAt() {
        return lastUpdatedAt;
    }

    public String getNotes() {
        return notes;
    }

    // Setters
    public void setStatus(JobApplicationStatus status) {
        this.status = status;
        this.lastUpdatedAt = LocalDateTime.now();
    }

    public void setCoverLetter(String coverLetter) {
        this.coverLetter = coverLetter;
    }

    public void setResumeUrl(String resumeUrl) {
        this.resumeUrl = resumeUrl;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    @Override
    public String toString() {
        return "JobApplication{" +
                "applicationId='" + applicationId + '\'' +
                ", jobId='" + jobId + '\'' +
                ", applicantId='" + applicantId + '\'' +
                ", status=" + status +
                ", appliedAt=" + appliedAt +
                '}';
    }
}

