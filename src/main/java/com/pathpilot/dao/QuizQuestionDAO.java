package com.pathpilot.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import com.pathpilot.model.QuizQuestion;
import java.util.List;

/**
 * QuizQuestionDAO - CRUD operations for Quiz Questions
 * Manages assessment questions within learning phases
 */
@Repository
public class QuizQuestionDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * CREATE - Add a new quiz question and return generated question ID
     */
    public int addQuestion(QuizQuestion question) {
        String sql = "INSERT INTO quiz_questions (phase_id, question_number, question_text, correct_answer, difficulty_level) " +
                     "VALUES (?, ?, ?, ?, ?)";
        org.springframework.jdbc.support.KeyHolder keyHolder = new org.springframework.jdbc.support.GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            java.sql.PreparedStatement ps = con.prepareStatement(sql, java.sql.Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, question.getPhaseId());
            ps.setInt(2, question.getQuestionNumber());
            ps.setString(3, question.getQuestionText());
            ps.setString(4, question.getCorrectAnswer());
            ps.setString(5, question.getDifficultyLevel());
            return ps;
        }, keyHolder);
        return keyHolder.getKey() != null ? keyHolder.getKey().intValue() : -1;
    }

    /**
     * READ - Get question by ID
     */
    public QuizQuestion getQuestionById(int questionId) {
        String sql = "SELECT * FROM quiz_questions WHERE question_id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(QuizQuestion.class), questionId);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * READ - Get all questions for a specific phase
     */
    public List<QuizQuestion> getQuestionsByPhaseId(int phaseId) {
        String sql = "SELECT * FROM quiz_questions WHERE phase_id = ? ORDER BY question_number ASC";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(QuizQuestion.class), phaseId);
    }

    /**
     * READ - Get all questions
     */
    public List<QuizQuestion> getAllQuestions() {
        String sql = "SELECT * FROM quiz_questions ORDER BY phase_id ASC, question_number ASC";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(QuizQuestion.class));
    }

    /**
     * UPDATE - Update question details
     */
    public int updateQuestion(QuizQuestion question) {
        String sql = "UPDATE quiz_questions SET question_text = ?, correct_answer = ?, difficulty_level = ? " +
                     "WHERE question_id = ?";
        return jdbcTemplate.update(sql, question.getQuestionText(), question.getCorrectAnswer(), 
                                   question.getDifficultyLevel(), question.getQuestionId());
    }

    /**
     * DELETE - Delete a question by ID
     */
    public int deleteQuestion(int questionId) {
        String sql = "DELETE FROM quiz_questions WHERE question_id = ?";
        return jdbcTemplate.update(sql, questionId);
    }

    /**
     * DELETE - Delete all questions for a phase
     */
    public int deleteQuestionsByPhaseId(int phaseId) {
        String sql = "DELETE FROM quiz_questions WHERE phase_id = ?";
        return jdbcTemplate.update(sql, phaseId);
    }

    /**
     * Get count of questions for a phase
     */
    public int getQuestionCountByPhaseId(int phaseId) {
        String sql = "SELECT COUNT(*) FROM quiz_questions WHERE phase_id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, phaseId);
        return count != null ? count : 0;
    }
}
