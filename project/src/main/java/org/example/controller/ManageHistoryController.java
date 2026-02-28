package org.example.controller;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.example.DTO.BookingDTO;
import org.example.dao.BookingDaoimpl;

import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

public class ManageHistoryController {

    @FXML
    private VBox resultContainer;
    private BookingDaoimpl bookingDAO = new BookingDaoimpl();
    private final DateTimeFormatter timeFormatter =
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    private final NumberFormat moneyFormatter =
            NumberFormat.getInstance(new Locale("vi", "VN"));
    @FXML
    public void initialize() {
        loadHistory();
    }
    private void loadHistory() {
        List<BookingDTO> list = bookingDAO.getAllBookings();
        renderHistoryList(list);
    }
    private void renderHistoryList(List<BookingDTO> list) {
        resultContainer.getChildren().clear();
        if (list == null || list.isEmpty()) {
            Label empty = new Label("No booking data");
            empty.setStyle("-fx-text-fill: gray; -fx-font-size: 14px;");
            resultContainer.getChildren().add(empty);
            return;
        }
        for (BookingDTO book : list) {
            VBox card = new VBox(8);
            card.setPadding(new Insets(12));
            card.setStyle("""
                    -fx-background-color: white;
                    -fx-border-color: #e0e0e0;
                    -fx-border-radius: 8;
                    -fx-background-radius: 8;
                    -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.08), 5, 0, 0, 2);
                    """);
            Label user = new Label(
                    "User ID: " + book.getUserId() +
                            " | Username: " + book.getUserName()
            );
            user.setStyle("-fx-text-fill: #2c3e50; -fx-font-weight: bold;");
            Label parking = new Label("Parking: " + book.getParkingName());
            parking.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");
            Label slot = new Label("Slot: " + book.getSlotCode());
            LocalDateTime start = book.getStartTime();
            LocalDateTime end = book.getEndTime();
            Label startLabel = new Label("Start: " + start.format(timeFormatter));
            Label endLabel = new Label("End  : " + end.format(timeFormatter));
            VBox timeBox = new VBox(2, startLabel, endLabel);
            String moneyText = moneyFormatter.format(book.getMoneyPaid()) + " VND";
            Label money = new Label("Paid: " + moneyText);
            money.setStyle("-fx-text-fill: #27ae60; -fx-font-weight: bold;");
            card.getChildren().addAll(user, parking, slot, timeBox, money);
            resultContainer.getChildren().add(card);
        }
    }
}