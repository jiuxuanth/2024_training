package com.jiuth.sysmonitorcapture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Map;

@Component
public class MetricCollector {

    private static final String SERVER_URL = "http://127.0.0.1:8080/api/metric/upload";
    private static final String CPU_STAT_FILE_PATH = "/proc/stat";
    private static final String MEMINFO_FILE_PATH = "/proc/meminfo";
    private static final String STAT_FILE_HEADER = "cpu  ";
    private static final long INTERVAL = 60000L; // 60 seconds

    private final RestTemplate restTemplate;
    private long previousIdleTime = 0, previousTotalTime = 0;
    private final NumberFormat percentFormatter;

    @Autowired
    public MetricCollector(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
        this.percentFormatter = NumberFormat.getPercentInstance();
        percentFormatter.setMaximumFractionDigits(2);
    }

    @Scheduled(fixedRate = INTERVAL)
    public void captureMetrics() {
        double cpuUtilization = getCpuUtilization();
        double memUtilization = getMemUtilization();
        long timestamp = System.currentTimeMillis() / 1000;

        Map<String, Object> cpuMetric = createMetric("cpu.used.percent", cpuUtilization, timestamp);
        Map<String, Object> memMetric = createMetric("mem.used.percent", memUtilization, timestamp);

        // 输出详细的 CPU 和内存信息
        System.out.println("CPU Utilization: " + percentFormatter.format(cpuUtilization / 100));
        System.out.println("Memory Utilization: " + percentFormatter.format(memUtilization / 100));

        // 准备指标数组
        Map<String, Object>[] metricsArray = new Map[]{cpuMetric, memMetric};

        // 发送指标到服务器
        restTemplate.postForObject(SERVER_URL, metricsArray, String.class);
    }

    private double getCpuUtilization() {
        try (RandomAccessFile statFile = new RandomAccessFile(CPU_STAT_FILE_PATH, "r")) {
            String[] values = statFile.readLine().substring(STAT_FILE_HEADER.length()).split("\\s+");

            long idleTime = Long.parseLong(values[3]);
            long totalTime = 0;
            for (String value : values) {
                totalTime += Long.parseLong(value);
            }

            long idleTimeDelta = idleTime - previousIdleTime;
            long totalTimeDelta = totalTime - previousTotalTime;
            double utilization = 1 - ((double) idleTimeDelta) / totalTimeDelta;

            previousIdleTime = idleTime;
            previousTotalTime = totalTime;

            return utilization * 100;
        } catch (IOException e) {
            e.printStackTrace();
            return 0;
        }
    }

    private double getMemUtilization() {
        try (BufferedReader meminfoReader = new BufferedReader(new FileReader(MEMINFO_FILE_PATH))) {
            String line;
            long totalMemory = 0;
            long availableMemory = 0;

            while ((line = meminfoReader.readLine()) != null) {
                if (line.startsWith("MemTotal:")) {
                    totalMemory = parseMemoryValue(line);
                } else if (line.startsWith("MemAvailable:")) {
                    availableMemory = parseMemoryValue(line);
                }
            }

            long usedMemory = totalMemory - availableMemory;
            double memoryUsage = (double) usedMemory / totalMemory * 100;

            // 输出详细的内存信息
            System.out.println("Total Memory: " + totalMemory + " KB");
            System.out.println("Used Memory: " + usedMemory + " KB");

            return memoryUsage;
        } catch (IOException e) {
            e.printStackTrace();
            return 0;
        }
    }

    private long parseMemoryValue(String line) {
        String[] parts = line.split("\\s+");
        return Long.parseLong(parts[1]);
    }

    private Map<String, Object> createMetric(String metricName, double value, long timestamp) {
        Map<String, Object> metric = new HashMap<>();
        metric.put("metric", metricName);
        metric.put("endpoint", "my-computer");
        metric.put("timestamp", timestamp);
        metric.put("step", 60);
        metric.put("value", value);
        return metric;
    }
}
