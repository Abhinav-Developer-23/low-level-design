package org.lowLevelDesign.MachineCoding.Scratch.BookMyShow.Service;

import org.lowLevelDesign.MachineCoding.Scratch.BookMyShow.Exceptions.BookingFailedException;

public class PaymentService {

    public void processPayment() throws BookingFailedException {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            throw new BookingFailedException("payment failed");
        }
    }


}
