package org.example.services;

import org.example.enums.JobApplicationStatus;
import org.example.interfaces.AuthenticationService;
import org.example.interfaces.JobService;
import org.example.model.Job;
import org.example.model.JobApplication;
import org.example.model.User;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Implementation of JobService
 * Manages job postings and applications
 * Thread-safe using ConcurrentHashMap
 */
public class JobServiceImpl implements JobService {
    private final Map<String, Job> jobs;
    private final Map<String, JobApplication> applications;
    private final AuthenticationService authService;

    public JobServiceImpl(AuthenticationService authService) {
        this.jobs = new ConcurrentHashMap<>();
        this.applications = new ConcurrentHashMap<>();
        this.authService = authService;
    }

    @Override
    public Job postJob(Job job) {
        if (job == null) {
            throw new IllegalArgumentException("Job cannot be null");
        }

        User employer = authService.getUserById(job.getEmployerId());
        if (employer == null) {
            throw new IllegalArgumentException("Employer not found");
        }

        jobs.put(job.getJobId(), job);
        employer.addPostedJob(job.getJobId());

        return job;
    }

    @Override
    public JobApplication applyForJob(JobApplication application) {
        if (application == null) {
            throw new IllegalArgumentException("Application cannot be null");
        }

        Job job = jobs.get(application.getJobId());
        if (job == null) {
            throw new IllegalArgumentException("Job not found");
        }

        User applicant = authService.getUserById(application.getApplicantId());
        if (applicant == null) {
            throw new IllegalArgumentException("Applicant not found");
        }

        if (hasApplied(application.getApplicantId(), application.getJobId())) {
            throw new IllegalArgumentException("User has already applied to this job");
        }

        applications.put(application.getApplicationId(), application);
        job.incrementApplicationCount();

        return application;
    }

    @Override
    public Job getJobById(String jobId) {
        return jobs.get(jobId);
    }

    @Override
    public List<Job> getJobsByEmployer(String employerId) {
        if (employerId == null) {
            throw new IllegalArgumentException("Employer ID cannot be null");
        }

        return jobs.values().stream()
                .filter(job -> job.getEmployerId().equals(employerId))
                .collect(Collectors.toList());
    }

    @Override
    public List<JobApplication> getApplicationsForJob(String jobId) {
        if (jobId == null) {
            throw new IllegalArgumentException("Job ID cannot be null");
        }

        return applications.values().stream()
                .filter(app -> app.getJobId().equals(jobId))
                .collect(Collectors.toList());
    }

    @Override
    public List<JobApplication> getApplicationsByUser(String userId) {
        if (userId == null) {
            throw new IllegalArgumentException("User ID cannot be null");
        }

        return applications.values().stream()
                .filter(app -> app.getApplicantId().equals(userId))
                .collect(Collectors.toList());
    }

    @Override
    public boolean updateApplicationStatus(String applicationId, JobApplicationStatus status) {
        JobApplication application = applications.get(applicationId);
        if (application == null) {
            throw new IllegalArgumentException("Application not found");
        }

        application.setStatus(status);
        return true;
    }

    @Override
    public boolean hasApplied(String userId, String jobId) {
        return applications.values().stream()
                .anyMatch(app -> app.getApplicantId().equals(userId) && 
                                 app.getJobId().equals(jobId));
    }

    @Override
    public JobApplication getApplicationById(String applicationId) {
        return applications.get(applicationId);
    }

    @Override
    public List<Job> getAllJobs() {
        return jobs.values().stream().collect(Collectors.toList());
    }
}

