# LinkedIn Platform - API Guide

## 📖 Complete API Reference

This guide provides a comprehensive reference for all available operations in the LinkedIn platform.

## 🔐 Authentication & User Management

### Register a New User
```java
User register(String email, String password, String firstName, String lastName)
```

**Parameters:**
- `email` - User's email address (must be unique)
- `password` - Password (minimum 8 characters)
- `firstName` - User's first name
- `lastName` - User's last name

**Returns:** `User` object with generated ID and profile

**Throws:** `IllegalArgumentException` if email already exists or password is weak

**Example:**
```java
LinkedInSystem linkedin = LinkedInSystem.getInstance();
User user = linkedin.register(
    "john.doe@example.com",
    "SecurePass123",
    "John",
    "Doe"
);
```

### Login
```java
User login(String email, String password)
```

**Parameters:**
- `email` - User's email
- `password` - User's password

**Returns:** `User` object if authentication successful, `null` otherwise

**Example:**
```java
User user = linkedin.login("john.doe@example.com", "SecurePass123");
if (user != null) {
    System.out.println("Login successful!");
}
```

### Logout
```java
void logout(String userId)
```

**Parameters:**
- `userId` - ID of the user to logout

**Example:**
```java
linkedin.logout(user.getUserId());
```

### Change Password
```java
boolean changePassword(String userId, String oldPassword, String newPassword)
```

**Returns:** `true` if password changed successfully

**Example:**
```java
boolean success = linkedin.changePassword(
    userId,
    "OldPass123",
    "NewPass456"
);
```

## 👤 Profile Management

### Update Profile
```java
void updateProfile(String userId, Profile updatedProfile)
```

**Example:**
```java
Profile profile = new Profile("John", "Doe");
profile.setHeadline("Senior Software Engineer");
profile.setLocation("San Francisco, CA");
profile.setSummary("Experienced engineer with 10+ years...");

// Add skills
profile.addSkill("Java");
profile.addSkill("Python");
profile.addSkill("System Design");

// Add experience
Profile.Experience exp = new Profile.Experience(
    "Senior Software Engineer",
    "TechCorp",
    LocalDate.of(2020, 1, 1)
);
exp.setCurrent(true);
exp.setDescription("Leading backend development team...");
profile.addExperience(exp);

// Add education
Profile.Education edu = new Profile.Education(
    "Stanford University",
    "Master of Science",
    "Computer Science"
);
edu.setStartDate(LocalDate.of(2015, 9, 1));
edu.setEndDate(LocalDate.of(2017, 6, 1));
profile.addEducation(edu);

linkedin.updateProfile(userId, profile);
```

### Get Profile
```java
Profile getProfile(String userId)
```

**Returns:** User's profile or `null` if user not found

## 🤝 Connection Management

### Send Connection Request
```java
Connection sendConnectionRequest(String senderId, String receiverId, String message)
```

**Parameters:**
- `senderId` - ID of user sending request
- `receiverId` - ID of user receiving request  
- `message` - Optional personalized message

**Returns:** `Connection` object with PENDING status

**Throws:** `IllegalArgumentException` if:
- Users don't exist
- Users are already connected
- Request already exists
- Sender and receiver are same user

**Example:**
```java
Connection connection = linkedin.sendConnectionRequest(
    aliceId,
    bobId,
    "Hi Bob! I'd love to connect and discuss product development."
);
```

### Accept Connection Request
```java
boolean acceptConnectionRequest(String connectionId)
```

**Returns:** `true` if accepted successfully

**Example:**
```java
// Get pending requests
List<Connection> pendingRequests = linkedin.getPendingConnectionRequests(userId);

// Accept first request
if (!pendingRequests.isEmpty()) {
    linkedin.acceptConnectionRequest(pendingRequests.get(0).getConnectionId());
}
```

### Decline Connection Request
```java
boolean declineConnectionRequest(String connectionId)
```

**Returns:** `true` if declined successfully

### Remove Connection
```java
boolean removeConnection(String userId1, String userId2)
```

**Returns:** `true` if connection removed successfully

### Get Connections
```java
List<User> getConnections(String userId)
```

**Returns:** List of connected users

**Example:**
```java
List<User> connections = linkedin.getConnections(userId);
System.out.println("You have " + connections.size() + " connections");

for (User connection : connections) {
    Profile profile = connection.getProfile();
    System.out.println("- " + profile.getFullName() + " (" + profile.getHeadline() + ")");
}
```

### Get Pending Connection Requests
```java
List<Connection> getPendingConnectionRequests(String userId)
```

**Returns:** List of pending connection requests

### Check Connection Status
```java
boolean areConnected(String userId1, String userId2)
```

**Returns:** `true` if users are connected

## 💬 Messaging

### Send Message
```java
Message sendMessage(String senderId, String receiverId, String content)
```

**Parameters:**
- `senderId` - ID of sender
- `receiverId` - ID of receiver
- `content` - Message content

**Returns:** `Message` object with SENT status

**Throws:** `IllegalArgumentException` if:
- Users are not connected
- Content is empty

**Example:**
```java
Message message = linkedin.sendMessage(
    aliceId,
    bobId,
    "Hey Bob! Thanks for connecting. Let's discuss the project next week."
);
```

### Get Conversation
```java
List<Message> getConversation(String userId1, String userId2)
```

**Returns:** List of messages between two users, sorted chronologically

**Example:**
```java
List<Message> conversation = linkedin.getConversation(aliceId, bobId);

for (Message msg : conversation) {
    User sender = linkedin.getUser(msg.getSenderId());
    System.out.println(sender.getProfile().getFirstName() + ": " + msg.getContent());
}
```

### Get Inbox
```java
List<Message> getInbox(String userId)
```

**Returns:** All received messages, sorted by most recent

### Get Sent Messages
```java
List<Message> getSentMessages(String userId)
```

**Returns:** All sent messages, sorted by most recent

### Mark Message as Read
```java
boolean markMessageAsRead(String messageId)
```

**Returns:** `true` if marked successfully

### Get Unread Message Count
```java
int getUnreadMessageCount(String userId)
```

**Returns:** Number of unread messages

**Example:**
```java
int unread = linkedin.getUnreadMessageCount(userId);
System.out.println("You have " + unread + " unread messages");
```

## 💼 Job Management

### Post a Job
```java
Job postJob(Job job)
```

**Parameters:**
- `job` - Job object created using Builder pattern

**Returns:** Posted job with generated ID

**Example:**
```java
Job job = new Job.Builder(
    employerId,
    "Senior Software Engineer",
    "We are looking for an experienced engineer to join our team...",
    "TechCorp"
)
    .location("San Francisco, CA")
    .jobType(JobType.FULL_TIME)
    .experienceLevel(ExperienceLevel.MID_SENIOR)
    .salaryRange("$120,000 - $180,000")
    .build();

// Add required skills
job.addRequiredSkill("Java");
job.addRequiredSkill("Spring Boot");
job.addRequiredSkill("Microservices");

Job postedJob = linkedin.postJob(job);
```

### Update Job
```java
boolean updateJob(String jobId, Job updatedJob)
```

**Returns:** `true` if updated successfully

### Delete Job
```java
boolean deleteJob(String jobId)
```

**Returns:** `true` if deleted (sets inactive) successfully

### Get Job
```java
Job getJob(String jobId)
```

**Returns:** Job object or `null` if not found

### Get Jobs by Employer
```java
List<Job> getJobsByEmployer(String employerId)
```

**Returns:** All jobs posted by employer

### Get All Active Jobs
```java
List<Job> getAllActiveJobs()
```

**Returns:** All active job postings

## 📝 Job Applications

### Apply for Job
```java
JobApplication applyForJob(JobApplication application)
```

**Parameters:**
- `application` - JobApplication object

**Returns:** Submitted application with generated ID

**Throws:** `IllegalArgumentException` if:
- Job doesn't exist or is inactive
- User already applied
- Invalid applicant ID

**Example:**
```java
JobApplication application = new JobApplication(
    jobId,
    applicantId,
    "I am very interested in this position. With 5+ years of experience in Java...",
    "https://example.com/resume.pdf"
);

JobApplication submitted = linkedin.applyForJob(application);
```

### Get Applications for Job
```java
List<JobApplication> getApplicationsForJob(String jobId)
```

**Returns:** All applications for a specific job

**Example:**
```java
List<JobApplication> applications = linkedin.getApplicationsForJob(jobId);
System.out.println("Received " + applications.size() + " applications");
```

### Get Applications by User
```java
List<JobApplication> getApplicationsByUser(String userId)
```

**Returns:** All applications submitted by a user

### Update Application Status
```java
boolean updateApplicationStatus(String applicationId, JobApplicationStatus status)
```

**Parameters:**
- `applicationId` - Application ID
- `status` - New status (PENDING, REVIEWING, SHORTLISTED, INTERVIEWED, ACCEPTED, REJECTED, WITHDRAWN)

**Returns:** `true` if updated successfully

**Example:**
```java
// Employer reviews application
linkedin.updateApplicationStatus(
    applicationId,
    JobApplicationStatus.REVIEWING
);

// Shortlist candidate
linkedin.updateApplicationStatus(
    applicationId,
    JobApplicationStatus.SHORTLISTED
);
```

## 🔍 Search

### Search Users
```java
List<User> searchUsers(String query, Map<String, Object> criteria)
```

**Parameters:**
- `query` - Search term (searches name, headline, skills, experience, location)
- `criteria` - Additional filters (optional)

**Supported Criteria:**
- `location` (String) - Filter by location
- `skill` (String) - Filter by specific skill

**Returns:** List of matching users, ranked by relevance

**Example:**
```java
// Basic search
List<User> users = linkedin.searchUsers("Software Engineer", new HashMap<>());

// Search with filters
Map<String, Object> criteria = new HashMap<>();
criteria.put("location", "San Francisco");
criteria.put("skill", "Java");

List<User> filteredUsers = linkedin.searchUsers("Engineer", criteria);

// Display results
for (User user : filteredUsers) {
    Profile profile = user.getProfile();
    System.out.println(profile.getFullName() + " - " + profile.getHeadline());
}
```

### Search Jobs
```java
List<Job> searchJobs(String query, Map<String, Object> criteria)
```

**Parameters:**
- `query` - Search term (searches title, company, location, description, skills)
- `criteria` - Additional filters (optional)

**Supported Criteria:**
- `location` (String) - Filter by location
- `jobType` (JobType) - Filter by job type
- `experienceLevel` (ExperienceLevel) - Filter by experience level
- `skill` (String) - Filter by required skill

**Returns:** List of matching jobs, ranked by relevance

**Example:**
```java
// Search for software engineer jobs
List<Job> jobs = linkedin.searchJobs("Software Engineer", new HashMap<>());

// Advanced search
Map<String, Object> criteria = new HashMap<>();
criteria.put("location", "San Francisco");
criteria.put("jobType", JobType.FULL_TIME);
criteria.put("experienceLevel", ExperienceLevel.MID_SENIOR);
criteria.put("skill", "Java");

List<Job> filteredJobs = linkedin.searchJobs("Engineer", criteria);

// Display results
for (Job job : filteredJobs) {
    System.out.println(job.getTitle() + " at " + job.getCompany());
    System.out.println("Location: " + job.getLocation());
    System.out.println("Type: " + job.getJobType());
    System.out.println("Required Skills: " + job.getRequiredSkills());
    System.out.println("---");
}
```

## 🔔 Notifications

### Register Notification Observer
```java
void registerNotificationObserver(NotificationObserver observer)
```

**Parameters:**
- `observer` - Observer implementation (ConsoleNotificationObserver, EmailNotificationObserver, etc.)

**Example:**
```java
// Console notifications
NotificationObserver consoleObserver = new ConsoleNotificationObserver(userId);
linkedin.registerNotificationObserver(consoleObserver);

// Email notifications
NotificationObserver emailObserver = new EmailNotificationObserver(
    userId,
    user.getEmail()
);
linkedin.registerNotificationObserver(emailObserver);
```

### Unregister Notification Observer
```java
void unregisterNotificationObserver(NotificationObserver observer)
```

### Get All Notifications
```java
List<Notification> getNotifications(String userId)
```

**Returns:** All notifications for user, sorted by most recent

### Get Unread Notifications
```java
List<Notification> getUnreadNotifications(String userId)
```

**Returns:** Only unread notifications

**Example:**
```java
List<Notification> unread = linkedin.getUnreadNotifications(userId);

for (Notification notif : unread) {
    System.out.println("[" + notif.getType() + "] " + notif.getTitle());
    System.out.println(notif.getMessage());
    
    // Mark as read
    linkedin.markNotificationAsRead(notif.getNotificationId());
}
```

### Mark Notification as Read
```java
boolean markNotificationAsRead(String notificationId)
```

**Returns:** `true` if marked successfully

### Get Unread Notification Count
```java
int getUnreadNotificationCount(String userId)
```

**Returns:** Number of unread notifications

## 📊 Notification Types

The system automatically sends notifications for:

| Event | Notification Type | Triggered When |
|-------|------------------|----------------|
| Welcome | CONNECTION_REQUEST | User registers |
| Connection Request | CONNECTION_REQUEST | Someone sends connection request |
| Connection Accepted | CONNECTION_ACCEPTED | Someone accepts your request |
| New Message | NEW_MESSAGE | Someone sends you a message |
| New Application | JOB_APPLICATION | Someone applies to your job |
| Application Update | JOB_APPLICATION | Application status changes |

## 🎯 Complete Workflow Example

```java
public class LinkedInWorkflow {
    public static void main(String[] args) {
        LinkedInSystem linkedin = LinkedInSystem.getInstance();
        
        // 1. Register users
        User employer = linkedin.register(
            "hr@techcorp.com",
            "password123",
            "Sarah",
            "Recruiter"
        );
        
        User candidate = linkedin.register(
            "john@example.com",
            "password456",
            "John",
            "Engineer"
        );
        
        // 2. Setup profiles
        Profile candidateProfile = candidate.getProfile();
        candidateProfile.setHeadline("Software Engineer");
        candidateProfile.addSkill("Java");
        candidateProfile.addSkill("Spring Boot");
        
        // 3. Register for notifications
        linkedin.registerNotificationObserver(
            new ConsoleNotificationObserver(candidate.getUserId())
        );
        
        // 4. Post a job
        Job job = new Job.Builder(
            employer.getUserId(),
            "Software Engineer",
            "Join our team...",
            "TechCorp"
        )
            .location("San Francisco")
            .jobType(JobType.FULL_TIME)
            .build();
        
        linkedin.postJob(job);
        
        // 5. Search and apply
        List<Job> jobs = linkedin.searchJobs("Software Engineer", new HashMap<>());
        
        if (!jobs.isEmpty()) {
            JobApplication application = new JobApplication(
                jobs.get(0).getJobId(),
                candidate.getUserId(),
                "I'm interested in this role...",
                "resume.pdf"
            );
            
            linkedin.applyForJob(application);
        }
        
        // 6. Employer reviews application
        List<JobApplication> applications = linkedin.getApplicationsForJob(job.getJobId());
        
        if (!applications.isEmpty()) {
            linkedin.updateApplicationStatus(
                applications.get(0).getApplicationId(),
                JobApplicationStatus.REVIEWING
            );
        }
    }
}
```

## 🔒 Security Considerations

### Current Implementation
- Password hashing (simple for demo - use BCrypt in production)
- Session management
- Connection-based messaging (privacy)
- Input validation

### Production Recommendations
```java
// Use BCrypt for password hashing
BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
String hashedPassword = encoder.encode(plainPassword);

// Use JWT for authentication
String token = jwtTokenProvider.createToken(user.getEmail());

// Add rate limiting
@RateLimit(requests = 100, per = "1 minute")
public List<Job> searchJobs(...) { }
```

## 📈 Performance Tips

1. **Batch Operations**: When processing multiple items, use batch operations
2. **Pagination**: For large result sets, implement pagination
3. **Caching**: Cache frequently accessed data (user profiles, job listings)
4. **Async Processing**: Use async for notifications and non-critical operations
5. **Connection Pooling**: Use connection pooling for database access

## 🐛 Error Handling

Common exceptions and how to handle them:

```java
try {
    User user = linkedin.register(email, password, firstName, lastName);
} catch (IllegalArgumentException e) {
    // Email already exists or password too weak
    System.err.println("Registration failed: " + e.getMessage());
}

try {
    Message message = linkedin.sendMessage(senderId, receiverId, content);
} catch (IllegalArgumentException e) {
    // Users not connected or content empty
    System.err.println("Message not sent: " + e.getMessage());
}

try {
    JobApplication app = linkedin.applyForJob(application);
} catch (IllegalArgumentException e) {
    // Already applied, job inactive, or invalid data
    System.err.println("Application failed: " + e.getMessage());
}
```

---

**For more examples, see the `Main.java` demo application.**

