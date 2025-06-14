package com.jpd.registration.service;

import java.time.LocalDateTime;
import org.springframework.stereotype.Service;
import com.jpd.registration.model.School;
import com.jpd.registration.model.Student;
import com.jpd.registration.payload.StudentPayload;
import com.jpd.registration.repository.SchoolRepository;
import com.jpd.registration.repository.StudentRepository;

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
        School school = schoolRepo.findByName(payload.getSchoolName())
            .orElseThrow(() -> new IllegalArgumentException("School not found" + payload.getSchoolName()));

            Student student = new Student
            (
                payload.getFirstName(),
                payload.getLastName(),
                school
            );

            return studentRepo.save(student);
    }
}
