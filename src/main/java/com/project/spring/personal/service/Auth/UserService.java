package com.project.spring.personal.service.Auth;

import com.project.spring.personal.dto.Auth.UserDto;
import com.project.spring.personal.entity.person.Admin;
import com.project.spring.personal.entity.person.Customer;
import com.project.spring.personal.entity.person.Supplier;
import com.project.spring.personal.repository.person.AdminRepository;
import com.project.spring.personal.repository.person.CustomerRepository;
import com.project.spring.personal.repository.person.SupplierRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final CustomerRepository customerRepository;
    private final AdminRepository adminRepository;
    private final SupplierRepository supplierRepository;

    public UserDto getUserByEmail(String email) {
        Customer customer = customerRepository.findByEmail(email).orElse(null);
        Admin admin = adminRepository.findByEmail(email).orElse(null);
        Supplier supplier = supplierRepository.findByEmail(email).orElse(null);

        if (customer == null && admin == null && supplier == null) {
            throw new RuntimeException("User not found");
        }

        return UserDto.builder()
                .id(customer != null ? customer.getId() : (admin != null ? admin.getId() : supplier.getId()))
                .email(customer != null ? customer.getEmail() : (admin != null ? admin.getEmail() : supplier.getEmail()))
                .fullName(customer != null ? customer.getFullName() : (admin != null ? admin.getFullName() : supplier.getFullName()))
                .role(customer != null ? customer.getRole().name() : (admin != null ? admin.getRole().name() : supplier.getRole().name()))
                .build();
    }
}
