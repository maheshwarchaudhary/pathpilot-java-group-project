package com.pathpilot.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import jakarta.servlet.http.HttpSession;

/**
 * AdminController handles UI navigation for secure management routes.
 * CRUD logic for Identity/Passwords is handled by AuthController via AJAX.
 */
@Controller
@RequestMapping("/admin")
public class AdminController {

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

    @GetMapping("/add-user")
    public String addUserPage(HttpSession session) {
        if (isNotAdmin(session)) return "redirect:/login";
        return "admin/admin_addnewuser";
    }

    @GetMapping("/edit-users")
    public String editUsers(HttpSession session) {
        if (isNotAdmin(session)) return "redirect:/login";
        return "admin/admin_editusers";
    }

    // ==========================================
    // 2. CAREER PATHS & ROADMAPS (UI)
    // ==========================================

    @GetMapping("/career-path")
    public String careerPath(HttpSession session) {
        if (isNotAdmin(session)) return "redirect:/login";
        return "admin/admin_career_path";
    }

    @GetMapping("/create-path")
    public String createPath(HttpSession session) {
        if (isNotAdmin(session)) return "redirect:/login";
        return "admin/admin_create_path";
    }

    @GetMapping("/edit-path")
    public String editPath(HttpSession session) {
        if (isNotAdmin(session)) return "redirect:/login";
        return "admin/admin_edit_path";
    }

    // ==========================================
    // 3. PLATFORM CONTENT MANAGEMENT (UI)
    // ==========================================

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