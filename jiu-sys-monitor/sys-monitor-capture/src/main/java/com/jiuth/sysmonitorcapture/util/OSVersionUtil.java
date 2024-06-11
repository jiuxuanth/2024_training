package com.jiuth.sysmonitorcapture.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class OSVersionUtil {

    public static String getSystemVersion() {
        String version = "Unknown";

        try {
            Process process = Runtime.getRuntime().exec("lsb_release -a");
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;

            while ((line = reader.readLine()) != null) {
                if (line.startsWith("Description:")) {
                    version = line.substring(line.indexOf(':') + 1).trim();
                    break;
                }
            }

            process.waitFor();
            reader.close();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        // 去除版本号中的空格
        version = version.replace(" ", "");

        return version;
    }

    public static void main(String[] args) {
        String version = getSystemVersion();
        System.out.println("System Version: " + version);
    }
}