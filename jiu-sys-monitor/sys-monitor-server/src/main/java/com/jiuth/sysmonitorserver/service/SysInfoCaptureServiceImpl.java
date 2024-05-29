package com.jiuth.sysmonitorserver.service;

import com.jiuth.sysmonitorserver.dao.SysInfoCaptureRepository;
import com.jiuth.sysmonitorserver.dao.enity.SysInfoCapture;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SysInfoCaptureServiceImpl implements SysInfoCaptureService {
    @Autowired
    private SysInfoCaptureRepository sysInfoCaptureRepository;

    @Override
    public Long addNewSysInfoCapture(SysInfoCapture sysInfoCapture) {
//        List<Student> studentList=studentRepository.findByEmail(studentDTO.getEmail());
//        if(!CollectionUtils.isEmpty(studentList)){
//            throw new IllegalStateException("email:" + studentDTO.getEmail() + " has been taken");
//        }
//        Student student=studentRepository.save(StudentConverter.convertStudentDTOToStudent(studentDTO));
//        return student.getId();
        System.out.println(sysInfoCapture.toString());
//        sysInfoCapture.setTags("");
        SysInfoCapture sysInfoCaptureResult = sysInfoCaptureRepository.save(sysInfoCapture);
        return sysInfoCaptureResult.getId();
//        return 0L;
    }
}
