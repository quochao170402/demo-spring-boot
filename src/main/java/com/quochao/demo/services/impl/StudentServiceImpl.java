package com.quochao.demo.services.impl;

import com.quochao.demo.dtos.CreationStudentDTO;
import com.quochao.demo.entities.Student;
import com.quochao.demo.repositories.StudentRepository;
import com.quochao.demo.services.StudentService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class StudentServiceImpl implements StudentService {
    private final StudentRepository studentRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public StudentServiceImpl(StudentRepository studentRepository, ModelMapper modelMapper) {
        this.studentRepository = studentRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<Student> getStudents() {
        return studentRepository.findAll();
    }

    @Override
    public void registerStudent(Student student) {
//        Check email is exists
        if (studentRepository.findStudentByEmail(student.getEmail()).isPresent())
            throw new IllegalStateException("Email taken");
        else studentRepository.save(student);
//        System.out.println(student);
    }

    @Override
    public void deleteStudent(Long id) {
        if (!studentRepository.existsById(id))
            throw new IllegalStateException("Student with id " + id + " does not exists");
        studentRepository.deleteById(id);
    }

    @Transactional
    @Override
    public Student updateStudent(Student student) {
        Student updated = studentRepository.findById(student.getId())
                .orElseThrow(() -> new IllegalStateException("Student with id" + student.getId() + " does not exists"));


        String email = student.getEmail();
        if (email == null || email.isEmpty()) {
            throw new IllegalStateException("Student's email can not null");
        }

        if (studentRepository.findStudentByEmail(email).isPresent()) {
            throw new IllegalStateException("Email taken");
        }

        updated.setEmail(email);

        if (student.getName() != null && !student.getName().isEmpty()) {
            updated.setName(student.getName());
        }

        if (student.getDob() != null) {
            updated.setDob(student.getDob());
        }
        return updated;
    }

    //    Convert entities to dtos
    public CreationStudentDTO convertStudentToDto(Student student) {
        CreationStudentDTO creationStudentDto = new CreationStudentDTO();
        creationStudentDto = modelMapper.map(student, CreationStudentDTO.class);
        return creationStudentDto;
    }

    public Student convertStudentDtoToStudent(CreationStudentDTO dto) {
        return modelMapper.map(dto, Student.class);
    }
}
