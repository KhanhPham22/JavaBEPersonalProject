package com.project.spring.personal.service.auth;

import com.project.spring.personal.dto.Auth.AuthResponse;
import com.project.spring.personal.dto.Auth.LoginRequest;
import com.project.spring.personal.dto.Auth.RegisterRequest;
import com.project.spring.personal.entity.person.Admin;
import com.project.spring.personal.entity.person.Customer;
import com.project.spring.personal.entity.person.Supplier;
import com.project.spring.personal.entity.person.Role;
import com.project.spring.personal.repository.person.AdminRepository;
import com.project.spring.personal.repository.person.CustomerRepository;
import com.project.spring.personal.repository.person.SupplierRepository;
import com.project.spring.personal.service.Auth.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private AdminRepository adminRepository;

    @Mock
    private SupplierRepository supplierRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthService authService;

    private RegisterRequest registerRequest;
    private LoginRequest loginRequest;
    private Customer customer;
    private Admin admin;
    private Supplier supplier;

    @BeforeEach
    void setUp() {
        registerRequest = new RegisterRequest();
        registerRequest.setEmail("test@example.com");
        registerRequest.setPassword("password");
        registerRequest.setFullName("Test User");
        registerRequest.setRole("CUSTOMER");

        loginRequest = new LoginRequest();
        loginRequest.setEmail("test@example.com");
        loginRequest.setPassword("password");

        customer = Customer.builder()
                .id(1L)
                .email("test@example.com")
                .password("encodedPassword")
                .fullName("Test User")
                .role(Role.CUSTOMER.name())
                .build();

        admin = Admin.builder()
                .id(1L)
                .email("admin@example.com")
                .password("encodedPassword")
                .fullName("Admin User")
                .role(Role.ADMIN.name())
                .build();

        supplier = Supplier.builder()
                .id(1L)
                .email("supplier@example.com")
                .password("encodedPassword")
                .fullName("Supplier User")
                .role(Role.SUPPLIER.name())
                .build();
    }

    @Test
    void registerCustomerSuccess() {
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(customerRepository.save(any(Customer.class))).thenReturn(customer);
        when(jwtService.generateToken(anyString())).thenReturn("accessToken");
        when(jwtService.generateRefreshToken(anyString())).thenReturn("refreshToken");

        AuthResponse response = authService.register(registerRequest);

        assertNotNull(response);
        assertEquals("accessToken", response.getAccessToken());
        assertEquals("refreshToken", response.getRefreshToken());
        assertEquals(Role.CUSTOMER.name(), response.getRole());
        verify(customerRepository).save(any(Customer.class));
    }

    @Test
    void registerAdminSuccess() {
        registerRequest.setRole("ADMIN");
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(adminRepository.save(any(Admin.class))).thenReturn(admin);
        when(jwtService.generateToken(anyString())).thenReturn("accessToken");
        when(jwtService.generateRefreshToken(anyString())).thenReturn("refreshToken");

        AuthResponse response = authService.register(registerRequest);

        assertNotNull(response);
        assertEquals("accessToken", response.getAccessToken());
        assertEquals("refreshToken", response.getRefreshToken());
        assertEquals(Role.ADMIN.name(), response.getRole());
        verify(adminRepository).save(any(Admin.class));
    }

    @Test
    void registerSupplierSuccess() {
        registerRequest.setRole("SUPPLIER");
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(supplierRepository.save(any(Supplier.class))).thenReturn(supplier);
        when(jwtService.generateToken(anyString())).thenReturn("accessToken");
        when(jwtService.generateRefreshToken(anyString())).thenReturn("refreshToken");

        AuthResponse response = authService.register(registerRequest);

        assertNotNull(response);
        assertEquals("accessToken", response.getAccessToken());
        assertEquals("refreshToken", response.getRefreshToken());
        assertEquals(Role.SUPPLIER.name(), response.getRole());
        verify(supplierRepository).save(any(Supplier.class));
    }

    @Test
    void loginCustomerSuccess() {
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(null);
        when(customerRepository.findByEmail(anyString())).thenReturn(Optional.of(customer));
        when(jwtService.generateToken(anyString())).thenReturn("accessToken");
        when(jwtService.generateRefreshToken(anyString())).thenReturn("refreshToken");

        AuthResponse response = authService.login(loginRequest);

        assertNotNull(response);
        assertEquals("accessToken", response.getAccessToken());
        assertEquals("refreshToken", response.getRefreshToken());
        assertEquals(Role.CUSTOMER.name(), response.getRole());
    }

    @Test
    void loginUserNotFound() {
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(null);
        when(customerRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(adminRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(supplierRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> authService.login(loginRequest));
    }

    @Test
    void refreshTokenSuccess() {
        when(jwtService.extractUsername(anyString())).thenReturn("test@example.com");
        when(customerRepository.findByEmail(anyString())).thenReturn(Optional.of(customer));
        when(jwtService.isTokenValid(anyString(), anyString())).thenReturn(true);
        when(jwtService.generateToken(anyString())).thenReturn("newAccessToken");

        AuthResponse response = authService.refreshToken("refreshToken");

        assertNotNull(response);
        assertEquals("newAccessToken", response.getAccessToken());
        assertEquals("refreshToken", response.getRefreshToken());
        assertEquals(Role.CUSTOMER.name(), response.getRole());
    }

    @Test
    void refreshTokenInvalid() {
        when(jwtService.extractUsername(anyString())).thenReturn("test@example.com");
        when(customerRepository.findByEmail(anyString())).thenReturn(Optional.of(customer));
        when(jwtService.isTokenValid(anyString(), anyString())).thenReturn(false);

        assertThrows(RuntimeException.class, () -> authService.refreshToken("refreshToken"));
    }
}
