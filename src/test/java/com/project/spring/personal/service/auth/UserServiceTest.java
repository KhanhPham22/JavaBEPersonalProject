package com.project.spring.personal.service.auth;

import com.project.spring.personal.dto.Auth.UserDto;
import com.project.spring.personal.entity.person.Admin;
import com.project.spring.personal.entity.person.Customer;
import com.project.spring.personal.entity.person.Supplier;
import com.project.spring.personal.entity.person.Role;
import com.project.spring.personal.repository.person.AdminRepository;
import com.project.spring.personal.repository.person.CustomerRepository;
import com.project.spring.personal.repository.person.SupplierRepository;
import com.project.spring.personal.service.Auth.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private AdminRepository adminRepository;

    @Mock
    private SupplierRepository supplierRepository;

    @InjectMocks
    private UserService userService;

    private Customer customer;
    private Admin admin;
    private Supplier supplier;

    @BeforeEach
    void setUp() {
        customer = Customer.builder()
                .id(1L)
                .email("test@example.com")
                .fullName("Test User")
                .role(Role.CUSTOMER.name())
                .build();

        admin = Admin.builder()
                .id(1L)
                .email("admin@example.com")
                .fullName("Admin User")
                .role(Role.ADMIN.name())
                .build();

        supplier = Supplier.builder()
                .id(1L)
                .email("supplier@example.com")
                .fullName("Supplier User")
                .role(Role.SUPPLIER.name())
                .build();
    }

    @Test
    void getUserByEmailCustomerSuccess() {
        when(customerRepository.findByEmail(anyString())).thenReturn(Optional.of(customer));
        when(adminRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(supplierRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        UserDto userDto = userService.getUserByEmail("test@example.com");

        assertNotNull(userDto);
        assertEquals(1L, userDto.getId());
        assertEquals("test@example.com", userDto.getEmail());
        assertEquals("Test User", userDto.getFullName());
        assertEquals(Role.CUSTOMER.name(), userDto.getRole());
    }

    @Test
    void getUserByEmailAdminSuccess() {
        when(customerRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(adminRepository.findByEmail(anyString())).thenReturn(Optional.of(admin));
        when(supplierRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        UserDto userDto = userService.getUserByEmail("admin@example.com");

        assertNotNull(userDto);
        assertEquals(1L, userDto.getId());
        assertEquals("admin@example.com", userDto.getEmail());
        assertEquals("Admin User", userDto.getFullName());
        assertEquals(Role.ADMIN.name(), userDto.getRole());
    }

    @Test
    void getUserByEmailSupplierSuccess() {
        when(customerRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(adminRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(supplierRepository.findByEmail(anyString())).thenReturn(Optional.of(supplier));

        UserDto userDto = userService.getUserByEmail("supplier@example.com");

        assertNotNull(userDto);
        assertEquals(1L, userDto.getId());
        assertEquals("supplier@example.com", userDto.getEmail());
        assertEquals("Supplier User", userDto.getFullName());
        assertEquals(Role.SUPPLIER.name(), userDto.getRole());
    }

    @Test
    void getUserByEmailNotFound() {
        when(customerRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(adminRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(supplierRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> userService.getUserByEmail("test@example.com"));
    }
}
