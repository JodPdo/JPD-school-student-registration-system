package com.jpd.registration.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jpd.registration.model.School;
import com.jpd.registration.payload.SchoolPayload;
import com.jpd.registration.payload.auth.LoginRequest;
import com.jpd.registration.payload.auth.RegisterRequest;
import com.jpd.registration.repository.SchoolRepository;
import com.jpd.registration.repository.StudentRepository;
import com.jpd.registration.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class SchoolControllerTest {

    @Autowired MockMvc mockMvc;
    @Autowired ObjectMapper objectMapper;
    @Autowired SchoolRepository schoolRepository;
    @Autowired StudentRepository studentRepository;
    @Autowired UserRepository userRepository;

    @BeforeEach
    void cleanUp() {
        studentRepository.deleteAll();
        schoolRepository.deleteAll();
        userRepository.deleteAll();
    }

    private String getJwtToken() throws Exception {
        RegisterRequest register = new RegisterRequest();
        register.setUsername("testuser");
        register.setEmail("test@example.com");
        register.setPassword("password123");

        MvcResult result = mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(register)))
                .andExpect(status().isCreated())
                .andReturn();

        String json = result.getResponse().getContentAsString();
        return objectMapper.readTree(json).get("token").asText();
    }

    @Test
    void getAllSchools_publicEndpoint_returnsOk() throws Exception {
        mockMvc.perform(get("/api/v1/schools"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.page", is(0)))
                .andExpect(jsonPath("$.totalElements", notNullValue()));
    }

    @Test
    void createSchool_withValidToken_returns201() throws Exception {
        String token = getJwtToken();
        SchoolPayload payload = new SchoolPayload();
        payload.setName("Harvard");

        mockMvc.perform(post("/api/v1/schools")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(payload)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is("Harvard")))
                .andExpect(jsonPath("$.id", notNullValue()));
    }

    @Test
    void createSchool_withoutToken_returns403() throws Exception {
        SchoolPayload payload = new SchoolPayload();
        payload.setName("MIT");

        mockMvc.perform(post("/api/v1/schools")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(payload)))
                .andExpect(status().isForbidden());
    }

    @Test
    void createSchool_invalidPayload_returns400WithErrors() throws Exception {
        String token = getJwtToken();
        SchoolPayload payload = new SchoolPayload();
        payload.setName(""); // blank — should fail @NotBlank

        mockMvc.perform(post("/api/v1/schools")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(payload)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", is("Validation failed")))
                .andExpect(jsonPath("$.errors.name", notNullValue()));
    }

    @Test
    void getSchoolById_notFound_returns404() throws Exception {
        mockMvc.perform(get("/api/v1/schools/9999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status", is(404)));
    }

    @Test
    void deleteSchool_withToken_returns204() throws Exception {
        String token = getJwtToken();

        School school = new School();
        school.setName("To Be Deleted");
        School saved = schoolRepository.save(school);

        mockMvc.perform(delete("/api/v1/schools/" + saved.getId())
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isNoContent());
    }

    @Test
    void login_withWrongPassword_returns403() throws Exception {
        // Register first
        RegisterRequest register = new RegisterRequest();
        register.setUsername("logintest");
        register.setEmail("login@example.com");
        register.setPassword("correctpass");
        mockMvc.perform(post("/api/v1/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(register)));

        LoginRequest login = new LoginRequest();
        login.setUsername("logintest");
        login.setPassword("wrongpass");

        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(login)))
                .andExpect(status().isForbidden());
    }
}
