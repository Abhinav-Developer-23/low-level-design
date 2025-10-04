package org.example.strategies;

import org.example.interfaces.SearchStrategy;
import org.example.model.User;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Strategy for searching users
 * Searches by name, headline, skills, and location
 */
public class UserSearchStrategy implements SearchStrategy<User> {

    @Override
    public List<User> search(String query, Map<String, Object> criteria, List<User> items) {
        if (query == null || query.trim().isEmpty()) {
            return items;
        }

        String lowerQuery = query.toLowerCase();

        return items.stream()
                .filter(user -> matchesQuery(user, lowerQuery))
                .filter(user -> matchesCriteria(user, criteria))
                .collect(Collectors.toList());
    }

    private boolean matchesQuery(User user, String query) {
        // Search in name
        if (user.getFullName().toLowerCase().contains(query)) {
            return true;
        }

        // Search in headline
        if (user.getProfile().getHeadline() != null && 
            user.getProfile().getHeadline().toLowerCase().contains(query)) {
            return true;
        }

        // Search in location
        if (user.getProfile().getLocation() != null && 
            user.getProfile().getLocation().toLowerCase().contains(query)) {
            return true;
        }

        // Search in skills
        return user.getProfile().getSkills().stream()
                .anyMatch(skill -> skill.toLowerCase().contains(query));
    }

    private boolean matchesCriteria(User user, Map<String, Object> criteria) {
        if (criteria == null || criteria.isEmpty()) {
            return true;
        }

        // Filter by location
        if (criteria.containsKey("location")) {
            String location = (String) criteria.get("location");
            if (user.getProfile().getLocation() == null || 
                !user.getProfile().getLocation().toLowerCase().contains(location.toLowerCase())) {
                return false;
            }
        }

        // Filter by skills
        if (criteria.containsKey("skills")) {
            @SuppressWarnings("unchecked")
            List<String> requiredSkills = (List<String>) criteria.get("skills");
            List<String> userSkills = user.getProfile().getSkills().stream()
                    .map(String::toLowerCase)
                    .collect(Collectors.toList());
            
            for (String skill : requiredSkills) {
                if (!userSkills.contains(skill.toLowerCase())) {
                    return false;
                }
            }
        }

        return true;
    }
}

