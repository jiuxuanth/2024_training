package com.jiuth.sysmonitorcapture.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jiuth.sysmonitorcapture.config.LogMonitorConfig;
import com.jiuth.sysmonitorcapture.config.LogMonitorConfig.Config;
import com.jiuth.sysmonitorcapture.storage.LocalFileLogStorage;
import com.jiuth.sysmonitorcapture.storage.LogStorage;
import com.jiuth.sysmonitorcapture.storage.MySQLLogStorage;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.apache.commons.io.input.Tailer;
import org.apache.commons.io.input.TailerListenerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

//结果实现一
@Component
public class LogMonitorService {

    private final LogMonitorConfig.Config logMonitorConfig;
    private final ObjectMapper objectMapper;
    private final RestTemplate restTemplate;
    private final Map<String, List<String>> fileLogs = new ConcurrentHashMap<>();
    private final ExecutorService executorService = Executors.newFixedThreadPool(1);

    private final AtomicInteger dataCounter = new AtomicInteger(0);
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private ScheduledFuture<?> sendJsonTask;

    @Autowired
    public LogMonitorService(LogMonitorConfig.Config logMonitorConfig, ObjectMapper objectMapper, RestTemplate restTemplate) {
        this.logMonitorConfig = logMonitorConfig;
        this.objectMapper = objectMapper;
        this.restTemplate = restTemplate;
    }

    @PostConstruct
    public void init() {
        List<String> files = logMonitorConfig.getFiles();
        for (String file : files) {
            startTailer(file);
        }
    }

    private void startTailer(String filePath) {
        File file = new File(filePath);
        Tailer tailer = new Tailer(file, new MyTailerListener(filePath), 1000, true);

        Thread thread = new Thread(tailer);
        thread.setDaemon(true);
        thread.start();
    }

    private class MyTailerListener extends TailerListenerAdapter {
        private final String filePath;

        public MyTailerListener(String filePath) {
            this.filePath = filePath;
        }

        @Override
        public void handle(String line) {
            fileLogs.computeIfAbsent(filePath, k -> new ArrayList<>()).add(line);
            dataCounter.incrementAndGet();

            // 如果已经有发送任务在执行，则取消当前计划，重新计划发送任务
            if (sendJsonTask != null && !sendJsonTask.isDone()) {
                sendJsonTask.cancel(false);
            }
            scheduleSendJsonTask();
        }

        private void scheduleSendJsonTask() {
            sendJsonTask = scheduler.schedule(() -> {
                try {
                    if (dataCounter.get() >= logMonitorConfig.getFiles().size()) {
                        System.out.print(fileLogs);

                        sendJsonData();

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    dataCounter.set(0);
                    fileLogs.clear();
                }
            }, 1, TimeUnit.SECONDS);
        }

        private void sendJsonData() throws Exception {
            List<Map<String, Object>> logEntries = new ArrayList<>();
            for (Map.Entry<String, List<String>> entry : fileLogs.entrySet()) {
                Map<String, Object> logEntry = new HashMap<>();
                logEntry.put("hostname", "my-computer");
                logEntry.put("file", entry.getKey());
                logEntry.put("logs", new ArrayList<>(entry.getValue()));
                logEntries.add(logEntry);
            }

            String json = objectMapper.writeValueAsString(logEntries);
            System.out.println(json);
            storeLogs();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> entity = new HttpEntity<>(json, headers);

            String url = "http://localhost:8080/api/log/upload"; // Replace with your actual API endpoint
            ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                System.out.println("JSON data sent successfully.");
            } else {
                System.out.println("Failed to send JSON data. Response code: " + response.getStatusCodeValue());
            }


        }
    }

    // 独立的存储日志方法
    private void storeLogs() {
        try {
            if ("local_file".equals(logMonitorConfig.getLogStorage())) {
                storeLogsToFile();
            } else if ("mysql".equals(logMonitorConfig.getLogStorage())) {
                storeLogsToDatabase();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void storeLogsToFile() throws IOException {
        for (Map.Entry<String, List<String>> entry : fileLogs.entrySet()) {
            File logFile = new File("stored_logs/" + entry.getKey().replaceAll("[^a-zA-Z0-9.-]", "_") + ".log");
            logFile.getParentFile().mkdirs();
            try (FileWriter writer = new FileWriter(logFile, true)) {
                for (String log : entry.getValue()) {
                    writer.write(log + System.lineSeparator());
                }
            }
        }
    }

    private void storeLogsToDatabase() throws SQLException {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/jiu_log_monitor", "root", "123456")) {
            String createTableSQL = "CREATE TABLE IF NOT EXISTS logs (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY," +
                    "hostname VARCHAR(255) NOT NULL," +
                    "file VARCHAR(255) NOT NULL," +
                    "log TEXT NOT NULL," +
                    "timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                    ")";
            try (Statement stmt = conn.createStatement()) {
                stmt.execute(createTableSQL);
            }

            String insertSQL = "INSERT INTO logs (hostname, file, log) VALUES (?, ?, ?)";
            try (PreparedStatement pstmt = conn.prepareStatement(insertSQL)) {
                for (Map.Entry<String, List<String>> entry : fileLogs.entrySet()) {
                    for (String log : entry.getValue()) {
                        pstmt.setString(1, "my-computer");
                        pstmt.setString(2, entry.getKey());
                        pstmt.setString(3, log);
                        pstmt.addBatch();
                    }
                }
                pstmt.executeBatch();
            }
        }
    }

    @PreDestroy
    public void destroy() {
        executorService.shutdown();
        scheduler.shutdown();
    }
}

//@Component
//public class LogMonitorService {
//
//    private final LogMonitorConfig.Config logMonitorConfig;
//    private final ObjectMapper objectMapper;
//    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
//    private final Map<String, List<String>> fileLogs = new ConcurrentHashMap<>();
//    private final Map<String, ScheduledFuture<?>> futures = new ConcurrentHashMap<>();
//
//    private final RestTemplate restTemplate;
//
//    @Autowired
//    public LogMonitorService(LogMonitorConfig.Config logMonitorConfig, ObjectMapper objectMapper, RestTemplate restTemplate) {
//        this.logMonitorConfig = logMonitorConfig;
//        this.objectMapper = objectMapper;
//        this.restTemplate = restTemplate;
//    }
//
//    @PostConstruct
//    public void init() {
//        // 启动日志文件监控
//        List<String> files = logMonitorConfig.getFiles();
//        for (String file : files) {
//            startTailer(file);
//        }
//    }
//
//    private void startTailer(String filePath) {
//        File file = new File(filePath);
//        Tailer tailer = new Tailer(file, new MyTailerListener(filePath), 1000,true);
//
//        Thread thread = new Thread(tailer);
//        thread.setDaemon(true);
//        thread.start();
//    }
//
//    private class MyTailerListener extends TailerListenerAdapter {
//        private final String filePath;
//
//        public MyTailerListener(String filePath) {
//            this.filePath = filePath;
//        }
//
//        @Override
//        public void handle(String line) {
//            fileLogs.computeIfAbsent(filePath, k -> new ArrayList<>()).add(line);
//            ScheduledFuture<?> future = futures.get(filePath);
//            if (future != null) {
//                future.cancel(false);
//            }
//            future = scheduler.schedule(() -> {
//                try {
//                    List<String> logs = fileLogs.get(filePath);
//                    if (logs != null && !logs.isEmpty()) {
//                        String json = buildJson(filePath, logs);
//                        System.out.println(json);  // 打印 JSON 格式的日志内容
//                        sendJsonData(json);
//                        fileLogs.remove(filePath);  // 清除已经发送的日志
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }, 1, TimeUnit.SECONDS);  // 设置文件变动结束的延迟时间为 1 秒
//            futures.put(filePath, future);
//        }
//
//        private String buildJson(String filePath, List<String> logs) throws Exception {
//            Map<String, Object> logEntry = new HashMap<>();
//            logEntry.put("hostname", "my-computer");
//            logEntry.put("file", filePath);
//            logEntry.put("logs", new ArrayList<>(logs));
//
//            List<Map<String, Object>> logEntries = new ArrayList<>();
//            logEntries.add(logEntry);
//
//            return objectMapper.writeValueAsString(logEntries);
//        }
//
//        private void sendJsonData(String json) {
//            HttpHeaders headers = new HttpHeaders();
//            headers.setContentType(MediaType.APPLICATION_JSON);
//            HttpEntity<String> entity = new HttpEntity<>(json, headers);
//
//            String url = "http://localhost:8080/api/log/upload"; // Replace this with your actual API endpoint
//
//            ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);
//
//            if (response.getStatusCode() == HttpStatus.OK) {
//                System.out.println("JSON data sent successfully.");
//            } else {
//                System.out.println("Failed to send JSON data. Response code: " + response.getStatusCodeValue());
//            }
//        }
//    }
//}