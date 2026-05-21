package com.jpd.registration.service;

import com.jpd.registration.exception.ResourceNotFoundException;
import com.jpd.registration.model.School;
import com.jpd.registration.payload.SchoolPayload;
import com.jpd.registration.payload.response.PageResponse;
import com.jpd.registration.payload.response.SchoolResponse;
import com.jpd.registration.repository.SchoolRepository;
import com.jpd.registration.repository.StudentRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SchoolServiceTest {

    @Mock SchoolRepository schoolRepo;
    @Mock StudentRepository studentRepo;
    @InjectMocks SchoolService schoolService;

    private School school;

    @BeforeEach
    void setUp() {
        school = new School();
        school.setId(1L);
        school.setName("Test School");
        // Simulate @PrePersist
        setField(school, "createdAt", LocalDateTime.now());
        setField(school, "updatedAt", LocalDateTime.now());
    }

    // Helper: set private fields to simulate JPA lifecycle callbacks in unit tests
    private void setField(Object target, String fieldName, Object value) {
        try {
            var field = target.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(target, value);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void createSchool_success() {
        SchoolPayload payload = new SchoolPayload();
        payload.setName("Test School");
        when(schoolRepo.save(any(School.class))).thenReturn(school);

        SchoolResponse response = schoolService.createSchool(payload);

        assertThat(response.getName()).isEqualTo("Test School");
        verify(schoolRepo).save(any(School.class));
    }

    @Test
    void getAllSchools_returnsPaginatedResults() {
        Page<School> schoolPage = new PageImpl<>(List.of(school), PageRequest.of(0, 10), 1);
        when(schoolRepo.findAll(any(PageRequest.class))).thenReturn(schoolPage);

        PageResponse<SchoolResponse> response = schoolService.getAllSchools(PageRequest.of(0, 10));

        assertThat(response.getContent()).hasSize(1);
        assertThat(response.getTotalElements()).isEqualTo(1);
        assertThat(response.getPage()).isEqualTo(0);
    }

    @Test
    void getSchoolById_found() {
        when(schoolRepo.findById(1L)).thenReturn(Optional.of(school));

        SchoolResponse response = schoolService.getSchoolById(1L);

        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getName()).isEqualTo("Test School");
    }

    @Test
    void getSchoolById_notFound_throwsResourceNotFoundException() {
        when(schoolRepo.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> schoolService.getSchoolById(99L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("99");
    }

    @Test
    void updateSchool_success() {
        SchoolPayload payload = new SchoolPayload();
        payload.setName("Updated School");
        when(schoolRepo.findById(1L)).thenReturn(Optional.of(school));
        when(schoolRepo.save(any(School.class))).thenReturn(school);

        SchoolResponse response = schoolService.updateSchool(1L, payload);

        assertThat(response).isNotNull();
        verify(schoolRepo).save(school);
    }

    @Test
    void updateSchool_notFound_throwsResourceNotFoundException() {
        when(schoolRepo.findById(99L)).thenReturn(Optional.empty());
        SchoolPayload payload = new SchoolPayload();
        payload.setName("X");

        assertThatThrownBy(() -> schoolService.updateSchool(99L, payload))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void deleteSchool_success_noStudents() {
        when(schoolRepo.findById(1L)).thenReturn(Optional.of(school));
        when(studentRepo.existsBySchool(school)).thenReturn(false);

        schoolService.deleteSchool(1L);

        verify(schoolRepo).delete(school);
    }

    @Test
    void deleteSchool_hasStudents_throwsIllegalArgumentException() {
        when(schoolRepo.findById(1L)).thenReturn(Optional.of(school));
        when(studentRepo.existsBySchool(school)).thenReturn(true);

        assertThatThrownBy(() -> schoolService.deleteSchool(1L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("enrolled students");

        verify(schoolRepo, never()).delete(any());
    }

    @Test
    void deleteSchool_notFound_throwsResourceNotFoundException() {
        when(schoolRepo.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> schoolService.deleteSchool(99L))
                .isInstanceOf(ResourceNotFoundException.class);
    }
}
