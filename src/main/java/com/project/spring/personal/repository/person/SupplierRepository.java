package com.project.spring.personal.repository.person;

import com.project.spring.personal.entity.person.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SupplierRepository extends JpaRepository<Supplier, Long> {
}