package org.example.system;

import org.example.enums.JobApplicationStatus;
import org.example.enums.NotificationType;
import org.example.interfaces.*;
import org.example.model.*;
import org.example.services.*;
import org.example.strategies.JobSearchStrategy;
import org.example.strategies.UserSearchStrategy;

import java.util.List;
import java.util.Map;

/**
 * Facade for the LinkedIn system
 * Implements Singleton Design Pattern
 * 
 * This class provides a simplified interface to the complex subsystem
 * of authentication, connections, messaging, jobs, and notifications.
 * 
 * Design Patterns:
 * 1. Singleton - Ensures only one instance of the system
 * 2. Facade - Simplifies complex subsystem interactions
 * 3. Observer - Notification system (delegated to NotificationService)
 * 4. Strategy - Search functionality (delegated to SearchStrategy implementations)
 */
public class LinkedInSystem {
    private static LinkedInSystem instance;
    
    // Services
    private final AuthenticationService authService;
    private final ConnectionService connectionService;
    private final MessagingService messagingService;
    private final JobService jobService;
    private final NotificationService notificationService;
    
    // Search strategies
    private final SearchStrategy<User> userSearchStrategy;
    private final SearchStrategy<Job> jobSearchStrategy;

    /**
     * Private constructor for Singleton pattern
     */
    private LinkedInSystem() {
        // Initialize services
        this.authService = new AuthenticationServiceImpl();
        this.connectionService = new ConnectionServiceImpl(authService);
        this.messagingService = new MessagingServiceImpl(authService, connectionService);
        this.jobService = new JobServiceImpl(authService);
        this.notificationService = new NotificationService();
        
        // Initialize search strategies
        this.userSearchStrategy = new UserSearchStrategy();
        this.jobSearchStrategy = new JobSearchStrategy();
    }

    /**
     * Get singleton instance with thread-safe double-checked locking
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

    /**
     * Register a new user
     */
    public User register(String email, String password, String firstName, String lastName) {
        User user = authService.register(email, password, firstName, lastName);
        
        // Send welcome notification
        notificationService.sendNotification(
            user.getUserId(),
            NotificationType.CONNECTION_REQUEST,
            "Welcome to LinkedIn!",
            "Welcome " + user.getFullName() + "! Complete your profile to get started.",
            null
        );
        
        return user;
    }

    /**
     * Login user
     */
    public User login(String email, String password) {
        return authService.authenticate(email, password);
    }

    /**
     * Get user by ID
     */
    public User getUserById(String userId) {
        return authService.getUserById(userId);
    }

    /**
     * Get user by email
     */
    public User getUserByEmail(String email) {
        return authService.getUserByEmail(email);
    }

    // ==================== Connection Methods ====================

    /**
     * Send connection request
     */
    public Connection sendConnectionRequest(String senderId, String receiverId, String message) {
        Connection connection = connectionService.sendConnectionRequest(senderId, receiverId, message);
        
        // Notify receiver
        User sender = authService.getUserById(senderId);
        notificationService.sendNotification(
            receiverId,
            NotificationType.CONNECTION_REQUEST,
            "New Connection Request",
            sender.getFullName() + " wants to connect with you",
            connection.getConnectionId()
        );
        
        return connection;
    }

    /**
     * Accept connection request
     */
    public boolean acceptConnectionRequest(String connectionId) {
        Connection connection = connectionService.getConnectionById(connectionId);
        if (connection == null) {
            return false;
        }
        
        boolean accepted = connectionService.acceptConnectionRequest(connectionId);
        
        if (accepted) {
            // Notify sender that connection was accepted
            User receiver = authService.getUserById(connection.getReceiverId());
            notificationService.sendNotification(
                connection.getSenderId(),
                NotificationType.CONNECTION_ACCEPTED,
                "Connection Accepted",
                receiver.getFullName() + " accepted your connection request",
                connection.getConnectionId()
            );
        }
        
        return accepted;
    }

    /**
     * Decline connection request
     */
    public boolean declineConnectionRequest(String connectionId) {
        return connectionService.declineConnectionRequest(connectionId);
    }

    /**
     * Remove connection
     */
    public boolean removeConnection(String userId1, String userId2) {
        return connectionService.removeConnection(userId1, userId2);
    }

    /**
     * Get all connections for a user
     */
    public List<User> getConnections(String userId) {
        return connectionService.getConnections(userId);
    }

    /**
     * Get pending connection requests
     */
    public List<Connection> getPendingRequests(String userId) {
        return connectionService.getPendingRequests(userId);
    }

    /**
     * Check if two users are connected
     */
    public boolean areConnected(String userId1, String userId2) {
        return connectionService.areConnected(userId1, userId2);
    }

    /**
     * Get connection count
     */
    public int getConnectionCount(String userId) {
        return connectionService.getConnectionCount(userId);
    }

    // ==================== Messaging Methods ====================

    /**
     * Send message
     */
    public Message sendMessage(String senderId, String receiverId, String content) {
        Message message = messagingService.sendMessage(senderId, receiverId, content);
        
        // Notify receiver
        User sender = authService.getUserById(senderId);
        notificationService.sendNotification(
            receiverId,
            NotificationType.MESSAGE,
            "New Message",
            "New message from " + sender.getFullName(),
            message.getMessageId()
        );
        
        return message;
    }

    /**
     * Get conversation between two users
     */
    public List<Message> getConversation(String userId1, String userId2) {
        return messagingService.getConversation(userId1, userId2);
    }

    /**
     * Get inbox
     */
    public List<Message> getInbox(String userId) {
        return messagingService.getInbox(userId);
    }

    /**
     * Get sent messages
     */
    public List<Message> getSentMessages(String userId) {
        return messagingService.getSentMessages(userId);
    }

    /**
     * Mark message as read
     */
    public boolean markMessageAsRead(String messageId, String userId) {
        return messagingService.markAsRead(messageId, userId);
    }

    /**
     * Get unread message count
     */
    public int getUnreadMessageCount(String userId) {
        return messagingService.getUnreadMessageCount(userId);
    }

    // ==================== Job Methods ====================

    /**
     * Post a job
     */
    public Job postJob(Job job) {
        return jobService.postJob(job);
    }

    /**
     * Apply for a job
     */
    public JobApplication applyForJob(JobApplication application) {
        JobApplication submittedApp = jobService.applyForJob(application);
        
        // Notify employer
        Job job = jobService.getJobById(application.getJobId());
        User applicant = authService.getUserById(application.getApplicantId());
        
        notificationService.sendNotification(
            job.getEmployerId(),
            NotificationType.JOB_APPLICATION,
            "New Job Application",
            applicant.getFullName() + " applied for " + job.getTitle(),
            submittedApp.getApplicationId()
        );
        
        return submittedApp;
    }

    /**
     * Get job by ID
     */
    public Job getJobById(String jobId) {
        return jobService.getJobById(jobId);
    }

    /**
     * Get jobs posted by employer
     */
    public List<Job> getJobsByEmployer(String employerId) {
        return jobService.getJobsByEmployer(employerId);
    }

    /**
     * Get applications for a job
     */
    public List<JobApplication> getApplicationsForJob(String jobId) {
        return jobService.getApplicationsForJob(jobId);
    }

    /**
     * Get applications by user
     */
    public List<JobApplication> getApplicationsByUser(String userId) {
        return jobService.getApplicationsByUser(userId);
    }

    /**
     * Update application status
     */
    public boolean updateApplicationStatus(String applicationId, JobApplicationStatus status) {
        return jobService.updateApplicationStatus(applicationId, status);
    }

    /**
     * Get all jobs
     */
    public List<Job> getAllJobs() {
        return jobService.getAllJobs();
    }

    // ==================== Search Methods ====================

    /**
     * Search for users
     */
    public List<User> searchUsers(String query, Map<String, Object> criteria) {
        // Get all users for search (in production, this would be paginated)
        List<User> allUsers = new java.util.ArrayList<>();
        // This is a workaround since we can't access all users directly
        // In production, AuthService would have a getAllUsers method
        return userSearchStrategy.search(query, criteria, allUsers);
    }

    /**
     * Search for jobs
     */
    public List<Job> searchJobs(String query, Map<String, Object> criteria) {
        List<Job> allJobs = jobService.getAllJobs();
        return jobSearchStrategy.search(query, criteria, allJobs);
    }

    // ==================== Notification Methods ====================

    /**
     * Register notification observer
     */
    public void registerNotificationObserver(NotificationObserver observer) {
        notificationService.registerObserver(observer);
    }

    /**
     * Unregister notification observer
     */
    public void unregisterNotificationObserver(NotificationObserver observer) {
        notificationService.unregisterObserver(observer);
    }

    /**
     * Get all notifications for user
     */
    public List<Notification> getNotifications(String userId) {
        return notificationService.getNotifications(userId);
    }

    /**
     * Get unread notifications
     */
    public List<Notification> getUnreadNotifications(String userId) {
        return notificationService.getUnreadNotifications(userId);
    }

    /**
     * Mark notification as read
     */
    public boolean markNotificationAsRead(String notificationId) {
        return notificationService.markAsRead(notificationId);
    }

    /**
     * Get unread notification count
     */
    public int getUnreadNotificationCount(String userId) {
        return notificationService.getUnreadCount(userId);
    }
}

