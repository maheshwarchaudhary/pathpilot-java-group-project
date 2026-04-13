package com.pathpilot.model;

import java.time.LocalDateTime;

/**
 * Data model representing a User Ban.
 * Maps to the 'user_bans' table in the database.
 * Tracks which students are banned by which instructors.
 */
public class UserBan {
    private int banId;
    private int instructorId;
    private int studentId;
    private String banType; // TEMPORARY or PERMANENT
    private String reason;
    private LocalDateTime bannedAt;
    private LocalDateTime liftedAt;
    private boolean isActive;
    private int createdBy;

    // Constructors
    public UserBan() {}

    public UserBan(int instructorId, int studentId, String banType, String reason, int createdBy) {
        this.instructorId = instructorId;
        this.studentId = studentId;
        this.banType = banType;
        this.reason = reason;
        this.isActive = true;
        this.createdBy = createdBy;
    }

    // Getters and Setters
    public int getBanId() { return banId; }
    public void setBanId(int banId) { this.banId = banId; }

    public int getInstructorId() { return instructorId; }
    public void setInstructorId(int instructorId) { this.instructorId = instructorId; }

    public int getStudentId() { return studentId; }
    public void setStudentId(int studentId) { this.studentId = studentId; }

    public String getBanType() { return banType; }
    public void setBanType(String banType) { this.banType = banType; }

    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }

    public LocalDateTime getBannedAt() { return bannedAt; }
    public void setBannedAt(LocalDateTime bannedAt) { this.bannedAt = bannedAt; }

    public LocalDateTime getLiftedAt() { return liftedAt; }
    public void setLiftedAt(LocalDateTime liftedAt) { this.liftedAt = liftedAt; }

    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }

    public int getCreatedBy() { return createdBy; }
    public void setCreatedBy(int createdBy) { this.createdBy = createdBy; }
}
