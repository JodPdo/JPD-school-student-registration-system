package com.jpd.registration.model;

import java.time.LocalDate;
import jakarta.persistence.*;

@Entity
@Table(name = "school")
public class School
{
    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(name = "created_at", nullable = false)
    private LocalDate createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDate updatedAt;

    public Long getId() { return id;}
    public void setId(Long id) {this.id = id; }

    public String getName() { return name;}
    public void setName(String name) { this.name = name; }

    public LocalDate getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDate createdAt) { this.createdAt = createdAt; }

    public LocalDate getUpdatedAt() { return updatedAt; }
    public void setUpdateAt(LocalDate updatedAt) { this.updatedAt = updatedAt; }
}
