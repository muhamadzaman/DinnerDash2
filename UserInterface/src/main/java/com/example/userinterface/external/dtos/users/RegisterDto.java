package com.example.userinterface.external.dtos.users;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RegisterDto {

    private String email;
    private String password;
    private String fullName;
    private String displayName;
    private String role = "USER";

}
