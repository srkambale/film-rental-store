package com.example.demo.auth.entity;



import jakarta.persistence.*;

@Entity
@Table(name = "user_auth")
public class UserAuth {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 255)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Role role;

    @OneToOne
    @JoinColumn(name = "staff_id", referencedColumnName = "staff_id")
    private Staff staff;

    @OneToOne
    @JoinColumn(name = "customer_id", referencedColumnName = "customer_id")
    private Customer customer;

    public enum Role {
        ADMIN, STAFF, CUSTOMER
    }

    // ── Constructors ───────────────────────────────────────────
    public UserAuth() {}

    // ── Helper: get username or email depending on role ────────
    public String getLoginIdentifier() {
        if (role == Role.CUSTOMER) {
            return customer != null ? customer.getEmail() : null;
        }
        return staff != null ? staff.getUsername() : null;
    }

    // ── Getters & Setters ──────────────────────────────────────
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }

    public Staff getStaff() { return staff; }
    public void setStaff(Staff staff) { this.staff = staff; }

    public Customer getCustomer() { return customer; }
    public void setCustomer(Customer customer) { this.customer = customer; }
}