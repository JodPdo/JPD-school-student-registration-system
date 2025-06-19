package com.jpd.registration.controller;

import com.jpd.registration.model.Student;
import com.jpd.registration.payload.StudentPayload;
import com.jpd.registration.payload.response.StudentListResponse;
import com.jpd.registration.payload.response.StudentResponse;
import com.jpd.registration.service.StudentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/students")
public class StudentController {
    private final StudentService studentService;

    public StudentController(StudentService studentService)
    {
        this.studentService = studentService;
    }

    @PostMapping
    public ResponseEntity<Student> createStudent(@RequestBody StudentPayload payload)
    {
        Student createStudent = studentService.createStudent(payload);
        
        return ResponseEntity.ok(createStudent);
    }

    @GetMapping
    public ResponseEntity<StudentListResponse> getAllStudents()
    {
        return ResponseEntity.ok(studentService.getAllStudent());
    }

    @GetMapping("/{id}")
    public ResponseEntity<StudentResponse> getStudentById(@PathVariable Long id)
    {
        return ResponseEntity.ok(studentService.getStudentById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<StudentResponse> updateStudent(@PathVariable Long id, @RequestBody StudentPayload payload)
    {
        Student updatedStudent = studentService.updateStudent(id, payload);

        return ResponseEntity.ok(new StudentResponse(updatedStudent));
    }
}