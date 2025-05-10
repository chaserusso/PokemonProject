package com.pokemon;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;

public class PrimaryController {

    @FXML 
    private ImageView background;

    @FXML
    private StackPane rootPane;

    @FXML
    private TextField usernameField; 

    public static DBConnector dbConnector;


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
    public void createAccount(ActionEvent event) {
        String username = usernameField.getText();
        if (username.isEmpty()) {
            System.out.println("Please enter a username.");
            return;
        }
        
        // Here you can add logic to create an account in the database
        System.out.println("Account created for user: " + username);
        
        // Proceed to the next screen
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
            App.setRoot("battle"); // This assumes you have a battle.fxml file
        } catch (IOException e) {
            System.err.println("Error loading battle screen: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
