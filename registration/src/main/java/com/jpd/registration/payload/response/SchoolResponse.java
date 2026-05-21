package com.jpd.registration.payload.response;

import com.jpd.registration.model.School;
import java.time.LocalDateTime;

public class SchoolResponse {

    private Long id;
    private String name;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public SchoolResponse(School school) {
        this.id = school.getId();
        this.name = school.getName();
        this.createdAt = school.getCreatedAt();
        this.updatedAt = school.getUpdatedAt();
    }

    // FIX: was getname() — non-standard, Jackson serialized as "name" but broke introspection tools
    public Long getId() { return id; }
    public String getName() { return name; }
    // FIX: was getCreatedTime() / getUpdatedTime() — inconsistent naming
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
}
