package com.project.spring.personal.repository.person;

import com.project.spring.personal.entity.person.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRepository extends JpaRepository<Admin, Long> {
}
