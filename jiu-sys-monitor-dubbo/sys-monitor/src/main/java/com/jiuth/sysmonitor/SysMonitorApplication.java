package com.jiuth.sysmonitor;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableDubbo
public class SysMonitorApplication {

    public static void main(String[] args) {
        SpringApplication.run(SysMonitorApplication.class, args);
    }

}
