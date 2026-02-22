package org.example;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.scheduler.BookingScheduler;
import org.example.utils.DBConnection;
import org.example.utils.DBinit;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main extends Application {
    @Override
    public void start(Stage s)  throws Exception{
        BookingScheduler.start();
        s.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/logindashboard.fxml"))));
        s.setTitle("Hệ thống đặt xe");
        s.show();
        System.out.println("connect");
    }
    public static void main(String[] args) {
        launch();
        }
    }
