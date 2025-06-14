package com.jpd.registration.service;

import java.time.LocalDateTime;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import com.jpd.registration.model.School;
import com.jpd.registration.payload.SchoolPayload;
import com.jpd.registration.repository.SchoolRepository;

@Service
public class SchoolService {

    private final SchoolRepository schoolRepo;

    public SchoolService(SchoolRepository schoolRepo)
    {
        this.schoolRepo = schoolRepo;
    }

    public School creaSchool(SchoolPayload payload)
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
}
