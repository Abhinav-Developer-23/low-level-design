package org.lowLevelDesign.MachineCoding.Final.BookMyShow.api;


import lombok.NonNull;
import org.lowLevelDesign.MachineCoding.Final.BookMyShow.services.BookingService;
import org.lowLevelDesign.MachineCoding.Final.BookMyShow.services.PaymentsService;

public class PaymentsController {
    private final PaymentsService paymentsService;
    private final BookingService bookingService;

    public PaymentsController(PaymentsService paymentsService, BookingService bookingService) {
        this.paymentsService = paymentsService;
        this.bookingService = bookingService;
    }

    public void paymentFailed(@NonNull final String bookingId, @NonNull final String user) {
        paymentsService.processPaymentFailed(bookingService.getBooking(bookingId), user);
    }

    public void paymentSuccess(@NonNull final  String bookingId, @NonNull final String user) {
        bookingService.confirmBooking(bookingService.getBooking(bookingId), user);
    }

}
