package com.pathpilot.config;

import java.util.Properties;
import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = "com.pathpilot") 
public class WebConfig implements WebMvcConfigurer {

    // 1. Configure View Resolver
    @Bean
    public ViewResolver viewResolver() {
        InternalResourceViewResolver resolver = new InternalResourceViewResolver();
        resolver.setPrefix("/WEB-INF/views/");
        resolver.setSuffix(".jsp");
        return resolver;
    }

    /**
     * 2. Configure Static Resources
     * 🔥 UPDATED: Mapping virtual URL to Drive D: for file uploads.
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Standard project assets (CSS, JS)
        registry.addResourceHandler("/assets/**")
                .addResourceLocations("/assets/");
        
        /**
         * 🔥 CRITICAL: External upload folder on Drive D
         */
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:///D:/pathpilot/uploads/");
    }

    // 3. Configure JavaMailSender
    @Bean
    public JavaMailSender javaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("smtp.gmail.com");
        mailSender.setPort(587);
        mailSender.setUsername("ramprakashkurmi3@gmail.com");
        mailSender.setPassword("frukmoiyanpmsbof"); 

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.starttls.required", "true"); // 🔥 FIX
        props.put("mail.smtp.ssl.trust", "smtp.gmail.com"); // 🔥 FIX
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.debug", "true"); 

        return mailSender;
    }

    // 4. Configure DataSource
    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource ds = new DriverManagerDataSource();
        ds.setDriverClassName("com.mysql.cj.jdbc.Driver");
        ds.setUrl("jdbc:mysql://localhost:3306/pathpilots_db?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true");
        ds.setUsername("root");
        ds.setPassword("root");
        return ds;
    }

    // 5. Configure JdbcTemplate
    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    /**
     * 6. Multipart Resolver for File Uploads
     * 🔥 FIXED: Added explicit bean name "multipartResolver".
     */
    @Bean(name = "multipartResolver")
    public StandardServletMultipartResolver multipartResolver() {
        return new StandardServletMultipartResolver();
    }
}