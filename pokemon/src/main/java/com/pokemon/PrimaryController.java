package com.pokemon;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;
import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;

public class PrimaryController {

    @FXML 
    private ImageView background;

    @FXML
    private StackPane rootPane;

    @FXML
    private TextField usernameField; 

    public static DBConnector dbConnector;
    public static int playerID;
    public static String playerName;


    @FXML
    public void initialize() {
        
        try{
        Image backgroundImage = new Image(getClass().getResourceAsStream("/background.jpg"));
        if(backgroundImage.isError()) {
            System.out.println("Error loading background image");
        } else {
            background.setImage(backgroundImage);
        }

        background.fitWidthProperty().bind(rootPane.widthProperty());
        background.fitHeightProperty().bind(rootPane.heightProperty());
        background.setPreserveRatio(false);
    }
        catch (Exception e) {
            System.out.println("Error loading background image: " + e.getMessage());
        }
    }

    @FXML 
    public void createAccount() {
        String username = usernameField.getText().trim();

        if (username.isEmpty()) {
            System.out.println("Please enter a username.");
            return;
        }
        
        try(Connection connect = DBConnectionManager.getConnection()) {
            PreparedStatement checkStmt = connect.prepareStatement("SELECT * FROM player WHERE username = ?");
            checkStmt.setString(1, username);
            ResultSet rs = checkStmt.executeQuery();
            if (rs.next()) {
                System.out.println("Username already exists. Please choose a different username.");
                return;
            }
            try(PreparedStatement insertStmt = connect.prepareStatement("INSERT INTO player (username) VALUES (?)", Statement.RETURN_GENERATED_KEYS)) {
                insertStmt.setString(1, username);
                insertStmt.executeUpdate();
                ResultSet generatedKeys = insertStmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    playerID = generatedKeys.getInt(1);
                    playerName = username;
                    System.out.println("Account created successfully. Player ID: " + playerID);
                }
            }
        } catch (Exception e) {
            System.out.println("Error creating account: " + e.getMessage());
            e.printStackTrace();
            return;
        }
        
        System.out.println("Account created for user: " + username);
        
        
        try {
            App.setRoot("card");
        } catch (IOException e) {
            System.err.println("Error loading card screen: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void switchToSecondary() throws IOException {
        App.setRoot("secondary");
    }

    @FXML 
    private void goToCardScreen(ActionEvent event) {
        System.out.println("Going to card collection screen...");
        try {
            App.setRoot("card");
        } catch (IOException e) {
            System.err.println("Error loading card screen: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void goToBattleScreen(ActionEvent event) {
        System.out.println("Going to battle screen...");
        try {
            App.setRoot("battle"); 
        } catch (IOException e) {
            System.err.println("Error loading battle screen: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
