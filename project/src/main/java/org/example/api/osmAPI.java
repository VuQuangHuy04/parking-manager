package org.example.api;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
public class osmAPI {
    public static void getCoordinateFromAddress(String address) {
        try {
            String encodedAddress = URLEncoder.encode(address, StandardCharsets.UTF_8);
            String urlString = "https://nominatim.openstreetmap.org/search?q="
                    + encodedAddress + "&format=json&limit=1";
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("parking-manager", "MyJavaApp/1.0 (contact@example.com)");
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            String jsonResponse = response.toString();
            if (!jsonResponse.equals("[]")) {
                String lat = jsonResponse.split("\"lat\":\"")[1].split("\"")[0];
                String lon = jsonResponse.split("\"lon\":\"")[1].split("\"")[0];
                String displayName = jsonResponse.split("\"display_name\":\"")[1].split("\"")[0];
                System.out.println("Địa chỉ đầy đủ: " + displayName);
                System.out.println("Vĩ độ (Lat): " + lat);
                System.out.println("Kinh độ (Lon): " + lon);
            } else {
                System.out.println("Không tìm thấy địa chỉ này trên OpenStreetMap.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
