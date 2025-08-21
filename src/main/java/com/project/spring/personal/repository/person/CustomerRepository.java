package com.project.spring.personal.repository.person;

import com.project.spring.personal.entity.person.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
}
