package com.jpd.registration.payload.response;

import java.time.LocalDateTime;
import com.jpd.registration.model.School;
import com.jpd.registration.model.Student;

public class StudentResponse {
    private Long id;
    private String firstName;
    private String lastName;
    private School school;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public StudentResponse(Student student)
    {
        this.id = student.getId();
        this.firstName = student.getFirstName();
        this.lastName = student.getLastName();
        this.school = student.getSchool();
        this.createdAt = student.getCreatedAt();
        this.updatedAt = student.getUpdatedAt();

    }

    public Long getId() { return id; }

    public String getFirstName() { return firstName; }

    public String getLastName() { return lastName; }

    public LocalDateTime getCreatedAt() { return createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }

    public School getSchool() { return school; }

}
