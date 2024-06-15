package com.jiuth.sysmonitorcapture.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.util.List;


/**
 * @author jiuth
 */
@Configuration
public class LogMonitorConfig {

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Bean
    public Config logMonitorConfiguration(ObjectMapper objectMapper) throws IOException {
        return objectMapper.readValue(
                getClass().getClassLoader().getResource("cfg.json"),
                Config.class
        );
    }

    public static class Config {
        @JsonProperty("files")
        private List<String> files;

        @JsonProperty("log_storage")
        private String logStorage;

        // Getters and Setters
        public List<String> getFiles() {
            return files;
        }

        public void setFiles(List<String> files) {
            this.files = files;
        }

        public String getLogStorage() {
            return logStorage;
        }

        public void setLogStorage(String logStorage) {
            this.logStorage = logStorage;
        }
    }
}
