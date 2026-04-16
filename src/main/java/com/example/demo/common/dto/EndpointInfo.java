package com.example.demo.common.dto;

public class EndpointInfo {
    private String method;
    private String path;
    private String description;
    private String access;
    private String sampleBody;

    public EndpointInfo() {}

    public EndpointInfo(String method, String path, String description, String access) {
        this(method, path, description, access, null);
    }

    public EndpointInfo(String method, String path, String description, String access, String sampleBody) {
        this.method = method;
        this.path = path;
        this.description = description;
        this.access = access;
        this.sampleBody = sampleBody;
    }

    public String getMethod() { return method; }
    public void setMethod(String method) { this.method = method; }

    public String getPath() { return path; }
    public void setPath(String path) { this.path = path; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getAccess() { return access; }
    public void setAccess(String access) { this.access = access; }

    public String getSampleBody() { return sampleBody; }
    public void setSampleBody(String sampleBody) { this.sampleBody = sampleBody; }
}
