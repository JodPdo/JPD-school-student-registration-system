package com.jpd.registration.payload;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class StudentPayload {

    @NotBlank(message = "First name is required")
    @Size(max = 50, message = "First name must not exceed 50 characters")
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Size(max = 50, message = "Last name must not exceed 50 characters")
    private String lastName;

    @NotBlank(message = "School name is required")
    private String schoolName;

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getSchoolName() { return schoolName; }
    // BUG FIX: was named setSchool() — Jackson couldn't map JSON "schoolName" to this field
    public void setSchoolName(String schoolName) { this.schoolName = schoolName; }
}
