package com.jpd.registration.service;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import com.jpd.registration.model.School;
import com.jpd.registration.model.Student;
import com.jpd.registration.payload.StudentPayload;
import com.jpd.registration.payload.response.StudentListResponse;
import com.jpd.registration.payload.response.StudentResponse;
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

    public StudentListResponse getAllStudent()
    {
        List<StudentResponse> student = studentRepo.findAll().stream()
            .map(StudentResponse::new)
            .collect(Collectors.toList());
        return new StudentListResponse(student);
    }

    public StudentResponse getStudentById(Long id)
    {
        Student student = studentRepo.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Student not found with id: " + id));
        return new StudentResponse(student);
    }
}
