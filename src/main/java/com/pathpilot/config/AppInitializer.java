package com.pathpilot.config;

import jakarta.servlet.MultipartConfigElement;
import jakarta.servlet.ServletRegistration;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

/**
 * PathPilot Initialization. 
 * Updated with Drive D: Multipart configuration for reliable file uploads.
 */
public class AppInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

    @Override
    protected Class<?>[] getRootConfigClasses() {
        return null; 
    }

    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class[] { WebConfig.class }; 
    }

    @Override
    protected String[] getServletMappings() {
        return new String[] { "/" }; 
    }

    
    @Override
    protected void customizeRegistration(ServletRegistration.Dynamic registration) {
        
        // CRITICAL: Location should NOT be null. Specified D:/temp for stability.
        String uploadTempLocation = "D:/temp";
        
        // Ensure the directory exists to avoid "Directory not found" error during startup
        java.io.File uploadDir = new java.io.File(uploadTempLocation);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }

        MultipartConfigElement multipartConfigElement = new MultipartConfigElement(
                uploadTempLocation, 
                52428800,  // 50MB
                104857600, // 100MB
                0          // Threshold
        ); 
        
        registration.setMultipartConfig(multipartConfigElement);
    }
}