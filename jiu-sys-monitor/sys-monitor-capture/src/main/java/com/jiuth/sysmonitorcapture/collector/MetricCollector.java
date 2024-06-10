package com.jiuth.sysmonitorcapture.collector;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jiuth.sysmonitorcapture.util.OSVersionUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Component
public class MetricCollector {

    @Value("${server.url}")
    private String serverUrl;

    @Value("${interval}")
    private long interval;

    private static String endpoint= System.getProperty("user.name", "UnknownUser")+"@"+OSVersionUtil.getLinuxDistribution();;

    private final RestTemplate restTemplate;
    private final CpuUtilizationCollector cpuUtilizationCollector;
    private final MemoryUtilizationCollector memoryUtilizationCollector;

    public MetricCollector(RestTemplate restTemplate, CpuUtilizationCollector cpuUtilizationCollector,
                           MemoryUtilizationCollector memoryUtilizationCollector) {
        this.restTemplate = restTemplate;
        this.cpuUtilizationCollector = cpuUtilizationCollector;
        this.memoryUtilizationCollector = memoryUtilizationCollector;
    }

    @Scheduled(fixedRateString = "${interval}")
    public void captureMetrics() {
        double cpuUtilization = cpuUtilizationCollector.getCpuUtilization();
        double memUtilization = memoryUtilizationCollector.getMemUtilization();
        long timestamp = System.currentTimeMillis() / 1000;

        Map<String, Object> cpuMetric = createMetric("cpu.used.percent", cpuUtilization, timestamp);
        Map<String, Object> memMetric = createMetric("mem.used.percent", memUtilization, timestamp);

        // Print detailed CPU and memory information
        System.out.println("CPU Utilization: " + cpuUtilization);
        System.out.println("Memory Utilization: " + memUtilization);

        // Prepare metrics array
        Map<String, Object>[] metricsArray = new Map[]{cpuMetric, memMetric};

        // Convert metrics array to JSON
        try {
            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writeValueAsString(metricsArray);
            System.out.println("Sending JSON: " + json);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        // Send metrics to server
        String response = restTemplate.postForObject(serverUrl, metricsArray, String.class);
        System.out.println("Server Response JSON: " + response);
    }

    private Map<String, Object> createMetric(String metricName, double value, long timestamp) {
        Map<String, Object> metric = new HashMap<>();
        metric.put("metric", metricName);
        metric.put("endpoint", endpoint);
        metric.put("timestamp", timestamp);
        metric.put("step", interval/1000);
        metric.put("value", value);
        return metric;
    }
}