package com.jpd.registration.payload.response;

import java.time.LocalDateTime;
import com.jpd.registration.model.School;

public class SchoolResponse {
    
    private Long id;
    private String name;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public SchoolResponse(School school)
    {
        this.id = school.getId();
        this.name = school.getName();
        this.createdAt = school.getCreatedAt();
        this.updatedAt = school.getUpdatedAt();
    }

    public Long getId() { return id; }
    public String getname() { return name; }
    public LocalDateTime getCreatedTime() { return createdAt; }
    public LocalDateTime getUpdatedTime() { return updatedAt; }

}
