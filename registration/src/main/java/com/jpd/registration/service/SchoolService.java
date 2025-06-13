package com.jpd.registration.service;

import java.time.LocalDateTime;
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

    public School createSchool(SchoolPayload payload)
    {
        schoolRepo.findByName(payload.getName())
            .ifPresent(s ->
            {
                throw new IllegalArgumentException("School with name '" + payload.getName() + "already exists");
            });

        School school = new School();
        school.setName(payload.getName());
        school.setCreatedAt(LocalDateTime.now());
        school.setUpdatedAt(LocalDateTime.now());
        return schoolRepo.save(school);
    }
}
