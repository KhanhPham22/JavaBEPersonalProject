package com.project.spring.personal.repository.other;

import com.project.spring.personal.entity.other.Log;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LogRepository extends JpaRepository<Log, Long> {
}