package org.lowLevelDesign.MachineCoding.Scratch.BookMyShow.Service;

import org.lowLevelDesign.MachineCoding.Scratch.BookMyShow.Entities.*;
import org.lowLevelDesign.MachineCoding.Scratch.BookMyShow.Enum.BookingStatus;
import org.lowLevelDesign.MachineCoding.Scratch.BookMyShow.Exceptions.BookingFailedException;

import java.util.List;

public class BookingService {
    PaymentService paymentService;

    public BookingService(PaymentService paymentService) {
        this.paymentService = paymentService;

    }

    public void bookTicket(User user, String cinema, String city, String hall, String movie, String seat, Show show, Integer row, Integer col, List<Booking> bookings) {

        Seat seat1 = show.getSeats()[row][col];
        if (seat1.getBookingStatus().equals(BookingStatus.VACANT)) {
            seat1.setBookingStatus(BookingStatus.PROCESSING);
            paymentService.processPayment();
            seat1.setBookingStatus(BookingStatus.BOOKED);

            Booking booking = new Booking();
            booking.setCinema(cinema);
            booking.setCity(city);
            booking.setRow(row);
            booking.setColumn(col);
            booking.setHallId(hall);
            bookings.add(booking);

        } else {
            seat1.setBookingStatus(BookingStatus.VACANT);
            throw new BookingFailedException("booking failed");
        }


    }

}
