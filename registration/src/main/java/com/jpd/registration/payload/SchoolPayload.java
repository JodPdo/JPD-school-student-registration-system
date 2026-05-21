package com.jpd.registration.payload;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class SchoolPayload {

    @NotBlank(message = "School name is required")
    @Size(min = 2, max = 100, message = "School name must be between 2 and 100 characters")
    private String name;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}
