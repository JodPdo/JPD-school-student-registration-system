package com.jpd.registration.service;

import java.time.LocalDateTime;
import org.springframework.stereotype.Service;
import com.jpd.registration.model.School;
import com.jpd.registration.model.Student;
import com.jpd.registration.payload.StudentPayload;
import com.jpd.registration.repository.*;

@Service
public class StudentService {
    
    private final StudentRepository studentRepo;
    private final SchoolRepository schoolRepo;

    public StudentService(StudentRepository studentRepo, SchoolRepository schoolRepo)
    {
        this.studentRepo = studentRepo;
        this.schoolRepo = schoolRepo;
    }

    public Student createStudent(StudentPayload payload)
    {
        School school = schoolRepo.findByName(payload.getSchool())
            .orElseThrow(() -> new IllegalArgumentException("School not found" + payload.getSchool()));

            Student student = new Student();
            student.setFirstName(payload.getFirstName());
            student.setLastName(payload.getLastName());
            student.setSchool(school);
            student.setCreatedAt(LocalDateTime.now());
            student.setUpdatedAt(LocalDateTime.now());

            return studentRepo.save(student);
    }
}
