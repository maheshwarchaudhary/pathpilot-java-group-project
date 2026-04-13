package com.pathpilot.model;

import java.time.LocalDateTime;

/**
 * QuizOption Model - Represents an answer option (A, B, C, D) for a quiz question
 */
public class QuizOption {
    private int optionId;
    private int questionId;
    private String optionLabel;  // A, B, C, D
    private String optionText;
    private boolean isCorrect;
    private LocalDateTime createdAt;

    // Constructors
    public QuizOption() {}

    public QuizOption(int questionId, String optionLabel, String optionText, boolean isCorrect) {
        this.questionId = questionId;
        this.optionLabel = optionLabel;
        this.optionText = optionText;
        this.isCorrect = isCorrect;
    }

    // Getters and Setters
    public int getOptionId() {
        return optionId;
    }

    public void setOptionId(int optionId) {
        this.optionId = optionId;
    }

    public int getQuestionId() {
        return questionId;
    }

    public void setQuestionId(int questionId) {
        this.questionId = questionId;
    }

    public String getOptionLabel() {
        return optionLabel;
    }

    public void setOptionLabel(String optionLabel) {
        this.optionLabel = optionLabel;
    }

    public String getOptionText() {
        return optionText;
    }

    public void setOptionText(String optionText) {
        this.optionText = optionText;
    }

    public boolean isCorrect() {
        return isCorrect;
    }

    public void setCorrect(boolean correct) {
        isCorrect = correct;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "QuizOption{" +
                "optionId=" + optionId +
                ", questionId=" + questionId +
                ", optionLabel='" + optionLabel + '\'' +
                ", isCorrect=" + isCorrect +
                '}';
    }
}
