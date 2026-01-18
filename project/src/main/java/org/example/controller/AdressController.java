package org.example.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import org.example.api.osmAPI;
public class AdressController {
      @FXML
     private TextField adresstxt;
      @FXML
     public void Submit(ActionEvent event){
          String adress = adresstxt.getText();
          osmAPI.getCoordinateFromAddress(adress);
      }
}
