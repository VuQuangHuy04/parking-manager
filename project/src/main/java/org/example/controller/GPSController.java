package org.example.controller;

import javafx.application.Platform;
import javafx.concurrent.Worker;
import javafx.fxml.FXML;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import java.util.Scanner;
import java.net.URL;

public class GPSController {
    @FXML
    private WebView webView;

    @FXML
    public void initialize() {
        WebEngine engine = webView.getEngine();
        engine.load(getClass().getResource("/web/map.html").toExternalForm());
        engine.getLoadWorker().stateProperty().addListener((obs, oldState, newState) -> {
            if (newState == Worker.State.SUCCEEDED) {
                System.out.println("✅ WebView đã sẵn sàng");
                fetchAndSendLocation();
            }
        });
    }
    private void fetchAndSendLocation() {
        // Chạy tác vụ mạng trên Thread riêng để không làm treo UI
        new Thread(() -> {
            try {
                // Gọi API lấy vị trí qua IP (Free)
                URL url = new URL("http://ip-api.com/json");
                Scanner s = new Scanner(url.openStream(), "UTF-8").useDelimiter("\\A");
                String response = s.next();
                s.close();
                // Parse tọa độ từ chuỗi JSON trả về
                double lat = Double.parseDouble(response.split("\"lat\":")[1].split(",")[0]);
                double lon = Double.parseDouble(response.split("\"lon\":")[1].split(",")[0]);
                System.out.println("📍 Tọa độ tìm thấy: " + lat + ", " + lon);
                // Gửi tọa độ vào hàm JavaScript trong WebView
                Platform.runLater(() -> {
                    webView.getEngine().executeScript("updateMap(" + lat + ", " + lon + ")");
                });

            } catch (Exception e) {
                System.err.println("❌ Lỗi lấy vị trí: " + e.getMessage());
            }
        }).start();
    }
}