package com.jiuth.sysmonitorserver.controller;

import com.jiuth.sysmonitorserver.dao.enity.SysInfoCapture;
import com.jiuth.sysmonitorserver.service.SysInfoCaptureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class SysInfoCaptureController {

    @Autowired
    private SysInfoCaptureService sysInfoCaptureService;

    @PostMapping("/sysInfoCapture")
    @ResponseBody
    public long addNewSysInfoCapture(@RequestBody SysInfoCapture sysInfoCapture) {
        //TODO 校验
//        return Response.newSuccess(studentService.addNewStudent(studentDTO));
        return sysInfoCaptureService.addNewSysInfoCapture(sysInfoCapture);
    }
}
