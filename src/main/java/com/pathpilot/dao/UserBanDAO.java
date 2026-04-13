package com.pathpilot.dao;

import com.pathpilot.model.UserBan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for User Ban operations.
 * Handles all database operations related to banning and unbanning students.
 */
@Repository
public class UserBanDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * Ban a student from viewing content by an instructor
     */
    public boolean banStudent(int instructorId, int studentId, String banType, String reason, int createdBy) {
        String sql = "INSERT INTO user_bans (instructor_id, student_id, ban_type, reason, is_active, created_by, banned_at) " +
                     "VALUES (?, ?, ?, ?, true, ?, NOW()) " +
                     "ON DUPLICATE KEY UPDATE " +
                     "ban_type = ?, reason = ?, is_active = true, banned_at = NOW()";
        
        try {
            int result = jdbcTemplate.update(sql, 
                instructorId, 
                studentId, 
                banType, 
                reason != null ? reason : "", 
                createdBy,
                banType,
                reason != null ? reason : "");
            return result > 0;
        } catch (Exception e) {
            System.err.println("Error banning student: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Lift a ban (temporary or permanent restoration)
     */
    public boolean liftBan(int instructorId, int studentId) {
        String sql = "UPDATE user_bans SET is_active = false, lifted_at = NOW() " +
                     "WHERE instructor_id = ? AND student_id = ? AND is_active = true";
        
        try {
            int result = jdbcTemplate.update(sql, instructorId, studentId);
            return result > 0;
        } catch (Exception e) {
            System.err.println("Error lifting ban: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Check if a student is banned by a specific instructor
     */
    public boolean isStudentBanned(int instructorId, int studentId) {
        String sql = "SELECT COUNT(*) FROM user_bans " +
                     "WHERE instructor_id = ? AND student_id = ? AND is_active = true";
        
        try {
            Integer count = jdbcTemplate.queryForObject(sql, Integer.class, instructorId, studentId);
            return count != null && count > 0;
        } catch (Exception e) {
            System.err.println("Error checking if student is banned: " + e.getMessage());
            return false;
        }
    }

    /**
     * Get ban details for a student by a specific instructor
     */
    public UserBan getBanDetails(int instructorId, int studentId) {
        String sql = "SELECT ban_id, instructor_id, student_id, ban_type, reason, banned_at, lifted_at, is_active, created_by " +
                     "FROM user_bans " +
                     "WHERE instructor_id = ? AND student_id = ? AND is_active = true";
        
        try {
            List<UserBan> results = jdbcTemplate.query(sql, (rs, rowNum) -> {
                UserBan ban = new UserBan();
                ban.setBanId(rs.getInt("ban_id"));
                ban.setInstructorId(rs.getInt("instructor_id"));
                ban.setStudentId(rs.getInt("student_id"));
                ban.setBanType(rs.getString("ban_type"));
                ban.setReason(rs.getString("reason"));
                ban.setActive(rs.getBoolean("is_active"));
                return ban;
            }, instructorId, studentId);
            
            return results.isEmpty() ? null : results.get(0);
        } catch (Exception e) {
            System.err.println("Error getting ban details: " + e.getMessage());
            return null;
        }
    }

    /**
     * Get all active bans created by an instructor
     */
    public List<UserBan> getActiveBansByInstructor(int instructorId) {
        List<UserBan> bans = new ArrayList<>();
        String sql = "SELECT ban_id, instructor_id, student_id, ban_type, reason, banned_at, lifted_at, is_active, created_by " +
                     "FROM user_bans " +
                     "WHERE instructor_id = ? AND is_active = true " +
                     "ORDER BY banned_at DESC";
        
        try {
            bans = jdbcTemplate.query(sql, (rs, rowNum) -> {
                UserBan ban = new UserBan();
                ban.setBanId(rs.getInt("ban_id"));
                ban.setInstructorId(rs.getInt("instructor_id"));
                ban.setStudentId(rs.getInt("student_id"));
                ban.setBanType(rs.getString("ban_type"));
                ban.setReason(rs.getString("reason"));
                ban.setActive(rs.getBoolean("is_active"));
                return ban;
            }, instructorId);
        } catch (Exception e) {
            System.err.println("Error fetching bans by instructor: " + e.getMessage());
        }
        return bans;
    }

    /**
     * Get all active bans on a specific student
     */
    public List<UserBan> getBansOnStudent(int studentId) {
        List<UserBan> bans = new ArrayList<>();
        String sql = "SELECT ban_id, instructor_id, student_id, ban_type, reason, banned_at, lifted_at, is_active, created_by " +
                     "FROM user_bans " +
                     "WHERE student_id = ? AND is_active = true " +
                     "ORDER BY banned_at DESC";
        
        try {
            bans = jdbcTemplate.query(sql, (rs, rowNum) -> {
                UserBan ban = new UserBan();
                ban.setBanId(rs.getInt("ban_id"));
                ban.setInstructorId(rs.getInt("instructor_id"));
                ban.setStudentId(rs.getInt("student_id"));
                ban.setBanType(rs.getString("ban_type"));
                ban.setReason(rs.getString("reason"));
                ban.setActive(rs.getBoolean("is_active"));
                return ban;
            }, studentId);
        } catch (Exception e) {
            System.err.println("Error fetching bans on student: " + e.getMessage());
        }
        return bans;
    }

    /**
     * Delete a ban record (admin only)
     */
    public boolean deleteBan(int banId) {
        String sql = "DELETE FROM user_bans WHERE ban_id = ?";
        
        try {
            int result = jdbcTemplate.update(sql, banId);
            return result > 0;
        } catch (Exception e) {
            System.err.println("Error deleting ban: " + e.getMessage());
            return false;
        }
    }
}
