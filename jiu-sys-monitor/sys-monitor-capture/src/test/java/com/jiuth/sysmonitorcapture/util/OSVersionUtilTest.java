package com.jiuth.sysmonitorcapture.util;

import org.junit.jupiter.api.Test;

public class OSVersionUtilTest {

    @Test
    public void OSVersionUtilTest() {
        String distribution = OSVersionUtil.getLinuxDistribution();
        String version = OSVersionUtil.getLinuxVersion();

        System.out.println("Linux Distribution: " + distribution);
        System.out.println("Linux Version: " + version);
    }
}
