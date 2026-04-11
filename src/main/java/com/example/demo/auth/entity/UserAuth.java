package com.example.demo.auth.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "user_auth")
public class UserAuth {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String username;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(nullable = false, length = 255)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private Role role;

    @Column(name = "ref_id")
    private Integer refId;

    public enum Role {
        ADMIN, STAFF, CUSTOMER
    }

    // ── Constructors ───────────────────────────────────────────
    public UserAuth() {}

    // ── Helper: get login identifier based on role ─────────────
    public String getLoginIdentifier() {
        if (role == Role.CUSTOMER) {
            return this.email;
        }
        return this.username;
    }

    // ── Getters & Setters ──────────────────────────────────────
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }

    public Integer getRefId() { return refId; }
    public void setRefId(Integer refId) { this.refId = refId; }
}
