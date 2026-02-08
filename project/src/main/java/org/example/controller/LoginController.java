package org.example.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.example.model.User;
import org.example.service.AuthService;
import org.example.session.UserSession;

public class LoginController {
    private AuthService authService = new AuthService();
    @FXML
    private TextField username;
    @FXML
    private PasswordField password;
    @FXML private Button LoginClick;
    @FXML private Hyperlink Dangkingay;
    @FXML
    public void LoginClick(ActionEvent event){
        String user = username.getText();
        String pass = password.getText();
        if (user.isEmpty() || pass.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Thông báo", "Vui lòng nhập đầy đủ tài khoản và mật khẩu!");
            return;
        }
        User users = authService.login(user,pass);
        UserSession.setUser(users); // set usersession de su dung
        if(users!= null){
           if(users.getRole().equals("ADMIN")){
                switchScene(event,"/view/User/Userdashboard.fxml");
            }else{
                switchScene(event,"/view/User/Userdashboard.fxml");
           }
        }else{
            showAlert(Alert.AlertType.ERROR,"loi","sai tai khoan hoac mat khau");
        }
    }
    private void switchScene(ActionEvent event,String fxmlPath) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxmlPath));
            Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.centerOnScreen();
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR,"loi","Không tìm thấy file giao diện: " + fxmlPath);
        }
    }
    @FXML
    private void Dangkingay(ActionEvent event){
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/view/regsister.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.centerOnScreen();
            stage.show();
        }catch (Exception e){
            e.printStackTrace();
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
