package com.epam.esm.service.dto;

import lombok.Data;

@Data
public class AuthenticationRequestDto {
    private String Login;
    private String password;
}
