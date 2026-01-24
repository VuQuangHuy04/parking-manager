package org.example.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.api.osmAPI;
public class AdressController {
      @FXML
     private TextField adresstxt;
      @FXML
     public void Submit(ActionEvent event){
          String adress = adresstxt.getText();
          Double[] ip = osmAPI.getCoordinateFromAddress(adress);
          if (adress.isEmpty()) {
              showAlert(Alert.AlertType.WARNING, "Thông báo", "Vui lòng nhập đầy đủ địa chỉ hiện tại");
              return;
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
    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
