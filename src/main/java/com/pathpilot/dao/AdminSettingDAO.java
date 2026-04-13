package com.pathpilot.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import com.pathpilot.model.AdminSetting;
import java.util.List;

/**
 * AdminSettingDAO - CRUD operations for Admin Settings
 * Manages application-wide configuration and settings
 */
@Repository
public class AdminSettingDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * CREATE - Add a new setting
     */
    public int addSetting(AdminSetting setting) {
        String sql = "INSERT INTO admin_settings (setting_key, setting_value, description, updated_by) " +
                     "VALUES (?, ?, ?, ?)";
        return jdbcTemplate.update(sql, setting.getSettingKey(), setting.getSettingValue(), 
                                   setting.getDescription(), setting.getUpdatedBy());
    }

    /**
     * READ - Get setting by ID
     */
    public AdminSetting getSettingById(int settingId) {
        String sql = "SELECT * FROM admin_settings WHERE setting_id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(AdminSetting.class), settingId);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * READ - Get setting by key
     */
    public AdminSetting getSettingByKey(String settingKey) {
        String sql = "SELECT * FROM admin_settings WHERE setting_key = ?";
        try {
            return jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(AdminSetting.class), settingKey);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * READ - Get setting value by key
     */
    public String getSettingValue(String settingKey) {
        String sql = "SELECT setting_value FROM admin_settings WHERE setting_key = ?";
        try {
            return jdbcTemplate.queryForObject(sql, String.class, settingKey);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * READ - Get all settings
     */
    public List<AdminSetting> getAllSettings() {
        String sql = "SELECT * FROM admin_settings ORDER BY setting_key ASC";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(AdminSetting.class));
    }

    /**
     * READ - Get settings by prefix (e.g., "email_" for all email settings)
     */
    public List<AdminSetting> getSettingsByPrefix(String prefix) {
        String sql = "SELECT * FROM admin_settings WHERE setting_key LIKE ? ORDER BY setting_key ASC";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(AdminSetting.class), prefix + "%");
    }

    /**
     * UPDATE - Update setting by ID
     */
    public int updateSetting(AdminSetting setting) {
        String sql = "UPDATE admin_settings SET setting_value = ?, description = ?, updated_by = ? WHERE setting_id = ?";
        return jdbcTemplate.update(sql, setting.getSettingValue(), setting.getDescription(), 
                                   setting.getUpdatedBy(), setting.getSettingId());
    }

    /**
     * UPDATE - Update setting by key
     */
    public int updateSettingByKey(String settingKey, String settingValue, int updatedBy) {
        String sql = "UPDATE admin_settings SET setting_value = ?, updated_by = ? WHERE setting_key = ?";
        return jdbcTemplate.update(sql, settingValue, updatedBy, settingKey);
    }

    /**
     * UPDATE - Increment numeric setting value
     */
    public int incrementSetting(String settingKey, int amount) {
        String sql = "UPDATE admin_settings SET setting_value = CAST(CAST(setting_value AS UNSIGNED) + ? AS CHAR) WHERE setting_key = ?";
        return jdbcTemplate.update(sql, amount, settingKey);
    }

    /**
     * DELETE - Delete setting by ID
     */
    public int deleteSetting(int settingId) {
        String sql = "DELETE FROM admin_settings WHERE setting_id = ?";
        return jdbcTemplate.update(sql, settingId);
    }

    /**
     * DELETE - Delete setting by key
     */
    public int deleteSettingByKey(String settingKey) {
        String sql = "DELETE FROM admin_settings WHERE setting_key = ?";
        return jdbcTemplate.update(sql, settingKey);
    }

    /**
     * Get count of settings
     */
    public int getSettingCount() {
        String sql = "SELECT COUNT(*) FROM admin_settings";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class);
        return count != null ? count : 0;
    }

    /**
     * Check if setting exists
     */
    public boolean settingExists(String settingKey) {
        String sql = "SELECT COUNT(*) FROM admin_settings WHERE setting_key = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, settingKey);
        return count != null && count > 0;
    }

    /**
     * Insert or update a setting (upsert operation)
     */
    public int upsertSetting(String settingKey, String settingValue, String description, int updatedBy) {
        if (settingExists(settingKey)) {
            return updateSettingByKey(settingKey, settingValue, updatedBy);
        } else {
            String sql = "INSERT INTO admin_settings (setting_key, setting_value, description, updated_by) VALUES (?, ?, ?, ?)";
            return jdbcTemplate.update(sql, settingKey, settingValue, description, updatedBy);
        }
    }
}
