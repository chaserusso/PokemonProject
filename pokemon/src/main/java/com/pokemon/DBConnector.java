package com.pokemon;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public enum DBConnector {

    INSTANCE;

    private Connection connenction; 

    private DBConnector() 
{
        try {

            Class.forName("com.mysql.cj.jdbc.Driver");
            connenction = DriverManager.getConnection("jdbc:mysql://localhost:3306/pokemon", "root", "Chaser580558");

            System.out.println("Connected to the database");
        } catch (SQLException e) {
            System.out.println("Error connecting to the database: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            System.out.println("JDBC Driver not found: " + e.getMessage());
        }
    
    }

    public Connection getConnection() {
        return this.connenction;
    }

    public void closeConnection() {
        try {
            if (connenction != null && !connenction.isClosed()) {
                connenction.close();
                System.out.println("Connection closed");
            }
        } catch (SQLException e) {
            System.out.println("Error closing the connection: " + e.getMessage());
        }
    }

    public void createAccount(String username) {
        
        if (username == null || username.isEmpty()) {
            System.out.println("Username cannot be null or empty");
            return;
        }
        String sql = "INSERT INTO player (username) VALUES (?)";
        try (PreparedStatement pstmt = connenction.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.executeUpdate();
            System.out.println("Account created for user: " + username);
        } catch (SQLException e) {
            System.out.println("Error creating account: " + e.getMessage());
        }
    }


}
