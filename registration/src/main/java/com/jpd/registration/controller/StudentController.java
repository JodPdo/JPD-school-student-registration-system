package com.jpd.registration.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.jpd.registration.model.Student;
import com.jpd.registration.payload.StudentPayload;
import com.jpd.registration.service.StudentService;

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
}
