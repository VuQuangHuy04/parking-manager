package org.example.utils;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.Statement;

public class DBinit {
    public static void InitDB(){
        try (InputStream is = DBinit.class.getResourceAsStream("/data/data.sql");
             Connection conn = DBConnection.getConnection()) {
            if (is == null) {
                System.err.println("LỖI: Không tìm thấy file data.sql trong resources/data/");
                return;
            }
            String sql = new String(is.readAllBytes(), StandardCharsets.UTF_8);
            Statement stmt = conn.createStatement();
            for (String cmd : sql.split(";")) {
                if (!cmd.trim().isEmpty()) {
                    stmt.execute(cmd);
                }
            }
        } catch (Exception e) {
            System.err.println("LỖI khi khởi tạo Database:");
            e.printStackTrace();
        }
    }
    }

