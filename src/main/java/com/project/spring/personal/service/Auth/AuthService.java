package com.project.spring.personal.service.Auth;

import com.project.spring.personal.dto.Auth.AuthResponse;
import com.project.spring.personal.dto.Auth.LoginRequest;
import com.project.spring.personal.dto.Auth.RegisterRequest;
import com.project.spring.personal.entity.person.Admin;
import com.project.spring.personal.entity.person.Customer;
import com.project.spring.personal.entity.person.Role;
import com.project.spring.personal.entity.person.Supplier;
import com.project.spring.personal.repository.person.AdminRepository;
import com.project.spring.personal.repository.person.CustomerRepository;
import com.project.spring.personal.repository.person.SupplierRepository;
import com.project.spring.personal.service.Auth.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final CustomerRepository customerRepository;
    private final AdminRepository adminRepository;
    private final SupplierRepository supplierRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthResponse register(RegisterRequest request) {
        Role role = Role.valueOf(request.getRole().toUpperCase());
        String encodedPassword = passwordEncoder.encode(request.getPassword());

        String jwt;
        String refresh;
        String roleName;

        switch (role) {
            case ADMIN:
                Admin admin = Admin.builder()
                        .email(request.getEmail())
                        .password(encodedPassword)
                        .fullName(request.getFullName())
                        .role(role) // Set Role enum
                        .adminCode("ADM-" + System.currentTimeMillis()) // Example adminCode
                        .build();
                adminRepository.save(admin);
                jwt = jwtService.generateToken(admin.getEmail());
                refresh = jwtService.generateRefreshToken(admin.getEmail());
                roleName = admin.getRole().name();
                break;
            case CUSTOMER:
                Customer customer = Customer.builder()
                        .email(request.getEmail())
                        .password(encodedPassword)
                        .fullName(request.getFullName())
                        .role(role) // Set Role enum
                        .address("") // Default or require in RegisterRequest
                        .phoneNumber("") // Default or require in RegisterRequest
                        .loyaltyPoints(0) // Default value
                        .build();
                customerRepository.save(customer);
                jwt = jwtService.generateToken(customer.getEmail());
                refresh = jwtService.generateRefreshToken(customer.getEmail());
                roleName = customer.getRole().name();
                break;
            case SUPPLIER:
                Supplier supplier = Supplier.builder()
                        .email(request.getEmail())
                        .password(encodedPassword)
                        .fullName(request.getFullName())
                        .role(role) // Set Role enum
                        .companyName("") // Default or require in RegisterRequest
                        .taxCode("") // Default or require in RegisterRequest
                        .build();
                supplierRepository.save(supplier);
                jwt = jwtService.generateToken(supplier.getEmail());
                refresh = jwtService.generateRefreshToken(supplier.getEmail());
                roleName = supplier.getRole().name();
                break;
            default:
                throw new IllegalArgumentException("Invalid role: " + role);
        }

        return AuthResponse.builder()
                .accessToken(jwt)
                .refreshToken(refresh)
                .role(roleName)
                .build();
    }

    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        Customer customer = customerRepository.findByEmail(request.getEmail()).orElse(null);
        Admin admin = adminRepository.findByEmail(request.getEmail()).orElse(null);
        Supplier supplier = supplierRepository.findByEmail(request.getEmail()).orElse(null);

        if (customer == null && admin == null && supplier == null) {
            throw new RuntimeException("User not found");
        }

        String email = customer != null ? customer.getEmail() : (admin != null ? admin.getEmail() : supplier.getEmail());
        String role = customer != null ? customer.getRole().name() : (admin != null ? admin.getRole().name() : supplier.getRole().name());

        String jwt = jwtService.generateToken(email);
        String refresh = jwtService.generateRefreshToken(email);

        return AuthResponse.builder()
                .accessToken(jwt)
                .refreshToken(refresh)
                .role(role)
                .build();
    }

    public AuthResponse refreshToken(String refreshToken) {
        String email = jwtService.extractUsername(refreshToken);

        Customer customer = customerRepository.findByEmail(email).orElse(null);
        Admin admin = adminRepository.findByEmail(email).orElse(null);
        Supplier supplier = supplierRepository.findByEmail(email).orElse(null);

        if (customer == null && admin == null && supplier == null) {
            throw new RuntimeException("User not found");
        }

        String username = customer != null ? customer.getEmail() : (admin != null ? admin.getEmail() : supplier.getEmail());
        String role = customer != null ? customer.getRole().name() : (admin != null ? admin.getRole().name() : supplier.getRole().name());

        if (jwtService.isTokenValid(refreshToken, username)) {
            String jwt = jwtService.generateToken(username);
            return AuthResponse.builder()
                    .accessToken(jwt)
                    .refreshToken(refreshToken)
                    .role(role)
                    .build();
        }
        throw new RuntimeException("Invalid refresh token");
    }
}