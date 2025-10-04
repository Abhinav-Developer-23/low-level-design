package org.example.services;

import org.example.enums.JobApplicationStatus;
import org.example.interfaces.JobService;
import org.example.model.Job;
import org.example.model.JobApplication;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Implementation of job service.
 * Manages job postings and applications.
 */
public class JobServiceImpl implements JobService {
    private final Map<String, Job> jobs;
    private final Map<String, JobApplication> applications;
    private final AuthenticationServiceImpl authService;

    public JobServiceImpl(AuthenticationServiceImpl authService) {
        this.jobs = new HashMap<>();
        this.applications = new HashMap<>();
        this.authService = authService;
    }

    @Override
    public Job postJob(Job job) {
        if (job == null) {
            throw new IllegalArgumentException("Job cannot be null");
        }

        // Validate employer exists
        if (authService.getUserById(job.getEmployerId()) == null) {
            throw new IllegalArgumentException("Invalid employer ID");
        }

        jobs.put(job.getJobId(), job);
        return job;
    }

    @Override
    public boolean updateJob(String jobId, Job updatedJob) {
        Job existingJob = jobs.get(jobId);

        if (existingJob == null) {
            return false;
        }

        // Update job details
        existingJob.setTitle(updatedJob.getTitle());
        existingJob.setDescription(updatedJob.getDescription());
        existingJob.setLocation(updatedJob.getLocation());
        existingJob.setJobType(updatedJob.getJobType());
        existingJob.setExperienceLevel(updatedJob.getExperienceLevel());
        existingJob.setSalaryRange(updatedJob.getSalaryRange());

        return true;
    }

    @Override
    public boolean deleteJob(String jobId) {
        Job job = jobs.get(jobId);

        if (job == null) {
            return false;
        }

        job.setActive(false);
        return true;
    }

    @Override
    public Job getJob(String jobId) {
        return jobs.get(jobId);
    }

    @Override
    public List<Job> getJobsByEmployer(String employerId) {
        return jobs.values().stream()
                .filter(job -> job.getEmployerId().equals(employerId))
                .collect(Collectors.toList());
    }

    @Override
    public JobApplication applyForJob(JobApplication application) {
        if (application == null) {
            throw new IllegalArgumentException("Application cannot be null");
        }

        // Validate job exists and is active
        Job job = jobs.get(application.getJobId());
        if (job == null || !job.isActive()) {
            throw new IllegalArgumentException("Job not found or inactive");
        }

        // Validate applicant exists
        if (authService.getUserById(application.getApplicantId()) == null) {
            throw new IllegalArgumentException("Invalid applicant ID");
        }

        // Check if user already applied
        boolean alreadyApplied = applications.values().stream()
                .anyMatch(app -> app.getJobId().equals(application.getJobId()) &&
                               app.getApplicantId().equals(application.getApplicantId()));

        if (alreadyApplied) {
            throw new IllegalArgumentException("Already applied to this job");
        }

        applications.put(application.getApplicationId(), application);
        job.incrementApplicationCount();

        return application;
    }

    @Override
    public List<JobApplication> getApplicationsForJob(String jobId) {
        return applications.values().stream()
                .filter(app -> app.getJobId().equals(jobId))
                .collect(Collectors.toList());
    }

    @Override
    public List<JobApplication> getApplicationsByUser(String userId) {
        return applications.values().stream()
                .filter(app -> app.getApplicantId().equals(userId))
                .collect(Collectors.toList());
    }

    @Override
    public boolean updateApplicationStatus(String applicationId, JobApplicationStatus status) {
        JobApplication application = applications.get(applicationId);

        if (application == null) {
            return false;
        }

        application.setStatus(status);
        return true;
    }

    @Override
    public List<Job> getAllActiveJobs() {
        return jobs.values().stream()
                .filter(Job::isActive)
                .collect(Collectors.toList());
    }

    /**
     * Gets an application by its ID.
     */
    public JobApplication getApplication(String applicationId) {
        return applications.get(applicationId);
    }
}

