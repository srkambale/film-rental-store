package com.example.demo.common.controller;

import com.example.demo.common.dto.ApiTestRequest;
import com.example.demo.common.dto.EndpointInfo;
import com.example.demo.common.dto.TeamMember;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Controller
public class WebController {

    private final List<TeamMember> teamMembers = new ArrayList<>();

    public WebController() {
        // Mayank Agrawal: Auth Module
        teamMembers.add(new TeamMember("Mayank Agrawal", "Auth Module", "/images/mayank.png", "auth", 
            "Authentication and Security System",
            Arrays.asList(
                new EndpointInfo("POST", "/api/v1/auth/register", "Register new Customer/Staff/Admin", "Public", "{\n  \"firstName\": \"John\",\n  \"lastName\": \"Doe\",\n  \"email\": \"john@example.com\",\n  \"password\": \"pass123\",\n  \"role\": \"CUSTOMER\"\n}"),
                new EndpointInfo("POST", "/api/v1/auth/login", "User Login (Email/Username)", "Public", "{\n  \"identifier\": \"john@example.com\",\n  \"password\": \"pass123\"\n}"),
                new EndpointInfo("GET", "/api/v1/auth/me", "Check Current Session Info", "Authenticated")
            )));

        // Roshni Bajaj: Customer Module
        teamMembers.add(new TeamMember("Roshni Bajaj", "Customer Module", "/images/roshni.png", "customer", 
            "Customer Management and Profile Operations",
            Arrays.asList(
                new EndpointInfo("POST", "/api/v1/customer", "Create New Customer Profile", "Public", "{\n  \"firstName\": \"Alice\",\n  \"lastName\": \"Smith\",\n  \"email\": \"alice@example.com\",\n  \"storeId\": 1,\n  \"addressId\": 1\n}"),
                new EndpointInfo("GET", "/api/v1/customer/{id}", "Get Customer Details", "Admin/Customer"),
                new EndpointInfo("PUT", "/api/v1/customer/{id}", "Update Profile Basics", "Admin/Customer", "{\n  \"firstName\": \"Alice\",\n  \"lastName\": \"Updated\",\n  \"email\": \"alice.new@example.com\"\n}"),
                new EndpointInfo("PATCH", "/api/v1/customer/{id}", "Partial Profile Update", "Admin/Customer", "{\n  \"lastName\": \"PartialUpdate\"\n}"),
                new EndpointInfo("PUT", "/api/v1/customer/{id}/address", "Update Customer Address", "Admin/Customer", "{\n  \"address\": \"456 New St\",\n  \"district\": \"Central\",\n  \"cityId\": 1,\n  \"postalCode\": \"12345\",\n  \"phone\": \"555-0199\"\n}"),
                new EndpointInfo("PATCH", "/api/v1/customer/{id}/address", "Partial Address Update", "Admin/Customer", "{\n  \"phone\": \"555-9999\"\n}"),
                new EndpointInfo("GET", "/api/v1/customer/search", "Search Customers by Name", "Admin/Staff"),
                new EndpointInfo("GET", "/api/v1/customer/location", "Search Customers by Location", "Admin/Staff"),
                new EndpointInfo("GET", "/api/v1/customer/{id}/rentals", "View Customer Rental History", "Admin/Customer"),
                new EndpointInfo("GET", "/api/v1/customer/{id}/rentals/{rentalId}", "Get Specific Rental Detail", "Admin/Customer"),
                new EndpointInfo("GET", "/api/v1/customer/{id}/payments", "View Personal Payments", "Admin/Customer"),
                new EndpointInfo("GET", "/api/v1/customer/{id}/payment/{paymentId}", "Get Specific Payment Detail", "Admin/Customer"),
                new EndpointInfo("POST", "/api/v1/customer/rentals", "Create Rental (Customer Side)", "Customer Only", "{\n  \"inventoryId\": 1\n}"),
                new EndpointInfo("PUT", "/api/v1/customer/rentals/{rentalId}/return", "Return Film (Customer Side)", "Customer Only"),
                new EndpointInfo("GET", "/api/v1/films/search/title", "Customer Search: Films by Title", "Any"),
                new EndpointInfo("GET", "/api/v1/films/search/actor", "Customer Search: Films by Actor", "Any"),
                new EndpointInfo("GET", "/api/v1/films/search/category", "Customer Search: Films by Category", "Any")
            )));

        // Abhishek Rodage: Catalog (Admin) Module
        teamMembers.add(new TeamMember("Abhishek Rodage", "Catalog (Admin) Module", "/images/abhishek.png", "catalog", 
            "Film Catalog Management and Admin Operations",
            Arrays.asList(
                new EndpointInfo("GET", "/api/v1/catalog/films", "List All Films (Summary)", "Any"),
                new EndpointInfo("GET", "/api/v1/catalog/films/{id}", "Get Detailed Film Info", "Any"),
                new EndpointInfo("GET", "/api/v1/catalog/films/search", "Admin Search: Films (Title/Year)", "Any"),
                new EndpointInfo("GET", "/api/v1/catalog/films/category/{name}", "Filter Films by Genre", "Any"),
                new EndpointInfo("GET", "/api/v1/catalog/films/actor/{id}", "Films by Actor ID", "Any"),
                new EndpointInfo("GET", "/api/v1/catalog/films/actor", "Films by Actor Name Query", "Any"),
                new EndpointInfo("GET", "/api/v1/catalog/films/language/{name}", "Films by Language", "Any"),
                new EndpointInfo("GET", "/api/v1/catalog/films/rating/{rating}", "Films by MPAA Rating", "Any"),
                new EndpointInfo("GET", "/api/v1/catalog/actors", "List All Actors", "Any"),
                new EndpointInfo("GET", "/api/v1/catalog/actors/search", "Search Actors by Name", "Any"),
                new EndpointInfo("GET", "/api/v1/catalog/actors/{id}", "Get Actor Details", "Any"),
                new EndpointInfo("POST", "/api/v1/catalog/actors", "Create New Actor", "Admin Only", "{\n  \"firstName\": \"MARLON\",\n  \"lastName\": \"BRANDO\"\n}"),
                new EndpointInfo("PUT", "/api/v1/catalog/actors/{id}", "Update Actor Info", "Admin Only", "{\n  \"firstName\": \"MARLON\",\n  \"lastName\": \"UPDATED\"\n}"),
                new EndpointInfo("DELETE", "/api/v1/catalog/actors/{id}", "Remove Actor Record", "Admin Only"),
                new EndpointInfo("GET", "/api/v1/catalog/categories", "List All Genres/Categories", "Any"),
                new EndpointInfo("GET", "/api/v1/catalog/categories/search", "Search Categories by Name", "Any"),
                new EndpointInfo("GET", "/api/v1/catalog/categories/{id}", "Get Category Details", "Any"),
                new EndpointInfo("POST", "/api/v1/catalog/categories", "Create New Category", "Admin Only", "{\n  \"name\": \"Noir\"\n}"),
                new EndpointInfo("PUT", "/api/v1/catalog/categories/{id}", "Update Category Info", "Admin Only", "{\n  \"name\": \"Noir Updated\"\n}"),
                new EndpointInfo("DELETE", "/api/v1/catalog/categories/{id}", "Remove Category", "Admin Only"),
                new EndpointInfo("GET", "/api/v1/admin/staff", "Admin Dash: List All Staff", "Admin Only"),
                new EndpointInfo("GET", "/api/v1/admin/staff/{id}", "Admin Dash: Get Staff by ID", "Admin Only"),
                new EndpointInfo("GET", "/api/v1/admin/staff/search", "Admin Dash: Search Staff", "Admin Only"),
                new EndpointInfo("POST", "/api/v1/admin/staff", "Admin Dash: Create New Staff", "Admin Only", "{\n  \"firstName\": \"Admin\",\n  \"lastName\": \"User\",\n  \"email\": \"admin@store.com\",\n  \"username\": \"admin.user\",\n  \"password\": \"admin123\",\n  \"storeId\": 1,\n  \"addressId\": 1,\n  \"role\": \"ADMIN\"\n}"),
                new EndpointInfo("PUT", "/api/v1/admin/staff/{id}", "Admin Dash: Update Staff", "Admin Only", "{\n  \"firstName\": \"AdminUpdate\"\n}"),
                new EndpointInfo("DELETE", "/api/v1/admin/staff/{id}", "Admin Dash: Delete Staff", "Admin Only")
            )));

        // Saniya Kamble: Rental and Staff Module
        teamMembers.add(new TeamMember("Saniya Kamble", "Rental and Staff Module", "/images/saniya.png", "rental", 
            "Internal Operations and Inventory Management",
            Arrays.asList(
                new EndpointInfo("GET", "/api/v1/staff", "Retrieve All Staff Members", "Admin/Staff"),
                new EndpointInfo("GET", "/api/v1/staff/{id}", "Get Specific Staff Details", "Admin/Staff"),
                new EndpointInfo("POST", "/api/v1/staff", "Add New Staff to Store", "Admin/Staff", "{\n  \"firstName\": \"Staff\",\n  \"lastName\": \"Member\",\n  \"email\": \"staff@store.com\",\n  \"username\": \"staff.member\",\n  \"password\": \"staff123\",\n  \"storeId\": 1,\n  \"addressId\": 1,\n  \"role\": \"STAFF\"\n}"),
                new EndpointInfo("PUT", "/api/v1/staff/{id}", "Update Staff Information", "Admin/Staff", "{\n  \"lastName\": \"Updated\"\n}"),
                new EndpointInfo("DELETE", "/api/v1/staff/{id}", "Terminate/Delete Staff Record", "Admin/Staff"),
                new EndpointInfo("POST", "/api/v1/rentals", "Process New Rental (Staff Side)", "Admin/Staff", "{\n  \"customerId\": 1,\n  \"inventoryId\":1,\n  \"staffId\": 1\n}"),
                new EndpointInfo("GET", "/api/v1/rentals", "List All Store Rentals", "Admin/Staff"),
                new EndpointInfo("GET", "/api/v1/rentals/{id}", "Get Single Rental Record", "Admin/Staff"),
                new EndpointInfo("PUT", "/api/v1/rentals/{id}/return", "Process Recording Return", "Admin/Staff"),
                new EndpointInfo("DELETE", "/api/v1/rentals/{id}", "Remove Rental Record", "Admin Only"),
                new EndpointInfo("GET", "/api/v1/inventory", "View Complete Inventory", "Any"),
                new EndpointInfo("GET", "/api/v1/inventory/{id}", "Get Inventory Item Details", "Any"),
                new EndpointInfo("GET", "/api/v1/inventory/film/{id}", "List All Copies of a Film", "Any"),
                new EndpointInfo("GET", "/api/v1/inventory/store/{id}", "View Inventory by Store", "Any"),
                new EndpointInfo("GET", "/api/v1/inventory/available", "Find Available Copies", "Any"),
                new EndpointInfo("POST", "/api/v1/inventory", "Add Film Copy to Store", "Admin/Staff", "{\n  \"filmId\": 1,\n  \"storeId\": 1\n}"),
                new EndpointInfo("DELETE", "/api/v1/inventory/{id}", "Remove Film Copy", "Admin/Staff")
            )));

        // Megha Muttha: Payment Module
        teamMembers.add(new TeamMember("Megha Muttha", "Payment Module", "/images/megha.png", "payment", 
            "Transaction Processing and Financial Reporting",
            Arrays.asList(
                new EndpointInfo("GET", "/api/v1/payments", "Get All System Payments", "Admin Only"),
                new EndpointInfo("GET", "/api/v1/payments/my", "Get Payments for Query-ID", "Admin/Customer"),
                new EndpointInfo("GET", "/api/v1/payments/{id}", "Get Payment by ID", "Admin/Customer"),
                new EndpointInfo("GET", "/api/v1/payments/customer/{customerId}", "Get Payments for Path-ID", "Admin/Customer"),
                new EndpointInfo("GET", "/api/v1/payments/rentals/{rentalId}", "Get Payment for Rental", "Admin/Customer"),
                new EndpointInfo("GET", "/api/v1/payments/balance/{customerId}", "Check Spending Balance", "Admin/Customer"),
                new EndpointInfo("POST", "/api/v1/payments", "Record New Payment", "Admin/Staff", "{\n  \"rentalId\": 1,\n  \"amount\": 4.99,\n  \"paymentDate\": \"2024-04-16T12:00:00\"\n}")
            )));
    }

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("team", teamMembers);
        return "index";
    }

    @GetMapping("/endpoints/{moduleId}")
    public String endpoints(@PathVariable String moduleId, Model model) {
        teamMembers.stream()
                .filter(m -> m.getModuleId().equals(moduleId))
                .findFirst()
                .ifPresent(member -> model.addAttribute("member", member));
        return "endpoints";
    }

    @PostMapping("/api-test/execute")
    public String executeTest(@ModelAttribute ApiTestRequest request, Model model) {
        String fullUrl = "http://localhost:8081" + request.getUrl();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        
        if (request.getToken() != null && !request.getToken().isEmpty()) {
            String token = request.getToken().trim();
            if (!token.startsWith("Bearer ")) {
                token = "Bearer " + token;
            }
            headers.set("Authorization", token);
        }

        HttpEntity<String> entity = new HttpEntity<>(request.getBody(), headers);
        
        try {
            ResponseEntity<Object> response = restTemplate.exchange(
                fullUrl,
                HttpMethod.valueOf(request.getMethod()),
                entity,
                Object.class
            );
            model.addAttribute("status", response.getStatusCode().value());
            model.addAttribute("data", response.getBody());
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            model.addAttribute("status", e.getStatusCode().value());
            model.addAttribute("error", e.getResponseBodyAsString());
        } catch (Exception e) {
            model.addAttribute("status", 500);
            model.addAttribute("error", e.getMessage());
        }

        model.addAttribute("method", request.getMethod());
        model.addAttribute("url", request.getUrl());
        return "results";
    }
}
