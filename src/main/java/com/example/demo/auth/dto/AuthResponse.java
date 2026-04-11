package com.example.demo.auth.dto;



public class AuthResponse {

    private String token;
    private String role;
    private String identifier; // username for staff/admin, email for customer
    private String message;

    // ── Constructors ───────────────────────────────────────────
    public AuthResponse() {}

    public AuthResponse(String token, String role, String identifier) {
        this.token      = token;
        this.role       = role;
        this.identifier = identifier;
        this.message    = "Success";
    }

    public AuthResponse(String message) {
        this.message = message;
    }

    // ── Getters & Setters ──────────────────────────────────────
    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public String getIdentifier() { return identifier; }
    public void setIdentifier(String identifier) { this.identifier = identifier; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}