package com.example.demo.auth.dto;



import jakarta.validation.constraints.NotBlank;

public class LoginRequest {

    // Staff/Admin logs in with username
    // Customer logs in with email
    // We accept a single field "identifier" to handle both
    @NotBlank(message = "Username or email is required")
    private String identifier;

    @NotBlank(message = "Password is required")
    private String password;

    // ── Getters & Setters ──────────────────────────────────────
    public String getIdentifier() { return identifier; }
    public void setIdentifier(String identifier) { this.identifier = identifier; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}