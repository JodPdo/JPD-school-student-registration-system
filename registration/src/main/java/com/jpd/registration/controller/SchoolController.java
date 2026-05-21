package com.jpd.registration.controller;

import com.jpd.registration.payload.SchoolPayload;
import com.jpd.registration.payload.response.PageResponse;
import com.jpd.registration.payload.response.SchoolResponse;
import com.jpd.registration.service.SchoolService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/schools")
public class SchoolController {

    private final SchoolService schoolService;

    public SchoolController(SchoolService schoolService) {
        this.schoolService = schoolService;
    }

    @PostMapping
    public ResponseEntity<SchoolResponse> createSchool(@Valid @RequestBody SchoolPayload payload) {
        return ResponseEntity.status(HttpStatus.CREATED).body(schoolService.createSchool(payload));
    }

    @GetMapping
    public ResponseEntity<PageResponse<SchoolResponse>> getAllSchools(
            @PageableDefault(size = 10, sort = "id") Pageable pageable) {
        return ResponseEntity.ok(schoolService.getAllSchools(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SchoolResponse> getSchoolById(@PathVariable Long id) {
        return ResponseEntity.ok(schoolService.getSchoolById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SchoolResponse> updateSchool(
            @PathVariable Long id, @Valid @RequestBody SchoolPayload payload) {
        return ResponseEntity.ok(schoolService.updateSchool(id, payload));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSchool(@PathVariable Long id) {
        schoolService.deleteSchool(id);
        return ResponseEntity.noContent().build();
    }
}
