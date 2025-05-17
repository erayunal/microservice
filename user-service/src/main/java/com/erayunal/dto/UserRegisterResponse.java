package com.erayunal.dto;

import lombok.Data;

@Data
public class UserRegisterResponse {
    private Long id;
    private String username;
    private String email;
}
