package com.pokemon;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.geometry.Pos;
import javafx.geometry.Insets;

public class CardController {
    
    @FXML
    private VBox cardContainer;
    
    @FXML
    private HBox cardsRow;
    
    @FXML
    private StackPane currentCardPane;
    
    @FXML
    private Label cardName;
    
    @FXML
    private Label cardType;
    
    @FXML
    private Label cardHP;
    
    @FXML
    private Label cardAttack;
    
    @FXML
    private Label cardDefense;
    
    @FXML
    private Label cardRarity;
    
    @FXML
    private Button drawCardButton;
    
    @FXML
    private Button addToCollectionButton;
    
    @FXML
    private Button backButton;
    
    private PokemonCard currentCard;
    private List<PokemonCard> collection = new ArrayList<>();
    private Random random = new Random();
    
    // Pokemon data
    private final String[] POKEMON_NAMES = {
        "Pikachu", "Charizard", "Bulbasaur", "Squirtle", "Eevee", 
        "Mewtwo", "Gengar", "Snorlax", "Gyarados", "Dragonite",
        "Lucario", "Garchomp", "Greninja", "Gardevoir", "Metagross"
    };
    
    private final String[] POKEMON_TYPES = {
        "Electric", "Fire", "Grass", "Water", "Normal",
        "Psychic", "Ghost", "Normal", "Water/Flying", "Dragon/Flying",
        "Fighting/Steel", "Dragon/Ground", "Water/Dark", "Psychic/Fairy", "Steel/Psychic"
    };
    
    private final String[] RARITIES = {
        "Common", "Common", "Common", "Common", "Common",
        "Uncommon", "Uncommon", "Uncommon", "Rare", "Rare",
        "Rare", "Ultra Rare", "Ultra Rare", "Ultra Rare", "Legendary"
    };
    
    // Card colors based on type
    private final String[] CARD_COLORS = {
        "#FFF200", "#FF4500", "#7CFC00", "#1E90FF", "#E5E5E5",
        "#FF69B4", "#8A2BE2", "#E5E5E5", "#1E90FF", "#FF8C00",
        "#A9A9A9", "#A52A2A", "#1E90FF", "#FF69B4", "#A9A9A9"
    };
    
    @FXML
    private void initialize() {
        // Initialize the card collection view
        updateCollectionView();
    }
    
    @FXML
    private void drawCard(ActionEvent event) {
        // Generate a random pokemon card
        int index = random.nextInt(POKEMON_NAMES.length);
        int hp = 50 + random.nextInt(150);
        int attack = 20 + random.nextInt(100);
        int defense = 10 + random.nextInt(90);
        
        currentCard = new PokemonCard(
            POKEMON_NAMES[index],
            POKEMON_TYPES[index],
            hp,
            attack,
            defense,
            RARITIES[index],
            CARD_COLORS[index]
        );
        
        // Display the newly drawn card
        cardName.setText(currentCard.getName());
        cardType.setText(currentCard.getType());
        cardHP.setText(String.valueOf(currentCard.getHp()));
        cardAttack.setText(String.valueOf(currentCard.getAttack()));
        cardDefense.setText(String.valueOf(currentCard.getDefense()));
        cardRarity.setText(currentCard.getRarity());
        
        // Set card background color based on type
        ((Rectangle)currentCardPane.getChildren().get(0)).setStyle(
            "-fx-fill: linear-gradient(to bottom right, " + currentCard.getColor() + ", #FFFFFF);" +
            "-fx-stroke: #3B4CCA; -fx-stroke-width: 3;"
        );
        
        // Show the card and enable the add button
        currentCardPane.setVisible(true);
        addToCollectionButton.setDisable(false);
    }
    
    @FXML
    private void addToCollection(ActionEvent event) {
        if (currentCard != null) {
            collection.add(currentCard);
            updateCollectionView();
            
            // Reset current card display
            currentCardPane.setVisible(false);
            addToCollectionButton.setDisable(true);
            currentCard = null;
        }
    }
    
    private void updateCollectionView() {
        cardsRow.getChildren().clear();
        
        // Create mini card displays for each card in collection
        for (PokemonCard card : collection) {
            // Create a mini card
            StackPane miniCard = createMiniCard(card);
            cardsRow.getChildren().add(miniCard);
        }
    }
    
    private StackPane createMiniCard(PokemonCard card) {
        StackPane cardPane = new StackPane();
        cardPane.setPrefSize(120, 180);
        
        // Card background
        Rectangle cardRect = new Rectangle(120, 180);
        cardRect.setArcWidth(15);
        cardRect.setArcHeight(15);
        cardRect.setStyle(
            "-fx-fill: linear-gradient(to bottom right, " + card.getColor() + ", #FFFFFF);" +
            "-fx-stroke: #3B4CCA; -fx-stroke-width: 2;"
        );
        
        // Card content
        BorderPane cardContent = new BorderPane();
        cardContent.setPadding(new Insets(5));
        
        // Card name at top
        Label nameLabel = new Label(card.getName());
        nameLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 12;");
        BorderPane.setAlignment(nameLabel, Pos.CENTER);
        cardContent.setTop(nameLabel);
        
        // Card stats in center
        VBox statsBox = new VBox(3);
        statsBox.setAlignment(Pos.CENTER);
        statsBox.getChildren().addAll(
            new Label("HP: " + card.getHp()),
            new Label("ATK: " + card.getAttack()),
            new Label("DEF: " + card.getDefense())
        );
        cardContent.setCenter(statsBox);
        
        // Card type and rarity at bottom
        VBox bottomBox = new VBox(2);
        bottomBox.setAlignment(Pos.CENTER);
        Label typeLabel = new Label(card.getType());
        typeLabel.setStyle("-fx-font-style: italic; -fx-font-size: 10;");
        Label rarityLabel = new Label(card.getRarity());
        rarityLabel.setStyle("-fx-font-style: italic; -fx-font-size: 10;");
        bottomBox.getChildren().addAll(typeLabel, rarityLabel);
        cardContent.setBottom(bottomBox);
        
        cardPane.getChildren().addAll(cardRect, cardContent);
        return cardPane;
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
    
    // Pokemon Card class
    public static class PokemonCard {
        private String name;
        private String type;
        private int hp;
        private int attack;
        private int defense;
        private String rarity;
        private String color;
        
        public PokemonCard(String name, String type, int hp, int attack, int defense, String rarity, String color) {
            this.name = name;
            this.type = type;
            this.hp = hp;
            this.attack = attack;
            this.defense = defense;
            this.rarity = rarity;
            this.color = color;
        }
        
        public String getName() { return name; }
        public String getType() { return type; }
        public int getHp() { return hp; }
        public int getAttack() { return attack; }
        public int getDefense() { return defense; }
        public String getRarity() { return rarity; }
        public String getColor() { return color; }
    }
}