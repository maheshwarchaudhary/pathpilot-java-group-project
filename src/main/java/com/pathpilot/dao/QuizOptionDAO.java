package com.pathpilot.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import com.pathpilot.model.QuizOption;
import java.util.List;

/**
 * QuizOptionDAO - CRUD operations for Quiz Answer Options
 * Manages A, B, C, D options for quiz questions
 */
@Repository
public class QuizOptionDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * CREATE - Add a new quiz option and return generated option ID
     */
    public int addOption(QuizOption option) {
        String sql = "INSERT INTO quiz_options (question_id, option_label, option_text, is_correct) " +
                     "VALUES (?, ?, ?, ?)";
        org.springframework.jdbc.support.KeyHolder keyHolder = new org.springframework.jdbc.support.GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            java.sql.PreparedStatement ps = con.prepareStatement(sql, java.sql.Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, option.getQuestionId());
            ps.setString(2, option.getOptionLabel());
            ps.setString(3, option.getOptionText());
            ps.setBoolean(4, option.isCorrect());
            return ps;
        }, keyHolder);
        return keyHolder.getKey() != null ? keyHolder.getKey().intValue() : -1;
    }

    /**
     * READ - Get option by ID
     */
    public QuizOption getOptionById(int optionId) {
        String sql = "SELECT * FROM quiz_options WHERE option_id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(QuizOption.class), optionId);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * READ - Get all options for a specific question
     */
    public List<QuizOption> getOptionsByQuestionId(int questionId) {
        String sql = "SELECT * FROM quiz_options WHERE question_id = ? ORDER BY option_label ASC";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(QuizOption.class), questionId);
    }

    /**
     * READ - Get all options
     */
    public List<QuizOption> getAllOptions() {
        String sql = "SELECT * FROM quiz_options ORDER BY question_id ASC, option_label ASC";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(QuizOption.class));
    }

    /**
     * READ - Get correct option for a question
     */
    public QuizOption getCorrectOptionByQuestionId(int questionId) {
        String sql = "SELECT * FROM quiz_options WHERE question_id = ? AND is_correct = true";
        try {
            return jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(QuizOption.class), questionId);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * UPDATE - Update option details
     */
    public int updateOption(QuizOption option) {
        String sql = "UPDATE quiz_options SET option_text = ?, is_correct = ? WHERE option_id = ?";
        return jdbcTemplate.update(sql, option.getOptionText(), option.isCorrect(), option.getOptionId());
    }

    /**
     * DELETE - Delete an option by ID
     */
    public int deleteOption(int optionId) {
        String sql = "DELETE FROM quiz_options WHERE option_id = ?";
        return jdbcTemplate.update(sql, optionId);
    }

    /**
     * DELETE - Delete all options for a question
     */
    public int deleteOptionsByQuestionId(int questionId) {
        String sql = "DELETE FROM quiz_options WHERE question_id = ?";
        return jdbcTemplate.update(sql, questionId);
    }

    /**
     * Get count of options for a question
     */
    public int getOptionCountByQuestionId(int questionId) {
        String sql = "SELECT COUNT(*) FROM quiz_options WHERE question_id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, questionId);
        return count != null ? count : 0;
    }
}
