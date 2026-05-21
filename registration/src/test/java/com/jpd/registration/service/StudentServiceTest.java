package com.jpd.registration.service;

import com.jpd.registration.exception.ResourceNotFoundException;
import com.jpd.registration.model.School;
import com.jpd.registration.model.Student;
import com.jpd.registration.payload.StudentPayload;
import com.jpd.registration.payload.response.StudentResponse;
import com.jpd.registration.repository.SchoolRepository;
import com.jpd.registration.repository.StudentRepository;
import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

    @Mock StudentRepository studentRepo;
    @Mock SchoolRepository schoolRepo;
    @InjectMocks StudentService studentService;

    private School school;
    private Student student;

    @BeforeEach
    void setUp() {
        school = new School();
        school.setId(1L);
        school.setName("Test School");
        setField(school, "createdAt", LocalDateTime.now());
        setField(school, "updatedAt", LocalDateTime.now());

        student = new Student();
        student.setId(10L);
        student.setFirstName("John");
        student.setLastName("Doe");
        student.setSchool(school);
        setField(student, "createdAt", LocalDateTime.now());
        setField(student, "updatedAt", LocalDateTime.now());
    }

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
    void createStudent_success() {
        StudentPayload payload = new StudentPayload();
        payload.setFirstName("John");
        payload.setLastName("Doe");
        payload.setSchoolName("Test School");

        when(schoolRepo.findByName("Test School")).thenReturn(Optional.of(school));
        when(studentRepo.save(any(Student.class))).thenReturn(student);

        StudentResponse response = studentService.createStudent(payload);

        assertThat(response.getFirstName()).isEqualTo("John");
        assertThat(response.getLastName()).isEqualTo("Doe");
        assertThat(response.getSchool().getName()).isEqualTo("Test School");
    }

    @Test
    void createStudent_schoolNotFound_throwsResourceNotFoundException() {
        StudentPayload payload = new StudentPayload();
        payload.setFirstName("John");
        payload.setLastName("Doe");
        payload.setSchoolName("Unknown School");

        when(schoolRepo.findByName("Unknown School")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> studentService.createStudent(payload))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Unknown School");
    }

    @Test
    void getStudentById_found() {
        when(studentRepo.findById(10L)).thenReturn(Optional.of(student));

        StudentResponse response = studentService.getStudentById(10L);

        assertThat(response.getId()).isEqualTo(10L);
        assertThat(response.getFirstName()).isEqualTo("John");
    }

    @Test
    void getStudentById_notFound_throwsResourceNotFoundException() {
        when(studentRepo.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> studentService.getStudentById(99L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("99");
    }

    @Test
    void updateStudent_success() {
        StudentPayload payload = new StudentPayload();
        payload.setFirstName("Jane");
        payload.setLastName("Smith");
        payload.setSchoolName("Test School");

        when(studentRepo.findById(10L)).thenReturn(Optional.of(student));
        when(schoolRepo.findByName("Test School")).thenReturn(Optional.of(school));
        when(studentRepo.save(any(Student.class))).thenReturn(student);

        StudentResponse response = studentService.updateStudent(10L, payload);

        assertThat(response).isNotNull();
        verify(studentRepo).save(student);
    }

    @Test
    void updateStudent_notFound_throwsResourceNotFoundException() {
        when(studentRepo.findById(99L)).thenReturn(Optional.empty());
        StudentPayload payload = new StudentPayload();
        payload.setFirstName("X");
        payload.setLastName("Y");
        payload.setSchoolName("Test School");

        assertThatThrownBy(() -> studentService.updateStudent(99L, payload))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void deleteStudent_success() {
        when(studentRepo.findById(10L)).thenReturn(Optional.of(student));

        studentService.deleteStudent(10L);

        verify(studentRepo).delete(student);
    }

    @Test
    void deleteStudent_notFound_throwsResourceNotFoundException() {
        when(studentRepo.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> studentService.deleteStudent(99L))
                .isInstanceOf(ResourceNotFoundException.class);
    }
}
