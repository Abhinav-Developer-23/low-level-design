package org.example.model;

import org.example.enums.JobApplicationStatus;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Represents a job application submitted by a user
 */
public class JobApplication {
    private final String applicationId;
    private final String jobId;
    private final String applicantId;
    private final String coverLetter;
    private final String resumeUrl;
    private JobApplicationStatus status;
    private final LocalDateTime appliedAt;
    private LocalDateTime lastUpdated;

    public JobApplication(String jobId, String applicantId, String coverLetter, String resumeUrl) {
        this.applicationId = UUID.randomUUID().toString();
        this.jobId = jobId;
        this.applicantId = applicantId;
        this.coverLetter = coverLetter;
        this.resumeUrl = resumeUrl;
        this.status = JobApplicationStatus.PENDING;
        this.appliedAt = LocalDateTime.now();
        this.lastUpdated = LocalDateTime.now();
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

    public String getCoverLetter() {
        return coverLetter;
    }

    public String getResumeUrl() {
        return resumeUrl;
    }

    public JobApplicationStatus getStatus() {
        return status;
    }

    public void setStatus(JobApplicationStatus status) {
        this.status = status;
        this.lastUpdated = LocalDateTime.now();
    }

    public LocalDateTime getAppliedAt() {
        return appliedAt;
    }

    public LocalDateTime getLastUpdated() {
        return lastUpdated;
    }

    public boolean isPending() {
        return status == JobApplicationStatus.PENDING;
    }

    public boolean isRejected() {
        return status == JobApplicationStatus.REJECTED;
    }

    public boolean isAccepted() {
        return status == JobApplicationStatus.ACCEPTED;
    }
}

