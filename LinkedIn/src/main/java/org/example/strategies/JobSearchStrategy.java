package org.example.strategies;

import org.example.enums.ExperienceLevel;
import org.example.enums.JobType;
import org.example.interfaces.SearchStrategy;
import org.example.model.Job;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Strategy for searching jobs
 * Searches by title, description, company, location, and skills
 */
public class JobSearchStrategy implements SearchStrategy<Job> {

    @Override
    public List<Job> search(String query, Map<String, Object> criteria, List<Job> items) {
        if (query == null || query.trim().isEmpty()) {
            return items;
        }

        String lowerQuery = query.toLowerCase();

        return items.stream()
                .filter(job -> matchesQuery(job, lowerQuery))
                .filter(job -> matchesCriteria(job, criteria))
                .collect(Collectors.toList());
    }

    private boolean matchesQuery(Job job, String query) {
        // Search in title
        if (job.getTitle().toLowerCase().contains(query)) {
            return true;
        }

        // Search in description
        if (job.getDescription().toLowerCase().contains(query)) {
            return true;
        }

        // Search in company
        if (job.getCompany().toLowerCase().contains(query)) {
            return true;
        }

        // Search in location
        if (job.getLocation() != null && job.getLocation().toLowerCase().contains(query)) {
            return true;
        }

        // Search in required skills
        return job.getRequiredSkills().stream()
                .anyMatch(skill -> skill.toLowerCase().contains(query));
    }

    private boolean matchesCriteria(Job job, Map<String, Object> criteria) {
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
            ExperienceLevel experienceLevel = (ExperienceLevel) criteria.get("experienceLevel");
            if (job.getExperienceLevel() != experienceLevel) {
                return false;
            }
        }

        // Filter by required skills
        if (criteria.containsKey("skills")) {
            @SuppressWarnings("unchecked")
            List<String> requiredSkills = (List<String>) criteria.get("skills");
            List<String> jobSkills = job.getRequiredSkills().stream()
                    .map(String::toLowerCase)
                    .collect(Collectors.toList());
            
            // Job must have at least one of the required skills
            boolean hasMatchingSkill = requiredSkills.stream()
                    .anyMatch(skill -> jobSkills.contains(skill.toLowerCase()));
            
            if (!hasMatchingSkill) {
                return false;
            }
        }

        return true;
    }
}

