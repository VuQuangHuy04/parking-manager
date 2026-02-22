package org.example.scheduler;

import org.example.service.BookingService;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class BookingScheduler {
    private static final ScheduledExecutorService scheduler =
            Executors.newSingleThreadScheduledExecutor();
    public static void start() {
        BookingService bookingService = new BookingService();
        scheduler.scheduleAtFixedRate(() -> {
            try {
                bookingService.handleExpiredBookings();
                System.out.println("Checked expired bookings...");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, 0, 30, TimeUnit.SECONDS);
    }
}
