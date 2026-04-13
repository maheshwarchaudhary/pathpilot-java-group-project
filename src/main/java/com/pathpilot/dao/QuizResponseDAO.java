package com.pathpilot.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import com.pathpilot.model.QuizResponse;
import java.util.List;
import java.math.BigDecimal;

/**
 * QuizResponseDAO - CRUD operations for User Quiz Answers
 * Tracks user responses to quiz questions
 */
@Repository
public class QuizResponseDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * CREATE - Record user's quiz response
     */
    public int addResponse(QuizResponse response) {
        String sql = "INSERT INTO quiz_responses (phase_progress_id, question_id, selected_answer, is_correct) " +
                     "VALUES (?, ?, ?, ?)";
        return jdbcTemplate.update(sql, response.getPhaseProgressId(), response.getQuestionId(), 
                                   response.getSelectedAnswer(), response.isCorrect());
    }

    /**
     * READ - Get response by ID
     */
    public QuizResponse getResponseById(int responseId) {
        String sql = "SELECT * FROM quiz_responses WHERE response_id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(QuizResponse.class), responseId);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * READ - Get all responses for a phase progress
     */
    public List<QuizResponse> getResponsesByPhaseProgressId(int phaseProgressId) {
        String sql = "SELECT * FROM quiz_responses WHERE phase_progress_id = ? ORDER BY question_id ASC";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(QuizResponse.class), phaseProgressId);
    }

    /**
     * READ - Get response for a specific question in phase progress
     */
    public QuizResponse getResponseByProgressAndQuestion(int phaseProgressId, int questionId) {
        String sql = "SELECT * FROM quiz_responses WHERE phase_progress_id = ? AND question_id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(QuizResponse.class), 
                                              phaseProgressId, questionId);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * READ - Get all responses by question
     */
    public List<QuizResponse> getResponsesByQuestionId(int questionId) {
        String sql = "SELECT * FROM quiz_responses WHERE question_id = ? ORDER BY attempted_at DESC";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(QuizResponse.class), questionId);
    }

    /**
     * READ - Get all responses
     */
    public List<QuizResponse> getAllResponses() {
        String sql = "SELECT * FROM quiz_responses ORDER BY phase_progress_id ASC, question_id ASC";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(QuizResponse.class));
    }

    /**
     * UPDATE - Update user's response and correctness
     */
    public int updateResponse(QuizResponse response) {
        String sql = "UPDATE quiz_responses SET selected_answer = ?, is_correct = ? WHERE response_id = ?";
        return jdbcTemplate.update(sql, response.getSelectedAnswer(), response.isCorrect(), 
                                   response.getResponseId());
    }

    /**
     * DELETE - Delete a response
     */
    public int deleteResponse(int responseId) {
        String sql = "DELETE FROM quiz_responses WHERE response_id = ?";
        return jdbcTemplate.update(sql, responseId);
    }

    /**
     * DELETE - Delete all responses for a phase progress
     */
    public int deleteResponsesByPhaseProgressId(int phaseProgressId) {
        String sql = "DELETE FROM quiz_responses WHERE phase_progress_id = ?";
        return jdbcTemplate.update(sql, phaseProgressId);
    }

    /**
     * DELETE - Delete all responses for a question
     */
    public int deleteResponsesByQuestionId(int questionId) {
        String sql = "DELETE FROM quiz_responses WHERE question_id = ?";
        return jdbcTemplate.update(sql, questionId);
    }

    /**
     * Get count of correct responses for phase progress
     */
    public int getCorrectResponseCount(int phaseProgressId) {
        String sql = "SELECT COUNT(*) FROM quiz_responses WHERE phase_progress_id = ? AND is_correct = true";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, phaseProgressId);
        return count != null ? count : 0;
    }

    /**
     * Get total response count for phase progress
     */
    public int getTotalResponseCount(int phaseProgressId) {
        String sql = "SELECT COUNT(*) FROM quiz_responses WHERE phase_progress_id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, phaseProgressId);
        return count != null ? count : 0;
    }

    /**
     * Calculate score percentage for phase progress
     */
    public BigDecimal calculateScorePercentage(int phaseProgressId) {
        String sql = "SELECT COALESCE(ROUND((SUM(CASE WHEN is_correct THEN 1 ELSE 0 END) * 100.0) / COUNT(*), 2), 0) " +
                     "FROM quiz_responses WHERE phase_progress_id = ?";
        BigDecimal percentage = jdbcTemplate.queryForObject(sql, BigDecimal.class, phaseProgressId);
        return percentage != null ? percentage : BigDecimal.ZERO;
    }

    /**
     * Get question difficulty statistics
     */
    public BigDecimal getQuestionDifficultyRating(int questionId) {
        String sql = "SELECT COALESCE(ROUND((SUM(CASE WHEN is_correct THEN 1 ELSE 0 END) * 100.0) / COUNT(*), 2), 0) " +
                     "FROM quiz_responses WHERE question_id = ?";
        BigDecimal rating = jdbcTemplate.queryForObject(sql, BigDecimal.class, questionId);
        return rating != null ? rating : BigDecimal.ZERO;
    }

    /**
     * Calculate average quiz score across all phases in a career path for a user
     */
    public BigDecimal getAverageScoreForCareerPath(int userId, int pathId) {
        String sql = "SELECT COALESCE(ROUND(AVG(CASE WHEN qr.is_correct THEN 100.0 ELSE 0 END), 2), 0) " +
                     "FROM quiz_responses qr " +
                     "JOIN phase_progress pp ON qr.phase_progress_id = pp.progress_id " +
                     "JOIN enrollments e ON pp.enrollment_id = e.enrollment_id " +
                     "JOIN phases p ON pp.phase_id = p.phase_id " +
                     "WHERE e.user_id = ? AND p.path_id = ? AND qr.is_correct IS NOT NULL";
        BigDecimal average = jdbcTemplate.queryForObject(sql, BigDecimal.class, userId, pathId);
        return average != null ? average : BigDecimal.ZERO;
    }

    /**
     * Check if user has completed all phases of a career path with quizzes
     */
    public boolean hasCompletedAllPhasesInPath(int userId, int pathId) {
        String sql = "SELECT COUNT(DISTINCT p.phase_id) " +
                     "FROM phases p " +
                     "WHERE p.path_id = ? " +
                     "AND p.phase_id IN (" +
                     "  SELECT DISTINCT pp.phase_id FROM phase_progress pp " +
                     "  JOIN enrollments e ON pp.enrollment_id = e.enrollment_id " +
                     "  WHERE e.user_id = ? AND pp.is_completed = TRUE" +
                     ")";
        Integer completedCount = jdbcTemplate.queryForObject(sql, Integer.class, pathId, userId);
        
        String totalSql = "SELECT COUNT(*) FROM phases WHERE path_id = ?";
        Integer totalCount = jdbcTemplate.queryForObject(totalSql, Integer.class, pathId);
        
        return completedCount != null && totalCount != null && completedCount.equals(totalCount) && totalCount > 0;
    }
}
