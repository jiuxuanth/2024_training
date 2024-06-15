package com.jiuth.sysmonitorcapture.storage;

import java.util.List;

public interface LogStorage {
    void storeLogs(String hostname, String file, List<String> logs);

    void storeLog(String line);
}
