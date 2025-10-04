package org.example.interfaces;

import org.example.enums.JobApplicationStatus;
import org.example.model.Job;
import org.example.model.JobApplication;

import java.util.List;

/**
 * Interface for job posting and application management
 */
public interface JobService {
    /**
     * Post a new job
     */
    Job postJob(Job job);

    /**
     * Apply for a job
     */
    JobApplication applyForJob(JobApplication application);

    /**
     * Get job by ID
     */
    Job getJobById(String jobId);

    /**
     * Get all jobs posted by a user
     */
    List<Job> getJobsByEmployer(String employerId);

    /**
     * Get all applications for a job
     */
    List<JobApplication> getApplicationsForJob(String jobId);

    /**
     * Get all applications by a user
     */
    List<JobApplication> getApplicationsByUser(String userId);

    /**
     * Update application status
     */
    boolean updateApplicationStatus(String applicationId, JobApplicationStatus status);

    /**
     * Check if user has already applied to a job
     */
    boolean hasApplied(String userId, String jobId);

    /**
     * Get application by ID
     */
    JobApplication getApplicationById(String applicationId);

    /**
     * Get all available jobs
     */
    List<Job> getAllJobs();
}

