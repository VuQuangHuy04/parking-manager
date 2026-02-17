package org.example.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.session.UserSession;
import java.io.IOException;

public class UserController {
    @FXML private StackPane contentPane;
    @FXML private Label usernameLabel;
    @FXML private VBox userMenuBox;
    @FXML private VBox adminMenuBox;
    private static UserController instance;
    @FXML
    public void initialize() {
        instance = this;
        if (UserSession.getUser() != null) {
            String username = UserSession.getUser().getUsername();
            String role = UserSession.getUser().getRole();
            usernameLabel.setText("Xin chào, " + username + " (" + role + ")");
            // 2. Phân quyền hiển thị Menu
            boolean isAdmin = "ADMIN".equalsIgnoreCase(role);
            adminMenuBox.setVisible(isAdmin);
            adminMenuBox.setManaged(isAdmin);
            userMenuBox.setVisible(!isAdmin);
            userMenuBox.setManaged(!isAdmin);
        }
    }
    @FXML public void openParkingLots(ActionEvent event) {
        if ("ADMIN".equalsIgnoreCase(UserSession.getUser().getRole())) {
            AdressController.setAdminView(true);
            loadInterface("/view/User/Parkingdashboad.fxml");
        } else {
            AdressController.setAdminView(false);
            // Nếu đã có tọa độ từ lần nhập trước hoặc từ Database
            if (UserSession.getUserLat() != 0 || AdressController.hasHistoryData()) {
                // Nếu Session có tọa độ nhưng danh sách chưa tính, thì tính lại
                if (!AdressController.hasHistoryData()) {
                    AdressController.refreshDataForUser();
                }
                loadInterface("/view/User/Parkingdashboad.fxml");
            } else {
                // Chỉ hiện trang nhập nếu hoàn toàn chưa có vị trí
                loadInterface("/view/User/addressInput.fxml");
            }
        }
    }
    @FXML public void openHistory(ActionEvent event) { loadInterface("/view/User/UserHistory.fxml"); }
    @FXML public void openInformation(ActionEvent event) { loadInterface("/view/User/profile.fxml"); }
    // --- CÁC HÀM CỦA ADMIN ---
    @FXML public void openAdminControl(ActionEvent event) {
        AdressController.setAdminView(true);
        loadInterface("/view/User/Parkingdashboad.fxml");
    }
    @FXML public void openUserManagement(ActionEvent event) { loadInterface("/view/RevenueView.fxml"); }
    @FXML public void openSystemReport(ActionEvent event) { loadInterface("/view/Admin/reports.fxml"); }
    // --- HÀM LOGOUT ---
    @FXML
    public void handleLogout(ActionEvent event) {
        try {
            UserSession.logout();
            Parent root = FXMLLoader.load(getClass().getResource("/view/Logindashboard.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.centerOnScreen();
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void loadInterface(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(UserController.class.getResource(fxmlPath));
            Node node = loader.load();
            loadNode(node);
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Không tìm thấy file FXML: " + fxmlPath);
        }
    }
    public static void loadNode(Node newNode) {
        if (instance != null && instance.contentPane != null) {
            instance.contentPane.getChildren().setAll(newNode);
        } else {
            System.err.println("UserController instance is null!");
        }
    }
    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}