package com.example.demo.dtos;

import com.example.demo.enums.UserRole;

public record SignUpDto(
    String login,
    String password,
    String email,
    UserRole role) {
}