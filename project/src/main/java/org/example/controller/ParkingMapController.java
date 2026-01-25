package org.example.controller;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import org.example.constant.SystemConstant;
import org.example.model.ParkingSlot;
import org.example.utils.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ParkingMapController {

    @FXML
    private GridPane gridPane;

    private String parkingLotName = "Bãi xe Trung Tâm"; // Se sua lai thanh code chay theo ket qua cua bai do xe gan nhat
    private List<ParkingSlot> slots = new ArrayList<>();

    @FXML
    public void initialize() {
        loadParkingSlots();
        renderGrid();
    }

    private void loadParkingSlots() {
        try (Connection conn = DBConnection.getConnection()) {
            // Lấy id của bãi xe theo tên cua bai xe duoc tra ve doi chieu voi database
            PreparedStatement psLot = conn.prepareStatement("SELECT id FROM parking_lots WHERE name = ?");
            psLot.setString(1, parkingLotName);
            ResultSet rsLot = psLot.executeQuery();
            int lotId = -1;
            if (rsLot.next()) lotId = rsLot.getInt("id");

            if (lotId == -1) return;

            // Lấy tất cả slot trong bãi
            PreparedStatement ps = conn.prepareStatement("SELECT slot_id, status, vehicle_type FROM parking_slots WHERE lot_id = ?");
            ps.setInt(1, lotId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                ParkingSlot slot = new ParkingSlot(
                        0,
                        lotId,
                        rs.getString("slot_id"),
                        rs.getString("status").equals("EMPTY") // EMPTY -> AVAILABLE
                );
                slots.add(slot);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void renderGrid() {
        gridPane.getChildren().clear();
        gridPane.setHgap(5);
        gridPane.setVgap(5);
        gridPane.setPadding(new Insets(10));

        int col = 0;
        int row = 0;
        int maxCols = 10; // mỗi hàng tối đa 10 ô

        for (ParkingSlot slot : slots) {
            Button btn = new Button(slot.getSlotCode());
            btn.setMinSize(60, 60);
            btn.setMaxSize(60, 60);

            switch (getSlotStatus(slot)) {
                case "AVAILABLE":
                    btn.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
                    btn.setOnAction(e -> handleAvailableSlot(e, slot));
                    break;
                case "BOOKED":
                    btn.setStyle("-fx-background-color: #F44336; -fx-text-fill: white;");
                    btn.setOnAction(e -> showAlert("Đã được đặt"));
                    break;
                case "OCCUPIED":
                    btn.setStyle("-fx-background-color: #9E9E9E; -fx-text-fill: white;");
                    btn.setOnAction(e -> showAlert("Không thể đặt"));
                    break;
            }

            gridPane.add(btn, col, row);
            col++;
            if (col >= maxCols) {
                col = 0;
                row++;
            }
        }
    }

    private String getSlotStatus(ParkingSlot slot) {
        if (slot.isAvailable()) return "AVAILABLE";
        else if (!slot.isAvailable() && slot.getSlotCode().startsWith("A")) return "BOOKED";
        else return "OCCUPIED";
    }

    private void handleAvailableSlot(ActionEvent e, ParkingSlot slot) {
        ChoiceDialog<String> dialog = new ChoiceDialog<>("Xe máy", "Xe máy", "Ô tô");
        dialog.setTitle("Chọn loại xe");
        dialog.setHeaderText("Có thể đặt: " + slot.getSlotCode());
        dialog.setContentText("Chọn loại xe:");

        dialog.showAndWait().ifPresent(type -> {
            double pricePerHour = type.equals("Xe máy") ? SystemConstant.Price_one_hour_for_motobike : SystemConstant.Price_one_hour_for_Car;
            int hours = 1; // giả lập số giờ đặt
            double total = pricePerHour * hours;

            showAlert("Đặt thành công " + slot.getSlotCode() + "\nThời gian kết thúc: " + LocalDateTime.now().plusHours(hours) + "\nTổng tiền: " + total);

            // Cập nhật slot thành BOOKED
            slot.setAvailable(false);
            renderGrid();
        });
    }

    private void showAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
