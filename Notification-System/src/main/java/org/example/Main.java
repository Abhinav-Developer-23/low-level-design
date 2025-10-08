package org.example;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

/**
 * Notification System - Low Level Design
 * Design Patterns Used:
 * 1. Strategy Pattern - For different notification channels
 * 2. Observer Pattern - For user subscriptions
 * 3. Factory Pattern - For creating notifications
 * 4. Singleton Pattern - For notification manager
 * 5. Decorator Pattern - For notification batching
 * 6. Template Method Pattern - For notification delivery
 */

// ==================== ENUMS ====================

/**
 * Enum representing different types of notification events
 */
enum NotificationType {
    LIKE("Like"),
    COMMENT("Comment"),
    FOLLOW("Follow"),
    SYSTEM_UPDATE("System Update"),
    MESSAGE("Message"),
    SHARE("Share");

    private final String displayName;

    NotificationType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}

/**
 * Enum representing different notification channels
 */
enum NotificationChannel {
    EMAIL("Email"),
    SMS("SMS"),
    IN_APP("In-App");

    private final String displayName;

    NotificationChannel(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}

/**
 * Enum representing notification priority levels
 */
enum NotificationPriority {
    LOW(3),
    MEDIUM(2),
    HIGH(1),
    URGENT(0);

    private final int priorityLevel;

    NotificationPriority(int priorityLevel) {
        this.priorityLevel = priorityLevel;
    }

    public int getPriorityLevel() {
        return priorityLevel;
    }
}

/**
 * Enum representing notification delivery modes
 */
enum DeliveryMode {
    IMMEDIATE,
    BATCHED
}

/**
 * Enum representing notification status
 */
enum NotificationStatus {
    PENDING,
    SENT,
    FAILED,
    READ,
    UNREAD
}

// ==================== MODEL CLASSES ====================

/**
 * Represents a User in the system
 * Encapsulates user details and notification preferences
 */
class User {
    private final String userId;
    private final String name;
    private final String email;
    private final String phoneNumber;
    private final Set<NotificationType> subscribedTypes;
    private final Set<NotificationChannel> preferredChannels;
    private final Map<NotificationType, DeliveryMode> deliveryModePreferences;

    private User(UserBuilder builder) {
        this.userId = builder.userId;
        this.name = builder.name;
        this.email = builder.email;
        this.phoneNumber = builder.phoneNumber;
        this.subscribedTypes = builder.subscribedTypes;
        this.preferredChannels = builder.preferredChannels;
        this.deliveryModePreferences = builder.deliveryModePreferences;
    }

    // Getters
    public String getUserId() { return userId; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getPhoneNumber() { return phoneNumber; }
    public Set<NotificationType> getSubscribedTypes() { return new HashSet<>(subscribedTypes); }
    public Set<NotificationChannel> getPreferredChannels() { return new HashSet<>(preferredChannels); }

    public boolean isSubscribedTo(NotificationType type) {
        return subscribedTypes.contains(type);
    }

    public DeliveryMode getDeliveryMode(NotificationType type) {
        return deliveryModePreferences.getOrDefault(type, DeliveryMode.IMMEDIATE);
    }

    public void subscribe(NotificationType type) {
        subscribedTypes.add(type);
    }

    public void unsubscribe(NotificationType type) {
        subscribedTypes.remove(type);
    }

    /**
     * Builder pattern for User construction
     */
    public static class UserBuilder {
        private final String userId;
        private final String name;
        private String email;
        private String phoneNumber;
        private Set<NotificationType> subscribedTypes = new HashSet<>();
        private Set<NotificationChannel> preferredChannels = new HashSet<>();
        private Map<NotificationType, DeliveryMode> deliveryModePreferences = new HashMap<>();

        public UserBuilder(String userId, String name) {
            this.userId = userId;
            this.name = name;
        }

        public UserBuilder email(String email) {
            this.email = email;
            return this;
        }

        public UserBuilder phoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
            return this;
        }

        public UserBuilder subscribeTo(NotificationType... types) {
            this.subscribedTypes.addAll(Arrays.asList(types));
            return this;
        }

        public UserBuilder addChannel(NotificationChannel... channels) {
            this.preferredChannels.addAll(Arrays.asList(channels));
            return this;
        }

        public UserBuilder setDeliveryMode(NotificationType type, DeliveryMode mode) {
            this.deliveryModePreferences.put(type, mode);
            return this;
        }

        public User build() {
            return new User(this);
        }
    }

    @Override
    public String toString() {
        return "User{" +
                "userId='" + userId + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}

/**
 * Represents a Notification in the system
 * Contains all notification details and metadata
 */
class Notification {
    private final String notificationId;
    private final String userId;
    private final NotificationType type;
    private final String content;
    private final String title;
    private final LocalDateTime createdAt;
    private final NotificationPriority priority;
    private final Map<String, String> metadata;
    private NotificationStatus status;
    private LocalDateTime readAt;

    private Notification(NotificationBuilder builder) {
        this.notificationId = builder.notificationId;
        this.userId = builder.userId;
        this.type = builder.type;
        this.content = builder.content;
        this.title = builder.title;
        this.createdAt = builder.createdAt;
        this.priority = builder.priority;
        this.metadata = builder.metadata;
        this.status = NotificationStatus.PENDING;
    }

    // Getters
    public String getNotificationId() { return notificationId; }
    public String getUserId() { return userId; }
    public NotificationType getType() { return type; }
    public String getContent() { return content; }
    public String getTitle() { return title; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public NotificationPriority getPriority() { return priority; }
    public NotificationStatus getStatus() { return status; }
    public LocalDateTime getReadAt() { return readAt; }
    public Map<String, String> getMetadata() { return new HashMap<>(metadata); }

    public void markAsRead() {
        this.status = NotificationStatus.READ;
        this.readAt = LocalDateTime.now();
    }

    public void markAsUnread() {
        this.status = NotificationStatus.UNREAD;
        this.readAt = null;
    }

    public void markAsSent() {
        this.status = NotificationStatus.SENT;
    }

    public void markAsFailed() {
        this.status = NotificationStatus.FAILED;
    }

    public void addMetadata(String key, String value) {
        this.metadata.put(key, value);
    }

    /**
     * Builder pattern for Notification construction
     */
    public static class NotificationBuilder {
        private final String notificationId;
        private final String userId;
        private final NotificationType type;
        private String content;
        private String title;
        private LocalDateTime createdAt = LocalDateTime.now();
        private NotificationPriority priority = NotificationPriority.MEDIUM;
        private Map<String, String> metadata = new HashMap<>();

        public NotificationBuilder(String userId, NotificationType type) {
            this.notificationId = UUID.randomUUID().toString();
            this.userId = userId;
            this.type = type;
        }

        public NotificationBuilder content(String content) {
            this.content = content;
            return this;
        }

        public NotificationBuilder title(String title) {
            this.title = title;
            return this;
        }

        public NotificationBuilder priority(NotificationPriority priority) {
            this.priority = priority;
            return this;
        }

        public NotificationBuilder addMetadata(String key, String value) {
            this.metadata.put(key, value);
            return this;
        }

        public Notification build() {
            return new Notification(this);
        }
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return "Notification{" +
                "id='" + notificationId.substring(0, 8) + "...' " +
                ", type=" + type.getDisplayName() +
                ", title='" + title + '\'' +
                ", status=" + status +
                ", createdAt=" + createdAt.format(formatter) +
                '}';
    }
}

/**
 * Represents a batch of notifications grouped together
 */
class NotificationBatch {
    private final String batchId;
    private final String userId;
    private final List<Notification> notifications;
    private final LocalDateTime createdAt;

    public NotificationBatch(String userId) {
        this.batchId = UUID.randomUUID().toString();
        this.userId = userId;
        this.notifications = new ArrayList<>();
        this.createdAt = LocalDateTime.now();
    }

    public void addNotification(Notification notification) {
        notifications.add(notification);
    }

    public List<Notification> getNotifications() {
        return new ArrayList<>(notifications);
    }

    public String getUserId() {
        return userId;
    }

    public int getSize() {
        return notifications.size();
    }

    public Map<NotificationType, Long> getNotificationsByType() {
        return notifications.stream()
                .collect(Collectors.groupingBy(Notification::getType, Collectors.counting()));
    }

    @Override
    public String toString() {
        return "NotificationBatch{" +
                "batchId='" + batchId.substring(0, 8) + "...' " +
                ", userId='" + userId + '\'' +
                ", count=" + notifications.size() +
                ", types=" + getNotificationsByType() +
                '}';
    }
}

// ==================== STRATEGY INTERFACES ====================

/**
 * Strategy interface for different notification channels
 * Implements Strategy Pattern for delivery mechanism
 */
interface NotificationChannelStrategy {
    boolean send(User user, Notification notification);
    NotificationChannel getChannelType();
}

/**
 * Strategy interface for grouping notifications
 */
interface NotificationGroupingStrategy {
    Map<String, List<Notification>> group(List<Notification> notifications);
}

// ==================== STRATEGY IMPLEMENTATIONS ====================

/**
 * Email notification channel implementation
 */
class EmailNotificationChannel implements NotificationChannelStrategy {
    @Override
    public boolean send(User user, Notification notification) {
        // Simulate email sending
        System.out.println("  [EMAIL] Sending to: " + user.getEmail());
        System.out.println("     Subject: " + notification.getTitle());
        System.out.println("     Content: " + notification.getContent());
        
        // Simulate network delay
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return false;
        }
        
        return true;
    }

    @Override
    public NotificationChannel getChannelType() {
        return NotificationChannel.EMAIL;
    }
}

/**
 * SMS notification channel implementation
 */
class SmsNotificationChannel implements NotificationChannelStrategy {
    @Override
    public boolean send(User user, Notification notification) {
        // Simulate SMS sending
        System.out.println("  [SMS] Sending to: " + user.getPhoneNumber());
        System.out.println("     Message: " + notification.getTitle() + " - " + notification.getContent());
        
        // Simulate network delay
        try {
            Thread.sleep(80);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return false;
        }
        
        return true;
    }

    @Override
    public NotificationChannel getChannelType() {
        return NotificationChannel.SMS;
    }
}

/**
 * In-App notification channel implementation
 */
class InAppNotificationChannel implements NotificationChannelStrategy {
    @Override
    public boolean send(User user, Notification notification) {
        // Simulate in-app notification
        System.out.println("  [IN-APP] Notification for user: " + user.getName());
        System.out.println("     Title: " + notification.getTitle());
        System.out.println("     Content: " + notification.getContent());
        
        // In-app is usually faster
        try {
            Thread.sleep(20);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return false;
        }
        
        return true;
    }

    @Override
    public NotificationChannel getChannelType() {
        return NotificationChannel.IN_APP;
    }
}

/**
 * Groups notifications by type
 */
class TypeBasedGroupingStrategy implements NotificationGroupingStrategy {
    @Override
    public Map<String, List<Notification>> group(List<Notification> notifications) {
        return notifications.stream()
                .collect(Collectors.groupingBy(n -> n.getType().getDisplayName()));
    }
}

/**
 * Groups notifications by time (hour)
 */
class TimeBasedGroupingStrategy implements NotificationGroupingStrategy {
    @Override
    public Map<String, List<Notification>> group(List<Notification> notifications) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:00");
        return notifications.stream()
                .collect(Collectors.groupingBy(n -> n.getCreatedAt().format(formatter)));
    }
}

// ==================== FACTORY PATTERN ====================

/**
 * Factory for creating notification channels
 * Implements Factory Pattern
 */
class NotificationChannelFactory {
    private static final Map<NotificationChannel, NotificationChannelStrategy> channels = new HashMap<>();

    static {
        channels.put(NotificationChannel.EMAIL, new EmailNotificationChannel());
        channels.put(NotificationChannel.SMS, new SmsNotificationChannel());
        channels.put(NotificationChannel.IN_APP, new InAppNotificationChannel());
    }

    public static NotificationChannelStrategy getChannel(NotificationChannel channelType) {
        NotificationChannelStrategy channel = channels.get(channelType);
        if (channel == null) {
            throw new IllegalArgumentException("Unsupported channel type: " + channelType);
        }
        return channel;
    }

    public static List<NotificationChannelStrategy> getAllChannels() {
        return new ArrayList<>(channels.values());
    }
}

/**
 * Factory for creating notifications based on type
 */
class NotificationFactory {
    public static Notification createLikeNotification(String userId, String likerName, String postId) {
        return new Notification.NotificationBuilder(userId, NotificationType.LIKE)
                .title("New Like")
                .content(likerName + " liked your post")
                .priority(NotificationPriority.LOW)
                .addMetadata("postId", postId)
                .addMetadata("likerName", likerName)
                .build();
    }

    public static Notification createCommentNotification(String userId, String commenterName, String postId) {
        return new Notification.NotificationBuilder(userId, NotificationType.COMMENT)
                .title("New Comment")
                .content(commenterName + " commented on your post")
                .priority(NotificationPriority.MEDIUM)
                .addMetadata("postId", postId)
                .addMetadata("commenterName", commenterName)
                .build();
    }

    public static Notification createFollowNotification(String userId, String followerName) {
        return new Notification.NotificationBuilder(userId, NotificationType.FOLLOW)
                .title("New Follower")
                .content(followerName + " started following you")
                .priority(NotificationPriority.MEDIUM)
                .addMetadata("followerName", followerName)
                .build();
    }

    public static Notification createSystemUpdateNotification(String userId, String message) {
        return new Notification.NotificationBuilder(userId, NotificationType.SYSTEM_UPDATE)
                .title("System Update")
                .content(message)
                .priority(NotificationPriority.HIGH)
                .build();
    }

    public static Notification createMessageNotification(String userId, String senderName, String messagePreview) {
        return new Notification.NotificationBuilder(userId, NotificationType.MESSAGE)
                .title("New Message")
                .content(senderName + ": " + messagePreview)
                .priority(NotificationPriority.HIGH)
                .addMetadata("senderName", senderName)
                .build();
    }
}

// ==================== OBSERVER PATTERN ====================

/**
 * Observer interface for notification events
 */
interface NotificationObserver {
    void onNotificationSent(Notification notification);
    void onNotificationFailed(Notification notification);
    void onNotificationRead(Notification notification);
}

/**
 * Analytics observer to track notification metrics
 */
class NotificationAnalyticsObserver implements NotificationObserver {
    private int sentCount = 0;
    private int failedCount = 0;
    private int readCount = 0;

    @Override
    public void onNotificationSent(Notification notification) {
        sentCount++;
        System.out.println("[ANALYTICS] Notification sent. Total sent: " + sentCount);
    }

    @Override
    public void onNotificationFailed(Notification notification) {
        failedCount++;
        System.out.println("[ANALYTICS] Notification failed. Total failed: " + failedCount);
    }

    @Override
    public void onNotificationRead(Notification notification) {
        readCount++;
        System.out.println("[ANALYTICS] Notification read. Total read: " + readCount);
    }

    public void printStats() {
        System.out.println("\n=== Notification Analytics ===");
        System.out.println("Total Sent: " + sentCount);
        System.out.println("Total Failed: " + failedCount);
        System.out.println("Total Read: " + readCount);
        double readRate = sentCount > 0 ? (readCount * 100.0 / sentCount) : 0;
        System.out.println("Read Rate: " + String.format("%.2f", readRate) + "%");
    }
}

/**
 * Logging observer for audit trail
 */
class NotificationLoggingObserver implements NotificationObserver {
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public void onNotificationSent(Notification notification) {
        log("SENT", notification);
    }

    @Override
    public void onNotificationFailed(Notification notification) {
        log("FAILED", notification);
    }

    @Override
    public void onNotificationRead(Notification notification) {
        log("READ", notification);
    }

    private void log(String event, Notification notification) {
        System.out.println("[LOG] " + LocalDateTime.now().format(formatter) +
                " | Event: " + event +
                " | NotificationId: " + notification.getNotificationId().substring(0, 8) +
                " | Type: " + notification.getType());
    }
}

// ==================== TEMPLATE METHOD PATTERN ====================

/**
 * Abstract class for notification delivery process
 * Implements Template Method Pattern
 */
abstract class NotificationDeliveryTemplate {
    // Template method defining the skeleton of notification delivery
    public final boolean deliver(User user, Notification notification, NotificationChannelStrategy channel) {
        if (!validateDelivery(user, notification)) {
            return false;
        }

        prepareNotification(notification);

        boolean success = sendNotification(user, notification, channel);

        postDeliveryAction(notification, success);

        return success;
    }

    // Hook methods that can be overridden
    protected boolean validateDelivery(User user, Notification notification) {
        return user != null && notification != null;
    }

    protected void prepareNotification(Notification notification) {
        // Default implementation - can be overridden
    }

    protected abstract boolean sendNotification(User user, Notification notification, NotificationChannelStrategy channel);

    protected void postDeliveryAction(Notification notification, boolean success) {
        if (success) {
            notification.markAsSent();
        } else {
            notification.markAsFailed();
        }
    }
}

/**
 * Immediate notification delivery implementation
 */
class ImmediateNotificationDelivery extends NotificationDeliveryTemplate {
    @Override
    protected boolean sendNotification(User user, Notification notification, NotificationChannelStrategy channel) {
        System.out.println("\n[IMMEDIATE DELIVERY]");
        return channel.send(user, notification);
    }
}

/**
 * Batched notification delivery implementation
 */
class BatchedNotificationDelivery extends NotificationDeliveryTemplate {
    @Override
    protected boolean sendNotification(User user, Notification notification, NotificationChannelStrategy channel) {
        System.out.println("\n[BATCHED DELIVERY]");
        return channel.send(user, notification);
    }

    @Override
    protected void prepareNotification(Notification notification) {
        // Add batching-specific preparation
        notification.addMetadata("batchDelivery", "true");
    }
}

// ==================== SERVICE CLASSES ====================

/**
 * Service to manage user subscriptions
 */
class SubscriptionService {
    private final Map<String, User> users = new ConcurrentHashMap<>();

    public void registerUser(User user) {
        users.put(user.getUserId(), user);
        System.out.println("[+] User registered: " + user.getName());
    }

    public User getUser(String userId) {
        return users.get(userId);
    }

    public void updateSubscription(String userId, NotificationType type, boolean subscribe) {
        User user = users.get(userId);
        if (user != null) {
            if (subscribe) {
                user.subscribe(type);
                System.out.println("[+] User " + user.getName() + " subscribed to " + type.getDisplayName());
            } else {
                user.unsubscribe(type);
                System.out.println("[-] User " + user.getName() + " unsubscribed from " + type.getDisplayName());
            }
        }
    }

    public List<User> getUsersSubscribedTo(NotificationType type) {
        return users.values().stream()
                .filter(user -> user.isSubscribedTo(type))
                .collect(Collectors.toList());
    }

    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }
}

/**
 * Service to store and manage notifications
 */
class NotificationStorageService {
    private final Map<String, List<Notification>> userNotifications = new ConcurrentHashMap<>();

    public void saveNotification(Notification notification) {
        userNotifications.computeIfAbsent(notification.getUserId(), k -> new ArrayList<>())
                .add(notification);
    }

    public List<Notification> getNotificationsForUser(String userId) {
        return new ArrayList<>(userNotifications.getOrDefault(userId, new ArrayList<>()));
    }

    public List<Notification> getUnreadNotifications(String userId) {
        return userNotifications.getOrDefault(userId, new ArrayList<>()).stream()
                .filter(n -> n.getStatus() == NotificationStatus.SENT || n.getStatus() == NotificationStatus.UNREAD)
                .collect(Collectors.toList());
    }

    public void markAsRead(String notificationId) {
        userNotifications.values().stream()
                .flatMap(List::stream)
                .filter(n -> n.getNotificationId().equals(notificationId))
                .findFirst()
                .ifPresent(Notification::markAsRead);
    }

    public void markAllAsRead(String userId) {
        userNotifications.getOrDefault(userId, new ArrayList<>())
                .forEach(Notification::markAsRead);
    }

    public Map<NotificationType, Long> getNotificationCountsByType(String userId) {
        return userNotifications.getOrDefault(userId, new ArrayList<>()).stream()
                .collect(Collectors.groupingBy(Notification::getType, Collectors.counting()));
    }
}

/**
 * Service to batch notifications
 */
class NotificationBatchingService {
    private final Map<String, NotificationBatch> pendingBatches = new ConcurrentHashMap<>();
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private final int batchIntervalSeconds;

    public NotificationBatchingService(int batchIntervalSeconds) {
        this.batchIntervalSeconds = batchIntervalSeconds;
    }

    public void addToBatch(Notification notification) {
        String userId = notification.getUserId();
        NotificationBatch batch = pendingBatches.computeIfAbsent(userId, NotificationBatch::new);
        batch.addNotification(notification);
    }

    public NotificationBatch getBatch(String userId) {
        return pendingBatches.remove(userId);
    }

    public void startBatchScheduler(NotificationManager manager) {
        scheduler.scheduleAtFixedRate(() -> {
            processBatches(manager);
        }, batchIntervalSeconds, batchIntervalSeconds, TimeUnit.SECONDS);
    }

    private void processBatches(NotificationManager manager) {
        if (!pendingBatches.isEmpty()) {
            System.out.println("\n[BATCH SCHEDULER] Processing pending batches...");
            List<String> userIds = new ArrayList<>(pendingBatches.keySet());
            for (String userId : userIds) {
                NotificationBatch batch = getBatch(userId);
                if (batch != null && batch.getSize() > 0) {
                    manager.sendBatch(batch);
                }
            }
        }
    }

    public void shutdown() {
        scheduler.shutdown();
    }
}

// ==================== MAIN NOTIFICATION MANAGER (Singleton) ====================

/**
 * Main Notification Manager - Singleton Pattern
 * Orchestrates the entire notification system
 */
class NotificationManager {
    private static volatile NotificationManager instance;
    private final SubscriptionService subscriptionService;
    private final NotificationStorageService storageService;
    private final NotificationBatchingService batchingService;
    private final List<NotificationObserver> observers;
    private final NotificationDeliveryTemplate immediateDelivery;
    private final NotificationDeliveryTemplate batchedDelivery;
    private final ExecutorService executorService;

    private NotificationManager() {
        this.subscriptionService = new SubscriptionService();
        this.storageService = new NotificationStorageService();
        this.batchingService = new NotificationBatchingService(5); // 5 seconds batch interval
        this.observers = new ArrayList<>();
        this.immediateDelivery = new ImmediateNotificationDelivery();
        this.batchedDelivery = new BatchedNotificationDelivery();
        this.executorService = Executors.newFixedThreadPool(5);
    }

    // Double-checked locking for thread-safe singleton
    public static NotificationManager getInstance() {
        if (instance == null) {
            synchronized (NotificationManager.class) {
                if (instance == null) {
                    instance = new NotificationManager();
                }
            }
        }
        return instance;
    }

    public void registerUser(User user) {
        subscriptionService.registerUser(user);
    }

    public void addObserver(NotificationObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(NotificationObserver observer) {
        observers.remove(observer);
    }

    /**
     * Send a notification to a user
     */
    public void sendNotification(Notification notification) {
        User user = subscriptionService.getUser(notification.getUserId());
        
        if (user == null) {
            System.out.println("[!] User not found: " + notification.getUserId());
            return;
        }

        if (!user.isSubscribedTo(notification.getType())) {
            System.out.println("[!] User " + user.getName() + " is not subscribed to " + notification.getType().getDisplayName());
            return;
        }

        // Save notification to storage
        storageService.saveNotification(notification);

        // Determine delivery mode
        DeliveryMode mode = user.getDeliveryMode(notification.getType());

        if (mode == DeliveryMode.BATCHED) {
            batchingService.addToBatch(notification);
            System.out.println("[BATCH] Added to batch for user: " + user.getName());
        } else {
            // Immediate delivery
            deliverNotification(user, notification, immediateDelivery);
        }
    }

    /**
     * Send a batch of notifications
     */
    public void sendBatch(NotificationBatch batch) {
        User user = subscriptionService.getUser(batch.getUserId());
        if (user == null) return;

        System.out.println("\n[BATCH] Sending batch: " + batch);

        for (Notification notification : batch.getNotifications()) {
            deliverNotification(user, notification, batchedDelivery);
        }
    }

    /**
     * Deliver notification through user's preferred channels
     */
    private void deliverNotification(User user, Notification notification, NotificationDeliveryTemplate delivery) {
        Set<NotificationChannel> channels = user.getPreferredChannels();

        for (NotificationChannel channelType : channels) {
            executorService.submit(() -> {
                try {
                    NotificationChannelStrategy channel = NotificationChannelFactory.getChannel(channelType);
                    boolean success = delivery.deliver(user, notification, channel);

                    if (success) {
                        notifyObservers(observer -> observer.onNotificationSent(notification));
                    } else {
                        notifyObservers(observer -> observer.onNotificationFailed(notification));
                    }
                } catch (Exception e) {
                    System.err.println("[ERROR] Error delivering notification: " + e.getMessage());
                    notifyObservers(observer -> observer.onNotificationFailed(notification));
                }
            });
        }
    }

    /**
     * Mark notification as read
     */
    public void markAsRead(String notificationId) {
        storageService.markAsRead(notificationId);
        storageService.getNotificationsForUser("").stream()
                .filter(n -> n.getNotificationId().equals(notificationId))
                .findFirst()
                .ifPresent(notification -> notifyObservers(observer -> observer.onNotificationRead(notification)));
    }

    /**
     * Get notifications for a user
     */
    public List<Notification> getNotifications(String userId) {
        return storageService.getNotificationsForUser(userId);
    }

    /**
     * Get unread notifications for a user
     */
    public List<Notification> getUnreadNotifications(String userId) {
        return storageService.getUnreadNotifications(userId);
    }

    /**
     * Group notifications using a strategy
     */
    public Map<String, List<Notification>> groupNotifications(String userId, NotificationGroupingStrategy strategy) {
        List<Notification> notifications = storageService.getNotificationsForUser(userId);
        return strategy.group(notifications);
    }

    /**
     * Broadcast notification to all subscribed users
     */
    public void broadcastNotification(NotificationType type, String title, String content) {
        List<User> subscribedUsers = subscriptionService.getUsersSubscribedTo(type);
        
        System.out.println("\n[BROADCAST] Broadcasting to " + subscribedUsers.size() + " users");

        for (User user : subscribedUsers) {
            Notification notification = new Notification.NotificationBuilder(user.getUserId(), type)
                    .title(title)
                    .content(content)
                    .priority(NotificationPriority.HIGH)
                    .build();
            
            sendNotification(notification);
        }
    }

    /**
     * Update user subscription
     */
    public void updateSubscription(String userId, NotificationType type, boolean subscribe) {
        subscriptionService.updateSubscription(userId, type, subscribe);
    }

    /**
     * Start batch processing scheduler
     */
    public void startBatchScheduler() {
        batchingService.startBatchScheduler(this);
    }

    /**
     * Notify all observers
     */
    private void notifyObservers(java.util.function.Consumer<NotificationObserver> action) {
        for (NotificationObserver observer : observers) {
            action.accept(observer);
        }
    }

    /**
     * Get notification statistics for a user
     */
    public void printUserStatistics(String userId) {
        User user = subscriptionService.getUser(userId);
        if (user == null) return;

        System.out.println("\n=== Notification Statistics for " + user.getName() + " ===");
        
        List<Notification> allNotifications = storageService.getNotificationsForUser(userId);
        List<Notification> unreadNotifications = storageService.getUnreadNotifications(userId);
        
        System.out.println("Total Notifications: " + allNotifications.size());
        System.out.println("Unread Notifications: " + unreadNotifications.size());
        
        Map<NotificationType, Long> countsByType = storageService.getNotificationCountsByType(userId);
        System.out.println("Notifications by Type:");
        countsByType.forEach((type, count) -> 
            System.out.println("  - " + type.getDisplayName() + ": " + count));
    }

    /**
     * Shutdown the notification manager
     */
    public void shutdown() {
        batchingService.shutdown();
        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(5, TimeUnit.SECONDS)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            executorService.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}

// ==================== MAIN CLASS ====================

public class Main {
    public static void main(String[] args) {
        System.out.println("============================================================");
        System.out.println("     NOTIFICATION SYSTEM - Low Level Design Demo       ");
        System.out.println("============================================================\n");

        // Get singleton instance of NotificationManager
        NotificationManager manager = NotificationManager.getInstance();

        // Add observers for analytics and logging
        NotificationAnalyticsObserver analyticsObserver = new NotificationAnalyticsObserver();
        NotificationLoggingObserver loggingObserver = new NotificationLoggingObserver();
        manager.addObserver(analyticsObserver);
        manager.addObserver(loggingObserver);

        System.out.println("=== Phase 1: User Registration ===\n");

        // Create and register users
        User alice = new User.UserBuilder("user001", "Alice Johnson")
                .email("alice@example.com")
                .phoneNumber("+1-555-0101")
                .subscribeTo(NotificationType.LIKE, NotificationType.COMMENT, NotificationType.FOLLOW)
                .addChannel(NotificationChannel.EMAIL, NotificationChannel.IN_APP)
                .setDeliveryMode(NotificationType.LIKE, DeliveryMode.BATCHED)
                .setDeliveryMode(NotificationType.COMMENT, DeliveryMode.IMMEDIATE)
                .build();

        User bob = new User.UserBuilder("user002", "Bob Smith")
                .email("bob@example.com")
                .phoneNumber("+1-555-0102")
                .subscribeTo(NotificationType.COMMENT, NotificationType.MESSAGE, NotificationType.SYSTEM_UPDATE)
                .addChannel(NotificationChannel.SMS, NotificationChannel.IN_APP)
                .setDeliveryMode(NotificationType.MESSAGE, DeliveryMode.IMMEDIATE)
                .build();

        User charlie = new User.UserBuilder("user003", "Charlie Davis")
                .email("charlie@example.com")
                .phoneNumber("+1-555-0103")
                .subscribeTo(NotificationType.LIKE, NotificationType.FOLLOW, NotificationType.SYSTEM_UPDATE)
                .addChannel(NotificationChannel.EMAIL, NotificationChannel.SMS, NotificationChannel.IN_APP)
                .build();

        manager.registerUser(alice);
        manager.registerUser(bob);
        manager.registerUser(charlie);

        System.out.println("\n=== Phase 2: Sending Individual Notifications ===\n");

        // Send immediate notifications
        System.out.println("--- Sending Comment Notification to Alice ---");
        Notification commentNotif = NotificationFactory.createCommentNotification(
                alice.getUserId(), "Bob Smith", "post123");
        manager.sendNotification(commentNotif);

        // Small delay to see the output
        sleep(200);

        System.out.println("\n--- Sending Message Notification to Bob ---");
        Notification messageNotif = NotificationFactory.createMessageNotification(
                bob.getUserId(), "Alice Johnson", "Hey, how are you?");
        manager.sendNotification(messageNotif);

        sleep(200);

        // Send batched notifications (will be queued)
        System.out.println("\n--- Sending Like Notifications to Alice (Batched) ---");
        for (int i = 1; i <= 3; i++) {
            Notification likeNotif = NotificationFactory.createLikeNotification(
                    alice.getUserId(), "User" + i, "post" + (123 + i));
            manager.sendNotification(likeNotif);
        }

        System.out.println("\n=== Phase 3: Broadcasting System Update ===\n");

        sleep(500);

        // Broadcast to all subscribed users
        manager.broadcastNotification(
                NotificationType.SYSTEM_UPDATE,
                "System Maintenance",
                "The system will undergo maintenance on Sunday from 2 AM to 4 AM EST");

        sleep(500);

        System.out.println("\n=== Phase 4: Sending Follow Notifications ===\n");

        Notification followNotif1 = NotificationFactory.createFollowNotification(
                alice.getUserId(), "Charlie Davis");
        manager.sendNotification(followNotif1);

        sleep(200);

        Notification followNotif2 = NotificationFactory.createFollowNotification(
                charlie.getUserId(), "Alice Johnson");
        manager.sendNotification(followNotif2);

        sleep(500);

        System.out.println("\n=== Phase 5: Managing Subscriptions ===\n");

        // Update subscription
        manager.updateSubscription(bob.getUserId(), NotificationType.LIKE, true);
        
        // Send a like notification to Bob (now subscribed)
        Notification bobLikeNotif = NotificationFactory.createLikeNotification(
                bob.getUserId(), "Alice Johnson", "post456");
        manager.sendNotification(bobLikeNotif);

        sleep(500);

        System.out.println("\n=== Phase 6: Grouping Notifications ===\n");

        // Group Alice's notifications by type
        Map<String, List<Notification>> groupedByType = manager.groupNotifications(
                alice.getUserId(), new TypeBasedGroupingStrategy());

        System.out.println("Alice's notifications grouped by type:");
        groupedByType.forEach((type, notifs) -> 
            System.out.println("  " + type + ": " + notifs.size() + " notification(s)"));

        // Group by time
        Map<String, List<Notification>> groupedByTime = manager.groupNotifications(
                alice.getUserId(), new TimeBasedGroupingStrategy());

        System.out.println("\nAlice's notifications grouped by time:");
        groupedByTime.forEach((time, notifs) -> 
            System.out.println("  " + time + ": " + notifs.size() + " notification(s)"));

        System.out.println("\n=== Phase 7: Marking Notifications as Read ===\n");

        List<Notification> aliceNotifications = manager.getNotifications(alice.getUserId());
        if (!aliceNotifications.isEmpty()) {
            Notification firstNotif = aliceNotifications.get(0);
            System.out.println("Marking notification as read: " + firstNotif.getTitle());
            manager.markAsRead(firstNotif.getNotificationId());
        }

        sleep(200);

        System.out.println("\n=== Phase 8: User Statistics ===\n");

        manager.printUserStatistics(alice.getUserId());
        manager.printUserStatistics(bob.getUserId());
        manager.printUserStatistics(charlie.getUserId());

        System.out.println("\n=== Phase 9: Analytics Summary ===");

        analyticsObserver.printStats();

        System.out.println("\n=== Phase 10: Displaying Unread Notifications ===\n");

        displayUnreadNotifications(manager, alice);
        displayUnreadNotifications(manager, bob);
        displayUnreadNotifications(manager, charlie);

        System.out.println("\n=== Demo Complete ===\n");
        
        System.out.println("Summary of Design Patterns Used:");
        System.out.println("  * Singleton Pattern - NotificationManager");
        System.out.println("  * Strategy Pattern - NotificationChannelStrategy, GroupingStrategy");
        System.out.println("  * Observer Pattern - NotificationObserver for analytics & logging");
        System.out.println("  * Factory Pattern - NotificationFactory, ChannelFactory");
        System.out.println("  * Builder Pattern - User and Notification builders");
        System.out.println("  * Template Method Pattern - NotificationDeliveryTemplate");

        System.out.println("\nSOLID Principles Applied:");
        System.out.println("  * Single Responsibility - Each class has one responsibility");
        System.out.println("  * Open/Closed - Open for extension, closed for modification");
        System.out.println("  * Liskov Substitution - Interfaces can be substituted");
        System.out.println("  * Interface Segregation - Small, specific interfaces");
        System.out.println("  * Dependency Inversion - Depend on abstractions");

        // Cleanup
        manager.shutdown();
        System.out.println("\n[+] System shutdown successfully");
    }

    private static void displayUnreadNotifications(NotificationManager manager, User user) {
        List<Notification> unread = manager.getUnreadNotifications(user.getUserId());
        System.out.println(user.getName() + " has " + unread.size() + " unread notification(s)");
        
        if (!unread.isEmpty()) {
            System.out.println("Unread notifications:");
            unread.forEach(n -> System.out.println("  - " + n.getTitle() + " - " + n.getContent()));
        }
        System.out.println();
    }

    private static void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}