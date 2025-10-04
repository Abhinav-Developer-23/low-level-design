package org.example;

import org.example.enums.ExperienceLevel;
import org.example.enums.JobApplicationStatus;
import org.example.enums.JobType;
import org.example.model.*;
import org.example.observers.ConsoleNotificationObserver;
import org.example.observers.EmailNotificationObserver;
import org.example.system.LinkedInSystem;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Demo class showcasing the LinkedIn system
 * 
 * Design Patterns Used:
 * 1. Singleton Pattern - LinkedInSystem
 * 2. Facade Pattern - LinkedInSystem simplifies subsystem interactions
 * 3. Strategy Pattern - UserSearchStrategy, JobSearchStrategy
 * 4. Observer Pattern - NotificationObserver implementations
 * 5. Builder Pattern - User and Job creation
 * 
 * SOLID Principles:
 * 1. Single Responsibility - Each class/service has one responsibility
 * 2. Open/Closed - Extensible through strategies and observers
 * 3. Liskov Substitution - All service implementations are interchangeable
 * 4. Interface Segregation - Small, focused interfaces
 * 5. Dependency Inversion - Services depend on abstractions (interfaces)
 */
public class Main {
    public static void main(String[] args) {
        System.out.println("=== LinkedIn Professional Networking Platform Demo ===\n");
        
        // Get singleton instance
        LinkedInSystem linkedin = LinkedInSystem.getInstance();
        
        // ==================== User Registration ====================
        System.out.println(">>> 1. User Registration");
        System.out.println("-".repeat(50));
        
        User alice = linkedin.register("alice@techcorp.com", "password123", "Alice", "Johnson");
        System.out.println("✓ Registered: " + alice.getFullName());
        
        User bob = linkedin.register("bob@startupco.com", "password456", "Bob", "Smith");
        System.out.println("✓ Registered: " + bob.getFullName());
        
        User carol = linkedin.register("carol@designstudio.com", "password789", "Carol", "Davis");
        System.out.println("✓ Registered: " + carol.getFullName());
        
        User david = linkedin.register("david@consulting.com", "password321", "David", "Wilson");
        System.out.println("✓ Registered: " + david.getFullName());
        System.out.println();
        
        // ==================== Setup Profiles ====================
        System.out.println(">>> 2. Profile Setup");
        System.out.println("-".repeat(50));
        
        Profile aliceProfile = alice.getProfile();
        aliceProfile.setHeadline("Senior Software Engineer @ TechCorp");
        aliceProfile.setSummary("Passionate about building scalable systems");
        aliceProfile.setLocation("San Francisco, CA");
        aliceProfile.addSkill("Java");
        aliceProfile.addSkill("System Design");
        aliceProfile.addSkill("Spring Boot");
        aliceProfile.addExperience("Senior Engineer at TechCorp (2020-Present)");
        aliceProfile.addExperience("Software Engineer at StartupX (2018-2020)");
        aliceProfile.addEducation("B.S. Computer Science - Stanford University");
        System.out.println("✓ " + alice.getFullName() + " - " + aliceProfile.getHeadline());
        
        Profile bobProfile = bob.getProfile();
        bobProfile.setHeadline("Product Manager @ StartupCo");
        bobProfile.setSummary("Building products that users love");
        bobProfile.setLocation("New York, NY");
        bobProfile.addSkill("Product Management");
        bobProfile.addSkill("Agile");
        bobProfile.addSkill("User Research");
        System.out.println("✓ " + bob.getFullName() + " - " + bobProfile.getHeadline());
        
        Profile carolProfile = carol.getProfile();
        carolProfile.setHeadline("UX Designer @ DesignStudio");
        carolProfile.setSummary("Creating beautiful and intuitive user experiences");
        carolProfile.setLocation("San Francisco, CA");
        carolProfile.addSkill("UI/UX Design");
        carolProfile.addSkill("Figma");
        carolProfile.addSkill("User Research");
        System.out.println("✓ " + carol.getFullName() + " - " + carolProfile.getHeadline());
        
        Profile davidProfile = david.getProfile();
        davidProfile.setHeadline("Engineering Manager @ Consulting Inc");
        davidProfile.setSummary("Leading high-performing engineering teams");
        davidProfile.setLocation("Seattle, WA");
        davidProfile.addSkill("Java");
        davidProfile.addSkill("Leadership");
        davidProfile.addSkill("System Design");
        System.out.println("✓ " + david.getFullName() + " - " + davidProfile.getHeadline());
        System.out.println();
        
        // ==================== Register Observers ====================
        System.out.println(">>> 3. Register Notification Observers");
        System.out.println("-".repeat(50));
        
        linkedin.registerNotificationObserver(new ConsoleNotificationObserver(alice.getUserId()));
        linkedin.registerNotificationObserver(new EmailNotificationObserver(bob.getUserId(), bob.getEmail()));
        System.out.println("✓ Registered console notifications for Alice");
        System.out.println("✓ Registered email notifications for Bob");
        System.out.println();
        
        // ==================== Connection Requests ====================
        System.out.println(">>> 4. Connection Management");
        System.out.println("-".repeat(50));
        
        // Alice sends connection requests
        Connection conn1 = linkedin.sendConnectionRequest(
            alice.getUserId(), 
            bob.getUserId(), 
            "Hi Bob! I'd love to connect and discuss product development."
        );
        System.out.println("✓ Alice → Bob: Connection request sent");
        
        Connection conn2 = linkedin.sendConnectionRequest(
            alice.getUserId(), 
            carol.getUserId(), 
            "Hi Carol! Love your design work. Let's connect!"
        );
        System.out.println("✓ Alice → Carol: Connection request sent");
        
        // Bob sends to Carol
        Connection conn3 = linkedin.sendConnectionRequest(
            bob.getUserId(), 
            carol.getUserId(), 
            "Hi Carol! We should collaborate on a project."
        );
        System.out.println("✓ Bob → Carol: Connection request sent");
        
        // David sends to Alice
        Connection conn4 = linkedin.sendConnectionRequest(
            david.getUserId(), 
            alice.getUserId(), 
            "Hi Alice! Interested in discussing system design."
        );
        System.out.println("✓ David → Alice: Connection request sent");
        System.out.println();
        
        // Accept connections
        System.out.println(">>> 5. Accept Connection Requests");
        System.out.println("-".repeat(50));
        
        linkedin.acceptConnectionRequest(conn1.getConnectionId());
        System.out.println("✓ Bob accepted Alice's connection request");
        
        linkedin.acceptConnectionRequest(conn2.getConnectionId());
        System.out.println("✓ Carol accepted Alice's connection request");
        
        linkedin.acceptConnectionRequest(conn3.getConnectionId());
        System.out.println("✓ Carol accepted Bob's connection request");
        
        linkedin.acceptConnectionRequest(conn4.getConnectionId());
        System.out.println("✓ Alice accepted David's connection request");
        
        System.out.println("\nConnection counts:");
        System.out.println("  Alice: " + linkedin.getConnectionCount(alice.getUserId()) + " connections");
        System.out.println("  Bob: " + linkedin.getConnectionCount(bob.getUserId()) + " connections");
        System.out.println("  Carol: " + linkedin.getConnectionCount(carol.getUserId()) + " connections");
        System.out.println("  David: " + linkedin.getConnectionCount(david.getUserId()) + " connections");
        System.out.println();
        
        // ==================== Messaging ====================
        System.out.println(">>> 6. Messaging");
        System.out.println("-".repeat(50));
        
        linkedin.sendMessage(
            alice.getUserId(), 
            bob.getUserId(), 
            "Hey Bob! Want to grab coffee and discuss the new feature?"
        );
        System.out.println("✓ Alice → Bob: Message sent");
        
        linkedin.sendMessage(
            bob.getUserId(), 
            alice.getUserId(), 
            "Sure Alice! How about Thursday at 2pm?"
        );
        System.out.println("✓ Bob → Alice: Message sent");
        
        linkedin.sendMessage(
            alice.getUserId(), 
            carol.getUserId(), 
            "Carol, I'd love to get your feedback on our new UI design."
        );
        System.out.println("✓ Alice → Carol: Message sent");
        
        System.out.println("\nBob's inbox: " + linkedin.getInbox(bob.getUserId()).size() + " messages");
        System.out.println("Alice's unread messages: " + linkedin.getUnreadMessageCount(alice.getUserId()));
        System.out.println();
        
        // ==================== Job Posting ====================
        System.out.println(">>> 7. Job Posting");
        System.out.println("-".repeat(50));
        
        Job job1 = new Job.Builder(
            david.getUserId(),
            "Senior Java Developer",
            "We're looking for an experienced Java developer to join our team. " +
            "You'll work on designing and building scalable backend systems.",
            "Consulting Inc"
        )
            .location("Seattle, WA")
            .jobType(JobType.FULL_TIME)
            .experienceLevel(ExperienceLevel.SENIOR)
            .requiredSkills(Arrays.asList("Java", "Spring Boot", "System Design", "Microservices"))
            .salaryRange("$140,000 - $180,000")
            .build();
        
        linkedin.postJob(job1);
        System.out.println("✓ David posted: " + job1.getTitle() + " at " + job1.getCompany());
        
        Job job2 = new Job.Builder(
            bob.getUserId(),
            "UX Designer",
            "Join our team to create amazing user experiences. " +
            "You'll work closely with product and engineering teams.",
            "StartupCo"
        )
            .location("New York, NY")
            .jobType(JobType.FULL_TIME)
            .experienceLevel(ExperienceLevel.MID_LEVEL)
            .requiredSkills(Arrays.asList("UI/UX Design", "Figma", "User Research"))
            .salaryRange("$90,000 - $120,000")
            .build();
        
        linkedin.postJob(job2);
        System.out.println("✓ Bob posted: " + job2.getTitle() + " at " + job2.getCompany());
        
        Job job3 = new Job.Builder(
            alice.getUserId(),
            "Junior Software Engineer",
            "Great opportunity for new graduates to learn and grow. " +
            "We provide mentorship and training.",
            "TechCorp"
        )
            .location("San Francisco, CA")
            .jobType(JobType.FULL_TIME)
            .experienceLevel(ExperienceLevel.JUNIOR)
            .requiredSkills(Arrays.asList("Java", "Python", "Problem Solving"))
            .salaryRange("$80,000 - $100,000")
            .build();
        
        linkedin.postJob(job3);
        System.out.println("✓ Alice posted: " + job3.getTitle() + " at " + job3.getCompany());
        System.out.println();
        
        // ==================== Job Applications ====================
        System.out.println(">>> 8. Job Applications");
        System.out.println("-".repeat(50));
        
        // Alice applies to David's job (same skills!)
        JobApplication app1 = new JobApplication(
            job1.getJobId(),
            alice.getUserId(),
            "I'm very interested in this position. I have 5 years of experience with Java " +
            "and have built several scalable systems. Would love to discuss further.",
            "https://resume.alice.com/resume.pdf"
        );
        linkedin.applyForJob(app1);
        System.out.println("✓ Alice applied for: " + job1.getTitle());
        
        // Carol applies to Bob's UX job
        JobApplication app2 = new JobApplication(
            job2.getJobId(),
            carol.getUserId(),
            "I've been working as a UX designer for 3 years and would love to bring my skills " +
            "to StartupCo. I'm passionate about creating intuitive user experiences.",
            "https://resume.carol.com/resume.pdf"
        );
        linkedin.applyForJob(app2);
        System.out.println("✓ Carol applied for: " + job2.getTitle());
        
        System.out.println("\nJob statistics:");
        System.out.println("  " + job1.getTitle() + ": " + job1.getApplicationCount() + " applications");
        System.out.println("  " + job2.getTitle() + ": " + job2.getApplicationCount() + " applications");
        System.out.println("  " + job3.getTitle() + ": " + job3.getApplicationCount() + " applications");
        System.out.println();
        
        // ==================== Update Application Status ====================
        System.out.println(">>> 9. Application Status Updates");
        System.out.println("-".repeat(50));
        
        linkedin.updateApplicationStatus(app1.getApplicationId(), JobApplicationStatus.INTERVIEW);
        System.out.println("✓ Alice's application moved to INTERVIEW stage");
        
        linkedin.updateApplicationStatus(app2.getApplicationId(), JobApplicationStatus.REVIEWING);
        System.out.println("✓ Carol's application is under REVIEW");
        System.out.println();
        
        // ==================== Search Jobs ====================
        System.out.println(">>> 10. Job Search (Strategy Pattern)");
        System.out.println("-".repeat(50));
        
        // Search for Java jobs
        System.out.println("Search: 'Java'");
        List<Job> javaJobs = linkedin.searchJobs("Java", new HashMap<>());
        javaJobs.forEach(job -> System.out.println("  • " + job.getTitle() + " at " + job.getCompany()));
        
        // Search with filters
        System.out.println("\nSearch: 'Designer' in San Francisco");
        Map<String, Object> criteria = new HashMap<>();
        criteria.put("location", "San Francisco");
        List<Job> designJobs = linkedin.searchJobs("Designer", criteria);
        designJobs.forEach(job -> System.out.println("  • " + job.getTitle() + " at " + job.getCompany() + 
                                                     " - " + job.getLocation()));
        
        // Search by job type
        System.out.println("\nSearch: Full-time jobs");
        Map<String, Object> fullTimeCriteria = new HashMap<>();
        fullTimeCriteria.put("jobType", JobType.FULL_TIME);
        List<Job> fullTimeJobs = linkedin.searchJobs("", fullTimeCriteria);
        System.out.println("  Found " + fullTimeJobs.size() + " full-time positions");
        System.out.println();
        
        // ==================== View Notifications ====================
        System.out.println(">>> 11. Notifications");
        System.out.println("-".repeat(50));
        
        System.out.println("Alice's notifications:");
        List<Notification> aliceNotifications = linkedin.getNotifications(alice.getUserId());
        aliceNotifications.forEach(notif -> 
            System.out.println("  • " + notif.getType() + ": " + notif.getMessage())
        );
        
        System.out.println("\nBob's notifications:");
        List<Notification> bobNotifications = linkedin.getNotifications(bob.getUserId());
        bobNotifications.forEach(notif -> 
            System.out.println("  • " + notif.getType() + ": " + notif.getMessage())
        );
        
        System.out.println("\nDavid's unread notifications: " + 
                         linkedin.getUnreadNotificationCount(david.getUserId()));
        System.out.println();
        
        // ==================== Conversation View ====================
        System.out.println(">>> 12. Conversation View");
        System.out.println("-".repeat(50));
        
        List<Message> aliceBobConversation = linkedin.getConversation(alice.getUserId(), bob.getUserId());
        System.out.println("Alice ↔ Bob conversation (" + aliceBobConversation.size() + " messages):");
        aliceBobConversation.forEach(msg -> {
            String sender = msg.getSenderId().equals(alice.getUserId()) ? "Alice" : "Bob";
            System.out.println("  " + sender + ": " + msg.getContent());
        });
        System.out.println();
        
        // ==================== Summary ====================
        System.out.println(">>> 13. System Summary");
        System.out.println("-".repeat(50));
        System.out.println("Total Users: 4");
        System.out.println("Total Connections: " + (linkedin.getConnectionCount(alice.getUserId()) + 
                                                     linkedin.getConnectionCount(bob.getUserId()) + 
                                                     linkedin.getConnectionCount(carol.getUserId()) + 
                                                     linkedin.getConnectionCount(david.getUserId())) / 2);
        System.out.println("Total Jobs Posted: " + linkedin.getAllJobs().size());
        System.out.println("Total Applications: 2");
        System.out.println();
        
        // ==================== Design Patterns Summary ====================
        System.out.println("=== Design Patterns Demonstrated ===");
        System.out.println("-".repeat(50));
        System.out.println("✓ Singleton Pattern - LinkedInSystem (single instance)");
        System.out.println("✓ Facade Pattern - LinkedInSystem (simplified interface)");
        System.out.println("✓ Strategy Pattern - UserSearchStrategy, JobSearchStrategy");
        System.out.println("✓ Observer Pattern - NotificationService with observers");
        System.out.println("✓ Builder Pattern - User and Job creation");
        System.out.println();
        
        System.out.println("=== SOLID Principles Applied ===");
        System.out.println("-".repeat(50));
        System.out.println("✓ Single Responsibility - Each service handles one domain");
        System.out.println("✓ Open/Closed - Extensible via interfaces (strategies, observers)");
        System.out.println("✓ Liskov Substitution - Services interchangeable via interfaces");
        System.out.println("✓ Interface Segregation - Focused, cohesive interfaces");
        System.out.println("✓ Dependency Inversion - Services depend on abstractions");
        System.out.println();
        
        System.out.println("=== Concurrency Features ===");
        System.out.println("-".repeat(50));
        System.out.println("✓ Thread-safe Singleton with double-checked locking");
        System.out.println("✓ ConcurrentHashMap for thread-safe storage");
        System.out.println("✓ CopyOnWriteArrayList for observer notifications");
        System.out.println();
        
        System.out.println("=== Demo Complete ===");
    }
}

