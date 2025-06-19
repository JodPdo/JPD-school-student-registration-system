package com.jpd.registration.payload.response;

import java.util.List;

public class StudentListResponse {
    private List<StudentResponse> students;

    public StudentListResponse(List<StudentResponse> students)
    {
        this.students = students;
    }

    public List<StudentResponse> getStudents()
    {
        return students;
    }
}