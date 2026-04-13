package com.pathpilot.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import com.pathpilot.model.Notification;
import java.util.List;

/**
 * NotificationDAO - CRUD operations for User Notifications
 * Manages notifications for users (alerts, messages, updates)
 */
@Repository
public class NotificationDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * CREATE - Send a new notification to user
     */
    public int addNotification(Notification notification) {
        String sql = "INSERT INTO notifications (user_id, title, message, notification_type, is_read) " +
                     "VALUES (?, ?, ?, ?, ?)";
        return jdbcTemplate.update(sql, notification.getUserId(), notification.getTitle(), 
                                   notification.getMessage(), notification.getNotificationType(), 
                                   notification.isRead());
    }

    /**
     * READ - Get notification by ID
     */
    public Notification getNotificationById(int notificationId) {
        String sql = "SELECT * FROM notifications WHERE notification_id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(Notification.class), notificationId);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * READ - Get all notifications for a user
     */
    public List<Notification> getNotificationsByUserId(int userId) {
        String sql = "SELECT * FROM notifications WHERE user_id = ? ORDER BY created_at DESC";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Notification.class), userId);
    }

    /**
     * READ - Get unread notifications for a user
     */
    public List<Notification> getUnreadNotificationsByUserId(int userId) {
        String sql = "SELECT * FROM notifications WHERE user_id = ? AND is_read = false ORDER BY created_at DESC";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Notification.class), userId);
    }

    /**
     * READ - Get notifications by type
     */
    public List<Notification> getNotificationsByType(String notificationType, int userId) {
        String sql = "SELECT * FROM notifications WHERE user_id = ? AND notification_type = ? ORDER BY created_at DESC";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Notification.class), userId, notificationType);
    }

    /**
     * READ - Get all notifications
     */
    public List<Notification> getAllNotifications() {
        String sql = "SELECT * FROM notifications ORDER BY created_at DESC";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Notification.class));
    }

    /**
     * UPDATE - Mark notification as read
     */
    public int markAsRead(int notificationId) {
        String sql = "UPDATE notifications SET is_read = true WHERE notification_id = ?";
        return jdbcTemplate.update(sql, notificationId);
    }

    /**
     * UPDATE - Mark all notifications as read for a user
     */
    public int markAllAsRead(int userId) {
        String sql = "UPDATE notifications SET is_read = true WHERE user_id = ? AND is_read = false";
        return jdbcTemplate.update(sql, userId);
    }

    /**
     * UPDATE - Update notification details
     */
    public int updateNotification(Notification notification) {
        String sql = "UPDATE notifications SET title = ?, message = ?, notification_type = ?, is_read = ? WHERE notification_id = ?";
        return jdbcTemplate.update(sql, notification.getTitle(), notification.getMessage(), 
                                   notification.getNotificationType(), notification.isRead(), 
                                   notification.getNotificationId());
    }

    /**
     * DELETE - Delete a notification
     */
    public int deleteNotification(int notificationId) {
        String sql = "DELETE FROM notifications WHERE notification_id = ?";
        return jdbcTemplate.update(sql, notificationId);
    }

    /**
     * DELETE - Delete all notifications for a user
     */
    public int deleteNotificationsByUserId(int userId) {
        String sql = "DELETE FROM notifications WHERE user_id = ?";
        return jdbcTemplate.update(sql, userId);
    }

    /**
     * DELETE - Delete old notifications (older than specified days)
     */
    public int deleteOldNotifications(int daysOld) {
        String sql = "DELETE FROM notifications WHERE created_at < DATE_SUB(NOW(), INTERVAL ? DAY)";
        return jdbcTemplate.update(sql, daysOld);
    }

    /**
     * Get count of unread notifications for a user
     */
    public int getUnreadNotificationCount(int userId) {
        String sql = "SELECT COUNT(*) FROM notifications WHERE user_id = ? AND is_read = false";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, userId);
        return count != null ? count : 0;
    }

    /**
     * Get total notification count for a user
     */
    public int getTotalNotificationCount(int userId) {
        String sql = "SELECT COUNT(*) FROM notifications WHERE user_id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, userId);
        return count != null ? count : 0;
    }

    /**
     * Bulk send notification to multiple users
     */
    public int bulkSendNotification(List<Integer> userIds, String title, String message, String notificationType) {
        String sql = "INSERT INTO notifications (user_id, title, message, notification_type, is_read) VALUES (?, ?, ?, ?, false)";
        int count = 0;
        for (int userId : userIds) {
            count += jdbcTemplate.update(sql, userId, title, message, notificationType);
        }
        return count;
    }
}
