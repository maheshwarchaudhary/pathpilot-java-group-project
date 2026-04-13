package com.pathpilot.model;

import java.time.LocalDateTime;

/**
 * CareerPath Model - Represents a learning roadmap/career path created by users
 */
public class CareerPath {
    private int pathId;
    private int id;  // Alias for pathId for JSP compatibility
    private String title;
    private String description;
    private String category;
    private String level;
    private String status;  // DRAFT, PUBLISHED
    private int createdBy;  // User ID
    private String createdByName;
    private LocalDateTime createdAt;
    private LocalDateTime publishedAt;
    private LocalDateTime updatedAt;
    private int totalPhases;
    private int phaseCount;  // Alias for totalPhases for JSP
    private String duration;  // Display duration string

    // Constructors
    public CareerPath() {}

    public CareerPath(String title, String description, String category, String level, int createdBy) {
        this.title = title;
        this.description = description;
        this.category = category;
        this.level = level;
        this.createdBy = createdBy;
        this.status = "DRAFT";
    }

    // Getters and Setters
    public int getPathId() {
        return pathId;
    }

    public void setPathId(int pathId) {
        this.pathId = pathId;
        this.id = pathId;  // Keep id in sync with pathId
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
        this.pathId = id;  // Keep pathId in sync with id
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(int createdBy) {
        this.createdBy = createdBy;
    }

    public String getCreatedByName() {
        return createdByName;
    }

    public void setCreatedByName(String createdByName) {
        this.createdByName = createdByName;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(LocalDateTime publishedAt) {
        this.publishedAt = publishedAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public int getTotalPhases() {
        return totalPhases;
    }

    public void setTotalPhases(int totalPhases) {
        this.totalPhases = totalPhases;
        this.phaseCount = totalPhases;  // Keep phaseCount in sync
    }

    public int getPhaseCount() {
        return phaseCount;
    }

    public void setPhaseCount(int phaseCount) {
        this.phaseCount = phaseCount;
        this.totalPhases = phaseCount;  // Keep totalPhases in sync
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    @Override
    public String toString() {
        return "CareerPath{" +
                "pathId=" + pathId +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", category='" + category + '\'' +
                ", level='" + level + '\'' +
                ", status='" + status + '\'' +
                ", createdBy=" + createdBy +
                ", createdAt=" + createdAt +
                ", publishedAt=" + publishedAt +
                '}';
    }
}
