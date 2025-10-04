package org.example.strategies;

import org.example.enums.ExperienceLevel;
import org.example.enums.JobType;
import org.example.interfaces.SearchStrategy;
import org.example.model.Job;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Search strategy for finding jobs by title, company, location, skills, etc.
 */
public class JobSearchStrategy implements SearchStrategy<Job> {

    @Override
    public List<Job> search(String query, Map<String, Object> criteria, List<Job> items) {
        if (query == null || query.trim().isEmpty()) {
            return items.stream()
                    .filter(Job::isActive)
                    .collect(Collectors.toList());
        }

        String searchTerm = query.toLowerCase();
        
        return items.stream()
                .filter(Job::isActive)
                .filter(job -> {
                    // Search in title
                    if (job.getTitle().toLowerCase().contains(searchTerm)) {
                        return true;
                    }

                    // Search in company
                    if (job.getCompany().toLowerCase().contains(searchTerm)) {
                        return true;
                    }

                    // Search in location
                    if (job.getLocation() != null && 
                        job.getLocation().toLowerCase().contains(searchTerm)) {
                        return true;
                    }

                    // Search in description
                    if (job.getDescription().toLowerCase().contains(searchTerm)) {
                        return true;
                    }

                    // Search in required skills
                    if (job.getRequiredSkills().stream()
                            .anyMatch(skill -> skill.toLowerCase().contains(searchTerm))) {
                        return true;
                    }

                    return false;
                })
                .filter(job -> applyFilters(job, criteria))
                .sorted((j1, j2) -> calculateRelevance(j2, searchTerm) - calculateRelevance(j1, searchTerm))
                .collect(Collectors.toList());
    }

    @Override
    public String getStrategyName() {
        return "Job Search Strategy";
    }

    /**
     * Applies additional filters from criteria.
     */
    private boolean applyFilters(Job job, Map<String, Object> criteria) {
        if (criteria == null || criteria.isEmpty()) {
            return true;
        }

        // Filter by location
        if (criteria.containsKey("location")) {
            String location = (String) criteria.get("location");
            if (job.getLocation() == null || 
                !job.getLocation().toLowerCase().contains(location.toLowerCase())) {
                return false;
            }
        }

        // Filter by job type
        if (criteria.containsKey("jobType")) {
            JobType jobType = (JobType) criteria.get("jobType");
            if (job.getJobType() != jobType) {
                return false;
            }
        }

        // Filter by experience level
        if (criteria.containsKey("experienceLevel")) {
            ExperienceLevel level = (ExperienceLevel) criteria.get("experienceLevel");
            if (job.getExperienceLevel() != level) {
                return false;
            }
        }

        // Filter by required skill
        if (criteria.containsKey("skill")) {
            String skill = (String) criteria.get("skill");
            if (!job.getRequiredSkills().stream()
                    .anyMatch(s -> s.equalsIgnoreCase(skill))) {
                return false;
            }
        }

        return true;
    }

    /**
     * Calculates relevance score for ranking results.
     */
    private int calculateRelevance(Job job, String searchTerm) {
        int score = 0;

        // Higher score for title matches
        if (job.getTitle().toLowerCase().contains(searchTerm)) {
            score += 100;
        }

        // High score for company matches
        if (job.getCompany().toLowerCase().contains(searchTerm)) {
            score += 80;
        }

        // Medium score for skill matches
        long skillMatches = job.getRequiredSkills().stream()
                .filter(skill -> skill.toLowerCase().contains(searchTerm))
                .count();
        score += (int) (skillMatches * 40);

        // Lower score for location matches
        if (job.getLocation() != null && 
            job.getLocation().toLowerCase().contains(searchTerm)) {
            score += 30;
        }

        // Bonus for recent postings (jobs posted more recently rank higher)
        long daysSincePosted = java.time.temporal.ChronoUnit.DAYS.between(
                job.getPostedAt().toLocalDate(), 
                java.time.LocalDate.now()
        );
        score += Math.max(0, 20 - (int) daysSincePosted);

        return score;
    }
}

