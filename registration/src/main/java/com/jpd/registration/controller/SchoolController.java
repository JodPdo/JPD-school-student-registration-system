package com.jpd.registration.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.jpd.registration.payload.SchoolPayload;
import com.jpd.registration.model.School;
import com.jpd.registration.service.SchoolService;

@RestController
@RequestMapping("/api/v1/schools")
public class SchoolController {
    
    private final SchoolService schoolService;

    public SchoolController(SchoolService schoolService)
    {
        this.schoolService = schoolService;
    }

    @PostMapping
    public ResponseEntity<School> createSchool(@RequestBody SchoolPayload payload)
    {
        School createdSchool = schoolService.createSchool(payload);
        return ResponseEntity.ok(createdSchool);
    }
}
