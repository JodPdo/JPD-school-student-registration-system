package com.jpd.registration.repository;

import com.jpd.registration.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, Long> {
    
}
