package org.example.interfaces;

import org.example.model.Job;
import org.example.model.JobApplication;

import java.util.List;

/**
 * Interface for job posting and application operations.
 */
public interface JobService {
    /**
     * Posts a new job listing.
     *
     * @param job The job to post
     * @return The posted job with generated ID
     */
    Job postJob(Job job);

    /**
     * Updates an existing job posting.
     *
     * @param jobId Job ID
     * @param updatedJob Updated job details
     * @return true if updated successfully
     */
    boolean updateJob(String jobId, Job updatedJob);

    /**
     * Deletes a job posting.
     *
     * @param jobId Job ID
     * @return true if deleted successfully
     */
    boolean deleteJob(String jobId);

    /**
     * Gets a job by its ID.
     *
     * @param jobId Job ID
     * @return The job or null if not found
     */
    Job getJob(String jobId);

    /**
     * Gets all active jobs posted by an employer.
     *
     * @param employerId Employer's user ID
     * @return List of jobs
     */
    List<Job> getJobsByEmployer(String employerId);

    /**
     * Submits a job application.
     *
     * @param application The job application
     * @return The submitted application with generated ID
     * @throws IllegalArgumentException if user already applied
     */
    JobApplication applyForJob(JobApplication application);

    /**
     * Gets all applications for a specific job.
     *
     * @param jobId Job ID
     * @return List of applications
     */
    List<JobApplication> getApplicationsForJob(String jobId);

    /**
     * Gets all applications submitted by a user.
     *
     * @param userId User ID
     * @return List of applications
     */
    List<JobApplication> getApplicationsByUser(String userId);

    /**
     * Updates the status of a job application.
     *
     * @param applicationId Application ID
     * @param status New status
     * @return true if updated successfully
     */
    boolean updateApplicationStatus(String applicationId, org.example.enums.JobApplicationStatus status);

    /**
     * Gets all active job postings.
     *
     * @return List of active jobs
     */
    List<Job> getAllActiveJobs();
}

