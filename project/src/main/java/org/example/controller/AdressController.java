package org.example.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.DTO.ParkingLotDTO;
import org.example.api.osmAPI;
import org.example.service.ParkingService;
import org.example.service.UserService;
import org.example.session.UserSession;

import java.util.List;

public class AdressController {
    private ParkingService parkingService = new ParkingService();
    private UserService userService = new UserService();
    @FXML private VBox resultContainer;
      @FXML
     private TextField adresstxt;
      @FXML
     public void Submit(ActionEvent event){
          String adress = adresstxt.getText();
          Double[] ip = osmAPI.getCoordinateFromAddress(adress);
          if (adress.isEmpty()) {
              showAlert(Alert.AlertType.WARNING, "Thông báo", "Vui lòng nhập đầy đủ địa chỉ hiện tại");
              return;
          }else{
              userService.updateUserAddress(UserSession.getUserId(),adress);
              renderParkingList(parkingService.SetDistance(parkingService.getParkingDTO()));
          }

      }
    private void renderParkingList(List<ParkingLotDTO> list) {
        resultContainer.getChildren().clear(); // Xóa kết quả cũ
        for (ParkingLotDTO lot : list) {
            // Tạo một "Thẻ" bãi xe bằng HBox hoặc VBox
            HBox card = new HBox(15);
            card.setStyle("-fx-background-color: #f4f4f4; -fx-padding: 10; -fx-border-color: #ddd; -fx-cursor: hand; -fx-border-radius: 5;");
            VBox info = new VBox(5);
            Label name = new Label(lot.getName());
            name.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
            Label dist = new Label(String.format("%.2f km", lot.getDistance()));
            dist.setStyle("-fx-text-fill: #27ae60;");
            info.getChildren().addAll(name, dist);
            card.getChildren().add(info);
            // Sự kiện khi bấm vào bãi xe này
            card.setOnMouseClicked(e -> {
                switchToParkingMap(lot.getName(),lot.getId());
            });

            resultContainer.getChildren().add(card);
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
    private void switchToParkingMap(String lotName, int lotId) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/Parking_map.fxml"));
            Parent root = loader.load();
            ParkingMapController controller = loader.getController();
            controller.setParkingLotData(lotName,lotId);
            Stage stage = (Stage) adresstxt.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
