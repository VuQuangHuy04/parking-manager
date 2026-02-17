package org.example.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.example.DTO.BookingDTO;
import org.example.DTO.ParkingLotDTO;
import org.example.dao.BookingDaoimpl;
import org.example.session.UserSession;

import java.util.List;

public class HistoryController {
    @FXML
    private VBox resultContainer;
    private BookingDaoimpl bookingDAO = new BookingDaoimpl();
    @FXML
    public void initialize() {
        loadHistory();
    }
    private void loadHistory() {
        int userId = UserSession.getUserId();
        List<BookingDTO> list = bookingDAO.userHistory(userId);
        renderHistoryList(list);
    }
    private void renderHistoryList(List<BookingDTO> list) {

        resultContainer.getChildren().clear();

        for (BookingDTO book : list) {

            HBox card = new HBox(15);
            card.setStyle("""
            -fx-background-color: white;
            -fx-padding: 12;
            -fx-border-color: #e0e0e0;
            -fx-border-radius: 8;
            -fx-background-radius: 8;
            -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.08), 5, 0, 0, 2);
        """);

            VBox info = new VBox(5);

            Label parking = new Label("Parking: " + book.getParkingName());
            parking.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");

            Label slot = new Label("Slot: " + book.getSlotCode());

            Label time = new Label(
                    "From: " + book.getStartTime() +
                            "\nTo: " + book.getEndTime()
            );
            Label money = new Label("Paid: " + book.getMoneyPaid() + " VND");
            money.setStyle("-fx-text-fill: #27ae60; -fx-font-weight: bold;");
            info.getChildren().addAll(parking, slot, time, money);
            card.getChildren().add(info);
            resultContainer.getChildren().add(card);
        }
    }

}
