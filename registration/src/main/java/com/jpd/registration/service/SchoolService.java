package com.jpd.registration.service;

import com.jpd.registration.exception.ResourceNotFoundException;
import com.jpd.registration.model.School;
import com.jpd.registration.payload.SchoolPayload;
import com.jpd.registration.payload.response.PageResponse;
import com.jpd.registration.payload.response.SchoolResponse;
import com.jpd.registration.repository.SchoolRepository;
import com.jpd.registration.repository.StudentRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SchoolService {

    private final SchoolRepository schoolRepo;
    private final StudentRepository studentRepo;

    public SchoolService(SchoolRepository schoolRepo, StudentRepository studentRepo) {
        this.schoolRepo = schoolRepo;
        this.studentRepo = studentRepo;
    }

    @Transactional
    public SchoolResponse createSchool(SchoolPayload payload) {
        School school = new School();
        school.setName(payload.getName());
        try {
            return new SchoolResponse(schoolRepo.save(school));
        } catch (DataIntegrityViolationException ex) {
            throw new IllegalArgumentException("School name already exists: " + payload.getName());
        }
    }

    @Transactional(readOnly = true)
    public PageResponse<SchoolResponse> getAllSchools(Pageable pageable) {
        Page<SchoolResponse> page = schoolRepo.findAll(pageable).map(SchoolResponse::new);
        return PageResponse.from(page);
    }

    @Transactional(readOnly = true)
    public SchoolResponse getSchoolById(Long id) {
        School school = schoolRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("School not found with id: " + id));
        return new SchoolResponse(school);
    }

    @Transactional
    public SchoolResponse updateSchool(Long id, SchoolPayload payload) {
        School school = schoolRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("School not found with id: " + id));
        school.setName(payload.getName());
        return new SchoolResponse(schoolRepo.save(school));
    }

    @Transactional
    public void deleteSchool(Long id) {
        School school = schoolRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("School not found with id: " + id));
        if (studentRepo.existsBySchool(school)) {
            throw new IllegalArgumentException(
                    "Cannot delete school '" + school.getName() + "' — it still has enrolled students");
        }
        schoolRepo.delete(school);
    }
}
