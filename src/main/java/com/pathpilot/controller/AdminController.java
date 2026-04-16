package com.pathpilot.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import jakarta.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.pathpilot.dao.UserDAO;
import com.pathpilot.model.User;

/**
 * AdminController handles UI navigation for secure management routes.
 * CRUD logic for Identity/Passwords is handled by AuthController via AJAX.
 */
@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UserDAO userDAO;

    /**
     * Helper to verify admin session status.
     */
    private boolean isNotAdmin(HttpSession session) {
        return session == null || !"ADMIN".equals(session.getAttribute("role"));
    }

    // ==========================================
    // 1. DASHBOARD & USER MANAGEMENT (UI)
    // ==========================================

    @GetMapping("/dashboard")
    public String dashboard(HttpSession session) {
        if (isNotAdmin(session)) return "redirect:/login";
        return "admin/admin_dashboard"; 
    }

    @GetMapping("/users")
    public String users(HttpSession session) {
        if (isNotAdmin(session)) return "redirect:/login";
        return "admin/admin_users";
    }

    // ==========================================
    // AJAX ENDPOINTS FOR USER MANAGEMENT
    // ==========================================

    /**
     * Health check endpoint
     */
    @GetMapping("/api/health")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> health() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "ok");
        response.put("message", "Admin API is working");
        return ResponseEntity.ok(response);
    }

    /**
     * Get all users (for table display)
     */
    @GetMapping("/api/users")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getAllUsers(HttpSession session) {
        if (isNotAdmin(session)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        try {
            System.out.println("🔍 Fetching all users...");
            
            List<User> users = userDAO.getUsersByRole("student");
            System.out.println("✅ Students: " + (users != null ? users.size() : 0));
            
            List<User> mentors = userDAO.getUsersByRole("USER");
            System.out.println("✅ Mentors: " + (mentors != null ? mentors.size() : 0));
            
            List<User> admins = userDAO.getUsersByRole("ADMIN");
            System.out.println("✅ Admins: " + (admins != null ? admins.size() : 0));
            
            // Add all users to list
            java.util.ArrayList<User> allUsers = new java.util.ArrayList<>();
            if (users != null) allUsers.addAll(users);
            if (mentors != null) allUsers.addAll(mentors);
            if (admins != null) allUsers.addAll(admins);
            
            System.out.println("✅ Total users collected: " + allUsers.size());

            // Convert to DTOs without passwords (security & encoding fix)
            java.util.List<Map<String, Object>> safeUsers = new java.util.ArrayList<>();
            for (User user : allUsers) {
                Map<String, Object> userMap = new HashMap<>();
                userMap.put("id", user.getId());
                userMap.put("name", user.getName());
                userMap.put("email", user.getEmail());
                userMap.put("phone", user.getPhone());
                userMap.put("role", user.getRole());
                userMap.put("verified", user.isVerified());
                safeUsers.add(userMap);
            }
            
            System.out.println("✅ Converted to safe users: " + safeUsers.size());

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", safeUsers);
            response.put("count", safeUsers.size());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            System.err.println("❌ Error fetching users: " + e.getMessage());
            e.printStackTrace();
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Create new user
     */
    @PostMapping("/api/users")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> createUser(
            @RequestParam String name,
            @RequestParam String email,
            @RequestParam String phone,
            @RequestParam String password,
            @RequestParam(defaultValue = "student") String role,
            HttpSession session) {
        if (isNotAdmin(session)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        try {
            if (userDAO.emailExists(email)) {
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("message", "Email already exists");
                return ResponseEntity.ok(response);
            }

            boolean created = userDAO.adminCreateUser(name, email, phone, password, role);
            Map<String, Object> response = new HashMap<>();
            response.put("success", created);
            response.put("message", created ? "User created successfully" : "Failed to create user");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Update user
     */
    @PutMapping("/api/users/{userId}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> updateUser(
            @PathVariable int userId,
            @RequestParam String name,
            @RequestParam String phone,
            @RequestParam(required = false) String password,
            @RequestParam String role,
            @RequestParam String status,
            HttpSession session) {
        if (isNotAdmin(session)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        try {
            User user = userDAO.getUserById(userId);
            if (user == null) {
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("message", "User not found");
                return ResponseEntity.ok(response);
            }
            
            boolean updated = userDAO.adminOverrideUser(user.getEmail(), name, password, phone, role, status);
            Map<String, Object> response = new HashMap<>();
            response.put("success", updated);
            response.put("message", updated ? "User updated successfully" : "Failed to update user");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Delete user
     */
    @DeleteMapping("/api/users/{userId}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> deleteUser(
            @PathVariable int userId,
            HttpSession session) {
        if (isNotAdmin(session)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        try {
            int result = userDAO.deleteUser(userId);
            Map<String, Object> response = new HashMap<>();
            response.put("success", result > 0);
            response.put("message", result > 0 ? "User deleted successfully" : "Failed to delete user");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }


    @GetMapping("/settings")
    public String settings(HttpSession session) {
        if (isNotAdmin(session)) return "redirect:/login";
        return "admin/admin_settings";
    }

    @GetMapping("/manage-home")
    public String manageHome(HttpSession session) {
        if (isNotAdmin(session)) return "redirect:/login";
        return "admin/admin_manage_home";
    }

    @GetMapping("/manage-features")
    public String manageFeatures(HttpSession session) {
        if (isNotAdmin(session)) return "redirect:/login";
        return "admin/admin_manage_features";
    }

    @GetMapping("/manage-about")
    public String manageAbout(HttpSession session) {
        if (isNotAdmin(session)) return "redirect:/login";
        return "admin/admin_manage_about";
    }

    @GetMapping("/manage-contact")
    public String manageContact(HttpSession session) {
        if (isNotAdmin(session)) return "redirect:/login";
        return "admin/admin_manage_contact";
    }

    @GetMapping("/manage-footer")
    public String manageFooter(HttpSession session) {
        if (isNotAdmin(session)) return "redirect:/login";
        return "admin/admin_manage_footer";
    }

    @GetMapping("/notify")
    public String notifyPage(HttpSession session) {
        if (isNotAdmin(session)) return "redirect:/login";
        return "admin/admin_notify";
    }

    // ==========================================
    // 4. CONTENT UPDATES (POST Mappings)
    // ==========================================

    /**
     * Handles general platform content saving.
     * Note: User registration/overrides are now handled in AuthController.
     */
    @PostMapping({"/update-content", "/manage-home", "/manage-features", "/manage-about", "/manage-contact", "/manage-footer"})
    public String updatePlatformContent(HttpSession session) {
        if (isNotAdmin(session)) return "redirect:/login";
        
        System.out.println("Platform content updated via AdminController.");
        return "redirect:/admin/settings?status=saved";
    }
}