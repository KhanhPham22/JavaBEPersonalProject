package com.project.spring.personal.dto.Auth;

import lombok.Data;
import lombok.Builder;

@Data
@Builder
public class UserDto {
    private Long id;
    private String email;
    private String fullName;
    private String role; // ADMIN / CUSTOMER / SUPPLIER
}
