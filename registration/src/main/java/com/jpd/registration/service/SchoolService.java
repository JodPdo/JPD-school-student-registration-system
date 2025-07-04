package com.jpd.registration.service;

import com.jpd.registration.model.School;
import com.jpd.registration.payload.SchoolPayload;
import com.jpd.registration.payload.response.SchoolListResponse;
import com.jpd.registration.payload.response.SchoolResponse;
import com.jpd.registration.repository.SchoolRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
public class SchoolService {
    private final SchoolRepository schoolRepo;

    public SchoolService(SchoolRepository schoolRepo)
    {
        this.schoolRepo = schoolRepo;
    }

    public School createSchool(SchoolPayload payload)
    {
        School school = new School();
        school.setName(payload.getName());
        school.setCreatedAt(LocalDateTime.now());
        school.setUpdatedAt(LocalDateTime.now());

        try
        {
            return schoolRepo.save(school);
        } catch (DataIntegrityViolationException ex)
        {
            throw new IllegalArgumentException("School name already exists: " + payload.getName());
        }
    }

    public SchoolListResponse getAllSchools()
    {
        List<SchoolResponse> schools = schoolRepo.findAll().stream()
            .map(SchoolResponse::new)
            .collect(Collectors.toList());
        return new SchoolListResponse(schools);
    }

    public SchoolResponse getSchoolById(Long id)
    {
        School school = schoolRepo.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("School not found with id: " + id));
    return new SchoolResponse(school);
    }

    public School updateSchool(Long id, SchoolPayload patload)
    {
        School school = schoolRepo.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("School not found with id: " + id));

        school.setName(patload.getName());
        school.setUpdatedAt(LocalDateTime.now());

        return schoolRepo.save(school);
    }
}
