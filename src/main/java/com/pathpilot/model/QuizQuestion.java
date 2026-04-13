package com.pathpilot.model;

import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;

/**
 * QuizQuestion Model - Represents a quiz question within a phase
 */
public class QuizQuestion {
    private int questionId;
    private int phaseId;
    private int questionNumber;
    private String questionText;
    private String correctAnswer;  // A, B, C, D
    private String difficultyLevel;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<QuizOption> options;  // List of answer options for this question

    // Constructors
    public QuizQuestion() {}

    public QuizQuestion(int phaseId, int questionNumber, String questionText, String correctAnswer) {
        this.phaseId = phaseId;
        this.questionNumber = questionNumber;
        this.questionText = questionText;
        this.correctAnswer = correctAnswer;
    }

    // Getters and Setters
    public int getQuestionId() {
        return questionId;
    }

    public void setQuestionId(int questionId) {
        this.questionId = questionId;
    }

    public int getPhaseId() {
        return phaseId;
    }

    public void setPhaseId(int phaseId) {
        this.phaseId = phaseId;
    }

    public int getQuestionNumber() {
        return questionNumber;
    }

    public void setQuestionNumber(int questionNumber) {
        this.questionNumber = questionNumber;
    }

    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public String getDifficultyLevel() {
        return difficultyLevel;
    }

    public void setDifficultyLevel(String difficultyLevel) {
        this.difficultyLevel = difficultyLevel;
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

    public List<QuizOption> getOptions() {
        return options;
    }

    public void setOptions(List<QuizOption> options) {
        this.options = options;
    }

    @Override
    public String toString() {
        return "QuizQuestion{" +
                "questionId=" + questionId +
                ", phaseId=" + phaseId +
                ", questionNumber=" + questionNumber +
                ", correctAnswer='" + correctAnswer + '\'' +
                ", difficultyLevel='" + difficultyLevel + '\'' +
                '}';
    }
}
