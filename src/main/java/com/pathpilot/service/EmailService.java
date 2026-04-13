package com.pathpilot.service;

import java.util.Properties;
import jakarta.mail.internet.MimeMessage;

import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * EmailService for PathPilot.
 * Manages automated dispatch for account verification and security OTPs.
 * Implements dual-port failover (587/465) for maximum deliverability.
 */
@Service
public class EmailService {

    /**
     * General purpose email method with optional file attachment capability.
     */
    public void sendEmailWithAttachment(String to, String subject, String body, MultipartFile file) throws Exception {
        try {
            // Primary attempt via TLS Port 587
            send(to, subject, body, file, 587);
        } catch (Exception e) {
            // Secondary fallback via SSL Port 465
            send(to, subject, body, file, 465);
        }
    }

    /**
     * 🔥 Verification Email
     * Dispatches a professional invitation link for new user activation.
     */
    public void sendVerificationLink(String toEmail, String token) throws Exception {
        String verificationUrl = "http://localhost:8080/pathpilot/user/verify?token=" + token;

        String htmlContent = 
            "<div style='font-family: Segoe UI, Tahoma, sans-serif; max-width: 550px; margin: auto; border: 1px solid #eef2f6; padding: 40px; border-radius: 20px; color: #1e293b;'>" +
            "  <div style='text-align: center; margin-bottom: 30px;'><h1 style='color: #4913ec; margin: 0;'>PathPilot</h1></div>" +
            "  <h2 style='color: #0f172a; margin-top: 0;'>Activate your account</h2>" +
            "  <p style='line-height: 1.6; color: #64748b;'>Welcome to PathPilot. To complete your registration and begin your curated learning journey, please verify your email address.</p>" +
            "  <div style='text-align: center; margin: 35px 0;'>" +
            "    <a href='" + verificationUrl + "' style='background-color: #4913ec; color: #ffffff !important; padding: 14px 30px; text-decoration: none; border-radius: 12px; font-weight: bold; font-size: 16px; display: inline-block;'>Verify My Account</a>" +
            "  </div>" +
            "  <p style='font-size: 12px; color: #94a3b8; border-top: 1px solid #f1f5f9; padding-top: 20px; text-align: center;'>&copy; 2026 PathPilot Identity Systems.</p>" +
            "</div>";

        sendEmailWithAttachment(toEmail, "PathPilot: Verify Your Account", htmlContent, null);
    }

    /**
     * 🔑 Security OTP Email - Password Reset
     */
    public void sendOtpEmail(String toEmail, String otp) throws Exception {
        String htmlContent = 
            "<div style='font-family: Segoe UI, Tahoma, sans-serif; max-width: 550px; margin: auto; border: 1px solid #eef2f6; padding: 40px; border-radius: 20px; color: #1e293b;'>" +
            "  <div style='text-align: center; margin-bottom: 30px;'><h1 style='color: #4913ec; margin: 0;'>PathPilot</h1></div>" +
            "  <h2 style='color: #0f172a; margin-top: 0;'>Security Code</h2>" +
            "  <p style='line-height: 1.6; color: #64748b;'>We received a request to access your PathPilot account. Use the code below to complete your password reset:</p>" +
            "  <div style='background: #f8f9fc; padding: 25px; text-align: center; border-radius: 15px; margin: 30px 0; border: 1px dashed #e2e8f0;'>" +
            "    <span style='font-size: 36px; font-weight: 800; letter-spacing: 12px; color: #4913ec;'>" + otp + "</span>" +
            "  </div>" +
            "  <p style='font-size: 13px; color: #64748b;'>This code is valid for 10 minutes.</p>" +
            "  <p style='font-size: 12px; color: #94a3b8; border-top: 1px solid #f1f5f9; padding-top: 20px; text-align: center;'>&copy; 2026 PathPilot Security Protocol.</p>" +
            "</div>";

        sendEmailWithAttachment(toEmail, "PathPilot: Password Reset OTP", htmlContent, null);
    }

    /**
     * 🔑 Registration OTP Email - Account Verification
     */
    public void sendRegistrationOtpEmail(String toEmail, String otp) throws Exception {
        String htmlContent = 
            "<div style='font-family: Segoe UI, Tahoma, sans-serif; max-width: 550px; margin: auto; border: 1px solid #eef2f6; padding: 40px; border-radius: 20px; color: #1e293b;'>" +
            "  <div style='text-align: center; margin-bottom: 30px;'><h1 style='color: #4913ec; margin: 0;'>PathPilot</h1></div>" +
            "  <h2 style='color: #0f172a; margin-top: 0;'>Verify Your Registration</h2>" +
            "  <p style='line-height: 1.6; color: #64748b;'>Welcome to PathPilot! Use the code below to complete your registration and verify your email address:</p>" +
            "  <div style='background: #f8f9fc; padding: 25px; text-align: center; border-radius: 15px; margin: 30px 0; border: 1px dashed #e2e8f0;'>" +
            "    <span style='font-size: 36px; font-weight: 800; letter-spacing: 12px; color: #4913ec;'>" + otp + "</span>" +
            "  </div>" +
            "  <p style='font-size: 13px; color: #64748b;'>This code is valid for 10 minutes.</p>" +
            "  <p style='font-size: 12px; color: #94a3b8; border-top: 1px solid #f1f5f9; padding-top: 20px; text-align: center;'>&copy; 2026 PathPilot Identity Systems.</p>" +
            "</div>";

        sendEmailWithAttachment(toEmail, "PathPilot: Registration OTP", htmlContent, null);
    }

    /**
     * Internal SMTP dispatch logic.
     */
    private void send(String to, String subject, String body, MultipartFile file, int port) throws Exception {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("smtp.gmail.com");
        mailSender.setPort(port);
        mailSender.setUsername("ramprakashkurmi3@gmail.com");
        mailSender.setPassword("frukmoiyanpmsbof");

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.connectiontimeout", "5000");
        props.put("mail.smtp.timeout", "5000");
        props.put("mail.smtp.ssl.trust", "smtp.gmail.com"); // 🔥 FIX
        props.put("mail.smtp.starttls.required", "true");   // 🔥 FIX

        if (port == 587) {
            props.put("mail.smtp.starttls.enable", "true");
        } else {
            props.put("mail.smtp.ssl.enable", "true");
            props.put("mail.smtp.socketFactory.port", "465");
            props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        }

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setFrom("ramprakashkurmi3@gmail.com"); // 🔥🔥 MAIN FIX
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(body, true);

        if (file != null && !file.isEmpty()) {
            helper.addAttachment(file.getOriginalFilename(), file);
        }

        mailSender.send(message);
    }
}