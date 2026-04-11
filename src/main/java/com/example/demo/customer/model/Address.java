package com.example.demo.customer.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import org.locationtech.jts.geom.Point;

@Entity
@Table(name = "address")
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "address_id")
    private Long addressId;

    @Column(name = "address", nullable = false, length = 50)
    private String address;

    @Column(name = "address2", length = 50)
    private String address2;

    @Column(name = "district", nullable = false, length = 20)
    private String district;

    @ManyToOne
    @JoinColumn(name = "city_id") 
    private City city;

    @Column(name = "postal_code", length = 10)
    private String postalCode;

    @Column(name = "phone", nullable = false, length = 20)
    private String phone;
    
    @Column(name = "location", columnDefinition = "Geometry")
    private Point location;

    @Column(name = "last_update", nullable = false)
    private LocalDateTime lastUpdate;

    // Constructors
    public Address() {}

    public Address(Long addressId, String address, String address2, String district,
            City city, String postalCode, String phone, Point location, LocalDateTime lastUpdate) {
    	this.addressId = addressId;
    	this.address = address;
    	this.address2 = address2;
    	this.district = district;
    	this.city = city;
    	this.postalCode = postalCode;
    	this.phone = phone;
    	this.location = location; // Added this
    	this.lastUpdate = lastUpdate;
    }

    // Getters and Setters
    public Long getAddressId() { return addressId; }
    public void setAddressId(Long addressId) { this.addressId = addressId; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getAddress2() { return address2; }
    public void setAddress2(String address2) { this.address2 = address2; }

    public String getDistrict() { return district; }
    public void setDistrict(String district) { this.district = district; }

    public City getCity() { return city; }
    public void setCity(City city) { this.city = city; }

    public String getPostalCode() { return postalCode; }
    public void setPostalCode(String postalCode) { this.postalCode = postalCode; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public LocalDateTime getLastUpdate() { return lastUpdate; }
    public void setLastUpdate(LocalDateTime lastUpdate) { this.lastUpdate = lastUpdate; }
    
    public Point getLocation() {
        return location;
    }

    public void setLocation(Point location) {
        this.location = location;
    }
}
