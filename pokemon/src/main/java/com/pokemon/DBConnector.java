package com.pokemon;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.CallableStatement;

public enum DBConnector {

    INSTANCE;

    private Connection connenction; 

    private DBConnector() 
{
        try {

            Class.forName("com.mysql.cj.jdbc.Driver");
            connenction = DriverManager.getConnection("jdbc:mysql://localhost:3306/pokemon_db", "root", "Chaser580558");

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
    
    public int getPlayerID(String username) {
        String sql = "SELECT player_id FROM player WHERE username = ?";
        try (PreparedStatement pstmt = connenction.prepareStatement(sql)) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                int playerId = rs.getInt("player_id");
                System.out.println("Player ID for user " + username + ": " + playerId);
                return playerId;
            } else {
                System.out.println("No account found for user: " + username);
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving player ID: " + e.getMessage());
        }
        return -1; 
    }

    public void deleteAccount(String username) {
        String sql = "DELETE FROM player WHERE username = ?";
        try (PreparedStatement pstmt = connenction.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.executeUpdate();
            System.out.println("Account deleted for user: " + username);
        } catch (SQLException e) {
            System.out.println("Error deleting account: " + e.getMessage());
        }
    }

    public void pokemonCard() {

        String sql = "SELECT * FROM pokemon_card";
        try (PreparedStatement pstmt = connenction.prepareStatement(sql)) {
            pstmt.executeQuery();
            System.out.println("Pokemon card retrieved");
        } catch (SQLException e) {
            System.out.println("Error retrieving Pokemon card: " + e.getMessage());
        }
    }

    public ResultSet pokemonStats(String pokemonName) {

        try (CallableStatement stmt = connenction.prepareCall("{CALL pokemon_base_stats(?)}")) {
            stmt.setString(1, pokemonName);
            return stmt.executeQuery();
        } catch (SQLException e) {
            System.out.println("Error retrieving Pokemon stats: " + e.getMessage());
            return null;
        }
    }

    public ResultSet pokemonType() {

        String sql = "SELECT * FROM pokemon_types";
        try (PreparedStatement pstmt = connenction.prepareStatement(sql)) {
            ResultSet rs = pstmt.executeQuery();
            System.out.println("Pokemon type retrieved");
            return rs;
        } catch (SQLException e) {
            System.out.println("Error retrieving Pokemon type: " + e.getMessage());
            return null;
        }
    }

    public ResultSet pokemonMoves() {

        String sql = "SELECT * FROM pokemon_moves";
        try (PreparedStatement pstmt = connenction.prepareStatement(sql)) {
            ResultSet rs = pstmt.executeQuery();
            System.out.println("Pokemon moves retrieved");
            return rs;
        } catch (SQLException e) {
            System.out.println("Error retrieving Pokemon moves: " + e.getMessage());
            return null;
        }
    }
    
    public ResultSet playerCard(){
        String sql = "SELECT * FROM cards WHERE owned_by_player = 1";
        try (PreparedStatement pstmt = connenction.prepareStatement(sql)) {
            ResultSet rs = pstmt.executeQuery();
            System.out.println("Player card retrieved");
            return rs;
        } catch (SQLException e) {
            System.out.println("Error retrieving Player card: " + e.getMessage());
            return null;
        }
    }


}
