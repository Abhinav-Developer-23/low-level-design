package org.example.enums;

/**
 * Status of a job application
 */
public enum JobApplicationStatus {
    PENDING,        // Application submitted, pending review
    REVIEWING,      // Application under review
    INTERVIEW,      // Candidate selected for interview
    OFFERED,        // Job offer extended
    ACCEPTED,       // Offer accepted by candidate
    REJECTED,       // Application rejected
    WITHDRAWN       // Application withdrawn by candidate
}

