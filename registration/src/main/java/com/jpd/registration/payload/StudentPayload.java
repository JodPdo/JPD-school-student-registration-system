package com.jpd.registration.payload;

public class StudentPayload {
    private String firstName;
    private String lastName;
    private String schoolName;

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getSchoolName() { return schoolName; }
    public void setSchool(String schoolName) { this.schoolName = schoolName; }
}
