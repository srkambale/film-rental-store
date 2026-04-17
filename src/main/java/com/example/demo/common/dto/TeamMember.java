package com.example.demo.common.dto;

import java.util.List;

public class TeamMember {
    private String name;
    private String role;
    private String photoUrl;
    private String moduleId;
    private String description;
    private List<EndpointInfo> endpoints;

    public TeamMember() {}

    public TeamMember(String name, String role, String photoUrl, String moduleId, String description, List<EndpointInfo> endpoints) {
        this.name = name;
        this.role = role;
        this.photoUrl = photoUrl;
        this.moduleId = moduleId;
        this.description = description;
        this.endpoints = endpoints;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public String getPhotoUrl() { return photoUrl; }
    public void setPhotoUrl(String photoUrl) { this.photoUrl = photoUrl; }

    public String getModuleId() { return moduleId; }
    public void setModuleId(String moduleId) { this.moduleId = moduleId; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public List<EndpointInfo> getEndpoints() { return endpoints; }
    public void setEndpoints(List<EndpointInfo> endpoints) { this.endpoints = endpoints; }
}
