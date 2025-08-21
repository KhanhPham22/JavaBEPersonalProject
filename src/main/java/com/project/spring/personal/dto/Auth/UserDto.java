package com.project.spring.personal.dto.Auth;

import lombok.Data;

@Data
public class UserDto {
    private Long id;
    private String email;
    private String fullName;
    private String role; // ADMIN / CUSTOMER / SUPPLIER
}
