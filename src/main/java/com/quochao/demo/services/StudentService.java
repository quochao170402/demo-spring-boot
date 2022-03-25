package com.quochao.demo.services;

import com.quochao.demo.entities.Student;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface StudentService {
    List<Student> getStudents();

    void registerStudent(Student student);

    void deleteStudent(Long id);

    @Transactional
    Student updateStudent(Student student);
}
