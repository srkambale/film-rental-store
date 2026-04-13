package com.example.demo.auth.entity;



import jakarta.persistence.*;
import java.time.LocalDateTime;

import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name = "staff")
public class AuthStaff {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "staff_id", columnDefinition = "TINYINT UNSIGNED")
    private Integer staffId;

    @Column(name = "first_name", nullable = false, length = 45)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 45)
    private String lastName;

    @Column(name = "address_id", nullable = false, columnDefinition = "SMALLINT UNSIGNED")
    private Short addressId;

    @Lob
    @Column(name = "picture", columnDefinition = "MEDIUMBLOB")
    private byte[] picture;

    @Column(name = "email", length = 50)
    private String email;

    @Column(name = "store_id", nullable = false, columnDefinition = "TINYINT UNSIGNED")
    private Short storeId;

    @Column(name = "active", nullable = false)
    private Boolean active = true;

    @Column(name = "username", nullable = false, length = 16)
    private String username;

    @Column(name = "password", length = 255)
    private String password;

    @UpdateTimestamp
    @Column(name = "last_update")
    private LocalDateTime lastUpdate;

    // ── Constructors ───────────────────────────────────────────
    public AuthStaff() {}

    // ── Getters & Setters ──────────────────────────────────────
    public Integer getStaffId() { return staffId; }
    public void setStaffId(Integer staffId) { this.staffId = staffId; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public Short getAddressId() { return addressId; }
    public void setAddressId(Short addressId) { this.addressId = addressId; }

    public byte[] getPicture() { return picture; }
    public void setPicture(byte[] picture) { this.picture = picture; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public Short getStoreId() { return storeId; }
    public void setStoreId(Short storeId) { this.storeId = storeId; }

    public Boolean getActive() { return active; }
    public void setActive(Boolean active) { this.active = active; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public LocalDateTime getLastUpdate() { return lastUpdate; }
    public void setLastUpdate(LocalDateTime lastUpdate) { this.lastUpdate = lastUpdate; }
}
