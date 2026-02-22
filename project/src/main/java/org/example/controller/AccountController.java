package org.example.controller;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.example.model.User;
import org.example.service.UserService;
import org.example.session.UserSession;

public class AccountController {
    @FXML
    private Label usernameLabel;
    @FXML private Label roleLabel;
    @FXML private TextField emailField;
    @FXML private Button saveBtn;
    private UserService userService = new UserService();
    @FXML
    public void initialize() {
        int userId = UserSession.getUserId();
        User user = userService.getCurrentUser(userId);
        usernameLabel.setText(user.getUsername());
        roleLabel.setText(user.getRole());
        if (user.getMail() != null) {
            emailField.setText(user.getMail());
            emailField.setDisable(true);
            saveBtn.setDisable(true);
        }
    }
    @FXML
    public void onSaveEmail() {
        String email = emailField.getText();
        int userId = UserSession.getUserId();

        if (email.isEmpty()) {
            showAlert("Email không được trống");
            return;
        }

        boolean success = userService.updateEmail(userId, email);

        if (success) {
            showAlert("Cập nhật email thành công!");
            emailField.setDisable(true);
            saveBtn.setDisable(true);
        } else {
            showAlert("Email đã tồn tại hoặc không thể cập nhật");
        }
    }

    private void showAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
