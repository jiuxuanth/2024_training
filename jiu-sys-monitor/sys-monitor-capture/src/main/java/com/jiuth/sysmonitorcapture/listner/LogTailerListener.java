package com.jiuth.sysmonitorcapture.listner;

import com.jiuth.sysmonitorcapture.storage.LogStorage;
import org.apache.commons.io.input.TailerListenerAdapter;

import java.util.ArrayList;
import java.util.List;

public class LogTailerListener extends TailerListenerAdapter {
    private LogStorage logStorage;
    private String filePath;
    private List<String> logs = new ArrayList<>();

    public LogTailerListener(LogStorage logStorage, String filePath) {
        this.logStorage = logStorage;
        this.filePath = filePath;
    }

    @Override
    public void handle(String line) {
        logs.add(line);
        if (logs.size() >= 10) { // 示例：每 10 行发送一次
            sendLogs();
            logs.clear();
        }
    }

    private void sendLogs() {
        String hostname = "my-computer"; // 示例：获取主机名
        logStorage.storeLogs(hostname, filePath, logs);
    }
}
