package com.pathpilot.model;

import java.time.LocalDateTime;
import java.math.BigDecimal;

/**
 * PhaseProgress Model - Tracks user progress through individual phases
 */
public class PhaseProgress {
    private int progressId;
    private int enrollmentId;
    private int phaseId;
    private LocalDateTime startedDate;
    private LocalDateTime completedDate;
    private boolean isCompleted;
    private int attempts;
    private BigDecimal bestScore;

    // Constructors
    public PhaseProgress() {}

    public PhaseProgress(int enrollmentId, int phaseId) {
        this.enrollmentId = enrollmentId;
        this.phaseId = phaseId;
        this.isCompleted = false;
        this.attempts = 0;
    }

    // Getters and Setters
    public int getProgressId() {
        return progressId;
    }

    public void setProgressId(int progressId) {
        this.progressId = progressId;
    }

    public int getEnrollmentId() {
        return enrollmentId;
    }

    public void setEnrollmentId(int enrollmentId) {
        this.enrollmentId = enrollmentId;
    }

    public int getPhaseId() {
        return phaseId;
    }

    public void setPhaseId(int phaseId) {
        this.phaseId = phaseId;
    }

    public LocalDateTime getStartedDate() {
        return startedDate;
    }

    public void setStartedDate(LocalDateTime startedDate) {
        this.startedDate = startedDate;
    }

    public LocalDateTime getCompletedDate() {
        return completedDate;
    }

    public void setCompletedDate(LocalDateTime completedDate) {
        this.completedDate = completedDate;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }

    public int getAttempts() {
        return attempts;
    }

    public void setAttempts(int attempts) {
        this.attempts = attempts;
    }

    public BigDecimal getBestScore() {
        return bestScore;
    }

    public void setBestScore(BigDecimal bestScore) {
        this.bestScore = bestScore;
    }

    @Override
    public String toString() {
        return "PhaseProgress{" +
                "progressId=" + progressId +
                ", enrollmentId=" + enrollmentId +
                ", phaseId=" + phaseId +
                ", isCompleted=" + isCompleted +
                ", attempts=" + attempts +
                ", bestScore=" + bestScore +
                '}';
    }
}
