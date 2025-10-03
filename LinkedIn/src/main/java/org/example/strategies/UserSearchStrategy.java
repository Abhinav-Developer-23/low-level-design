package org.example.strategies;

import org.example.interfaces.SearchStrategy;
import org.example.model.Profile;
import org.example.model.User;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Search strategy for finding users by name, skills, location, etc.
 */
public class UserSearchStrategy implements SearchStrategy<User> {

    @Override
    public List<User> search(String query, Map<String, Object> criteria, List<User> items) {
        if (query == null || query.trim().isEmpty()) {
            return items;
        }

        String searchTerm = query.toLowerCase();
        
        return items.stream()
                .filter(user -> {
                    Profile profile = user.getProfile();
                    if (profile == null) return false;

                    // Search in name
                    if (profile.getFullName().toLowerCase().contains(searchTerm)) {
                        return true;
                    }

                    // Search in headline
                    if (profile.getHeadline() != null && 
                        profile.getHeadline().toLowerCase().contains(searchTerm)) {
                        return true;
                    }

                    // Search in location
                    if (profile.getLocation() != null && 
                        profile.getLocation().toLowerCase().contains(searchTerm)) {
                        return true;
                    }

                    // Search in skills
                    if (profile.getSkills().stream()
                            .anyMatch(skill -> skill.toLowerCase().contains(searchTerm))) {
                        return true;
                    }

                    // Search in experiences
                    if (profile.getExperiences().stream()
                            .anyMatch(exp -> exp.getTitle().toLowerCase().contains(searchTerm) ||
                                           exp.getCompany().toLowerCase().contains(searchTerm))) {
                        return true;
                    }

                    return false;
                })
                .filter(user -> applyFilters(user, criteria))
                .sorted((u1, u2) -> calculateRelevance(u2, searchTerm) - calculateRelevance(u1, searchTerm))
                .collect(Collectors.toList());
    }

    @Override
    public String getStrategyName() {
        return "User Search Strategy";
    }

    /**
     * Applies additional filters from criteria.
     */
    private boolean applyFilters(User user, Map<String, Object> criteria) {
        if (criteria == null || criteria.isEmpty()) {
            return true;
        }

        Profile profile = user.getProfile();
        if (profile == null) return false;

        // Filter by location
        if (criteria.containsKey("location")) {
            String location = (String) criteria.get("location");
            if (profile.getLocation() == null || 
                !profile.getLocation().toLowerCase().contains(location.toLowerCase())) {
                return false;
            }
        }

        // Filter by skill
        if (criteria.containsKey("skill")) {
            String skill = (String) criteria.get("skill");
            if (!profile.getSkills().stream()
                    .anyMatch(s -> s.equalsIgnoreCase(skill))) {
                return false;
            }
        }

        return true;
    }

    /**
     * Calculates relevance score for ranking results.
     */
    private int calculateRelevance(User user, String searchTerm) {
        int score = 0;
        Profile profile = user.getProfile();

        if (profile == null) return 0;

        // Higher score for name matches
        if (profile.getFullName().toLowerCase().contains(searchTerm)) {
            score += 100;
        }

        // Medium score for headline matches
        if (profile.getHeadline() != null && 
            profile.getHeadline().toLowerCase().contains(searchTerm)) {
            score += 50;
        }

        // Lower score for skill matches
        long skillMatches = profile.getSkills().stream()
                .filter(skill -> skill.toLowerCase().contains(searchTerm))
                .count();
        score += (int) (skillMatches * 25);

        // Bonus for number of connections (popularity factor)
        score += user.getConnectionIds().size();

        return score;
    }
}

