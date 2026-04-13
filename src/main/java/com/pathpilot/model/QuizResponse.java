package com.pathpilot.model;

import java.time.LocalDateTime;

/**
 * QuizResponse Model - Tracks user answers to quiz questions
 */
public class QuizResponse {
    private int responseId;
    private int phaseProgressId;
    private int questionId;
    private String selectedAnswer;  // A, B, C, D
    private boolean isCorrect;
    private LocalDateTime attemptedAt;

    // Constructors
    public QuizResponse() {}

    public QuizResponse(int phaseProgressId, int questionId, String selectedAnswer, boolean isCorrect) {
        this.phaseProgressId = phaseProgressId;
        this.questionId = questionId;
        this.selectedAnswer = selectedAnswer;
        this.isCorrect = isCorrect;
    }

    // Getters and Setters
    public int getResponseId() {
        return responseId;
    }

    public void setResponseId(int responseId) {
        this.responseId = responseId;
    }

    public int getPhaseProgressId() {
        return phaseProgressId;
    }

    public void setPhaseProgressId(int phaseProgressId) {
        this.phaseProgressId = phaseProgressId;
    }

    public int getQuestionId() {
        return questionId;
    }

    public void setQuestionId(int questionId) {
        this.questionId = questionId;
    }

    public String getSelectedAnswer() {
        return selectedAnswer;
    }

    public void setSelectedAnswer(String selectedAnswer) {
        this.selectedAnswer = selectedAnswer;
    }

    public boolean isCorrect() {
        return isCorrect;
    }

    public void setCorrect(boolean correct) {
        isCorrect = correct;
    }

    public LocalDateTime getAttemptedAt() {
        return attemptedAt;
    }

    public void setAttemptedAt(LocalDateTime attemptedAt) {
        this.attemptedAt = attemptedAt;
    }

    @Override
    public String toString() {
        return "QuizResponse{" +
                "responseId=" + responseId +
                ", phaseProgressId=" + phaseProgressId +
                ", questionId=" + questionId +
                ", selectedAnswer='" + selectedAnswer + '\'' +
                ", isCorrect=" + isCorrect +
                '}';
    }
}
