package com.pathpilot.dao;

import java.util.HashMap;
import java.util.Map;
import java.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * UserStatisticsDAO - Handles user learning progress, time tracking, and streaks
 */
@Repository
public class UserStatisticsDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * Get user statistics for a specific enrollment
     * Returns: progress %, learning time this week, and current streak
     */
    public Map<String, Object> getUserStatistics(int userId, int enrollmentId) {
        String sql = "SELECT " +
                     "e.progress_percentage, " +
                     "COALESCE(SUM(ss.duration_minutes), 0) as total_learning_minutes_week, " +
                     "COALESCE(us.current_streak, 0) as current_streak, " +
                     "COALESCE(us.longest_streak, 0) as longest_streak, " +
                     "COUNT(DISTINCT DATE(ss.session_start)) as days_active_week " +
                     "FROM enrollments e " +
                     "LEFT JOIN user_streaks us ON e.user_id = us.user_id " +
                     "LEFT JOIN study_sessions ss ON e.enrollment_id = ss.enrollment_id " +
                     "  AND ss.session_start >= DATE_SUB(NOW(), INTERVAL 7 DAY) " +
                     "  AND ss.is_completed = TRUE " +
                     "WHERE e.user_id = ? AND e.enrollment_id = ? " +
                     "GROUP BY e.enrollment_id, e.progress_percentage, us.current_streak, us.longest_streak";

        try {
            return jdbcTemplate.queryForMap(sql, userId, enrollmentId);
        } catch (Exception e) {
            // Return default values if no data found
            Map<String, Object> defaults = new HashMap<>();
            defaults.put("progress_percentage", 0);
            defaults.put("total_learning_minutes_week", 0);
            defaults.put("current_streak", 0);
            defaults.put("longest_streak", 0);
            defaults.put("days_active_week", 0);
            return defaults;
        }
    }

    /**
     * Get current user streak
     */
    public Map<String, Object> getUserStreak(int userId) {
        String sql = "SELECT current_streak, longest_streak, streak_started_date " +
                     "FROM user_streaks " +
                     "WHERE user_id = ?";

        try {
            return jdbcTemplate.queryForMap(sql, userId);
        } catch (Exception e) {
            Map<String, Object> defaults = new HashMap<>();
            defaults.put("current_streak", 0);
            defaults.put("longest_streak", 0);
            defaults.put("streak_started_date", null);
            return defaults;
        }
    }

    /**
     * Get total learning time for current week
     */
    public Integer getTotalLearningTimeWeek(int userId) {
        String sql = "SELECT COALESCE(SUM(duration_minutes), 0) as total_minutes " +
                     "FROM study_sessions " +
                     "WHERE user_id = ? " +
                     "  AND session_start >= DATE_SUB(NOW(), INTERVAL 7 DAY) " +
                     "  AND is_completed = TRUE";

        Integer minutes = jdbcTemplate.queryForObject(sql, Integer.class, userId);
        return minutes != null ? minutes : 0;
    }

    /**
     * Get total learning time for all enrollments
     */
    public Integer getTotalLearningTime(int userId) {
        String sql = "SELECT COALESCE(SUM(duration_minutes), 0) as total_minutes " +
                     "FROM study_sessions " +
                     "WHERE user_id = ? AND is_completed = TRUE";

        Integer minutes = jdbcTemplate.queryForObject(sql, Integer.class, userId);
        return minutes != null ? minutes : 0;
    }

    /**
     * Log user activity (called when user completes a study session)
     */
    public void logStudySession(int enrollmentId, int userId, int phaseId, String contentStudied, int durationMinutes) {
        String sql = "INSERT INTO study_sessions " +
                     "(enrollment_id, user_id, phase_id, session_end, duration_minutes, content_studied, is_completed) " +
                     "VALUES (?, ?, ?, NOW(), ?, ?, TRUE)";

        jdbcTemplate.update(sql, enrollmentId, userId, phaseId, durationMinutes, contentStudied);
        
        // Update user activity
        updateUserActivityLog(userId, durationMinutes);
        
        // Update streak
        updateUserStreak(userId);
    }

    /**
     * Update daily user activity log
     */
    private void updateUserActivityLog(int userId, int durationMinutes) {
        String sql = "INSERT INTO user_activity_log " +
                     "(user_id, activity_date, session_count, total_learning_minutes) " +
                     "VALUES (?, DATE(NOW()), 1, ?) " +
                     "ON DUPLICATE KEY UPDATE " +
                     "session_count = session_count + 1, " +
                     "total_learning_minutes = total_learning_minutes + VALUES(total_learning_minutes), " +
                     "updated_at = CURRENT_TIMESTAMP";

        jdbcTemplate.update(sql, userId, Math.max(0, durationMinutes));
    }

    /**
     * Update user streak
     */
    private void updateUserStreak(int userId) {
        String checkSql = "SELECT current_streak, longest_streak, last_activity_date, streak_started_date FROM user_streaks WHERE user_id = ?";
        try {
            Map<String, Object> streak = jdbcTemplate.queryForMap(checkSql, userId);
            
            LocalDate today = LocalDate.now();
            LocalDate yesterday = today.minusDays(1);
            LocalDate lastActivityDate = toLocalDate(streak.get("last_activity_date"));

            int currentStreak = ((Number) streak.getOrDefault("current_streak", 0)).intValue();
            int longestStreak = ((Number) streak.getOrDefault("longest_streak", 0)).intValue();

            if (lastActivityDate != null && lastActivityDate.isEqual(today)) {
                return;
            }

            int nextStreak = (lastActivityDate != null && lastActivityDate.isEqual(yesterday)) ? currentStreak + 1 : 1;
            int nextLongest = Math.max(longestStreak, nextStreak);
            LocalDate streakStartDate = (lastActivityDate != null && lastActivityDate.isEqual(yesterday))
                    ? (toLocalDate(streak.get("streak_started_date")) != null ? toLocalDate(streak.get("streak_started_date")) : yesterday)
                    : today;

            String updateSql = "UPDATE user_streaks SET current_streak = ?, longest_streak = ?, last_activity_date = ?, streak_started_date = ? WHERE user_id = ?";
            jdbcTemplate.update(updateSql, nextStreak, nextLongest, today, streakStartDate, userId);
        } catch (Exception e) {
            String upsertSql = "INSERT INTO user_streaks (user_id, current_streak, longest_streak, last_activity_date, streak_started_date) " +
                    "VALUES (?, 1, 1, CURDATE(), CURDATE()) " +
                    "ON DUPLICATE KEY UPDATE " +
                    "current_streak = CASE " +
                    "  WHEN last_activity_date = CURDATE() THEN current_streak " +
                    "  WHEN last_activity_date = DATE_SUB(CURDATE(), INTERVAL 1 DAY) THEN current_streak + 1 " +
                    "  ELSE 1 END, " +
                    "longest_streak = GREATEST(longest_streak, CASE " +
                    "  WHEN last_activity_date = CURDATE() THEN current_streak " +
                    "  WHEN last_activity_date = DATE_SUB(CURDATE(), INTERVAL 1 DAY) THEN current_streak + 1 " +
                    "  ELSE 1 END), " +
                    "last_activity_date = CURDATE(), " +
                    "streak_started_date = CASE " +
                    "  WHEN last_activity_date = DATE_SUB(CURDATE(), INTERVAL 1 DAY) THEN COALESCE(streak_started_date, CURDATE()) " +
                    "  ELSE CURDATE() END, " +
                    "updated_at = CURRENT_TIMESTAMP";
            jdbcTemplate.update(upsertSql, userId);
        }
    }

    private LocalDate toLocalDate(Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof LocalDate) {
            return (LocalDate) value;
        }
        if (value instanceof java.sql.Date) {
            return ((java.sql.Date) value).toLocalDate();
        }
        if (value instanceof java.sql.Timestamp) {
            return ((java.sql.Timestamp) value).toLocalDateTime().toLocalDate();
        }
        if (value instanceof java.util.Date) {
            return new java.sql.Date(((java.util.Date) value).getTime()).toLocalDate();
        }
        try {
            return LocalDate.parse(String.valueOf(value));
        } catch (Exception ex) {
            return null;
            }
    }
}
