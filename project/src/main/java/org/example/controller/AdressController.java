package org.example.controller;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.example.DTO.ParkingLotDTO;
import org.example.service.ParkingService;
import org.example.service.UserService;
import org.example.session.UserSession;
import java.util.List;
public class AdressController {
    private ParkingService parkingService = new ParkingService();
    private UserService userService = new UserService();
    @FXML private VBox resultContainer;
    @FXML private TextField adresstxt;
    @FXML private Button btnBack;
    private static List<ParkingLotDTO> lastFoundList;
    private static boolean isAdminMode = false;
    public static void setAdminView(boolean admin) {
        isAdminMode = admin;
        lastFoundList = null;
    }
    @FXML
    public void initialize() {
        if (resultContainer != null) {
            handleViewLogic();
        }
    }
    private void handleViewLogic() {
        if (btnBack != null) {
            btnBack.setVisible(!isAdminMode);
            btnBack.setManaged(!isAdminMode);
        }
        if (isAdminMode) {
            List<ParkingLotDTO> allLots = parkingService.getParkingDTO();
            renderParkingList(allLots);
        } else if (lastFoundList != null) {
            renderParkingList(lastFoundList);
        }
    }
    public static boolean hasHistoryData() {
        return lastFoundList != null && !lastFoundList.isEmpty();
    }
    @FXML
    public void handleSearchAddress(ActionEvent event) {
        String addressInput = adresstxt.getText();
        if (addressInput == null || addressInput.trim().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Thông báo", "Vui lòng nhập địa chỉ!");
            return;
        }
        try {
            boolean success = userService.updateUserAddress(UserSession.getUserId(), addressInput);
            if (!success) {
                // Nếu osmAPI trả về null, dừng lại ở đây và báo lỗi
                showAlert(Alert.AlertType.ERROR, "Lỗi định vị",
                        "Không tìm thấy tọa độ cho địa chỉ này. Vui lòng thử lại với địa chỉ rõ ràng hơn!");
                return;
            }
            // 2. Nếu thành công, lúc này UserSession đã có Lat/Lon mới
            // Tính toán khoảng cách và lưu vào biến static
            lastFoundList = parkingService.SetDistance(parkingService.getParkingDTO());
            isAdminMode = false;
            // 3. Chuyển sang trang danh sách bãi xe
            UserController.loadInterface("/view/User/Parkingdashboad.fxml");
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Lỗi", "Không thể xác định vị trí.");
        }
    }
    public static void refreshDataForUser() {
        ParkingService ps = new ParkingService();
        lastFoundList = ps.SetDistance(ps.getParkingDTO());
    }
    private void renderParkingList(List<ParkingLotDTO> list) {
        resultContainer.getChildren().clear();
        for (ParkingLotDTO lot : list) {
            HBox card = new HBox(15);
            card.setStyle("-fx-background-color: #f4f4f4; -fx-padding: 12; -fx-border-color: #ddd; -fx-cursor: hand; -fx-border-radius: 8;");

            VBox info = new VBox(5);
            Label name = new Label("Bãi xe: " + lot.getName());
            name.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
            // Hiển thị khoảng cách tính từ địa chỉ user đã nhập
            String distLabel = isAdminMode ? "Quản trị hệ thống" : String.format("Cách bạn: %.2f km", lot.getDistance());
            Label dist = new Label(distLabel);
            dist.setStyle("-fx-text-fill: " + (isAdminMode ? "#7f8c8d;" : "#2e7d32;"));
            info.getChildren().addAll(name, dist);
            card.getChildren().add(info);
            card.setOnMouseClicked(e -> {
                if (isAdminMode) {
                    switchToParkingMap(lot.getName(), lot.getId());
                } else {
                    switchToParkingMap(lot.getName(), lot.getId());
                }
            });

            resultContainer.getChildren().add(card);
        }
    }
    private void switchToParkingMap(String lotName, int lotId) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Parking_map.fxml"));
            Parent root = loader.load();
            ParkingMapController controller = loader.getController();
            controller.setParkingLotData(lotName, lotId);
            UserController.loadNode(root);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @FXML
    public void backToInput() {
        UserController.loadInterface("/view/User/addressInput.fxml");
    }
    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}