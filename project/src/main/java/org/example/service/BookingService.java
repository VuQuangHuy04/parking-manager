package org.example.service;

import org.example.DTO.BookingDTO;
import org.example.dao.BookingDaoimpl;
import org.example.dao.SlotDaoimpl;
import org.example.model.ParkingSlot;

import java.time.LocalDateTime;
import java.util.List;

public class BookingService {
    BookingDaoimpl bookingDaoimpl = new BookingDaoimpl();
    MailService mailService = new MailService();

    public void handleExpiredBookings() {
        LocalDateTime now = LocalDateTime.now();

        // 1. Update hết hạn -> DONE + release slot
        bookingDaoimpl.clearExpiredBookings(now);

        // 2. Lấy những booking vừa DONE nhưng chưa gửi mail
        List<BookingDTO> list = bookingDaoimpl.getExpiredBookings(now);

        System.out.println("Done not mail: " + list.size());

        // 3. Gửi mail
        for (BookingDTO b : list) {
            System.out.println("ID=" + b.getId() + " | email=" + b.getEmail());

            if (b.getEmail() != null && !b.getEmail().isEmpty()) {
                System.out.println("Sending mail to " + b.getEmail());

                boolean sent = mailService.sendMail(
                        b.getEmail(),
                        "Thông báo hết thời gian đỗ xe",
                        "Ô " + b.getSlotCode() + " của bạn đã hết thời gian."
                );

                System.out.println("Result = " + sent);

                if (sent) {
                    bookingDaoimpl.markMailSent(b.getId());
                }
            } else {
                System.out.println("Email NULL or EMPTY -> skip");
            }
        }
}}


