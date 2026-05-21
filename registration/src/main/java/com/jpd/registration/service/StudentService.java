package com.jpd.registration.service;

import com.jpd.registration.exception.ResourceNotFoundException;
import com.jpd.registration.model.School;
import com.jpd.registration.model.Student;
import com.jpd.registration.payload.StudentPayload;
import com.jpd.registration.payload.response.PageResponse;
import com.jpd.registration.payload.response.StudentResponse;
import com.jpd.registration.repository.SchoolRepository;
import com.jpd.registration.repository.StudentRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class StudentService {

    private final StudentRepository studentRepo;
    private final SchoolRepository schoolRepo;

    public StudentService(StudentRepository studentRepo, SchoolRepository schoolRepo) {
        this.studentRepo = studentRepo;
        this.schoolRepo = schoolRepo;
    }

    @Transactional
    public StudentResponse createStudent(StudentPayload payload) {
        School school = schoolRepo.findByName(payload.getSchoolName())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "School not found: " + payload.getSchoolName()));
        Student student = new Student();
        student.setFirstName(payload.getFirstName());
        student.setLastName(payload.getLastName());
        student.setSchool(school);
        return new StudentResponse(studentRepo.save(student));
    }

    @Transactional(readOnly = true)
    public PageResponse<StudentResponse> getAllStudents(Pageable pageable) {
        Page<StudentResponse> page = studentRepo.findAll(pageable).map(StudentResponse::new);
        return PageResponse.from(page);
    }

    @Transactional(readOnly = true)
    public StudentResponse getStudentById(Long id) {
        Student student = studentRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + id));
        return new StudentResponse(student);
    }

    @Transactional
    public StudentResponse updateStudent(Long id, StudentPayload payload) {
        Student student = studentRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + id));
        School school = schoolRepo.findByName(payload.getSchoolName())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "School not found: " + payload.getSchoolName()));
        student.setFirstName(payload.getFirstName());
        student.setLastName(payload.getLastName());
        student.setSchool(school);
        return new StudentResponse(studentRepo.save(student));
    }

    @Transactional
    public void deleteStudent(Long id) {
        Student student = studentRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + id));
        studentRepo.delete(student);
    }
}
