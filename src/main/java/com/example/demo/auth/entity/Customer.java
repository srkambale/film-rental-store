package com.example.demo.auth.entity;



import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "customer")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "customer_id")
    private Integer customerId;

    @Column(name = "store_id", nullable = false)
    private Short storeId;

    @Column(name = "first_name", nullable = false, length = 45)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 45)
    private String lastName;

    @Column(name = "email", length = 50)
    private String email;

    @Column(name = "address_id", nullable = false)
    private Short addressId;

    @Column(name = "active", nullable = false)
    private Boolean active = true;

    @Column(name = "create_date", nullable = false)
    private LocalDateTime createDate;

    @Column(name = "last_update")
    private LocalDateTime lastUpdate;

    // ── Constructors ───────────────────────────────────────────
    public Customer() {}

    // ── Getters & Setters ──────────────────────────────────────
    public Integer getCustomerId() { return customerId; }
    public void setCustomerId(Integer customerId) { this.customerId = customerId; }

    public Short getStoreId() { return storeId; }
    public void setStoreId(Short storeId) { this.storeId = storeId; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public Short getAddressId() { return addressId; }
    public void setAddressId(Short addressId) { this.addressId = addressId; }

    public Boolean getActive() { return active; }
    public void setActive(Boolean active) { this.active = active; }

    public LocalDateTime getCreateDate() { return createDate; }
    public void setCreateDate(LocalDateTime createDate) { this.createDate = createDate; }

    public LocalDateTime getLastUpdate() { return lastUpdate; }
    public void setLastUpdate(LocalDateTime lastUpdate) { this.lastUpdate = lastUpdate; }
}
