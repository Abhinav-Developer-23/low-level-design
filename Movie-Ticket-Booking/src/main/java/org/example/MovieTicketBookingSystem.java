package org.example;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

/**
 * Movie Ticket Booking System - Complete Low Level Design
 * 
 * Design Patterns Used:
 * 1. Singleton Pattern - BookingSystem (thread-safe)
 * 2. Factory Pattern - SeatFactory
 * 3. Strategy Pattern - SearchStrategy, PaymentStrategy
 * 4. Observer Pattern - BookingObserver for notifications
 * 5. Builder Pattern - Movie, Theatre builders
 * 
 * SOLID Principles:
 * - Single Responsibility: Each class has one clear purpose
 * - Open/Closed: Extensible via interfaces (SearchStrategy, PaymentStrategy)
 * - Liskov Substitution: Seat hierarchy is substitutable
 * - Interface Segregation: Small, focused interfaces
 * - Dependency Inversion: Depends on abstractions (interfaces)
 */
public class MovieTicketBookingSystem {

    // ==================== ENUMS ====================
    
    /**
     * Enum for different seat types in a theatre
     */
    enum SeatType {
        REGULAR(100.0),
        PREMIUM(200.0),
        VIP(300.0),
        RECLINER(400.0);
        
        private final double basePrice;
        
        SeatType(double basePrice) {
            this.basePrice = basePrice;
        }
        
        public double getBasePrice() {
            return basePrice;
        }
    }
    
    /**
     * Enum for seat availability status
     */
    enum SeatStatus {
        AVAILABLE,
        RESERVED,    // Temporarily reserved during booking process
        BOOKED,      // Confirmed booking
        BLOCKED      // Blocked by admin
    }
    
    /**
     * Enum for booking status
     */
    enum BookingStatus {
        PENDING,
        CONFIRMED,
        CANCELLED,
        FAILED
    }
    
    /**
     * Enum for payment status
     */
    enum PaymentStatus {
        PENDING,
        COMPLETED,
        FAILED,
        REFUNDED
    }
    
    /**
     * Enum for payment methods
     */
    enum PaymentMethod {
        CREDIT_CARD,
        DEBIT_CARD,
        UPI,
        NET_BANKING,
        WALLET
    }
    
    /**
     * Enum for movie languages
     */
    enum Language {
        ENGLISH,
        HINDI,
        TAMIL,
        TELUGU,
        KANNADA,
        MALAYALAM
    }
    
    /**
     * Enum for movie genres
     */
    enum Genre {
        ACTION,
        COMEDY,
        DRAMA,
        HORROR,
        THRILLER,
        ROMANCE,
        SCI_FI,
        DOCUMENTARY
    }
    
    /**
     * Enum for user roles
     */
    enum UserRole {
        CUSTOMER,
        ADMIN,
        THEATRE_OWNER
    }
    
    // ==================== INTERFACES ====================
    
    /**
     * Interface for payment strategies (Strategy Pattern)
     */
    interface PaymentStrategy {
        boolean processPayment(double amount, String paymentDetails);
        boolean refund(double amount, String transactionId);
    }
    
    /**
     * Interface for search strategies (Strategy Pattern)
     */
    interface SearchStrategy {
        List<Movie> search(String keyword, List<Movie> movies);
    }
    
    /**
     * Observer interface for booking notifications (Observer Pattern)
     */
    interface BookingObserver {
        void onBookingConfirmed(Booking booking);
        void onBookingCancelled(Booking booking);
    }
    
    /**
     * Interface for pricing strategy (Strategy Pattern)
     */
    interface PricingStrategy {
        double calculatePrice(Seat seat, Show show);
    }
    
    // ==================== MODEL CLASSES ====================
    
    /**
     * Base User class - Demonstrates inheritance and abstraction
     */
    static abstract class User {
        private final String userId;
        private String name;
        private String email;
        private String phoneNumber;
        private final UserRole role;
        
        public User(String userId, String name, String email, String phoneNumber, UserRole role) {
            this.userId = userId;
            this.name = name;
            this.email = email;
            this.phoneNumber = phoneNumber;
            this.role = role;
        }
        
        // Getters
        public String getUserId() { return userId; }
        public String getName() { return name; }
        public String getEmail() { return email; }
        public String getPhoneNumber() { return phoneNumber; }
        public UserRole getRole() { return role; }
        
        // Setters
        public void setName(String name) { this.name = name; }
        public void setEmail(String email) { this.email = email; }
        public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
        
        // Abstract method - to be implemented by subclasses
        public abstract void displayDashboard();
    }
    
    /**
     * Customer class - concrete implementation of User
     */
    static class Customer extends User {
        private List<Booking> bookingHistory;
        
        public Customer(String userId, String name, String email, String phoneNumber) {
            super(userId, name, email, phoneNumber, UserRole.CUSTOMER);
            this.bookingHistory = new ArrayList<>();
        }
        
        public void addBooking(Booking booking) {
            bookingHistory.add(booking);
        }
        
        public List<Booking> getBookingHistory() {
            return new ArrayList<>(bookingHistory);
        }
        
        @Override
        public void displayDashboard() {
            System.out.println("=== Customer Dashboard ===");
            System.out.println("Name: " + getName());
            System.out.println("Total Bookings: " + bookingHistory.size());
        }
    }
    
    /**
     * Admin class - concrete implementation of User
     */
    static class Admin extends User {
        private List<String> permissions;
        
        public Admin(String userId, String name, String email, String phoneNumber) {
            super(userId, name, email, phoneNumber, UserRole.ADMIN);
            this.permissions = new ArrayList<>();
            initializePermissions();
        }
        
        private void initializePermissions() {
            permissions.add("ADD_MOVIE");
            permissions.add("ADD_THEATRE");
            permissions.add("ADD_SHOW");
            permissions.add("BLOCK_SEAT");
            permissions.add("VIEW_REPORTS");
        }
        
        public boolean hasPermission(String permission) {
            return permissions.contains(permission);
        }
        
        @Override
        public void displayDashboard() {
            System.out.println("=== Admin Dashboard ===");
            System.out.println("Name: " + getName());
            System.out.println("Permissions: " + String.join(", ", permissions));
        }
    }
    
    /**
     * Movie class - uses Builder Pattern for flexible construction
     */
    static class Movie {
        private final String movieId;
        private final String title;
        private final String description;
        private final int durationMinutes;
        private final Language language;
        private final Set<Genre> genres;
        private final LocalDateTime releaseDate;
        private final String director;
        private final List<String> cast;
        private double rating;
        
        private Movie(Builder builder) {
            this.movieId = builder.movieId;
            this.title = builder.title;
            this.description = builder.description;
            this.durationMinutes = builder.durationMinutes;
            this.language = builder.language;
            this.genres = builder.genres;
            this.releaseDate = builder.releaseDate;
            this.director = builder.director;
            this.cast = builder.cast;
            this.rating = builder.rating;
        }
        
        // Getters
        public String getMovieId() { return movieId; }
        public String getTitle() { return title; }
        public String getDescription() { return description; }
        public int getDurationMinutes() { return durationMinutes; }
        public Language getLanguage() { return language; }
        public Set<Genre> getGenres() { return new HashSet<>(genres); }
        public LocalDateTime getReleaseDate() { return releaseDate; }
        public String getDirector() { return director; }
        public List<String> getCast() { return new ArrayList<>(cast); }
        public double getRating() { return rating; }
        
        public void setRating(double rating) {
            this.rating = rating;
        }
        
        /**
         * Builder class for Movie (Builder Pattern)
         */
        static class Builder {
            private final String movieId;
            private final String title;
            private String description = "";
            private int durationMinutes;
            private Language language;
            private Set<Genre> genres = new HashSet<>();
            private LocalDateTime releaseDate;
            private String director = "";
            private List<String> cast = new ArrayList<>();
            private double rating = 0.0;
            
            public Builder(String movieId, String title) {
                this.movieId = movieId;
                this.title = title;
            }
            
            public Builder description(String description) {
                this.description = description;
                return this;
            }
            
            public Builder durationMinutes(int durationMinutes) {
                this.durationMinutes = durationMinutes;
                return this;
            }
            
            public Builder language(Language language) {
                this.language = language;
                return this;
            }
            
            public Builder addGenre(Genre genre) {
                this.genres.add(genre);
                return this;
            }
            
            public Builder releaseDate(LocalDateTime releaseDate) {
                this.releaseDate = releaseDate;
                return this;
            }
            
            public Builder director(String director) {
                this.director = director;
                return this;
            }
            
            public Builder addCast(String actor) {
                this.cast.add(actor);
                return this;
            }
            
            public Builder rating(double rating) {
                this.rating = rating;
                return this;
            }
            
            public Movie build() {
                return new Movie(this);
            }
        }
        
        @Override
        public String toString() {
            return String.format("Movie[%s: %s (%s), %d mins]", 
                movieId, title, language, durationMinutes);
        }
    }
    
    /**
     * City class - represents a city with multiple theatres
     */
    static class City {
        private final String cityId;
        private final String name;
        private final String state;
        private final List<Theatre> theatres;
        
        public City(String cityId, String name, String state) {
            this.cityId = cityId;
            this.name = name;
            this.state = state;
            this.theatres = new ArrayList<>();
        }
        
        public void addTheatre(Theatre theatre) {
            theatres.add(theatre);
        }
        
        public void removeTheatre(Theatre theatre) {
            theatres.remove(theatre);
        }
        
        // Getters
        public String getCityId() { return cityId; }
        public String getName() { return name; }
        public String getState() { return state; }
        public List<Theatre> getTheatres() { return new ArrayList<>(theatres); }
        
        @Override
        public String toString() {
            return String.format("City[%s: %s, %s]", cityId, name, state);
        }
    }
    
    /**
     * Theatre class - represents a cinema hall
     */
    static class Theatre {
        private final String theatreId;
        private final String name;
        private final String address;
        private final City city;
        private final List<Screen> screens;
        
        public Theatre(String theatreId, String name, String address, City city) {
            this.theatreId = theatreId;
            this.name = name;
            this.address = address;
            this.city = city;
            this.screens = new ArrayList<>();
        }
        
        public void addScreen(Screen screen) {
            screens.add(screen);
        }
        
        // Getters
        public String getTheatreId() { return theatreId; }
        public String getName() { return name; }
        public String getAddress() { return address; }
        public City getCity() { return city; }
        public List<Screen> getScreens() { return new ArrayList<>(screens); }
        
        @Override
        public String toString() {
            return String.format("Theatre[%s: %s, %s]", theatreId, name, city.getName());
        }
    }
    
    /**
     * Screen class - represents a screen/auditorium in a theatre
     */
    static class Screen {
        private final String screenId;
        private final String name;
        private final Theatre theatre;
        private final List<Seat> seats;
        private final int totalRows;
        private final int seatsPerRow;
        
        public Screen(String screenId, String name, Theatre theatre, int totalRows, int seatsPerRow) {
            this.screenId = screenId;
            this.name = name;
            this.theatre = theatre;
            this.totalRows = totalRows;
            this.seatsPerRow = seatsPerRow;
            this.seats = new ArrayList<>();
            initializeSeats();
        }
        
        /**
         * Initialize seats with different types based on rows
         */
        private void initializeSeats() {
            for (int row = 1; row <= totalRows; row++) {
                for (int seatNum = 1; seatNum <= seatsPerRow; seatNum++) {
                    SeatType type;
                    // Last 2 rows are RECLINER
                    if (row > totalRows - 2) {
                        type = SeatType.RECLINER;
                    }
                    // Next 3 rows are VIP
                    else if (row > totalRows - 5) {
                        type = SeatType.VIP;
                    }
                    // Next 5 rows are PREMIUM
                    else if (row > totalRows - 10) {
                        type = SeatType.PREMIUM;
                    }
                    // Rest are REGULAR
                    else {
                        type = SeatType.REGULAR;
                    }
                    
                    String seatId = String.format("%s-%s%d", screenId, (char)('A' + row - 1), seatNum);
                    Seat seat = SeatFactory.createSeat(seatId, row, seatNum, type);
                    seats.add(seat);
                }
            }
        }
        
        // Getters
        public String getScreenId() { return screenId; }
        public String getName() { return name; }
        public Theatre getTheatre() { return theatre; }
        public List<Seat> getSeats() { return new ArrayList<>(seats); }
        public int getTotalRows() { return totalRows; }
        public int getSeatsPerRow() { return seatsPerRow; }
        
        @Override
        public String toString() {
            return String.format("Screen[%s: %s, %d seats]", 
                screenId, name, seats.size());
        }
    }
    
    /**
     * Abstract Seat class - demonstrates abstraction and inheritance
     */
    static abstract class Seat {
        private final String seatId;
        private final int rowNumber;
        private final int seatNumber;
        private final SeatType type;
        
        public Seat(String seatId, int rowNumber, int seatNumber, SeatType type) {
            this.seatId = seatId;
            this.rowNumber = rowNumber;
            this.seatNumber = seatNumber;
            this.type = type;
        }
        
        // Getters
        public String getSeatId() { return seatId; }
        public int getRowNumber() { return rowNumber; }
        public int getSeatNumber() { return seatNumber; }
        public SeatType getType() { return type; }
        
        // Abstract method for getting price
        public abstract double getBasePrice();
        
        @Override
        public String toString() {
            return String.format("Seat[%s: Row %d, Seat %d, %s]", 
                seatId, rowNumber, seatNumber, type);
        }
        
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Seat)) return false;
            Seat seat = (Seat) o;
            return seatId.equals(seat.seatId);
        }
        
        @Override
        public int hashCode() {
            return Objects.hash(seatId);
        }
    }
    
    /**
     * Concrete Seat implementations
     */
    static class RegularSeat extends Seat {
        public RegularSeat(String seatId, int rowNumber, int seatNumber) {
            super(seatId, rowNumber, seatNumber, SeatType.REGULAR);
        }
        
        @Override
        public double getBasePrice() {
            return SeatType.REGULAR.getBasePrice();
        }
    }
    
    static class PremiumSeat extends Seat {
        public PremiumSeat(String seatId, int rowNumber, int seatNumber) {
            super(seatId, rowNumber, seatNumber, SeatType.PREMIUM);
        }
        
        @Override
        public double getBasePrice() {
            return SeatType.PREMIUM.getBasePrice();
        }
    }
    
    static class VIPSeat extends Seat {
        public VIPSeat(String seatId, int rowNumber, int seatNumber) {
            super(seatId, rowNumber, seatNumber, SeatType.VIP);
        }
        
        @Override
        public double getBasePrice() {
            return SeatType.VIP.getBasePrice();
        }
    }
    
    static class ReclinerSeat extends Seat {
        public ReclinerSeat(String seatId, int rowNumber, int seatNumber) {
            super(seatId, rowNumber, seatNumber, SeatType.RECLINER);
        }
        
        @Override
        public double getBasePrice() {
            return SeatType.RECLINER.getBasePrice();
        }
    }
    
    /**
     * Factory class for creating seats (Factory Pattern)
     */
    static class SeatFactory {
        public static Seat createSeat(String seatId, int rowNumber, int seatNumber, SeatType type) {
            switch (type) {
                case REGULAR:
                    return new RegularSeat(seatId, rowNumber, seatNumber);
                case PREMIUM:
                    return new PremiumSeat(seatId, rowNumber, seatNumber);
                case VIP:
                    return new VIPSeat(seatId, rowNumber, seatNumber);
                case RECLINER:
                    return new ReclinerSeat(seatId, rowNumber, seatNumber);
                default:
                    throw new IllegalArgumentException("Invalid seat type: " + type);
            }
        }
    }
    
    /**
     * Show class - represents a movie screening
     */
    static class Show {
        private final String showId;
        private final Movie movie;
        private final Screen screen;
        private final LocalDateTime startTime;
        private final LocalDateTime endTime;
        private final Map<String, SeatStatus> seatAvailability; // seatId -> status
        private final ReentrantLock bookingLock; // For thread-safe booking
        
        public Show(String showId, Movie movie, Screen screen, LocalDateTime startTime) {
            this.showId = showId;
            this.movie = movie;
            this.screen = screen;
            this.startTime = startTime;
            this.endTime = startTime.plusMinutes(movie.getDurationMinutes());
            this.seatAvailability = new ConcurrentHashMap<>();
            this.bookingLock = new ReentrantLock();
            initializeSeatAvailability();
        }
        
        /**
         * Initialize all seats as available
         */
        private void initializeSeatAvailability() {
            for (Seat seat : screen.getSeats()) {
                seatAvailability.put(seat.getSeatId(), SeatStatus.AVAILABLE);
            }
        }
        
        /**
         * Check if seats are available (thread-safe)
         */
        public boolean areSeatsAvailable(List<String> seatIds) {
            bookingLock.lock();
            try {
                for (String seatId : seatIds) {
                    SeatStatus status = seatAvailability.get(seatId);
                    if (status != SeatStatus.AVAILABLE) {
                        return false;
                    }
                }
                return true;
            } finally {
                bookingLock.unlock();
            }
        }
        
        /**
         * Reserve seats temporarily (thread-safe)
         */
        public boolean reserveSeats(List<String> seatIds) {
            bookingLock.lock();
            try {
                if (!areSeatsAvailable(seatIds)) {
                    return false;
                }
                for (String seatId : seatIds) {
                    seatAvailability.put(seatId, SeatStatus.RESERVED);
                }
                return true;
            } finally {
                bookingLock.unlock();
            }
        }
        
        /**
         * Confirm seat booking (thread-safe)
         */
        public void confirmSeats(List<String> seatIds) {
            bookingLock.lock();
            try {
                for (String seatId : seatIds) {
                    seatAvailability.put(seatId, SeatStatus.BOOKED);
                }
            } finally {
                bookingLock.unlock();
            }
        }
        
        /**
         * Release seats (for cancellation) (thread-safe)
         */
        public void releaseSeats(List<String> seatIds) {
            bookingLock.lock();
            try {
                for (String seatId : seatIds) {
                    seatAvailability.put(seatId, SeatStatus.AVAILABLE);
                }
            } finally {
                bookingLock.unlock();
            }
        }
        
        /**
         * Get available seats
         */
        public List<Seat> getAvailableSeats() {
            return screen.getSeats().stream()
                .filter(seat -> seatAvailability.get(seat.getSeatId()) == SeatStatus.AVAILABLE)
                .collect(Collectors.toList());
        }
        
        /**
         * Get seat status
         */
        public SeatStatus getSeatStatus(String seatId) {
            return seatAvailability.get(seatId);
        }
        
        // Getters
        public String getShowId() { return showId; }
        public Movie getMovie() { return movie; }
        public Screen getScreen() { return screen; }
        public LocalDateTime getStartTime() { return startTime; }
        public LocalDateTime getEndTime() { return endTime; }
        
        @Override
        public String toString() {
            return String.format("Show[%s: %s at %s, %s]", 
                showId, movie.getTitle(), screen.getName(), startTime);
        }
    }
    
    /**
     * Booking class - represents a ticket booking
     */
    static class Booking {
        private final String bookingId;
        private final Customer customer;
        private final Show show;
        private final List<Seat> seats;
        private final LocalDateTime bookingTime;
        private BookingStatus status;
        private Payment payment;
        private final double totalAmount;
        
        public Booking(String bookingId, Customer customer, Show show, List<Seat> seats, double totalAmount) {
            this.bookingId = bookingId;
            this.customer = customer;
            this.show = show;
            this.seats = new ArrayList<>(seats);
            this.bookingTime = LocalDateTime.now();
            this.status = BookingStatus.PENDING;
            this.totalAmount = totalAmount;
        }
        
        public void confirmBooking() {
            this.status = BookingStatus.CONFIRMED;
        }
        
        public void cancelBooking() {
            this.status = BookingStatus.CANCELLED;
        }
        
        public void setPayment(Payment payment) {
            this.payment = payment;
        }
        
        // Getters
        public String getBookingId() { return bookingId; }
        public Customer getCustomer() { return customer; }
        public Show getShow() { return show; }
        public List<Seat> getSeats() { return new ArrayList<>(seats); }
        public LocalDateTime getBookingTime() { return bookingTime; }
        public BookingStatus getStatus() { return status; }
        public Payment getPayment() { return payment; }
        public double getTotalAmount() { return totalAmount; }
        
        @Override
        public String toString() {
            return String.format("Booking[%s: %s, %d seats, Rs.%.2f, %s]", 
                bookingId, show.getMovie().getTitle(), seats.size(), totalAmount, status);
        }
    }
    
    /**
     * Payment class - represents a payment transaction
     */
    static class Payment {
        private final String paymentId;
        private final Booking booking;
        private final double amount;
        private final PaymentMethod method;
        private PaymentStatus status;
        private final LocalDateTime paymentTime;
        private String transactionId;
        
        public Payment(String paymentId, Booking booking, double amount, PaymentMethod method) {
            this.paymentId = paymentId;
            this.booking = booking;
            this.amount = amount;
            this.method = method;
            this.status = PaymentStatus.PENDING;
            this.paymentTime = LocalDateTime.now();
        }
        
        public void markCompleted(String transactionId) {
            this.status = PaymentStatus.COMPLETED;
            this.transactionId = transactionId;
        }
        
        public void markFailed() {
            this.status = PaymentStatus.FAILED;
        }
        
        public void markRefunded() {
            this.status = PaymentStatus.REFUNDED;
        }
        
        // Getters
        public String getPaymentId() { return paymentId; }
        public Booking getBooking() { return booking; }
        public double getAmount() { return amount; }
        public PaymentMethod getMethod() { return method; }
        public PaymentStatus getStatus() { return status; }
        public LocalDateTime getPaymentTime() { return paymentTime; }
        public String getTransactionId() { return transactionId; }
        
        @Override
        public String toString() {
            return String.format("Payment[%s: Rs.%.2f, %s, %s]", 
                paymentId, amount, method, status);
        }
    }
    
    // ==================== STRATEGY IMPLEMENTATIONS ====================
    
    /**
     * Credit Card Payment Strategy
     */
    static class CreditCardPayment implements PaymentStrategy {
        @Override
        public boolean processPayment(double amount, String paymentDetails) {
            System.out.println("Processing Credit Card payment of Rs." + amount);
            // Simulate payment processing
            return true;
        }
        
        @Override
        public boolean refund(double amount, String transactionId) {
            System.out.println("Refunding Rs." + amount + " to Credit Card (TxnId: " + transactionId + ")");
            return true;
        }
    }
    
    /**
     * UPI Payment Strategy
     */
    static class UPIPayment implements PaymentStrategy {
        @Override
        public boolean processPayment(double amount, String paymentDetails) {
            System.out.println("Processing UPI payment of Rs." + amount);
            // Simulate payment processing
            return true;
        }
        
        @Override
        public boolean refund(double amount, String transactionId) {
            System.out.println("Refunding Rs." + amount + " to UPI (TxnId: " + transactionId + ")");
            return true;
        }
    }
    
    /**
     * Net Banking Payment Strategy
     */
    static class NetBankingPayment implements PaymentStrategy {
        @Override
        public boolean processPayment(double amount, String paymentDetails) {
            System.out.println("Processing Net Banking payment of Rs." + amount);
            // Simulate payment processing
            return true;
        }
        
        @Override
        public boolean refund(double amount, String transactionId) {
            System.out.println("Refunding Rs." + amount + " via Net Banking (TxnId: " + transactionId + ")");
            return true;
        }
    }
    
    /**
     * Search by movie name strategy
     */
    static class MovieNameSearchStrategy implements SearchStrategy {
        @Override
        public List<Movie> search(String keyword, List<Movie> movies) {
            return movies.stream()
                .filter(movie -> movie.getTitle().toLowerCase().contains(keyword.toLowerCase()))
                .collect(Collectors.toList());
        }
    }
    
    /**
     * Search by language strategy
     */
    static class LanguageSearchStrategy implements SearchStrategy {
        @Override
        public List<Movie> search(String keyword, List<Movie> movies) {
            try {
                Language language = Language.valueOf(keyword.toUpperCase());
                return movies.stream()
                    .filter(movie -> movie.getLanguage() == language)
                    .collect(Collectors.toList());
            } catch (IllegalArgumentException e) {
                return new ArrayList<>();
            }
        }
    }
    
    /**
     * Search by genre strategy
     */
    static class GenreSearchStrategy implements SearchStrategy {
        @Override
        public List<Movie> search(String keyword, List<Movie> movies) {
            try {
                Genre genre = Genre.valueOf(keyword.toUpperCase());
                return movies.stream()
                    .filter(movie -> movie.getGenres().contains(genre))
                    .collect(Collectors.toList());
            } catch (IllegalArgumentException e) {
                return new ArrayList<>();
            }
        }
    }
    
    /**
     * Standard pricing strategy
     */
    static class StandardPricingStrategy implements PricingStrategy {
        @Override
        public double calculatePrice(Seat seat, Show show) {
            return seat.getBasePrice();
        }
    }
    
    /**
     * Weekend pricing strategy (20% markup on weekends)
     */
    static class WeekendPricingStrategy implements PricingStrategy {
        @Override
        public double calculatePrice(Seat seat, Show show) {
            double basePrice = seat.getBasePrice();
            // Check if show is on weekend
            int dayOfWeek = show.getStartTime().getDayOfWeek().getValue();
            if (dayOfWeek >= 6) { // Saturday or Sunday
                return basePrice * 1.2; // 20% markup
            }
            return basePrice;
        }
    }
    
    // ==================== OBSERVER IMPLEMENTATIONS ====================
    
    /**
     * Email notification observer
     */
    static class EmailNotificationObserver implements BookingObserver {
        @Override
        public void onBookingConfirmed(Booking booking) {
            System.out.println("\n[EMAIL] Notification:");
            System.out.println("To: " + booking.getCustomer().getEmail());
            System.out.println("Subject: Booking Confirmed - " + booking.getBookingId());
            System.out.println("Your booking for " + booking.getShow().getMovie().getTitle() + 
                " has been confirmed!");
        }
        
        @Override
        public void onBookingCancelled(Booking booking) {
            System.out.println("\n[EMAIL] Notification:");
            System.out.println("To: " + booking.getCustomer().getEmail());
            System.out.println("Subject: Booking Cancelled - " + booking.getBookingId());
            System.out.println("Your booking has been cancelled. Refund will be processed.");
        }
    }
    
    /**
     * SMS notification observer
     */
    static class SMSNotificationObserver implements BookingObserver {
        @Override
        public void onBookingConfirmed(Booking booking) {
            System.out.println("\n[SMS] Notification:");
            System.out.println("To: " + booking.getCustomer().getPhoneNumber());
            System.out.println("Booking confirmed for " + booking.getShow().getMovie().getTitle() + 
                ". BookingID: " + booking.getBookingId());
        }
        
        @Override
        public void onBookingCancelled(Booking booking) {
            System.out.println("\n[SMS] Notification:");
            System.out.println("To: " + booking.getCustomer().getPhoneNumber());
            System.out.println("Booking " + booking.getBookingId() + " cancelled. Refund initiated.");
        }
    }
    
    // ==================== CORE SYSTEM ====================
    
    /**
     * Main Booking System - Singleton Pattern for thread-safe access
     */
    static class BookingSystem {
        private static volatile BookingSystem instance;
        private static final ReentrantLock lock = new ReentrantLock();
        
        // Data stores
        private final Map<String, Movie> movies;
        private final Map<String, City> cities;
        private final Map<String, Theatre> theatres;
        private final Map<String, Show> shows;
        private final Map<String, Booking> bookings;
        private final Map<String, User> users;
        
        // Observers
        private final List<BookingObserver> observers;
        
        // Strategies
        private PricingStrategy pricingStrategy;
        
        // Counters for ID generation
        private int movieCounter = 1;
        private int cityCounter = 1;
        private int theatreCounter = 1;
        private int screenCounter = 1;
        private int showCounter = 1;
        private int bookingCounter = 1;
        private int paymentCounter = 1;
        private int userCounter = 1;
        
        /**
         * Private constructor for Singleton
         */
        private BookingSystem() {
            this.movies = new ConcurrentHashMap<>();
            this.cities = new ConcurrentHashMap<>();
            this.theatres = new ConcurrentHashMap<>();
            this.shows = new ConcurrentHashMap<>();
            this.bookings = new ConcurrentHashMap<>();
            this.users = new ConcurrentHashMap<>();
            this.observers = new ArrayList<>();
            this.pricingStrategy = new StandardPricingStrategy();
            
            // Register default observers
            registerObserver(new EmailNotificationObserver());
            registerObserver(new SMSNotificationObserver());
        }
        
        /**
         * Get singleton instance (thread-safe double-checked locking)
         */
        public static BookingSystem getInstance() {
            if (instance == null) {
                lock.lock();
                try {
                    if (instance == null) {
                        instance = new BookingSystem();
                    }
                } finally {
                    lock.unlock();
                }
            }
            return instance;
        }
        
        /**
         * Register a booking observer
         */
        public void registerObserver(BookingObserver observer) {
            observers.add(observer);
        }
        
        /**
         * Notify all observers about booking confirmation
         */
        private void notifyBookingConfirmed(Booking booking) {
            for (BookingObserver observer : observers) {
                observer.onBookingConfirmed(booking);
            }
        }
        
        /**
         * Notify all observers about booking cancellation
         */
        private void notifyBookingCancelled(Booking booking) {
            for (BookingObserver observer : observers) {
                observer.onBookingCancelled(booking);
            }
        }
        
        /**
         * Set pricing strategy
         */
        public void setPricingStrategy(PricingStrategy strategy) {
            this.pricingStrategy = strategy;
        }
        
        // ==================== USER MANAGEMENT ====================
        
        public Customer createCustomer(String name, String email, String phoneNumber) {
            String userId = "CUST" + String.format("%04d", userCounter++);
            Customer customer = new Customer(userId, name, email, phoneNumber);
            users.put(userId, customer);
            return customer;
        }
        
        public Admin createAdmin(String name, String email, String phoneNumber) {
            String userId = "ADMIN" + String.format("%04d", userCounter++);
            Admin admin = new Admin(userId, name, email, phoneNumber);
            users.put(userId, admin);
            return admin;
        }
        
        public User getUser(String userId) {
            return users.get(userId);
        }
        
        // ==================== CITY MANAGEMENT ====================
        
        public City addCity(String name, String state) {
            String cityId = "CITY" + String.format("%04d", cityCounter++);
            City city = new City(cityId, name, state);
            cities.put(cityId, city);
            return city;
        }
        
        public City getCity(String cityId) {
            return cities.get(cityId);
        }
        
        public List<City> getAllCities() {
            return new ArrayList<>(cities.values());
        }
        
        // ==================== THEATRE MANAGEMENT ====================
        
        public Theatre addTheatre(String name, String address, City city) {
            String theatreId = "THT" + String.format("%04d", theatreCounter++);
            Theatre theatre = new Theatre(theatreId, name, address, city);
            theatres.put(theatreId, theatre);
            city.addTheatre(theatre);
            return theatre;
        }
        
        public Theatre getTheatre(String theatreId) {
            return theatres.get(theatreId);
        }
        
        public List<Theatre> getTheatresByCity(City city) {
            return city.getTheatres();
        }
        
        // ==================== SCREEN MANAGEMENT ====================
        
        public Screen addScreen(Theatre theatre, String name, int totalRows, int seatsPerRow) {
            String screenId = "SCR" + String.format("%04d", screenCounter++);
            Screen screen = new Screen(screenId, name, theatre, totalRows, seatsPerRow);
            theatre.addScreen(screen);
            return screen;
        }
        
        // ==================== MOVIE MANAGEMENT ====================
        
        public Movie addMovie(String title, String description, int durationMinutes, 
                            Language language, List<Genre> genres, String director, List<String> cast) {
            String movieId = "MOV" + String.format("%04d", movieCounter++);
            Movie.Builder builder = new Movie.Builder(movieId, title)
                .description(description)
                .durationMinutes(durationMinutes)
                .language(language)
                .director(director)
                .releaseDate(LocalDateTime.now());
            
            for (Genre genre : genres) {
                builder.addGenre(genre);
            }
            
            for (String actor : cast) {
                builder.addCast(actor);
            }
            
            Movie movie = builder.build();
            movies.put(movieId, movie);
            return movie;
        }
        
        public Movie getMovie(String movieId) {
            return movies.get(movieId);
        }
        
        public List<Movie> getAllMovies() {
            return new ArrayList<>(movies.values());
        }
        
        /**
         * Search movies using a search strategy
         */
        public List<Movie> searchMovies(String keyword, SearchStrategy strategy) {
            return strategy.search(keyword, getAllMovies());
        }
        
        // ==================== SHOW MANAGEMENT ====================
        
        public Show addShow(Movie movie, Screen screen, LocalDateTime startTime) {
            String showId = "SHOW" + String.format("%04d", showCounter++);
            Show show = new Show(showId, movie, screen, startTime);
            shows.put(showId, show);
            return show;
        }
        
        public Show getShow(String showId) {
            return shows.get(showId);
        }
        
        public List<Show> getAllShows() {
            return new ArrayList<>(shows.values());
        }
        
        /**
         * Get shows for a specific movie in a city
         */
        public List<Show> getShowsForMovie(Movie movie, City city) {
            return shows.values().stream()
                .filter(show -> show.getMovie().equals(movie))
                .filter(show -> show.getScreen().getTheatre().getCity().equals(city))
                .filter(show -> show.getStartTime().isAfter(LocalDateTime.now()))
                .sorted(Comparator.comparing(Show::getStartTime))
                .collect(Collectors.toList());
        }
        
        /**
         * Get shows for a theatre
         */
        public List<Show> getShowsForTheatre(Theatre theatre, LocalDateTime date) {
            return shows.values().stream()
                .filter(show -> show.getScreen().getTheatre().equals(theatre))
                .filter(show -> show.getStartTime().toLocalDate().equals(date.toLocalDate()))
                .sorted(Comparator.comparing(Show::getStartTime))
                .collect(Collectors.toList());
        }
        
        // ==================== BOOKING MANAGEMENT ====================
        
        /**
         * Create a booking (main booking flow)
         */
        public Booking createBooking(Customer customer, Show show, List<String> seatIds, 
                                    PaymentMethod paymentMethod, String paymentDetails) {
            // Step 1: Validate seats exist
            List<Seat> seats = new ArrayList<>();
            for (String seatId : seatIds) {
                Seat seat = show.getScreen().getSeats().stream()
                    .filter(s -> s.getSeatId().equals(seatId))
                    .findFirst()
                    .orElse(null);
                
                if (seat == null) {
                    System.out.println("[ERROR] Seat not found: " + seatId);
                    return null;
                }
                seats.add(seat);
            }
            
            // Step 2: Check seat availability and reserve
            if (!show.reserveSeats(seatIds)) {
                System.out.println("[ERROR] Selected seats are not available");
                return null;
            }
            
            try {
                // Step 3: Calculate total amount
                double totalAmount = seats.stream()
                    .mapToDouble(seat -> pricingStrategy.calculatePrice(seat, show))
                    .sum();
                
                // Step 4: Create booking
                String bookingId = "BKG" + String.format("%06d", bookingCounter++);
                Booking booking = new Booking(bookingId, customer, show, seats, totalAmount);
                
                // Step 5: Process payment
                PaymentStrategy paymentStrategy = getPaymentStrategy(paymentMethod);
                String paymentId = "PAY" + String.format("%06d", paymentCounter++);
                Payment payment = new Payment(paymentId, booking, totalAmount, paymentMethod);
                
                if (paymentStrategy.processPayment(totalAmount, paymentDetails)) {
                    // Payment successful
                    String transactionId = "TXN" + UUID.randomUUID().toString().substring(0, 8);
                    payment.markCompleted(transactionId);
                    booking.setPayment(payment);
                    
                    // Confirm booking
                    booking.confirmBooking();
                    show.confirmSeats(seatIds);
                    
                    // Save booking
                    bookings.put(bookingId, booking);
                    customer.addBooking(booking);
                    
                    // Notify observers
                    notifyBookingConfirmed(booking);
                    
                    System.out.println("[SUCCESS] Booking successful!");
                    return booking;
                } else {
                    // Payment failed
                    payment.markFailed();
                    show.releaseSeats(seatIds);
                    System.out.println("[ERROR] Payment failed");
                    return null;
                }
            } catch (Exception e) {
                // Release seats in case of any error
                show.releaseSeats(seatIds);
                System.out.println("[ERROR] Booking failed: " + e.getMessage());
                return null;
            }
        }
        
        /**
         * Cancel a booking
         */
        public boolean cancelBooking(String bookingId) {
            Booking booking = bookings.get(bookingId);
            
            if (booking == null) {
                System.out.println("[ERROR] Booking not found");
                return false;
            }
            
            if (booking.getStatus() == BookingStatus.CANCELLED) {
                System.out.println("[ERROR] Booking already cancelled");
                return false;
            }
            
            // Check if show is in the future (can't cancel past shows)
            if (booking.getShow().getStartTime().isBefore(LocalDateTime.now())) {
                System.out.println("[ERROR] Cannot cancel past bookings");
                return false;
            }
            
            // Process refund
            Payment payment = booking.getPayment();
            PaymentStrategy paymentStrategy = getPaymentStrategy(payment.getMethod());
            
            if (paymentStrategy.refund(payment.getAmount(), payment.getTransactionId())) {
                // Refund successful
                payment.markRefunded();
                booking.cancelBooking();
                
                // Release seats
                List<String> seatIds = booking.getSeats().stream()
                    .map(Seat::getSeatId)
                    .collect(Collectors.toList());
                booking.getShow().releaseSeats(seatIds);
                
                // Notify observers
                notifyBookingCancelled(booking);
                
                System.out.println("[SUCCESS] Booking cancelled successfully");
                return true;
            } else {
                System.out.println("[ERROR] Refund failed");
                return false;
            }
        }
        
        /**
         * Get booking by ID
         */
        public Booking getBooking(String bookingId) {
            return bookings.get(bookingId);
        }
        
        /**
         * Get all bookings
         */
        public List<Booking> getAllBookings() {
            return new ArrayList<>(bookings.values());
        }
        
        /**
         * Get customer bookings
         */
        public List<Booking> getCustomerBookings(Customer customer) {
            return customer.getBookingHistory();
        }
        
        /**
         * Get payment strategy based on payment method
         */
        private PaymentStrategy getPaymentStrategy(PaymentMethod method) {
            switch (method) {
                case CREDIT_CARD:
                case DEBIT_CARD:
                    return new CreditCardPayment();
                case UPI:
                case WALLET:
                    return new UPIPayment();
                case NET_BANKING:
                    return new NetBankingPayment();
                default:
                    throw new IllegalArgumentException("Unsupported payment method: " + method);
            }
        }
        
        // ==================== DISPLAY METHODS ====================
        
        /**
         * Display available seats for a show
         */
        public void displayAvailableSeats(Show show) {
            System.out.println("\n=== Available Seats for " + show.getMovie().getTitle() + " ===");
            System.out.println("Show Time: " + show.getStartTime());
            System.out.println("Screen: " + show.getScreen().getName());
            System.out.println("Theatre: " + show.getScreen().getTheatre().getName());
            
            Map<SeatType, List<Seat>> seatsByType = show.getAvailableSeats().stream()
                .collect(Collectors.groupingBy(Seat::getType));
            
            for (SeatType type : SeatType.values()) {
                List<Seat> seats = seatsByType.getOrDefault(type, new ArrayList<>());
                if (!seats.isEmpty()) {
                    System.out.println("\n" + type + " (Rs." + type.getBasePrice() + "): " + 
                        seats.size() + " seats available");
                    System.out.println("Seats: " + seats.stream()
                        .map(Seat::getSeatId)
                        .collect(Collectors.joining(", ")));
                }
            }
        }
        
        /**
         * Display booking details
         */
        public void displayBookingDetails(Booking booking) {
            System.out.println("\n=== Booking Details ===");
            System.out.println("Booking ID: " + booking.getBookingId());
            System.out.println("Customer: " + booking.getCustomer().getName());
            System.out.println("Movie: " + booking.getShow().getMovie().getTitle());
            System.out.println("Theatre: " + booking.getShow().getScreen().getTheatre().getName());
            System.out.println("Screen: " + booking.getShow().getScreen().getName());
            System.out.println("Show Time: " + booking.getShow().getStartTime());
            System.out.println("Seats: " + booking.getSeats().stream()
                .map(Seat::getSeatId)
                .collect(Collectors.joining(", ")));
            System.out.println("Total Amount: Rs." + booking.getTotalAmount());
            System.out.println("Status: " + booking.getStatus());
            System.out.println("Booking Time: " + booking.getBookingTime());
            
            if (booking.getPayment() != null) {
                System.out.println("\nPayment Details:");
                System.out.println("Payment ID: " + booking.getPayment().getPaymentId());
                System.out.println("Method: " + booking.getPayment().getMethod());
                System.out.println("Status: " + booking.getPayment().getStatus());
                if (booking.getPayment().getTransactionId() != null) {
                    System.out.println("Transaction ID: " + booking.getPayment().getTransactionId());
                }
            }
        }
    }
    
    // ==================== MAIN METHOD FOR DEMONSTRATION ====================
    
    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println("   Movie Ticket Booking System");
        System.out.println("========================================\n");
        
        // Get singleton instance
        BookingSystem system = BookingSystem.getInstance();
        
        // Create admin
        Admin admin = system.createAdmin("John Admin", "admin@movies.com", "9999999999");
        admin.displayDashboard();
        
        // Create cities
        City bangalore = system.addCity("Bangalore", "Karnataka");
        City mumbai = system.addCity("Mumbai", "Maharashtra");
        System.out.println("\n[SUCCESS] Cities added: " + bangalore + ", " + mumbai);
        
        // Create theatres
        Theatre pvr = system.addTheatre("PVR Cinemas", "Forum Mall, Koramangala", bangalore);
        Theatre inox = system.addTheatre("INOX", "Phoenix Market City", bangalore);
        System.out.println("[SUCCESS] Theatres added: " + pvr + ", " + inox);
        
        // Create screens
        Screen screen1 = system.addScreen(pvr, "Screen 1", 10, 15);
        Screen screen2 = system.addScreen(pvr, "Screen 2", 12, 20);
        Screen screen3 = system.addScreen(inox, "Audi 1", 15, 18);
        System.out.println("[SUCCESS] Screens added to theatres");
        
        // Add movies
        Movie movie1 = system.addMovie(
            "Inception",
            "A mind-bending thriller about dream invasion",
            148,
            Language.ENGLISH,
            Arrays.asList(Genre.SCI_FI, Genre.THRILLER),
            "Christopher Nolan",
            Arrays.asList("Leonardo DiCaprio", "Tom Hardy", "Marion Cotillard")
        );
        
        Movie movie2 = system.addMovie(
            "3 Idiots",
            "A comedy-drama about engineering students",
            170,
            Language.HINDI,
            Arrays.asList(Genre.COMEDY, Genre.DRAMA),
            "Rajkumar Hirani",
            Arrays.asList("Aamir Khan", "R. Madhavan", "Sharman Joshi")
        );
        
        Movie movie3 = system.addMovie(
            "The Dark Knight",
            "Batman fights the Joker",
            152,
            Language.ENGLISH,
            Arrays.asList(Genre.ACTION, Genre.THRILLER),
            "Christopher Nolan",
            Arrays.asList("Christian Bale", "Heath Ledger", "Aaron Eckhart")
        );
        
        System.out.println("\n[SUCCESS] Movies added:");
        System.out.println("  - " + movie1);
        System.out.println("  - " + movie2);
        System.out.println("  - " + movie3);
        
        // Add shows
        LocalDateTime now = LocalDateTime.now();
        Show show1 = system.addShow(movie1, screen1, now.plusHours(2));
        Show show2 = system.addShow(movie1, screen1, now.plusHours(5));
        Show show3 = system.addShow(movie2, screen2, now.plusHours(3));
        Show show4 = system.addShow(movie3, screen3, now.plusHours(4));
        
        System.out.println("\n[SUCCESS] Shows scheduled:");
        System.out.println("  - " + show1);
        System.out.println("  - " + show2);
        System.out.println("  - " + show3);
        System.out.println("  - " + show4);
        
        // Create customers
        Customer customer1 = system.createCustomer("Alice Johnson", "alice@example.com", "9876543210");
        Customer customer2 = system.createCustomer("Bob Smith", "bob@example.com", "9876543211");
        
        System.out.println("\n[SUCCESS] Customers created:");
        System.out.println("  - " + customer1.getName() + " (" + customer1.getUserId() + ")");
        System.out.println("  - " + customer2.getName() + " (" + customer2.getUserId() + ")");
        
        // Search movies
        System.out.println("\n\n=== MOVIE SEARCH DEMO ===");
        
        System.out.println("\n[SEARCH] Searching movies by name 'Inception':");
        List<Movie> searchResults = system.searchMovies("Inception", new MovieNameSearchStrategy());
        searchResults.forEach(m -> System.out.println("  - " + m));
        
        System.out.println("\n[SEARCH] Searching movies by language 'HINDI':");
        searchResults = system.searchMovies("HINDI", new LanguageSearchStrategy());
        searchResults.forEach(m -> System.out.println("  - " + m));
        
        System.out.println("\n[SEARCH] Searching movies by genre 'THRILLER':");
        searchResults = system.searchMovies("THRILLER", new GenreSearchStrategy());
        searchResults.forEach(m -> System.out.println("  - " + m));
        
        // Display available shows for a movie in a city
        System.out.println("\n\n=== SHOWS FOR MOVIE ===");
        List<Show> movieShows = system.getShowsForMovie(movie1, bangalore);
        System.out.println("Shows for '" + movie1.getTitle() + "' in " + bangalore.getName() + ":");
        movieShows.forEach(s -> System.out.println("  - " + s));
        
        // Display available seats
        system.displayAvailableSeats(show1);
        
        // Book tickets - Customer 1
        System.out.println("\n\n=== BOOKING DEMO ===");
        System.out.println("\n[BOOKING] Customer 1 booking tickets...");
        
        List<String> selectedSeats1 = Arrays.asList(
            screen1.getSeats().get(0).getSeatId(),
            screen1.getSeats().get(1).getSeatId(),
            screen1.getSeats().get(2).getSeatId()
        );
        
        Booking booking1 = system.createBooking(
            customer1,
            show1,
            selectedSeats1,
            PaymentMethod.UPI,
            "alice@upi"
        );
        
        if (booking1 != null) {
            system.displayBookingDetails(booking1);
        }
        
        // Book tickets - Customer 2
        System.out.println("\n\n[BOOKING] Customer 2 booking tickets...");
        
        List<String> selectedSeats2 = Arrays.asList(
            screen1.getSeats().get(3).getSeatId(),
            screen1.getSeats().get(4).getSeatId()
        );
        
        Booking booking2 = system.createBooking(
            customer2,
            show1,
            selectedSeats2,
            PaymentMethod.CREDIT_CARD,
            "1234-5678-9012-3456"
        );
        
        if (booking2 != null) {
            system.displayBookingDetails(booking2);
        }
        
        // Try to book already booked seats (should fail)
        System.out.println("\n\n[BOOKING] Customer 2 trying to book already booked seats...");
        system.createBooking(
            customer2,
            show1,
            selectedSeats1, // Same seats as booking1
            PaymentMethod.UPI,
            "bob@upi"
        );
        
        // Display customer booking history
        System.out.println("\n\n=== CUSTOMER BOOKING HISTORY ===");
        customer1.displayDashboard();
        List<Booking> customer1Bookings = system.getCustomerBookings(customer1);
        System.out.println("Bookings:");
        customer1Bookings.forEach(b -> System.out.println("  - " + b));
        
        // Cancel booking
        System.out.println("\n\n=== CANCELLATION DEMO ===");
        System.out.println("\n[CANCEL] Cancelling booking: " + booking1.getBookingId());
        boolean cancelled = system.cancelBooking(booking1.getBookingId());
        
        if (cancelled) {
            system.displayBookingDetails(booking1);
        }
        
        // Display updated seat availability
        System.out.println("\n\n=== UPDATED SEAT AVAILABILITY ===");
        system.displayAvailableSeats(show1);
        
        // Weekend pricing demo
        System.out.println("\n\n=== PRICING STRATEGY DEMO ===");
        System.out.println("\n[PRICING] Switching to Weekend Pricing Strategy...");
        system.setPricingStrategy(new WeekendPricingStrategy());
        
        // Check pricing for different seat types
        System.out.println("\nSample seat prices with weekend pricing:");
        for (SeatType type : SeatType.values()) {
            Seat sampleSeat = show1.getScreen().getSeats().stream()
                .filter(s -> s.getType() == type)
                .findFirst()
                .orElse(null);
            if (sampleSeat != null) {
                double price = system.pricingStrategy.calculatePrice(sampleSeat, show1);
                System.out.println("  " + type + ": Rs." + price);
            }
        }
        
        // Show system statistics
        System.out.println("\n\n=== SYSTEM STATISTICS ===");
        System.out.println("Total Movies: " + system.getAllMovies().size());
        System.out.println("Total Cities: " + system.getAllCities().size());
        System.out.println("Total Shows: " + system.getAllShows().size());
        System.out.println("Total Bookings: " + system.getAllBookings().size());
        
        long confirmedBookings = system.getAllBookings().stream()
            .filter(b -> b.getStatus() == BookingStatus.CONFIRMED)
            .count();
        long cancelledBookings = system.getAllBookings().stream()
            .filter(b -> b.getStatus() == BookingStatus.CANCELLED)
            .count();
        
        System.out.println("Confirmed Bookings: " + confirmedBookings);
        System.out.println("Cancelled Bookings: " + cancelledBookings);
        
        double totalRevenue = system.getAllBookings().stream()
            .filter(b -> b.getStatus() == BookingStatus.CONFIRMED)
            .mapToDouble(Booking::getTotalAmount)
            .sum();
        
        System.out.println("Total Revenue: Rs. " + totalRevenue);
        
        System.out.println("\n\n[SUCCESS] Demo completed successfully!");
    }
}

