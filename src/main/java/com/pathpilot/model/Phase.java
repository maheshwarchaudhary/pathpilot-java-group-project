package com.pathpilot.model;

import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;

/**
 * Phase Model - Represents a learning phase within a career path
 */
public class Phase {
    private int phaseId;
    private int pathId;
    private int phaseNumber;
    private String title;
    private String content;
    private boolean isCompleted;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<QuizQuestion> questions;  // List of quiz questions for this phase
    private String fileResourcePath;
    private String fileResourceName;
    private String videoResourceUrl;

    // Constructors
    public Phase() {}

    public Phase(int pathId, int phaseNumber, String title, String content) {
        this.pathId = pathId;
        this.phaseNumber = phaseNumber;
        this.title = title;
        this.content = content;
        this.isCompleted = false;
    }

    // Getters and Setters
    public int getPhaseId() {
        return phaseId;
    }

    public void setPhaseId(int phaseId) {
        this.phaseId = phaseId;
    }

    public int getPathId() {
        return pathId;
    }

    public void setPathId(int pathId) {
        this.pathId = pathId;
    }

    public int getPhaseNumber() {
        return phaseNumber;
    }

    public void setPhaseNumber(int phaseNumber) {
        this.phaseNumber = phaseNumber;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public List<QuizQuestion> getQuestions() {
        return questions;
    }

    public void setQuestions(List<QuizQuestion> questions) {
        this.questions = questions;
    }

    public String getFileResourcePath() {
        return fileResourcePath;
    }

    public void setFileResourcePath(String fileResourcePath) {
        this.fileResourcePath = fileResourcePath;
    }

    public String getFileResourceName() {
        return fileResourceName;
    }

    public void setFileResourceName(String fileResourceName) {
        this.fileResourceName = fileResourceName;
    }

    public String getVideoResourceUrl() {
        return videoResourceUrl;
    }

    public void setVideoResourceUrl(String videoResourceUrl) {
        this.videoResourceUrl = videoResourceUrl;
    }

    @Override
    public String toString() {
        return "Phase{" +
                "phaseId=" + phaseId +
                ", pathId=" + pathId +
                ", phaseNumber=" + phaseNumber +
                ", title='" + title + '\'' +
                ", isCompleted=" + isCompleted +
                '}';
    }
}
