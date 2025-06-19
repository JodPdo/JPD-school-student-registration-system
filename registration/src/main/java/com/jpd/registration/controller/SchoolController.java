package com.jpd.registration.controller;

import com.jpd.registration.payload.SchoolPayload;
import com.jpd.registration.payload.response.SchoolListResponse;
import com.jpd.registration.payload.response.SchoolResponse;
import com.jpd.registration.model.School;
import com.jpd.registration.service.SchoolService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @GetMapping
    public ResponseEntity<SchoolListResponse> getAllSchools()
    {
        return ResponseEntity.ok(schoolService.getAllSchools());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<SchoolResponse> getSchoolById(@PathVariable Long id)
    {
        return ResponseEntity.ok(schoolService.getSchoolById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<School> updateSchool(@PathVariable Long id, @RequestBody SchoolPayload payload)
    {
        School updatedSchool = schoolService.updateSchool(id, payload);
        return ResponseEntity.ok(updatedSchool);
    }
}
