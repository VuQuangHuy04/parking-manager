package org.example.controller;

import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import org.example.dao.BookingDaoimpl;
import java.util.Map;
public class RevenueController {
    @FXML
    private BarChart<String, Number> revenueChart;
    @FXML private CategoryAxis xAxis;
    @FXML private Label lblTotalRevenue;
    private BookingDaoimpl bookingDAO = new BookingDaoimpl();
    @FXML
    public void showDailyRevenue() {
        updateChart(bookingDAO.getRevenueByDay("2026-02"), "Ngày trong tháng");
    }
    // Hàm gọi khi nhấn nút "Xem theo Tháng" (năm 2026)
    @FXML
    public void showMonthlyRevenue() {
        updateChart(bookingDAO.getRevenueByMonth("2026"), "Tháng trong năm");
    }
    private void updateChart(Map<String, Double> data, String axisLabel) {
        xAxis.setLabel(axisLabel);
        revenueChart.getData().clear();
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Doanh thu (VND)");
        double total = 0;
        for (var entry : data.entrySet()) {
            series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
            total += entry.getValue();
        }
        revenueChart.getData().add(series);
        lblTotalRevenue.setText(String.format("%,.0f VND", total));
    }
}
