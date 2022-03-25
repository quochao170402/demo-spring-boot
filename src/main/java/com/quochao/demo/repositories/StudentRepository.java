package com.quochao.demo.repositories;

import com.quochao.demo.entities.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Long> {

//    @Query("SELECT s FROM Student s WHERE s.email= ?1") // Query to execute
    Optional<Student> findStudentByEmail(String email);
}
