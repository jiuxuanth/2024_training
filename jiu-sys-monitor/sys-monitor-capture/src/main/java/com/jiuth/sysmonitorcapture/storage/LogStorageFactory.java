package com.jiuth.sysmonitorcapture.storage;

import javax.sql.DataSource;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.Map;
import java.util.logging.Logger;


public class LogStorageFactory {
    public static LogStorage createLogStorage(String type, Map<String, String> config) {
        switch (type.toLowerCase()) {
            case "local_file":
                return new LocalFileLogStorage(config.get("logDir"));
            case "mysql":
                DataSource dataSource = createDataSource(config);
                return new LocalFileLogStorage(config.get("logDir"));
            default:
                throw new IllegalArgumentException("Unsupported log storage type: " + type);
        }
    }

    private static DataSource createDataSource(Map<String, String> config) {
//        HikariConfig hikariConfig = new HikariConfig();
//        hikariConfig.setJdbcUrl(config.get("jdbcUrl"));
//        hikariConfig.setUsername(config.get("username"));
//        hikariConfig.setPassword(config.get("password"));
        return new DataSource() {
            @Override
            public <T> T unwrap(Class<T> iface) throws SQLException {
                return null;
            }

            @Override
            public boolean isWrapperFor(Class<?> iface) throws SQLException {
                return false;
            }

            @Override
            public Connection getConnection() throws SQLException {
                return null;
            }

            @Override
            public Connection getConnection(String username, String password) throws SQLException {
                return null;
            }

            @Override
            public PrintWriter getLogWriter() throws SQLException {
                return null;
            }

            @Override
            public void setLogWriter(PrintWriter out) throws SQLException {

            }

            @Override
            public void setLoginTimeout(int seconds) throws SQLException {

            }

            @Override
            public int getLoginTimeout() throws SQLException {
                return 0;
            }

            @Override
            public Logger getParentLogger() throws SQLFeatureNotSupportedException {
                return null;
            }
        };
    }
}