package com.jpd.registration.service;

import com.jpd.registration.model.School;
import com.jpd.registration.payload.SchoolPayload;
import com.jpd.registration.payload.response.SchoolListResponse;
import com.jpd.registration.payload.response.SchoolResponse;
import com.jpd.registration.repository.SchoolRepository;
import com.jpd.registration.repository.StudentRepository;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
public class SchoolService {
    private final SchoolRepository schoolRepo;
    private final StudentRepository studentRepo;

    public SchoolService(SchoolRepository schoolRepo, StudentRepository studentRepo)
    {
        this.schoolRepo = schoolRepo;
        this.studentRepo = studentRepo;
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

    public Map<String, Object> deleteSchool(Long id)
    {
        School school = schoolRepo.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("School not found with id: " + id));
        
        long studentCount = studentRepo.findAll().stream()
            .filter(s -> s.getSchool().getId().equals(id))
            .count();

        if (studentCount > 0)
        {
            throw new IllegalArgumentException("Cannot delete school with students enrolled");
        }

        schoolRepo.delete(school);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "School has been removed");

        return response;
    }
}
