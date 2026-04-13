package com.pathpilot.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import com.pathpilot.model.PhaseProgress;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.math.BigDecimal;

/**
 * PhaseProgressDAO - CRUD operations for User Phase Progress
 * Tracks user progress through individual phases
 */
@Repository
public class PhaseProgressDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * CREATE - Initialize phase progress for user
     */
    public int addPhaseProgress(PhaseProgress progress) {
        String sql = "INSERT INTO phase_progress (enrollment_id, phase_id, is_completed, attempts, best_score) " +
                     "VALUES (?, ?, ?, ?, ?)";
        return jdbcTemplate.update(sql, progress.getEnrollmentId(), progress.getPhaseId(), 
                                   progress.isCompleted(), progress.getAttempts(), 
                                   progress.getBestScore());
    }

    /**
     * READ - Get phase progress by ID
     */
    public PhaseProgress getProgressById(int progressId) {
        String sql = "SELECT * FROM phase_progress WHERE progress_id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(PhaseProgress.class), progressId);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * READ - Get progress for a specific enrollment
     */
    public List<PhaseProgress> getProgressByEnrollmentId(int enrollmentId) {
        String sql = "SELECT * FROM phase_progress WHERE enrollment_id = ? ORDER BY phase_id ASC";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(PhaseProgress.class), enrollmentId);
    }

    /**
     * READ - Get progress for a specific phase in an enrollment
     */
    public PhaseProgress getProgressByEnrollmentAndPhase(int enrollmentId, int phaseId) {
        String sql = "SELECT * FROM phase_progress WHERE enrollment_id = ? AND phase_id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(PhaseProgress.class), 
                                              enrollmentId, phaseId);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * READ - Get all phase progress records
     */
    public List<PhaseProgress> getAllProgress() {
        String sql = "SELECT * FROM phase_progress ORDER BY enrollment_id ASC, phase_id ASC";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(PhaseProgress.class));
    }

    /**
     * UPDATE - Update phase progress
     */
    public int updateProgress(PhaseProgress progress) {
        String sql = "UPDATE phase_progress SET is_completed = ?, attempts = ?, best_score = ?, " +
                     "completed_date = CASE WHEN ? THEN NOW() ELSE completed_date END WHERE progress_id = ?";
        return jdbcTemplate.update(sql, progress.isCompleted(), progress.getAttempts(), 
                                   progress.getBestScore(), progress.isCompleted(), 
                                   progress.getProgressId());
    }

    public int upsertProgressByEnrollmentAndPhase(int enrollmentId, int phaseId, boolean completed, int attempts, BigDecimal bestScore) {
        String updateSql = "UPDATE phase_progress SET is_completed = ?, attempts = ?, best_score = ?, " +
                           "completed_date = CASE WHEN ? THEN NOW() ELSE completed_date END " +
                           "WHERE enrollment_id = ? AND phase_id = ?";
        int updated = jdbcTemplate.update(updateSql, completed, attempts, bestScore, completed, enrollmentId, phaseId);
        if (updated > 0) {
            return updated;
        }

        String insertSql = "INSERT INTO phase_progress (enrollment_id, phase_id, is_completed, attempts, best_score, completed_date) " +
                           "VALUES (?, ?, ?, ?, ?, CASE WHEN ? THEN NOW() ELSE NULL END)";
        return jdbcTemplate.update(insertSql, enrollmentId, phaseId, completed, attempts, bestScore, completed);
    }

    /**
     * UPDATE - Increment attempt count
     */
    public int incrementAttempts(int progressId) {
        String sql = "UPDATE phase_progress SET attempts = attempts + 1 WHERE progress_id = ?";
        return jdbcTemplate.update(sql, progressId);
    }

    /**
     * UPDATE - Update best score if new score is better
     */
    public int updateBestScore(int progressId, BigDecimal newScore) {
        String sql = "UPDATE phase_progress SET best_score = GREATEST(COALESCE(best_score, 0), ?) WHERE progress_id = ?";
        return jdbcTemplate.update(sql, newScore, progressId);
    }

    /**
     * DELETE - Delete phase progress
     */
    public int deleteProgress(int progressId) {
        String sql = "DELETE FROM phase_progress WHERE progress_id = ?";
        return jdbcTemplate.update(sql, progressId);
    }

    /**
     * DELETE - Delete all progress for an enrollment
     */
    public int deleteProgressByEnrollmentId(int enrollmentId) {
        String sql = "DELETE FROM phase_progress WHERE enrollment_id = ?";
        return jdbcTemplate.update(sql, enrollmentId);
    }

    /**
     * Get count of completed phases for an enrollment
     */
    public int getCompletedPhaseCount(int enrollmentId) {
        String sql = "SELECT COUNT(*) FROM phase_progress WHERE enrollment_id = ? AND is_completed = true";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, enrollmentId);
        return count != null ? count : 0;
    }

    /**
     * Calculate overall progress percentage for an enrollment
     */
    public BigDecimal calculateProgressPercentage(int enrollmentId) {
        String sql = "SELECT COALESCE(ROUND((COUNT(CASE WHEN is_completed THEN 1 END) * 100.0) / COUNT(*), 2), 0) " +
                     "FROM phase_progress WHERE enrollment_id = ?";
        BigDecimal percentage = jdbcTemplate.queryForObject(sql, BigDecimal.class, enrollmentId);
        return percentage != null ? percentage : BigDecimal.ZERO;
    }

    /**
     * Get average score for a phase
     */
    public BigDecimal getAverageScoreByPhaseId(int phaseId) {
        String sql = "SELECT COALESCE(AVG(best_score), 0) FROM phase_progress WHERE phase_id = ?";
        BigDecimal average = jdbcTemplate.queryForObject(sql, BigDecimal.class, phaseId);
        return average != null ? average : BigDecimal.ZERO;
    }

    /**
     * GET - Get detailed phase progress for a specific path and enrollment
     * @param pathId The ID of the career path
     * @param enrollmentId The ID of the enrollment
     * @return List of Maps containing phase details and progress information
     */
    public List<Map<String, Object>> getPhaseProgressByPathId(int pathId, int enrollmentId) {
        String sql = "SELECT " +
                     "p.phase_id, " +
                     "p.title as phase_title, " +
                     "p.content as phase_content, " +
                     "p.phase_number, " +
                     "pp.progress_id, " +
                     "pp.is_completed, " +
                     "pp.attempts, " +
                     "pp.best_score, " +
                     "pp.completed_date " +
                     "FROM phases p " +
                     "LEFT JOIN phase_progress pp ON p.phase_id = pp.phase_id AND pp.enrollment_id = ? " +
                     "WHERE p.path_id = ? " +
                     "ORDER BY p.phase_number ASC";
        
        return jdbcTemplate.query(sql, new RowMapper<Map<String, Object>>() {
            @Override
            public Map<String, Object> mapRow(ResultSet rs, int rowNum) throws SQLException {
                Map<String, Object> row = new HashMap<>();
                row.put("phase_id", rs.getInt("phase_id"));
                row.put("phase_title", rs.getString("phase_title"));
                row.put("phase_content", rs.getString("phase_content"));
                row.put("phase_number", rs.getInt("phase_number"));
                row.put("progress_id", rs.getObject("progress_id") != null ? rs.getInt("progress_id") : null);
                row.put("is_completed", rs.getObject("is_completed") != null ? rs.getBoolean("is_completed") : false);
                row.put("attempts", rs.getObject("attempts") != null ? rs.getInt("attempts") : 0);
                row.put("best_score", rs.getBigDecimal("best_score"));
                row.put("completed_date", rs.getTimestamp("completed_date"));
                return row;
            }
        }, enrollmentId, pathId);
    }
}
