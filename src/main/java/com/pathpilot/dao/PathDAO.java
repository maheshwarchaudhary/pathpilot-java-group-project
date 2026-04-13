package com.pathpilot.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import com.pathpilot.model.CareerPath;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

/**
 * PathDAO - CRUD operations for Career Paths
 * Manages learning roadmaps and career paths created by users
 */
@Repository
public class PathDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * CREATE - Add a new career path and return the generated ID
     */
    public int addPath(CareerPath path) {
        String sql = "INSERT INTO career_paths (title, description, category, level, status, created_by, total_phases, published_at, created_at) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, CASE WHEN ? = 'PUBLISHED' THEN NOW() ELSE NULL END, NOW())";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, path.getTitle());
            ps.setString(2, path.getDescription());
            ps.setString(3, path.getCategory());
            ps.setString(4, path.getLevel());
            ps.setString(5, path.getStatus());
            ps.setInt(6, path.getCreatedBy());
            ps.setInt(7, path.getTotalPhases());
            ps.setString(8, path.getStatus());
            return ps;
        }, keyHolder);
        return keyHolder.getKey().intValue();
    }

    /**
     * READ - Get path by ID
     */
    public CareerPath getPathById(int pathId) {
        String sql = "SELECT * FROM career_paths WHERE path_id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(CareerPath.class), pathId);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * READ - Get all paths created by a specific user
     */
    public List<CareerPath> getPathsByUserId(int userId) {
        String sql = "SELECT cp.*, " +
                     "(SELECT COUNT(*) FROM phases WHERE path_id = cp.path_id) as phaseCount " +
                     "FROM career_paths cp WHERE created_by = ? ORDER BY created_at DESC";
        try {
            return jdbcTemplate.query(sql, (rs, rowNum) -> {
                CareerPath path = new CareerPath();
                path.setPathId(rs.getInt("path_id"));
                path.setId(rs.getInt("path_id"));
                path.setTitle(rs.getString("title"));
                path.setDescription(rs.getString("description"));
                path.setCategory(rs.getString("category"));
                path.setLevel(rs.getString("level"));
                path.setStatus(rs.getString("status"));
                path.setCreatedBy(rs.getInt("created_by"));
                path.setPhaseCount(rs.getInt("phaseCount"));
                path.setTotalPhases(rs.getInt("phaseCount"));
                path.setDuration("4 MONTHS");  // Default duration - can be calculated later
                return path;
            }, userId);
        } catch (Exception e) {
            System.err.println("Error fetching paths for user " + userId + ": " + e.getMessage());
            return new java.util.ArrayList<>();
        }
    }

    /**
     * READ - Get all published paths (for students to browse)
     */
    public List<CareerPath> getPublishedPaths() {
        String sql = "SELECT * FROM career_paths WHERE status = 'PUBLISHED' ORDER BY created_at DESC";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(CareerPath.class));
    }

    /**
     * READ - Get paths by category
     */
    public List<CareerPath> getPathsByCategory(String category) {
        String sql = "SELECT * FROM career_paths WHERE category = ? AND status = 'PUBLISHED' ORDER BY created_at DESC";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(CareerPath.class), category);
    }

    /**
     * READ - Get all paths
     */
    public List<CareerPath> getAllPaths() {
        String sql = "SELECT * FROM career_paths ORDER BY created_at DESC";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(CareerPath.class));
    }

    /**
     * UPDATE - Update path details
     */
    public int updatePath(CareerPath path) {
        String sql = "UPDATE career_paths SET title = ?, description = ?, category = ?, level = ?, status = ?, total_phases = ?, " +
                     "updated_at = NOW(), published_at = CASE WHEN ? = 'PUBLISHED' THEN COALESCE(published_at, NOW()) ELSE NULL END " +
                     "WHERE path_id = ?";
        return jdbcTemplate.update(sql, path.getTitle(), path.getDescription(), path.getCategory(), 
                                   path.getLevel(), path.getStatus(), path.getTotalPhases(), path.getStatus(), path.getPathId());
    }

    /**
     * UPDATE - Publish a path
     */
    public int publishPath(int pathId) {
        String sql = "UPDATE career_paths SET status = 'PUBLISHED', published_at = NOW() WHERE path_id = ?";
        return jdbcTemplate.update(sql, pathId);
    }

    /**
     * DELETE - Delete a path by ID
     */
    public int deletePath(int pathId) {
        String sql = "DELETE FROM career_paths WHERE path_id = ?";
        return jdbcTemplate.update(sql, pathId);
    }

    /**
     * Get count of paths created by a user
     */
    public int getPathCountByUserId(int userId) {
        String sql = "SELECT COUNT(*) FROM career_paths WHERE created_by = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, userId);
        return count != null ? count : 0;
    }

    /**
     * Get count of published paths
     */
    public int getPublishedPathCount() {
        String sql = "SELECT COUNT(*) FROM career_paths WHERE status = 'PUBLISHED'";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class);
        return count != null ? count : 0;
    }
}
