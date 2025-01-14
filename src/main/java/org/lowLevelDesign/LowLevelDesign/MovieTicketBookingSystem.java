package org.lowLevelDesign.LowLevelDesign;

import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class MovieTicketBookingSystem {


    // Enums
    enum BookingStatus {
        REQUESTED, PENDING, CONFIRMED, CHECKED_IN, CANCELED, ABANDONED
    }

    enum SeatType {
        REGULAR, PREMIUM, ACCESSIBLE, EMERGENCY_EXIT
    }

    enum AccountStatus {
        ACTIVE, BLOCKED, BANNED, COMPROMISED, ARCHIVED
    }

    enum PaymentStatus {
        UNPAID, PENDING, COMPLETED, DECLINED, CANCELLED, REFUNDED
    }

    // Search Interface and Implementation
    interface Search {
        public List<Movie> searchByTitle(String title);

        public List<Movie> searchByLanguage(String language);

        public List<Movie> searchByGenre(String genre);

        public List<Movie> searchByReleaseDate(Date relDate);

        public List<Movie> searchByCity(String cityName);
    }

    // Notification System
    interface NotificationService {
        boolean sendNotification(String message);
    }

    // Address value object
    class Address {
        private String streetAddress;
        private String city;
        private String state;
        private String zipCode;
        private String country;
    }

    // Base abstract class for users
    abstract class Person {
        private String name;
        private Address address;
        private String email;
        private String phone;
        private Account account;
    }

    class Account {
        private String id;
        private String password;
        private AccountStatus status;

        public boolean resetPassword() {
            return true;
        }
    }

    // User types
    class Customer extends Person {
        private List<Booking> bookings;

        public boolean makeBooking(Booking booking) {
            // Implementation
            return true;
        }

        public List<Booking> getBookings() {
            return bookings;
        }
    }

    class Admin extends Person {
        public boolean addMovie(Movie movie) {
            return true;
        }

        public boolean addShow(Show show) {
            return true;
        }

        public boolean blockUser(Customer customer) {
            return true;
        }
    }

    class FrontDeskOfficer extends Person {
        public boolean createBooking(Booking booking) {
            return true;
        }
    }

    class Guest {
        public boolean registerAccount() {
            return true;
        }
    }

    // Movie and Show related classes
    class Movie {
        private String title;
        private String description;
        private int durationInMins;
        private String language;
        private Date releaseDate;
        private String country;
        private String genre;
        private Admin movieAddedBy;
        private List<Show> shows;

        public List<Show> getShows() {
            return shows;
        }
    }

    class Show {
        private int showId;
        private Date createdOn;
        private Date startTime;
        private Date endTime;
        private CinemaHall playedAt;
        private Movie movie;
    }

    class CinemaHall {
        private String name;
        private int totalSeats;
        private List<CinemaHallSeat> seats;
        private List<Show> shows;
    }

    class CinemaHallSeat {
        private int seatId;
        private SeatType type;
    }

    @Data
    class ShowSeat extends CinemaHallSeat {
        private int showSeatId;
        private boolean isReserved;
        private double price;
    }

    // Booking and Payment
    @Data
    class Booking {
        private String bookingNumber;
        private int numberOfSeats;
        private Date createdOn;
        private BookingStatus status;
        private Show show;
        private List<ShowSeat> seats;
        private Payment payment;

        public boolean makePayment(Payment payment) {
            this.payment = payment;
            return true;
        }

        public boolean cancel() {
            this.status = BookingStatus.CANCELED;
            return true;
        }

        public boolean assignSeats(List<ShowSeat> seats) {
            this.seats = seats;
            return true;
        }
    }

    class Payment {
        private double amount;
        private Date createdOn;
        private int transactionId;
        private PaymentStatus status;
    }

    // Cinema and City
    class Cinema {
        private String name;
        private int totalCinemaHalls;
        private Address location;
        private List<CinemaHall> halls;
    }

    class City {
        private String name;
        private String state;
        private String zipCode;
    }

    class Catalog implements Search {
        private HashMap<String, List<Movie>> movieTitles;
        private HashMap<String, List<Movie>> movieLanguages;
        private HashMap<String, List<Movie>> movieGenres;
        private HashMap<Date, List<Movie>> movieReleaseDates;
        private HashMap<String, List<Movie>> movieCities;

        public List<Movie> searchByTitle(String title) {
            return movieTitles.get(title);
        }

        public List<Movie> searchByLanguage(String language) {
            return movieLanguages.get(language);
        }

        public List<Movie> searchByGenre(String genre) {
            return movieGenres.get(genre);
        }

        public List<Movie> searchByReleaseDate(Date relDate) {
            return movieReleaseDates.get(relDate);
        }

        public List<Movie> searchByCity(String cityName) {
            return movieCities.get(cityName);
        }
    }

    class EmailNotification implements NotificationService {
        @Override
        public boolean sendNotification(String message) {
            // Email notification implementation
            return true;
        }
    }

    class SMSNotification implements NotificationService {
        @Override
        public boolean sendNotification(String message) {
            // SMS notification implementation
            return true;
        }
    }

    // Concurrency handling
    class BookingLock {
        private static final Object mutex = new Object();
        private static HashMap<String, Lock> lockMap = new HashMap<>();

        public static Lock getLock(String showSeatId) {
            synchronized (mutex) {
                if (!lockMap.containsKey(showSeatId)) {
                    lockMap.put(showSeatId, new ReentrantLock());
                }
                return lockMap.get(showSeatId);
            }
        }
    }

    // Main Booking Service
    class BookingService {
        private NotificationService notificationService;

        public boolean createBooking(Customer customer, Show show, List<ShowSeat> seats) {
            // Lock all seats
            List<Lock> locks = new ArrayList<>();
            try {
                for (ShowSeat seat : seats) {
                    Lock lock = BookingLock.getLock(String.valueOf(seat.getShowSeatId()));
                    if (!lock.tryLock(10, TimeUnit.MILLISECONDS)) {
                        // Release all acquired locks and return false
                        for (Lock acquiredLock : locks) {
                            acquiredLock.unlock();
                        }
                        return false;
                    }
                    locks.add(lock);
                }

                // All seats are locked, proceed with booking
                Booking booking = new Booking();
                booking.assignSeats(seats);
                booking.setShow(show);

                if (customer.makeBooking(booking)) {
                    notificationService.sendNotification("Booking confirmed");
                    return true;
                }

            } catch (InterruptedException e) {
                // Handle exception
            } finally {
                // Release all locks
                for (Lock lock : locks) {
                    lock.unlock();
                }
            }
            return false;
        }
    }
}