package com.pathpilot.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import com.pathpilot.model.Phase;
import java.util.List;

/**
 * PhaseDAO - CRUD operations for Learning Phases
 * Manages individual learning phases within career paths
 */
@Repository
public class PhaseDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * CREATE - Add a new phase to a career path and return generated phase ID
     */
    public int addPhase(Phase phase) {
        String sql = "INSERT INTO phases (path_id, phase_number, title, content) VALUES (?, ?, ?, ?)";
        org.springframework.jdbc.support.KeyHolder keyHolder = new org.springframework.jdbc.support.GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            java.sql.PreparedStatement ps = con.prepareStatement(sql, java.sql.Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, phase.getPathId());
            ps.setInt(2, phase.getPhaseNumber());
            ps.setString(3, phase.getTitle());
            ps.setString(4, phase.getContent());
            return ps;
        }, keyHolder);
        return keyHolder.getKey() != null ? keyHolder.getKey().intValue() : -1;
    }

    /**
     * READ - Get phase by ID
     */
    public Phase getPhaseById(int phaseId) {
        String sql = "SELECT * FROM phases WHERE phase_id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(Phase.class), phaseId);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * READ - Get all phases for a specific career path
     */
    public List<Phase> getPhasesByPathId(int pathId) {
        String sql = "SELECT * FROM phases WHERE path_id = ? ORDER BY phase_number ASC";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Phase.class), pathId);
    }

    public Phase getPhaseByPathIdAndNumber(int pathId, int phaseNumber) {
        String sql = "SELECT * FROM phases WHERE path_id = ? AND phase_number = ?";
        try {
            return jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(Phase.class), pathId, phaseNumber);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * READ - Get all phases
     */
    public List<Phase> getAllPhases() {
        String sql = "SELECT * FROM phases ORDER BY path_id ASC, phase_number ASC";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Phase.class));
    }

    /**
     * UPDATE - Update phase details
     */
    public int updatePhase(Phase phase) {
        String sql = "UPDATE phases SET phase_number = ?, title = ?, content = ?, is_completed = ? WHERE phase_id = ?";
        return jdbcTemplate.update(sql, phase.getPhaseNumber(), phase.getTitle(), phase.getContent(), 
                                   phase.isCompleted(), phase.getPhaseId());
    }

    /**
     * DELETE - Delete a phase by ID
     */
    public int deletePhase(int phaseId) {
        String sql = "DELETE FROM phases WHERE phase_id = ?";
        return jdbcTemplate.update(sql, phaseId);
    }

    /**
     * DELETE - Delete all phases for a career path
     */
    public int deletePhasesByPathId(int pathId) {
        String sql = "DELETE FROM phases WHERE path_id = ?";
        return jdbcTemplate.update(sql, pathId);
    }

    /**
     * Get count of phases for a path
     */
    public int getPhaseCountByPathId(int pathId) {
        String sql = "SELECT COUNT(*) FROM phases WHERE path_id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, pathId);
        return count != null ? count : 0;
    }

    /**
     * Mark phase as completed
     */
    public int markPhaseComplete(int phaseId) {
        String sql = "UPDATE phases SET is_completed = true WHERE phase_id = ?";
        return jdbcTemplate.update(sql, phaseId);
    }
}
