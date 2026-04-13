package com.pathpilot.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.nio.file.Files;
import java.nio.file.Paths;

public class DatabaseMigration {
    public static void main(String[] args) {
        String jdbcUrl = "jdbc:mysql://localhost:3306/pathpilots_db?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
        String username = "root";
        String password = "root";
        String sqlFilePath = "D:\\FINAL_PATHPILOT BACKUP\\pathpilot\\ban_system_update.sql";
        
        try {
            // Load MySQL JDBC Driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            
            // Read SQL file
            String sqlScript = new String(Files.readAllBytes(Paths.get(sqlFilePath)));
            
            // Remove markdown code markers if present
            sqlScript = sqlScript.replace("```sql", "").replace("```", "");
            
            // Create connection
            try (Connection conn = DriverManager.getConnection(jdbcUrl, username, password)) {
                Statement stmt = conn.createStatement();
                
                // Split and execute each statement
                String[] statements = sqlScript.split(";");
                for (String sql : statements) {
                    String trimmed = sql.trim();
                    if (!trimmed.isEmpty() && !trimmed.startsWith("--")) {
                        try {
                            System.out.println("Executing: " + trimmed.substring(0, Math.min(100, trimmed.length())) + "...");
                            stmt.execute(trimmed + ";");
                            System.out.println("✓ Success");
                        } catch (Exception e) {
                            System.err.println("✗ Error: " + e.getMessage());
                        }
                    }
                }
                stmt.close();
                System.out.println("\n✓ Database migration completed successfully!");
            }
        } catch (Exception e) {
            System.err.println("Migration failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
