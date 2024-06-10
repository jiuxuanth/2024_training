package com.jiuth.sysmonitorcapture.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * 获取Linux发行版信息
 * @return 发行版描述信息，如果获取失败则返回"Unknown"
 */
public class OSVersionUtil {

    public static String getLinuxDistribution() {
        String distribution = "Unknown";
        BufferedReader reader = null;

        try {
            // 执行lsb_release -d命令获取发行版描述信息
            Process process = Runtime.getRuntime().exec("lsb_release -d");
            reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;

            // 读取命令输出并提取描述信息
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("Description:")) {
                    distribution = line.substring(line.indexOf(':') + 1).trim();
                    break;
                }
            }

            // 等待命令执行完毕
            process.waitFor();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return distribution;
    }


    /**
     * 获取Linux发行版版本号
     * @return 发行版版本号，如果获取失败则返回"Unknown"
     */
    public static String getLinuxVersion() {
        String version = "Unknown";
        String distributionInfo = getLinuxDistribution();

        // 从发行版描述信息中提取版本号
        if (!distributionInfo.equals("Unknown")) {
            if (distributionInfo.contains("Ubuntu")) {
                version = distributionInfo.substring(distributionInfo.indexOf(' ') + 1);
            }
            if (distributionInfo.contains("Fedora")) {
                version = distributionInfo.substring(distributionInfo.indexOf(' ') + 1, distributionInfo.lastIndexOf(' '));
            }
            // 可根据需要添加更多发行版的版本提取规则
        }

        return version;
    }

    public static void main(String[] args) {
        String distribution = getLinuxDistribution();
        String version = getLinuxVersion();

        System.out.println("Linux Distribution: " + distribution);
        System.out.println("Linux Version: " + version);
    }
}