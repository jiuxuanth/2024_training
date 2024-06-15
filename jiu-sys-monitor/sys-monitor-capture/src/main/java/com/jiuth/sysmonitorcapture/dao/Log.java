package com.jiuth.sysmonitorcapture.dao;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author jiuth
 */
@Data
public class Log implements Serializable {
    //采集主机名
    private String hostName;
    //文件路径
    private String file;
    //日志内容
    private List<String> logs;

    @Override
    public String toString() {
        return "Log{" +
                "hostName= \"" + hostName + '\"' + '\n' +
                "file= \"" + file +  '\"'  + '\n' +
                '}';
    }
}