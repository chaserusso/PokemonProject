package com.pokemon;

import java.sql.Connection;
import java.sql.DriverManager;
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


}
