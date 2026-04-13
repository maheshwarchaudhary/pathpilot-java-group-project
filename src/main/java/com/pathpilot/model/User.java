package com.pathpilot.model;

/**
 * Data model representing a PathPilot User.
 * Maps directly to the 'users' table in the database.
 */
public class User {
    private int id;
    private String name;
    private String email;
    private String phone;
    private String password;
    private String role;
    private String token;
    private boolean isVerified;
    private String profilePic;

    // Default Constructor
    public User() {}

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
    public boolean isVerified() { return isVerified; }
    public void setVerified(boolean isVerified) { this.isVerified = isVerified; }
    public String getProfilePic() { return profilePic; }
    public void setProfilePic(String profilePic) { this.profilePic = profilePic; }
}