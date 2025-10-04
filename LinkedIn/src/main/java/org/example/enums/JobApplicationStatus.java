package org.example.enums;

/**
 * Represents the status of a job application.
 */
public enum JobApplicationStatus {
    PENDING,        // Application submitted, awaiting review
    REVIEWING,      // Application under review
    SHORTLISTED,    // Candidate shortlisted for interview
    INTERVIEWED,    // Interview completed
    ACCEPTED,       // Job offer accepted
    REJECTED,       // Application rejected
    WITHDRAWN       // Application withdrawn by candidate
}

