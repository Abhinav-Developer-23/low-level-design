package org.example;

import org.example.enums.ExperienceLevel;
import org.example.enums.JobType;
import org.example.model.*;
import org.example.observers.ConsoleNotificationObserver;
import org.example.system.LinkedInSystem;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Demonstration of the LinkedIn Professional Networking Platform.
 * Shows all major features including:
 * - User registration and authentication
 * - Profile management
 * - Connection requests
 * - Messaging
 * - Job posting and applications
 * - Search functionality
 * - Real-time notifications
 */
public class Main {
    public static void main(String[] args) {
        System.out.println("================================================================");
        System.out.println("   LinkedIn Professional Networking Platform - Demo          ");
        System.out.println("================================================================\n");

        LinkedInSystem linkedin = LinkedInSystem.getInstance();

        try {
            // ==================== User Registration & Authentication ====================
            System.out.println("\n>> 1. USER REGISTRATION & AUTHENTICATION");
            System.out.println("----------------------------------------------------------");

            User alice = linkedin.register("alice@example.com", "password123", "Alice", "Johnson");
            User bob = linkedin.register("bob@example.com", "password456", "Bob", "Smith");
            User carol = linkedin.register("carol@example.com", "password789", "Carol", "Williams");
            User recruiter = linkedin.register("recruiter@techcorp.com", "password000", "David", "Recruiter");

            System.out.println("[OK] Registered 4 users successfully");
            System.out.println("  - Alice Johnson (Software Engineer)");
            System.out.println("  - Bob Smith (Product Manager)");
            System.out.println("  - Carol Williams (Data Scientist)");
            System.out.println("  - David Recruiter (Tech Recruiter)");

            // ==================== Profile Setup ====================
            System.out.println("\n>> 2. PROFILE MANAGEMENT");
            System.out.println("----------------------------------------------------------");

            // Setup Alice's profile
            Profile aliceProfile = alice.getProfile();
            aliceProfile.setHeadline("Senior Software Engineer @ TechCorp");
            aliceProfile.setSummary("Passionate software engineer with 5+ years of experience.");
            aliceProfile.setLocation("San Francisco, CA");
            aliceProfile.addSkill("Java");
            aliceProfile.addSkill("Python");
            aliceProfile.addSkill("System Design");

            Profile.Experience exp1 = new Profile.Experience(
                    "Senior Software Engineer",
                    "TechCorp",
                    LocalDate.of(2020, 1, 1)
            );
            exp1.setCurrent(true);
            aliceProfile.addExperience(exp1);

            // Setup other profiles
            Profile bobProfile = bob.getProfile();
            bobProfile.setHeadline("Product Manager @ InnovateLabs");
            bobProfile.setLocation("Seattle, WA");

            Profile carolProfile = carol.getProfile();
            carolProfile.setHeadline("Data Scientist | ML Engineer");
            carolProfile.setLocation("Boston, MA");
            carolProfile.addSkill("Machine Learning");
            carolProfile.addSkill("Python");

            System.out.println("[OK] All profiles setup complete");
            System.out.println("  Alice's Profile: " + aliceProfile);

            // ==================== Notification Setup ====================
            System.out.println("\n>> 3. NOTIFICATION SYSTEM (Observer Pattern)");
            System.out.println("----------------------------------------------------------");

            // Register notification observers
            linkedin.registerNotificationObserver(new ConsoleNotificationObserver(alice.getUserId()));
            linkedin.registerNotificationObserver(new ConsoleNotificationObserver(bob.getUserId()));
            linkedin.registerNotificationObserver(new ConsoleNotificationObserver(carol.getUserId()));

            System.out.println("[OK] Notification observers registered");

            // ==================== Connection Management ====================
            System.out.println("\n>> 4. CONNECTION MANAGEMENT");
            System.out.println("----------------------------------------------------------");

            Connection conn1 = linkedin.sendConnectionRequest(
                    alice.getUserId(),
                    bob.getUserId(),
                    "Hi Bob! I'd love to connect."
            );
            System.out.println("\n[OK] Alice sent connection request to Bob");

            Thread.sleep(300);
            linkedin.acceptConnectionRequest(conn1.getConnectionId());
            System.out.println("\n[OK] Bob accepted connection from Alice");

            Thread.sleep(300);
            Connection conn2 = linkedin.sendConnectionRequest(
                    alice.getUserId(),
                    carol.getUserId(),
                    "Hi Carol! Let's connect."
            );
            linkedin.acceptConnectionRequest(conn2.getConnectionId());
            System.out.println("\n[OK] Alice and Carol are now connected");

            Thread.sleep(300);
            System.out.println("\n[Stats] Connection Summary:");
            System.out.println("  Alice's connections: " + linkedin.getConnections(alice.getUserId()).size());
            System.out.println("  Bob's connections: " + linkedin.getConnections(bob.getUserId()).size());

            // ==================== Messaging ====================
            System.out.println("\n>> 5. MESSAGING SYSTEM");
            System.out.println("----------------------------------------------------------");

            Message msg1 = linkedin.sendMessage(
                    alice.getUserId(),
                    bob.getUserId(),
                    "Hey Bob! Thanks for connecting."
            );
            System.out.println("\n[OK] Alice sent message to Bob");

            Thread.sleep(300);
            Message msg2 = linkedin.sendMessage(
                    bob.getUserId(),
                    alice.getUserId(),
                    "Hi Alice! Happy to connect."
            );
            System.out.println("\n[OK] Bob replied to Alice");

            Thread.sleep(300);
            List<Message> conversation = linkedin.getConversation(alice.getUserId(), bob.getUserId());
            System.out.println("\n[Chat] Conversation (" + conversation.size() + " messages)");

            // ==================== Job Posting & Applications ====================
            System.out.println("\n>> 6. JOB POSTING & APPLICATIONS");
            System.out.println("----------------------------------------------------------");

            Job job1 = new Job.Builder(
                    recruiter.getUserId(),
                    "Senior Software Engineer",
                    "We are looking for an experienced engineer...",
                    "TechCorp"
            )
                    .location("San Francisco, CA")
                    .jobType(JobType.FULL_TIME)
                    .experienceLevel(ExperienceLevel.MID_SENIOR)
                    .salaryRange("$120k - $180k")
                    .build();

            job1.addRequiredSkill("Java");
            job1.addRequiredSkill("System Design");
            linkedin.postJob(job1);
            System.out.println("[OK] Job posted: " + job1.getTitle());

            Thread.sleep(300);
            JobApplication app1 = new JobApplication(
                    job1.getJobId(),
                    alice.getUserId(),
                    "I am very interested in this position...",
                    "resume.pdf"
            );
            linkedin.applyForJob(app1);
            System.out.println("\n[OK] Alice applied for: " + job1.getTitle());

            Thread.sleep(300);
            System.out.println("\n[Stats] Job Application Summary:");
            System.out.println("  Total Active Jobs: " + linkedin.getAllActiveJobs().size());
            System.out.println("  Applications: " + linkedin.getApplicationsForJob(job1.getJobId()).size());

            // ==================== Search Functionality ====================
            System.out.println("\n>> 7. SEARCH FUNCTIONALITY (Strategy Pattern)");
            System.out.println("----------------------------------------------------------");

            List<Job> searchResults = linkedin.searchJobs("Software Engineer", new HashMap<>());
            System.out.println("\n[Search] Found " + searchResults.size() + " jobs for 'Software Engineer'");

            Map<String, Object> criteria = new HashMap<>();
            criteria.put("location", "San Francisco");
            List<Job> filteredResults = linkedin.searchJobs("", criteria);
            System.out.println("[Search] Found " + filteredResults.size() + " jobs in San Francisco");

            // ==================== Notification Summary ====================
            Thread.sleep(300);
            System.out.println("\n>> 8. NOTIFICATION SUMMARY");
            System.out.println("----------------------------------------------------------");

            System.out.println("\n[Stats] Notification Statistics:");
            System.out.println("  Alice's notifications: " + linkedin.getNotifications(alice.getUserId()).size());
            System.out.println("  Bob's notifications: " + linkedin.getNotifications(bob.getUserId()).size());
            System.out.println("  Recruiter's notifications: " + linkedin.getNotifications(recruiter.getUserId()).size());

            // ==================== System Overview ====================
            System.out.println("\n>> 9. SYSTEM OVERVIEW");
            System.out.println("----------------------------------------------------------");

            System.out.println("\n[Chart] Platform Statistics:");
            System.out.println("  Total Users: 4");
            System.out.println("  Total Connections: 2");
            System.out.println("  Messages Exchanged: " + linkedin.getInbox(alice.getUserId()).size());
            System.out.println("  Active Job Postings: " + linkedin.getAllActiveJobs().size());

            // ==================== Design Patterns ====================
            System.out.println("\n>> 10. DESIGN PATTERNS IMPLEMENTED");
            System.out.println("----------------------------------------------------------");
            System.out.println("\n[OK] Singleton Pattern: LinkedInSystem");
            System.out.println("[OK] Observer Pattern: Real-time notifications");
            System.out.println("[OK] Strategy Pattern: Pluggable search algorithms");
            System.out.println("[OK] Facade Pattern: Simplified system interface");
            System.out.println("[OK] Builder Pattern: Flexible object creation");

            // ==================== Demo Complete ====================
            System.out.println("\n================================================================");
            System.out.println("              Demo Completed Successfully!                   ");
            System.out.println("================================================================\n");

        } catch (Exception e) {
            System.err.println("Error during demo: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
