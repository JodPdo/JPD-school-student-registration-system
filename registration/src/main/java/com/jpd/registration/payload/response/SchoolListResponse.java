package com.jpd.registration.payload.response;

import java.util.List;

public class SchoolListResponse {
    private List<SchoolResponse> schools;

    public SchoolListResponse(List<SchoolResponse> schools)
    {
        this.schools = schools;
    }

    public List<SchoolResponse> getSchools()
    {
        return schools;
    }
}