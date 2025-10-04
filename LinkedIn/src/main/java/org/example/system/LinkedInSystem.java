package org.example.system;

import org.example.enums.JobApplicationStatus;
import org.example.enums.NotificationType;
import org.example.interfaces.NotificationObserver;
import org.example.interfaces.SearchStrategy;
import org.example.model.*;
import org.example.services.*;
import org.example.strategies.JobSearchStrategy;
import org.example.strategies.UserSearchStrategy;

import java.util.List;
import java.util.Map;

/**
 * Main facade for the LinkedIn system.
 * Provides a unified interface to all system functionality.
 * Implements Facade pattern to simplify complex subsystem interactions.
 */
public class LinkedInSystem {
    private static LinkedInSystem instance;
    
    private final AuthenticationServiceImpl authService;
    private final ConnectionServiceImpl connectionService;
    private final MessagingServiceImpl messagingService;
    private final JobServiceImpl jobService;
    private final NotificationService notificationService;
    
    private final SearchStrategy<User> userSearchStrategy;
    private final SearchStrategy<Job> jobSearchStrategy;

    private LinkedInSystem() {
        // Initialize all services
        this.authService = new AuthenticationServiceImpl();
        this.connectionService = new ConnectionServiceImpl(authService);
        this.messagingService = new MessagingServiceImpl(connectionService);
        this.jobService = new JobServiceImpl(authService);
        this.notificationService = new NotificationService();
        
        // Initialize search strategies
        this.userSearchStrategy = new UserSearchStrategy();
        this.jobSearchStrategy = new JobSearchStrategy();
    }

    /**
     * Gets the singleton instance of the LinkedIn system.
     * Implements Singleton pattern for system-wide access.
     */
    public static LinkedInSystem getInstance() {
        if (instance == null) {
            synchronized (LinkedInSystem.class) {
                if (instance == null) {
                    instance = new LinkedInSystem();
                }
            }
        }
        return instance;
    }

    // ==================== Authentication Methods ====================

    public User register(String email, String password, String firstName, String lastName) {
        User user = authService.register(email, password, firstName, lastName);
        
        // Send welcome notification
        notificationService.sendNotification(
                user.getUserId(),
                NotificationType.CONNECTION_REQUEST,
                "Welcome to LinkedIn!",
                "Your account has been created successfully. Start building your professional network!",
                null
        );
        
        return user;
    }

    public User login(String email, String password) {
        return authService.authenticate(email, password);
    }

    public void logout(String userId) {
        authService.logout(userId);
    }

    public boolean changePassword(String userId, String oldPassword, String newPassword) {
        return authService.changePassword(userId, oldPassword, newPassword);
    }

    public User getUser(String userId) {
        return authService.getUserById(userId);
    }

    public User getUserByEmail(String email) {
        return authService.getUserByEmail(email);
    }

    // ==================== Profile Methods ====================

    public void updateProfile(String userId, Profile updatedProfile) {
        User user = authService.getUserById(userId);
        if (user != null) {
            user.setProfile(updatedProfile);
        }
    }

    public Profile getProfile(String userId) {
        User user = authService.getUserById(userId);
        return user != null ? user.getProfile() : null;
    }

    // ==================== Connection Methods ====================

    public Connection sendConnectionRequest(String senderId, String receiverId, String message) {
        Connection connection = connectionService.sendConnectionRequest(senderId, receiverId, message);
        
        // Send notification to receiver
        User sender = authService.getUserById(senderId);
        String senderName = sender.getProfile() != null ? sender.getProfile().getFullName() : "A user";
        
        notificationService.sendNotification(
                receiverId,
                NotificationType.CONNECTION_REQUEST,
                "New Connection Request",
                senderName + " sent you a connection request.",
                connection.getConnectionId()
        );
        
        return connection;
    }

    public boolean acceptConnectionRequest(String connectionId) {
        Connection connection = connectionService.getConnection(connectionId);
        if (connection == null) {
            return false;
        }
        
        boolean accepted = connectionService.acceptConnectionRequest(connectionId);
        
        if (accepted) {
            // Notify sender that request was accepted
            User receiver = authService.getUserById(connection.getReceiverId());
            String receiverName = receiver.getProfile() != null ? receiver.getProfile().getFullName() : "A user";
            
            notificationService.sendNotification(
                    connection.getSenderId(),
                    NotificationType.CONNECTION_ACCEPTED,
                    "Connection Request Accepted",
                    receiverName + " accepted your connection request.",
                    connectionId
            );
        }
        
        return accepted;
    }

    public boolean declineConnectionRequest(String connectionId) {
        return connectionService.declineConnectionRequest(connectionId);
    }

    public boolean removeConnection(String userId1, String userId2) {
        return connectionService.removeConnection(userId1, userId2);
    }

    public List<User> getConnections(String userId) {
        return connectionService.getConnections(userId);
    }

    public List<Connection> getPendingConnectionRequests(String userId) {
        return connectionService.getPendingRequests(userId);
    }

    public boolean areConnected(String userId1, String userId2) {
        return connectionService.areConnected(userId1, userId2);
    }

    // ==================== Messaging Methods ====================

    public Message sendMessage(String senderId, String receiverId, String content) {
        Message message = messagingService.sendMessage(senderId, receiverId, content);
        
        // Send notification to receiver
        User sender = authService.getUserById(senderId);
        String senderName = sender.getProfile() != null ? sender.getProfile().getFullName() : "A user";
        
        notificationService.sendNotification(
                receiverId,
                NotificationType.NEW_MESSAGE,
                "New Message",
                "You received a message from " + senderName,
                message.getMessageId()
        );
        
        return message;
    }

    public List<Message> getConversation(String userId1, String userId2) {
        return messagingService.getConversation(userId1, userId2);
    }

    public List<Message> getInbox(String userId) {
        return messagingService.getInbox(userId);
    }

    public List<Message> getSentMessages(String userId) {
        return messagingService.getSentMessages(userId);
    }

    public boolean markMessageAsRead(String messageId) {
        return messagingService.markAsRead(messageId);
    }

    public int getUnreadMessageCount(String userId) {
        return messagingService.getUnreadCount(userId);
    }

    // ==================== Job Methods ====================

    public Job postJob(Job job) {
        return jobService.postJob(job);
    }

    public boolean updateJob(String jobId, Job updatedJob) {
        return jobService.updateJob(jobId, updatedJob);
    }

    public boolean deleteJob(String jobId) {
        return jobService.deleteJob(jobId);
    }

    public Job getJob(String jobId) {
        return jobService.getJob(jobId);
    }

    public List<Job> getJobsByEmployer(String employerId) {
        return jobService.getJobsByEmployer(employerId);
    }

    public JobApplication applyForJob(JobApplication application) {
        JobApplication submittedApp = jobService.applyForJob(application);
        
        // Notify employer of new application
        Job job = jobService.getJob(application.getJobId());
        if (job != null) {
            User applicant = authService.getUserById(application.getApplicantId());
            String applicantName = applicant.getProfile() != null ? 
                    applicant.getProfile().getFullName() : "A candidate";
            
            notificationService.sendNotification(
                    job.getEmployerId(),
                    NotificationType.JOB_APPLICATION,
                    "New Job Application",
                    applicantName + " applied for " + job.getTitle(),
                    submittedApp.getApplicationId()
            );
        }
        
        return submittedApp;
    }

    public List<JobApplication> getApplicationsForJob(String jobId) {
        return jobService.getApplicationsForJob(jobId);
    }

    public List<JobApplication> getApplicationsByUser(String userId) {
        return jobService.getApplicationsByUser(userId);
    }

    public boolean updateApplicationStatus(String applicationId, JobApplicationStatus status) {
        JobApplication application = jobService.getApplication(applicationId);
        if (application == null) {
            return false;
        }
        
        boolean updated = jobService.updateApplicationStatus(applicationId, status);
        
        if (updated) {
            // Notify applicant of status change
            Job job = jobService.getJob(application.getJobId());
            String jobTitle = job != null ? job.getTitle() : "a job";
            
            notificationService.sendNotification(
                    application.getApplicantId(),
                    NotificationType.JOB_APPLICATION,
                    "Application Status Update",
                    "Your application for " + jobTitle + " is now: " + status,
                    applicationId
            );
        }
        
        return updated;
    }

    public List<Job> getAllActiveJobs() {
        return jobService.getAllActiveJobs();
    }

    // ==================== Search Methods ====================

    public List<User> searchUsers(String query, Map<String, Object> criteria) {
        // Get all users for searching
        List<User> allUsers = authService.getUserByEmail("").getConnectionIds().stream()
                .map(authService::getUserById)
                .toList();
        
        // In practice, you'd maintain a separate user repository
        return userSearchStrategy.search(query, criteria, allUsers);
    }

    public List<Job> searchJobs(String query, Map<String, Object> criteria) {
        return jobSearchStrategy.search(query, criteria, jobService.getAllActiveJobs());
    }

    // ==================== Notification Methods ====================

    public void registerNotificationObserver(NotificationObserver observer) {
        notificationService.registerObserver(observer);
    }

    public void unregisterNotificationObserver(NotificationObserver observer) {
        notificationService.unregisterObserver(observer);
    }

    public List<Notification> getNotifications(String userId) {
        return notificationService.getNotificationsForUser(userId);
    }

    public List<Notification> getUnreadNotifications(String userId) {
        return notificationService.getUnreadNotifications(userId);
    }

    public boolean markNotificationAsRead(String notificationId) {
        return notificationService.markAsRead(notificationId);
    }

    public int getUnreadNotificationCount(String userId) {
        return notificationService.getUnreadCount(userId);
    }

    // ==================== Utility Methods ====================

    /**
     * Gets system statistics.
     */
    public String getSystemStats() {
        // This would aggregate stats from all services
        return "LinkedIn System Statistics:\n" +
               "- Active Users: [Tracked by auth service]\n" +
               "- Total Connections: [Tracked by connection service]\n" +
               "- Active Jobs: " + jobService.getAllActiveJobs().size() + "\n" +
               "- Messages Sent: [Tracked by messaging service]";
    }
}

