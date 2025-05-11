package com.pokemon;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class BattleController {
    
    @FXML
    private Label playerPokemonName;
    
    @FXML
    private Label playerPokemonHP;
    
    @FXML
    private Label opponentPokemonName;
    
    @FXML
    private Label opponentPokemonHP;
    
    @FXML
    private Button attackButton;
    
    @FXML
    private Button healButton;
    
    @FXML
    private Button runButton;
    
    private int playerCurrentHP = 100;
    private int playerMaxHP = 100;
    private int opponentCurrentHP = 100;
    private int opponentMaxHP = 100;
    private CardController cardController;
    
    @FXML
    private void performAttack(ActionEvent event) {
        
        int damage = (int)(Math.random() * 16) + 5;
        opponentCurrentHP = Math.max(0, opponentCurrentHP - damage);
        opponentPokemonHP.setText("HP: " + opponentCurrentHP + "/" + opponentMaxHP);
        
        
        if (opponentCurrentHP > 0) {
            int counterDamage = (int)(Math.random() * 11) + 5;
            playerCurrentHP = Math.max(0, playerCurrentHP - counterDamage);
            playerPokemonHP.setText("HP: " + playerCurrentHP + "/" + playerMaxHP);
        }
        
        checkBattleStatus();
    }
    
    @FXML
    private void performHeal(ActionEvent event) {
        
        int healAmount = (int)(Math.random() * 16) + 10;
        playerCurrentHP = Math.min(playerMaxHP, playerCurrentHP + healAmount);
        playerPokemonHP.setText("HP: " + playerCurrentHP + "/" + playerMaxHP);
        
        
        int damage = (int)(Math.random() * 11) + 5;
        playerCurrentHP = Math.max(0, playerCurrentHP - damage);
        playerPokemonHP.setText("HP: " + playerCurrentHP + "/" + playerMaxHP);
        
        checkBattleStatus();
    }
    
    @FXML
    private void returnToMain(ActionEvent event) {
        try {
            App.setRoot("primary");
        } catch (IOException e) {
            System.err.println("Error returning to main screen: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void checkBattleStatus() {
        if (playerCurrentHP <= 0) {
            
            playerPokemonHP.setText("HP: 0/" + playerMaxHP);
            disableButtons();
            
        } else if (opponentCurrentHP <= 0) {
            
            opponentPokemonHP.setText("HP: 0/" + opponentMaxHP);
            disableButtons();
            
        }
    }
    
    private void disableButtons() {
        attackButton.setDisable(true);
        healButton.setDisable(true);
        
    }
}