package com.jiuth.sysmonitorcapture.storage;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class MySQLLogStorage implements LogStorage {

    private final String url = "jdbc:mysql://localhost:3306/jiu-sys-monitor?characterEncoding=utf-8";
    private final String username = "root";
    private final String password = "123456";

    @Override
    public void storeLogs(String hostname, String file, List<String> logs) {

    }

    @Override
    public void storeLog(String logEntry) {
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            String sql = "INSERT INTO logs (log_entry) VALUES (?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, logEntry);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
