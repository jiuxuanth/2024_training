package com.jiuth.sysmonitorcapture.storage;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

public class LocalFileLogStorage extends BaseLogStorage {
    private final String logDir;

    public LocalFileLogStorage(String logDir) {
        this.logDir = logDir;
    }

    @Override
    public void storeLogs(String hostname, String file, List<String> logs) {
        String logFilePath = Paths.get(logDir, hostname + "_" + file).toString();
        try (FileWriter writer = new FileWriter(logFilePath, true)) {
            for (String log : logs) {
                writer.write(log + System.lineSeparator());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
