package com.quochao.demo.controllers;

import com.quochao.demo.entities.Student;
import com.quochao.demo.services.impl.StudentServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping(path = "/api/v1/student")
public class StudentController {

    private final StudentServiceImpl studentServiceImpl;

    @Autowired
    public StudentController(StudentServiceImpl studentServiceImpl) {
        this.studentServiceImpl = studentServiceImpl;
    }

    @GetMapping
    public List<Student> getStudents() {
        return studentServiceImpl.getStudents();
    }

    @PostMapping
    public void registerStudent(@ModelAttribute Student student) {
        studentServiceImpl.registerStudent(student);
    }

    @PutMapping
    public Student updateStudent(@RequestBody Student student) {
        return studentServiceImpl.updateStudent(student);
    }

    @DeleteMapping(path = "{id}")
    public void deleteStudent(@PathVariable("id") Long id) {
        studentServiceImpl.deleteStudent(id);
    }

    //    Upload file to local storage in server
    @Value("${file.upload-dir}")
    String FILE_DIR;

    @PostMapping(path = "/upload")
    public Boolean uploadImageToLocalDirectory(@RequestParam("File") MultipartFile file) throws IOException {
        File myFile = new File(FILE_DIR + file.getOriginalFilename());
//        boolean newFile = myFile.createNewFile();
        FileOutputStream fileOutputStream = new FileOutputStream(myFile);
        fileOutputStream.write(file.getBytes());
        fileOutputStream.close();
        return true;
    }
}
