package org.example.PhotoSharingApp;

import org.example.PhotoSharingApp.enums.PrivacyLevel;
import org.example.PhotoSharingApp.exception.UnauthorizedException;
import org.example.PhotoSharingApp.model.Photo;
import org.example.PhotoSharingApp.model.User;
import org.example.PhotoSharingApp.system.PhotoSharingApp;

import java.util.List;
import java.util.Set;

/**
 * Demo class showcasing the Photo Sharing Application features.
 * Demonstrates all major functionality including user management, photo operations,
 * privacy settings, feed generation, and notifications.
 */
public class Main {
    public static void main(String[] args) {
        // Get singleton instance of the application
        PhotoSharingApp app = PhotoSharingApp.getInstance();
        
        System.out.println("========================================");
        System.out.println("   PHOTO SHARING APP - DEMO");
        System.out.println("========================================\n");
        
        // 1. Register users
        System.out.println("=== 1. Registering Users ===");
        User alice = new User.Builder("user1")
            .username("alice")
            .email("alice@example.com")
            .build();
        
        User bob = new User.Builder("user2")
            .username("bob")
            .email("bob@example.com")
            .build();
        
        User charlie = new User.Builder("user3")
            .username("charlie")
            .email("charlie@example.com")
            .build();
        
        User david = new User.Builder("user4")
            .username("david")
            .email("david@example.com")
            .build();
        
        app.registerUser(alice);
        app.registerUser(bob);
        app.registerUser(charlie);
        app.registerUser(david);
        
        // 2. Users follow each other
        System.out.println("\n=== 2. Following Users ===");
        app.followUser("user1", "user2"); // Alice follows Bob
        app.followUser("user1", "user3"); // Alice follows Charlie
        app.followUser("user2", "user1"); // Bob follows Alice
        app.followUser("user3", "user1"); // Charlie follows Alice
        app.followUser("user4", "user1"); // David follows Alice
        
        // 3. Users upload photos with different privacy settings
        System.out.println("\n=== 3. Uploading Photos ===");
        
        // Alice's public photo
        Photo alicePhoto1 = new Photo.Builder("photo1", "user1", "https://example.com/photo1.jpg")
            .caption("Beautiful sunset at the beach!")
            .privacyLevel(PrivacyLevel.PUBLIC)
            .addTag("sunset")
            .addTag("beach")
            .addTag("nature")
            .build();
        app.uploadPhoto(alicePhoto1, "user1");
        
        // Alice's followers-only photo
        Photo alicePhoto2 = new Photo.Builder("photo2", "user1", "https://example.com/photo2.jpg")
            .caption("My cozy home office setup")
            .privacyLevel(PrivacyLevel.FOLLOWERS_ONLY)
            .addTag("office")
            .addTag("workspace")
            .build();
        app.uploadPhoto(alicePhoto2, "user1");
        
        // Alice's private photo
        Photo alicePhoto3 = new Photo.Builder("photo3", "user1", "https://example.com/photo3.jpg")
            .caption("Personal diary entry")
            .privacyLevel(PrivacyLevel.PRIVATE)
            .addTag("personal")
            .build();
        app.uploadPhoto(alicePhoto3, "user1");
        
        // Bob's public photo
        Photo bobPhoto1 = new Photo.Builder("photo4", "user2", "https://example.com/photo4.jpg")
            .caption("Amazing mountain hike today!")
            .privacyLevel(PrivacyLevel.PUBLIC)
            .addTag("mountains")
            .addTag("hiking")
            .addTag("adventure")
            .build();
        app.uploadPhoto(bobPhoto1, "user2");
        
        // Charlie's public photo
        Photo charliePhoto1 = new Photo.Builder("photo5", "user3", "https://example.com/photo5.jpg")
            .caption("Delicious homemade pasta!")
            .privacyLevel(PrivacyLevel.PUBLIC)
            .addTag("food")
            .addTag("cooking")
            .build();
        app.uploadPhoto(charliePhoto1, "user3");
        
        // 4. Users like photos
        System.out.println("\n=== 4. Liking Photos ===");
        app.likePhoto("photo1", "user2"); // Bob likes Alice's photo
        app.likePhoto("photo1", "user3"); // Charlie likes Alice's photo
        app.likePhoto("photo1", "user4"); // David likes Alice's photo
        app.likePhoto("photo4", "user1"); // Alice likes Bob's photo
        app.likePhoto("photo5", "user1"); // Alice likes Charlie's photo
        
        // 5. Users comment on photos
        System.out.println("\n=== 5. Commenting on Photos ===");
        app.commentOnPhoto("photo1", "user2", "Absolutely stunning!");
        app.commentOnPhoto("photo1", "user3", "Wish I was there!");
        app.commentOnPhoto("photo4", "user1", "Great shot! Love the view.");
        
        // 6. View notifications
        System.out.println("\n=== 6. Viewing Notifications ===");
        app.showNotifications("user1");
        app.showNotifications("user2");
        
        // 7. Search photos by tag
        System.out.println("\n=== 7. Searching Photos by Tag ===");
        System.out.println("\nSearching for #sunset as Alice:");
        List<Photo> sunsetPhotos = app.searchByTag("sunset", "user1");
        displayPhotos(sunsetPhotos);
        
        System.out.println("\nSearching for #food as Bob:");
        List<Photo> foodPhotos = app.searchByTag("food", "user2");
        displayPhotos(foodPhotos);
        
        // 8. View user's photos
        System.out.println("\n=== 8. Viewing User Photos ===");
        System.out.println("\nAlice's photos (viewed by Bob - a follower):");
        List<Photo> alicePhotosForBob = app.getUserPhotos("user1", "user2");
        displayPhotos(alicePhotosForBob);
        
        System.out.println("\nAlice's photos (viewed by David - NOT a follower of Alice, but Alice follows David):");
        List<Photo> alicePhotosForDavid = app.getUserPhotos("user1", "user4");
        displayPhotos(alicePhotosForDavid);
        
        // 9. Generate personalized feed
        System.out.println("\n=== 9. Generating Personalized Feed ===");
        System.out.println("\nAlice's feed (photos from users she follows):");
        List<Photo> aliceFeed = app.getFeed("user1", 10);
        displayPhotos(aliceFeed);
        
        System.out.println("\nBob's feed (photos from users he follows):");
        List<Photo> bobFeed = app.getFeed("user2", 10);
        displayPhotos(bobFeed);
        
        // 10. Generate explore feed (popular photos)
        System.out.println("\n=== 10. Generating Explore Feed ===");
        System.out.println("\nExplore feed for Charlie (popular public photos):");
        List<Photo> exploreFeed = app.getExploreFeed("user3", 5);
        displayPhotos(exploreFeed);
        
        // 11. Test privacy controls
        System.out.println("\n=== 11. Testing Privacy Controls ===");
        
        // Try to view Alice's private photo as Bob (should fail)
        System.out.println("\nBob trying to view Alice's private photo:");
        try {
            Photo privatePhoto = app.getPhoto("user2", "photo3");
            System.out.println("Successfully viewed: " + privatePhoto);
        } catch (UnauthorizedException e) {
            System.out.println("[X] Access Denied: " + e.getMessage());
        }
        
        // Alice viewing her own private photo (should succeed)
        System.out.println("\nAlice viewing her own private photo:");
        try {
            Photo privatePhoto = app.getPhoto("user1", "photo3");
            System.out.println("[OK] Successfully viewed: " + privatePhoto);
        } catch (UnauthorizedException e) {
            System.out.println("Access Denied: " + e.getMessage());
        }
        
        // 12. Unfollow functionality
        System.out.println("\n=== 12. Unfollowing Users ===");
        app.unfollowUser("user1", "user3"); // Alice unfollows Charlie
        
        System.out.println("\nAlice's feed after unfollowing Charlie:");
        List<Photo> aliceFeedAfterUnfollow = app.getFeed("user1", 10);
        displayPhotos(aliceFeedAfterUnfollow);
        
        // 13. Display final statistics
        System.out.println("\n=== 13. Final Statistics ===");
        displayUserStats(app, alice);
        displayUserStats(app, bob);
        displayUserStats(app, charlie);
        displayUserStats(app, david);
        
        System.out.println("\n========================================");
        System.out.println("   DEMO COMPLETED SUCCESSFULLY!");
        System.out.println("========================================");
    }
    
    /**
     * Helper method to display a list of photos.
     */
    private static void displayPhotos(List<Photo> photos) {
        if (photos.isEmpty()) {
            System.out.println("  (no photos)");
            return;
        }
        for (Photo photo : photos) {
            System.out.println("  • " + photo);
        }
    }
    
    /**
     * Helper method to display user statistics.
     */
    private static void displayUserStats(PhotoSharingApp app, User user) {
        User currentUser = app.getUser(user.getUserId());
        System.out.println("\n" + currentUser.getUsername() + "'s Stats:");
        System.out.println("  - Followers: " + currentUser.getFollowers().size());
        System.out.println("  - Following: " + currentUser.getFollowing().size());
        System.out.println("  - Photos: " + currentUser.getPhotoIds().size());
        System.out.println("  - Notifications: " + currentUser.getNotifications().size());
        
        // Display photo details
        for (String photoId : currentUser.getPhotoIds()) {
            List<Photo> photos = app.getUserPhotos(user.getUserId(), user.getUserId());
            for (Photo photo : photos) {
                if (photo.getPhotoId().equals(photoId)) {
                    System.out.println("    Photo: " + photo.getCaption() + 
                        " [Likes: " + photo.getLikeCount() + 
                        ", Comments: " + photo.getCommentCount() + "]");
                }
            }
        }
    }
}
