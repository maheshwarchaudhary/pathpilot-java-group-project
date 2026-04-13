package com.pathpilot.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import com.pathpilot.model.Enrollment;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * EnrollmentDAO - CRUD operations for Enrollments
 * Manages user enrollments in career paths
 */
@Repository
public class EnrollmentDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * CREATE - Add a new enrollment and return generated enrollment ID
     */
    public int addEnrollment(Enrollment enrollment) {
        String sql = "INSERT INTO enrollments (user_id, path_id, progress_percentage, is_active) " +
                     "VALUES (?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, enrollment.getUserId());
            ps.setInt(2, enrollment.getPathId());
            ps.setBigDecimal(3, enrollment.getProgressPercentage());
            ps.setBoolean(4, enrollment.isActive());
            return ps;
        }, keyHolder);
        return keyHolder.getKey() != null ? keyHolder.getKey().intValue() : -1;
    }

    /**
     * READ - Get enrollment by ID
     */
    public Enrollment getEnrollmentById(int enrollmentId) {
        String sql = "SELECT * FROM enrollments WHERE enrollment_id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(Enrollment.class), enrollmentId);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * READ - Get enrollments by user
     */
    public List<Enrollment> getEnrollmentsByUserId(int userId) {
        String sql = "SELECT * FROM enrollments WHERE user_id = ? ORDER BY enrolled_date DESC";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Enrollment.class), userId);
    }

    /**
     * READ - Get enrollments by path
     */
    public List<Enrollment> getEnrollmentsByPathId(int pathId) {
        String sql = "SELECT * FROM enrollments WHERE path_id = ? ORDER BY enrolled_date DESC";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Enrollment.class), pathId);
    }

    public Enrollment getEnrollmentByUserAndPath(int userId, int pathId) {
        String sql = "SELECT * FROM enrollments WHERE user_id = ? AND path_id = ? LIMIT 1";
        try {
            return jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(Enrollment.class), userId, pathId);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * READ - Check if user is enrolled in a path
     */
    public boolean isEnrolled(int userId, int pathId) {
        String sql = "SELECT COUNT(*) FROM enrollments WHERE user_id = ? AND path_id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, userId, pathId);
        return count != null && count > 0;
    }

    /**
     * UPDATE - Update enrollment progress
     */
    public int updateProgress(int enrollmentId, java.math.BigDecimal percentage) {
        String sql = "UPDATE enrollments SET progress_percentage = ? WHERE enrollment_id = ?";
        return jdbcTemplate.update(sql, percentage, enrollmentId);
    }

    /**
     * UPDATE - Mark enrollment as completed
     */
    public int completeEnrollment(int enrollmentId) {
        String sql = "UPDATE enrollments SET is_completed = true, completion_date = CURRENT_TIMESTAMP WHERE enrollment_id = ?";
        return jdbcTemplate.update(sql, enrollmentId);
    }

    /**
     * DELETE - Delete enrollment
     */
    public int deleteEnrollment(int enrollmentId) {
        String sql = "DELETE FROM enrollments WHERE enrollment_id = ?";
        return jdbcTemplate.update(sql, enrollmentId);
    }

    /**
     * Get active enrollments count
     */
    public int getActiveEnrollmentsCount() {
        String sql = "SELECT COUNT(*) FROM enrollments WHERE is_active = true";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class);
        return count != null ? count : 0;
    }

    /**
     * FETCH - Get students enrolled in courses/paths created by a specific user
     * @param creatorId The ID of the user who created the career paths
     * @return List of Maps containing student and enrollment information
     */
    public List<Map<String, Object>> getStudentsEnrolledInUserCreatedPaths(int creatorId) {
        String sql = "SELECT " +
                     "u.id as student_id, " +
                     "u.name as student_name, " +
                     "u.email as student_email, " +
                     "u.phone as student_phone, " +
                     "cp.path_id, " +
                     "cp.title as path_title, " +
                     "e.enrollment_id, " +
                     "e.progress_percentage, " +
                     "e.is_completed, " +
                     "e.enrolled_date, " +
                     "e.completion_date " +
                     "FROM enrollments e " +
                     "INNER JOIN career_paths cp ON e.path_id = cp.path_id " +
                     "INNER JOIN users u ON e.user_id = u.id " +
                     "WHERE cp.created_by = ? " +
                     "ORDER BY e.enrolled_date DESC";
        
        return jdbcTemplate.query(sql, new RowMapper<Map<String, Object>>() {
            @Override
            public Map<String, Object> mapRow(ResultSet rs, int rowNum) throws SQLException {
                Map<String, Object> row = new HashMap<>();
                row.put("student_id", rs.getInt("student_id"));
                row.put("student_name", rs.getString("student_name"));
                row.put("student_email", rs.getString("student_email"));
                row.put("student_phone", rs.getString("student_phone"));
                row.put("path_id", rs.getInt("path_id"));
                row.put("path_title", rs.getString("path_title"));
                row.put("enrollment_id", rs.getInt("enrollment_id"));
                row.put("progress_percentage", rs.getBigDecimal("progress_percentage"));
                row.put("is_completed", rs.getBoolean("is_completed"));
                row.put("enrolled_date", rs.getTimestamp("enrolled_date"));
                row.put("completion_date", rs.getTimestamp("completion_date"));
                return row;
            }
        }, creatorId);
    }

    /**
     * GET - Get full enrollment details with path and user information by enrollment ID
     * @param enrollmentId The ID of the enrollment
     * @return Map containing enrollment, path, and progress details, or null if not found
     */
    public Map<String, Object> getEnrollmentDetailsById(int enrollmentId) {
        String sql = "SELECT " +
                     "e.enrollment_id, " +
                     "e.user_id, " +
                     "e.path_id, " +
                     "e.progress_percentage, " +
                     "e.is_completed, " +
                     "e.enrolled_date, " +
                     "e.completion_date, " +
                     "cp.title as path_title, " +
                     "cp.description as path_description, " +
                     "cp.category, " +
                     "cp.level, " +
                     "cp.total_phases " +
                     "FROM enrollments e " +
                     "INNER JOIN career_paths cp ON e.path_id = cp.path_id " +
                     "WHERE e.enrollment_id = ?";
        
        try {
            return jdbcTemplate.queryForObject(sql, new RowMapper<Map<String, Object>>() {
                @Override
                public Map<String, Object> mapRow(ResultSet rs, int rowNum) throws SQLException {
                    Map<String, Object> row = new HashMap<>();
                    row.put("enrollment_id", rs.getInt("enrollment_id"));
                    row.put("user_id", rs.getInt("user_id"));
                    row.put("path_id", rs.getInt("path_id"));
                    row.put("progress_percentage", rs.getBigDecimal("progress_percentage"));
                    row.put("is_completed", rs.getBoolean("is_completed"));
                    row.put("enrolled_date", rs.getTimestamp("enrolled_date"));
                    row.put("completion_date", rs.getTimestamp("completion_date"));
                    row.put("path_title", rs.getString("path_title"));
                    row.put("path_description", rs.getString("path_description"));
                    row.put("category", rs.getString("category"));
                    row.put("level", rs.getString("level"));
                    row.put("total_phases", rs.getInt("total_phases"));
                    return row;
                }
            }, enrollmentId);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * GET - Get all enrollment details with path information for a specific user
     * @param userId The ID of the user
     * @return List of Maps containing enrollment and path information for all user enrollments
     */
    public List<Map<String, Object>> getAllEnrollmentDetailsForUser(int userId) {
        String sql = "SELECT " +
                     "e.enrollment_id, " +
                     "e.user_id, " +
                     "e.path_id, " +
                     "e.progress_percentage, " +
                     "e.is_completed, " +
                     "e.enrolled_date, " +
                     "e.completion_date, " +
                     "cp.title as path_title, " +
                     "cp.description as path_description, " +
                     "cp.category, " +
                     "cp.level, " +
                     "cp.total_phases " +
                     "FROM enrollments e " +
                     "INNER JOIN career_paths cp ON e.path_id = cp.path_id " +
                     "WHERE e.user_id = ? " +
                     "ORDER BY e.enrolled_date DESC";
        
        return jdbcTemplate.query(sql, new RowMapper<Map<String, Object>>() {
            @Override
            public Map<String, Object> mapRow(ResultSet rs, int rowNum) throws SQLException {
                Map<String, Object> row = new HashMap<>();
                row.put("enrollment_id", rs.getInt("enrollment_id"));
                row.put("user_id", rs.getInt("user_id"));
                row.put("path_id", rs.getInt("path_id"));
                row.put("progress_percentage", rs.getBigDecimal("progress_percentage"));
                row.put("is_completed", rs.getBoolean("is_completed"));
                row.put("enrolled_date", rs.getTimestamp("enrolled_date"));
                row.put("completion_date", rs.getTimestamp("completion_date"));
                row.put("path_title", rs.getString("path_title"));
                row.put("path_description", rs.getString("path_description"));
                row.put("category", rs.getString("category"));
                row.put("level", rs.getString("level"));
                row.put("total_phases", rs.getInt("total_phases"));
                return row;
            }
        }, userId);
    }
}
