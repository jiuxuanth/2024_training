package com.example.demo.service;

import com.example.demo.converter.StudentConverter;
import com.example.demo.dao.Student;
import com.example.demo.dao.StudentRepository;
import com.example.demo.dto.StudentDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class StudentServiceImpl implements StudentService {

    @Autowired
    private StudentRepository studentRepository;

    @Override
    public StudentDTO getStudentById(long id) {
        Student student=  studentRepository.findById(id).orElseThrow(RuntimeException::new);
        return  StudentConverter.convertStudentToStudentDTO(student);
    }

    @Override
    public Long addNewStudent(StudentDTO studentDTO) {
        List<Student> studentList=studentRepository.findByEmail(studentDTO.getEmail());
        if(!CollectionUtils.isEmpty(studentList)){
            throw new IllegalStateException("email:" + studentDTO.getEmail() + " has been taken");
        }
        Student student=studentRepository.save(StudentConverter.convertStudentDTOToStudent(studentDTO));
        return student.getId();
    }

    @Override
    public void deletrStudentById(long id) {
        studentRepository.findById(id).orElseThrow(()->new RuntimeException("id" + id +"does not exist!"));
        studentRepository.deleteById(id);
    }

    @Override
    @Transactional //操作失败回滚数据
    public StudentDTO updateStudentById(long id, String name, String email) {
        Student stundentInDB=studentRepository.findById(id).orElseThrow(()->new RuntimeException("id" + id +"does not exist!"));
        if(StringUtils.hasText(name)&& !stundentInDB.getName().equals(name)){
            stundentInDB.setName(name);
        }
        if(StringUtils.hasText(email)&&stundentInDB.getEmail().equals(email)){
            stundentInDB.setEmail(email);
        }
        Student student= studentRepository.save(stundentInDB);
        return StudentConverter.convertStudentToStudentDTO(student);
    }
}
