package com.pathpilot.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import com.pathpilot.model.User;

/**
 * UserDAO for PathPilot Identity Management.
 * Handles Database operations for Students (USER role), Admins, and Mentors.
 */
@Repository
public class UserDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 1. Check if an email address is already registered.
     */
    public boolean emailExists(String email) {
        String sql = "SELECT count(*) FROM users WHERE email = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, email);
        return count != null && count > 0;
    }

    /**
     * 2. Flexible Registration.
     * Allows setting the role (e.g., 'USER' or 'STUDENT') dynamically.
     * Default state: is_verified = 0.
     */
    public int register(String name, String email, String password, String token, String role) {
        String sql = "INSERT INTO users (name, email, password, token, is_verified, role) VALUES (?, ?, ?, ?, 0, ?)";
        return jdbcTemplate.update(sql, name, email, password, token, role);
    }

    /**
     * 3. Secure Login Verification.
     * Fetches all columns (including role, profile_pic) for verified users.
     */
    public User login(String email, String password) {
        String sql = "SELECT * FROM users WHERE email = ? AND password = ? AND is_verified = 1";
        try {
            return jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(User.class), email, password);
        } catch (Exception e) {
            return null; 
        }
    }

    /**
     * 4. Verify User Account.
     * Activates account by token.
     */
    public int verifyUser(String token) {
        String sql = "UPDATE users SET is_verified = 1, token = NULL WHERE token = ?";
        return jdbcTemplate.update(sql, token);
    }

    /**
     * 5. Password Recovery: Save Reset Token/OTP.
     */
    public int saveResetToken(String email, String token) {
        String sql = "UPDATE users SET token = ? WHERE email = ?";
        return jdbcTemplate.update(sql, token, email);
    }

    /**
     * 6. Password Recovery: Update Password by Token.
     */
    public boolean updatePasswordByToken(String token, String newPassword) {
        String sql = "UPDATE users SET password = ?, token = NULL WHERE token = ?";
        return jdbcTemplate.update(sql, newPassword, token) > 0;
    }

    /**
     * 6a. Verify OTP for password reset and return email if valid
     */
    public String verifyOtpAndGetEmail(String otp) {
        String sql = "SELECT email FROM users WHERE token = ?";
        try {
            return jdbcTemplate.queryForObject(sql, String.class, otp);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 6b. Verify Registration OTP
     */
    public boolean verifyRegistrationOtp(String email, String otp) {
        String sql = "SELECT COUNT(*) FROM users WHERE email = ? AND token = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, email, otp);
        return count != null && count > 0;
    }

    /**
     * 6c. Mark user as verified after OTP verification
     */
    public boolean markUserVerified(String email) {
        String sql = "UPDATE users SET is_verified = 1, token = NULL WHERE email = ?";
        return jdbcTemplate.update(sql, email) > 0;
    }

    /**
     * 7. Security: Verify Current Password.
     * Crucial for the UserController's updateProfile method.
     */
    public boolean verifyCurrentPassword(int userId, String inputPassword) {
        String sql = "SELECT count(*) FROM users WHERE id = ? AND password = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, userId, inputPassword);
        return count != null && count > 0;
    }

    /**
     * 8. Profile Management.
     * Updates profile picture and name. Handles password update only if provided.
     */
    public boolean updateProfile(int id, String name, String password, String profilePic) {
        if (password != null && !password.trim().isEmpty()) {
            String sql = "UPDATE users SET name = ?, password = ?, profile_pic = ? WHERE id = ?";
            return jdbcTemplate.update(sql, name, password, profilePic, id) > 0;
        } else {
            String sql = "UPDATE users SET name = ?, profile_pic = ? WHERE id = ?";
            return jdbcTemplate.update(sql, name, profilePic, id) > 0;
        }
    }

    /**
     * 9. Fetch User by ID.
     * Useful for refreshing session data.
     */
    public User getUserById(int id) {
        String sql = "SELECT * FROM users WHERE id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(User.class), id);
        } catch (Exception e) {
            return null;
        }
    }

    public boolean updatePhone(int userId, String phone) {
        String sql = "UPDATE users SET phone = ? WHERE id = ?";
        return jdbcTemplate.update(sql, phone, userId) > 0;
    }

    /**
     * 10. Administrative: Delete User.
     */
    public int deleteUser(int userId) {
        String sql = "DELETE FROM users WHERE id = ?";
        return jdbcTemplate.update(sql, userId);
    }

    /**
     * Get users by role (e.g., ADMIN, MENTOR)
     */
    public java.util.List<User> getUsersByRole(String role) {
        String sql = "SELECT * FROM users WHERE role = ? ORDER BY created_at DESC";
        try {
            return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(User.class), role);
        } catch (Exception e) {
            return new java.util.ArrayList<>();
        }
    }

    // ==========================================
    // ADDITIONAL FUNCTIONAL IMPLEMENTATIONS
    // ==========================================

    /**
     * 11. Self-Registration implementation for AuthController.
     * Maps to the 'register' page logic.
     */
    public void selfRegister(String name, String email, String password, String token) {
        String sql = "INSERT INTO users (name, email, password, token, is_verified, role) VALUES (?, ?, ?, ?, 0, 'student')";
        jdbcTemplate.update(sql, name, email, password, token);
    }

    /**
     * 12. Admin User Creation.
     * Directly sets is_verified to 1 and includes the phone field.
     */
    public boolean adminCreateUser(String name, String email, String phone, String password, String role) {
        String sql = "INSERT INTO users (name, email, phone, password, is_verified, role) VALUES (?, ?, ?, ?, 1, ?)";
        return jdbcTemplate.update(sql, name, email, phone, password, role) > 0;
    }

    /**
     * 13. Admin Record Override.
     * Updates password only if a new one is provided.
     */
    public boolean adminOverrideUser(String email, String name, String password, String phone, String role, String status) {
        int isVerified = "Active".equalsIgnoreCase(status) ? 1 : 0;
        
        if (password != null && !password.trim().isEmpty()) {
            String sql = "UPDATE users SET name = ?, password = ?, phone = ?, role = ?, is_verified = ? WHERE email = ?";
            return jdbcTemplate.update(sql, name, password, phone, role, isVerified, email) > 0;
        } else {
            String sql = "UPDATE users SET name = ?, phone = ?, role = ?, is_verified = ? WHERE email = ?";
            return jdbcTemplate.update(sql, name, phone, role, isVerified, email) > 0;
        }
    }
}