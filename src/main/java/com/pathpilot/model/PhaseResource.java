package com.pathpilot.model;

import java.time.LocalDateTime;

/**
 * PhaseResource Model - Represents study materials (PDFs, videos, documents) for a phase
 */
public class PhaseResource {
    private int resourceId;
    private int phaseId;
    private String resourceType;  // PDF, VIDEO, DOCUMENT
    private String resourceName;
    private String resourceUrl;
    private String filePath;
    private long fileSize;
    private String mimeType;
    private int uploadedBy;  // user_id
    private LocalDateTime createdAt;

    // Constructors
    public PhaseResource() {}

    public PhaseResource(int phaseId, String resourceType, String resourceName, String resourceUrl) {
        this.phaseId = phaseId;
        this.resourceType = resourceType;
        this.resourceName = resourceName;
        this.resourceUrl = resourceUrl;
    }

    // Getters and Setters
    public int getResourceId() {
        return resourceId;
    }

    public void setResourceId(int resourceId) {
        this.resourceId = resourceId;
    }

    public int getPhaseId() {
        return phaseId;
    }

    public void setPhaseId(int phaseId) {
        this.phaseId = phaseId;
    }

    public String getResourceType() {
        return resourceType;
    }

    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }

    public String getResourceName() {
        return resourceName;
    }

    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }

    public String getResourceUrl() {
        return resourceUrl;
    }

    public void setResourceUrl(String resourceUrl) {
        this.resourceUrl = resourceUrl;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public int getUploadedBy() {
        return uploadedBy;
    }

    public void setUploadedBy(int uploadedBy) {
        this.uploadedBy = uploadedBy;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "PhaseResource{" +
                "resourceId=" + resourceId +
                ", phaseId=" + phaseId +
                ", resourceType='" + resourceType + '\'' +
                ", resourceName='" + resourceName + '\'' +
                ", fileSize=" + fileSize +
                '}';
    }
}
