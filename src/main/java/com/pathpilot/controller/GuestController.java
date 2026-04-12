package com.pathpilot.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * GuestController handles all navigation for non-authenticated users using Spring MVC.
 */
@Controller
@RequestMapping("/guest")
public class GuestController {

    @GetMapping("/home")
    public String home() {
        // Resolves to /WEB-INF/views/guest/guest_home.jsp
        return "guest/guest_home"; 
    }

    @GetMapping("/features")
    public String features() {
        return "guest/guest_features";
    }

    @GetMapping("/resources")
    public String resources() {
        return "guest/guest_resources";
    }

    @GetMapping("/about")
    public String about() {
        return "guest/guest_about";
    }

    @GetMapping("/course_detail")
    public String courseDetail() {
        return "guest/guest_course_details";
    }

    @GetMapping("/contact")
    public String contact() {
        return "guest/guest_contact";
    }

    @GetMapping("/careerpath")
    public String careerPath() {
        return "guest/guest_career_path";
    }
}