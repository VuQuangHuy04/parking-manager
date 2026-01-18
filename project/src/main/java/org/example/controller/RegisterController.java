package org.example.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.service.AuthService;

public class RegisterController {
    private AuthService authService = new AuthService();
       @FXML
       private TextField username;
       @FXML
       private PasswordField password;
       @FXML
       private PasswordField repassword;
       @FXML
       private void Submit(ActionEvent event){
           String user = username.getText();
           String pass = password.getText();
           String repass = repassword.getText();
           if (user.isEmpty() || pass.isEmpty()) {
               ShowAlert(Alert.AlertType.WARNING, "Thông báo", "Vui lòng nhập đầy đủ tài khoản và mật khẩu!");
               return;
           }
           switch (authService.Regsister(user,pass,repass)){
               case SUCCESS -> ShowAlert(Alert.AlertType.INFORMATION,"Thông báo","Đăng kí thành công");
               case ERROR -> ShowAlert(Alert.AlertType.INFORMATION,"Thông báo","Lỗi Đăng kí");
               case PASSWORD_NOT_MATCH -> ShowAlert(Alert.AlertType.INFORMATION,"Thông báo","Password không giống nhau");
               case USERNAME_EXISTS ->ShowAlert(Alert.AlertType.INFORMATION,"Thông báo","User đã tồn tại");
           }
       }
       @FXML
       private void BackLogin(ActionEvent event){
           try {
               Parent root = FXMLLoader.load(getClass().getResource("/view/logindashboard.fxml"));
               Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
               stage.setScene(new Scene(root));
               stage.centerOnScreen();
               stage.show();
           }catch (Exception e){
               e.printStackTrace();
           }
       }
       private void ShowAlert(Alert.AlertType type, String title, String content){
           Alert alert = new Alert(type);
           alert.setTitle(title);
           alert.setContentText(content);
           alert.showAndWait();
       }
}
