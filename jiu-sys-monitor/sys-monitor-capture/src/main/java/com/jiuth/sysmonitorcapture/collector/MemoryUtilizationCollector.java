package com.jiuth.sysmonitorcapture.collector;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

@Component
public class MemoryUtilizationCollector {

    @Value("${meminfo.file.path}")
    private String meminfoFilePath;

    public double getMemUtilization() {
        try (BufferedReader meminfoReader = new BufferedReader(new FileReader(meminfoFilePath))) {
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
}