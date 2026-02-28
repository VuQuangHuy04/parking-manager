package org.example.controller;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import org.example.model.ParkingSlot;
import org.example.service.BookingService;
import org.example.service.ParkingService;
import org.example.session.UserSession;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
public class ParkingMapController {
    @FXML private Label lotNameLabel;
    @FXML private HBox adminToolBar;
    @FXML private Pane mapPane;
    private String parkingLotName;
    private int currentLotId;
    private List<ParkingSlot> slots = new ArrayList<>();
    BookingService bookingService = new BookingService();
    private ParkingService parkingService = new ParkingService();
    public void setParkingLotData(String lotName, int id) {
        this.parkingLotName = lotName;
        this.currentLotId = id;
        this.lotNameLabel.setText("Bãi xe: " + lotName);
        boolean isAdmin = UserSession.getUser().getRole().equals("ADMIN");
        adminToolBar.setVisible(isAdmin);
        loadDataAndRender(isAdmin);
        startAutoUpdate(isAdmin);
    }
    private void loadDataAndRender(boolean isAdmin) {
        this.slots = parkingService.getSlots(currentLotId);
        mapPane.getChildren().clear();
        for (ParkingSlot slot : slots) {
            createSlotButton(slot, isAdmin);
        }
    }
    private void createSlotButton(ParkingSlot slot, boolean isAdmin) {
        Button btn = new Button(slot.getSlotCode());
        btn.setPrefSize(60, 40);
        btn.setLayoutX(slot.getX());
        btn.setLayoutY(slot.getY());
        updateButtonStyle(btn, slot);
        if (isAdmin){
            enableAdminFeatures(btn, slot);
        } else {
            // Thay đổi action: Mở Card thông tin thay vì Alert đơn giản
            btn.setOnAction(e -> showSlotInfoCard(slot, btn));
        }
        mapPane.getChildren().add(btn);
    }
    private void showSlotInfoCard(ParkingSlot slot, Button btn) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Thông tin ô " + slot.getSlotCode());

        VBox container = new VBox(10);
        container.setPadding(new Insets(20));

        // ================= EMPTY SLOT =================
        if (slot.getStatus().equals("EMPTY")) {

            // Spinner giờ
            Spinner<Integer> hourSpinner = new Spinner<>(0, 23, 0);
            hourSpinner.setEditable(true);

            // Spinner phút
            Spinner<Integer> minuteSpinner = new Spinner<>(0, 59, 30);
            minuteSpinner.setEditable(true);

            HBox timeBox = new HBox(10,
                    new Label("Giờ:"), hourSpinner,
                    new Label("Phút:"), minuteSpinner
            );

            ComboBox<String> paymentBox = new ComboBox<>();
            paymentBox.getItems().addAll("MOMO", "VNPAY", "CASH");
            paymentBox.setPromptText("Chọn phương thức thanh toán");

            container.getChildren().addAll(
                    new Label("Mã: " + slot.getSlotCode()),
                    new Label("Giá: 5000 / giờ"),
                    timeBox,
                    paymentBox
            );

            dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

            dialog.setResultConverter(bt -> {
                if (bt == ButtonType.OK) {
                    try {
                        int hours = hourSpinner.getValue();
                        int minutes = minuteSpinner.getValue();
                        int totalMinutes = hours * 60 + minutes;
                        if (totalMinutes <= 0) {
                            showAlert(Alert.AlertType.ERROR, "Lỗi", "Thời gian phải lớn hơn 0");
                            return null;
                        }

                        String method = paymentBox.getValue();
                        if (method == null) {
                            showAlert(Alert.AlertType.ERROR, "Lỗi", "Vui lòng chọn phương thức thanh toán");
                            return null;
                        }

                        // Tính tiền theo phút
                        double totalPrice = parkingService.calculatePrice(totalMinutes);

                        Alert paymentAlert = new Alert(Alert.AlertType.CONFIRMATION);
                        paymentAlert.setTitle("Xác nhận thanh toán");
                        paymentAlert.setHeaderText("Thông tin thanh toán");
                        paymentAlert.setContentText(
                                "Thời gian: " + hours + " giờ " + minutes + " phút" +
                                        "\nTổng tiền: " + Math.round(totalPrice) + " VNĐ" +
                                        "\nPhương thức: " + method
                        );

                        Optional<ButtonType> result = paymentAlert.showAndWait();

                        if (result.isPresent() && result.get() == ButtonType.OK) {

                            boolean booked = parkingService.bookSlot(
                                    slot.getId(),
                                    UserSession.getUserId(),
                                    totalMinutes,
                                    method
                            );

                            if (booked) {
                                parkingService.confirmPayment(slot.getId());
                                slot.setStatus("OCCUPIED");
                                updateButtonStyle(btn, slot);
                                showAlert(Alert.AlertType.INFORMATION,
                                        "Thành công",
                                        "Thanh toán thành công qua " + method);
                            }
                        }

                    } catch (Exception e) {
                        showAlert(Alert.AlertType.ERROR, "Lỗi", "Dữ liệu thời gian không hợp lệ");
                    }
                }
                return null;
            });

        }
        // ================= OCCUPIED SLOT =================
        else {
            Label countdownLabel = new Label("--:--:--");
            countdownLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #e74c3c;");
            countdownLabel.setMinWidth(150);

            LocalDateTime endTime = parkingService.getBookingEndTime(slot.getId());
            if (endTime != null) {
                startCountdown(endTime, countdownLabel, slot, btn);
            }

            container.getChildren().addAll(
                    new Label("Mã: " + slot.getSlotCode()),
                    new Label("Thời gian còn lại:"),
                    countdownLabel
            );
            dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
        }
        dialog.getDialogPane().setContent(container);
        dialog.showAndWait();
    }
    private void startCountdown(LocalDateTime endTime,
                                Label label,
                                ParkingSlot slot,
                                Button btn) {
        Timeline timeline = new Timeline();
        KeyFrame keyFrame = new KeyFrame(Duration.seconds(1), e -> {
            java.time.Duration diff =
                    java.time.Duration.between(LocalDateTime.now(), endTime);
            if (diff.isNegative() || diff.isZero()) {
                label.setText("HẾT GIỜ");
                loadDataAndRender(
                        UserSession.getUser().getRole().equals("ADMIN")
                );
                timeline.stop();
            } else {
                long h = diff.toHours();
                long m = diff.toMinutesPart();
                long s = diff.toSecondsPart();
                label.setText(String.format("%02d:%02d:%02d", h, m, s));
            }
        });
        timeline.getKeyFrames().add(keyFrame);
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
        label.sceneProperty().addListener((obs, old, newVal) -> {
            if (newVal == null) {
                timeline.stop();
            }
        });
    }
    private void updateButtonStyle(Button btn, ParkingSlot slot) {
        if (slot.getStatus().equals("EMPTY")) {
            btn.setStyle("-fx-background-color: #2ecc71; -fx-text-fill: white; -fx-background-radius: 5;");
        } else if (slot.getStatus().equals("MAINTENANCE")) {
            btn.setStyle("-fx-background-color: #f1c40f; -fx-text-fill: white;");
        } else {
            btn.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-background-radius: 5;");
        }
    }
    private void enableAdminFeatures(Button btn, ParkingSlot slot) {
        btn.setOnMouseDragged(e -> {
            btn.setLayoutX(e.getSceneX() - mapPane.getLayoutX() - (btn.getWidth() / 2));
            btn.setLayoutY(e.getSceneY() - mapPane.getLayoutY() - (btn.getHeight() / 2));
        });
        ContextMenu contextMenu = new ContextMenu();
        MenuItem deleteItem = new MenuItem("Xóa ô này");
        deleteItem.setOnAction(e -> {
            if (parkingService.deleteSlot(slot.getId())) {
                mapPane.getChildren().remove(btn);
            }
        });
        contextMenu.getItems().add(deleteItem);
        btn.setContextMenu(contextMenu);
    }
    @FXML
    public void onSaveLayout() {
        for (javafx.scene.Node node : mapPane.getChildren()) {
            if (node instanceof Button btn) {
                parkingService.updateSlotPositionp(currentLotId, btn.getText(), btn.getLayoutX(), btn.getLayoutY());
            }
        }
        showAlert(Alert.AlertType.INFORMATION, "Thành công", "Đã lưu sơ đồ!");
    }
    @FXML
    public void onAddNewSlot() {
        TextInputDialog dialog = new TextInputDialog("A-");
        dialog.setTitle("Thêm ô mới");
        dialog.showAndWait().ifPresent(code -> {
            ParkingSlot newSlot = new ParkingSlot(0, currentLotId, code, 50, 50, "EMPTY");
            if (parkingService.insertSlot(newSlot)) {
                createSlotButton(newSlot, true);
            }
        });
    }
    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
    public void startAutoUpdate(boolean isAdmin) {
        Timeline autoUpdate = new Timeline(new KeyFrame(Duration.seconds(30), event -> {
            this.slots = parkingService.getSlots(currentLotId);
            loadDataAndRender(isAdmin);
        }));
        autoUpdate.setCycleCount(Animation.INDEFINITE); // Chạy vô hạn cho đến khi đóng App
        autoUpdate.play(); // Bắt đầu chạy
    }
}