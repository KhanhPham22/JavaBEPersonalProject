package com.project.spring.personal.service.Auth;

import com.project.spring.personal.dto.Auth.UserDto;
import com.project.spring.personal.entity.person.Admin;
import com.project.spring.personal.entity.person.Customer;
import com.project.spring.personal.entity.person.Supplier;
import com.project.spring.personal.repository.person.AdminRepository;
import com.project.spring.personal.repository.person.CustomerRepository;
import com.project.spring.personal.repository.person.SupplierRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

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

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Customer customer = customerRepository.findByEmail(email).orElse(null);
        if (customer != null) {
            return new User(
                    customer.getEmail(),
                    customer.getPassword(),
                    Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + customer.getRole().name()))
            );
        }

        Admin admin = adminRepository.findByEmail(email).orElse(null);
        if (admin != null) {
            return new User(
                    admin.getEmail(),
                    admin.getPassword(),
                    Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + admin.getRole().name()))
            );
        }

        Supplier supplier = supplierRepository.findByEmail(email).orElse(null);
        if (supplier != null) {
            return new User(
                    supplier.getEmail(),
                    supplier.getPassword(),
                    Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + supplier.getRole().name()))
            );
        }

        throw new UsernameNotFoundException("User not found with email: " + email);
    }
}