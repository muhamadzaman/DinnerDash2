package com.example.userservice.dto;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;

import java.io.Serializable;
import java.util.Collection;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class GetUserDto implements Serializable {

    private Long id;
    private String email;
    private String fullName;
    private String displayName;
    private String role;
    private String token;
}
