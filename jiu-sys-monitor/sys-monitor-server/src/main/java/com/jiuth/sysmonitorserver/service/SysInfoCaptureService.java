package com.jiuth.sysmonitorserver.service;

import com.jiuth.sysmonitorserver.dao.enity.SysInfoCapture;
import org.springframework.stereotype.Service;

@Service
public interface SysInfoCaptureService {

    //捕获信息的增加
     Long addNewSysInfoCapture(SysInfoCapture sysInfoCapture);


    //捕获信息的获取


}
