package org.example.controller;

import javafx.concurrent.Worker;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import netscape.javascript.JSObject;
import org.example.session.UserSession;

public class UserController {
    @FXML
    private StackPane contentPane;

    @FXML
    private Label usernameLabel;

    @FXML
    public void initialize() {
        usernameLabel.setText("Xin chào, " + UserSession.getUser().getUsername());
    }
    @FXML
    public void openParkingLots(ActionEvent event){
        switchScene(event,"/view/User/adressUser.fxml");
    }

    @FXML
    public void openHistory(ActionEvent event){
        switchScene(event,"/view/User/adressUser.fxml");
    }
    @FXML
    public void openInfomation(ActionEvent event){
        switchScene(event,"/view/User/adressUser.fxml");
    }

    @FXML
    public void handleLogout(ActionEvent event)  {
        switchScene(event,"/view/Logindashboard.fxml");
    }
    private void switchScene(ActionEvent event, String fxmlPath) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxmlPath));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.centerOnScreen();
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "loi", "Không tìm thấy file giao diện: " + fxmlPath);
        }
    }
    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

}
