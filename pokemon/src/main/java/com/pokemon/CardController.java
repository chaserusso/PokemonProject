package com.pokemon;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.Alert.AlertType;
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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.io.File;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import java.sql.SQLException;

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
    private DBConnector dbConnector;
    private DBConnectionManager dbConnectionManager;
    public int id;
    
    
    private List<Pokemon> pokemonList = new ArrayList<>();
    
   
    private final String IMAGE_DIRECTORY = "src/main/resources/com/pokemon/images/PokemonDataset/";
    
    
    private final String[] TYPE_COLORS = {
        "#FFF200", // Normal
        "#FF4500", // Fire
        "#7CFC00", // Grass
        "#1E90FF", // Water
        "#E5E5E5", // Steel
        "#FF69B4", // Fairy
        "#8A2BE2", // Psychic
        "#E5E5E5", // Normal alternate
        "#1E90FF", // Ice
        "#FF8C00", // Dragon
        "#A9A9A9", // Dark
        "#A52A2A", // Fighting
        "#1E90FF", // Flying
        "#FF69B4", // Poison
        "#A9A9A9"  // Ghost
    };
    
    private final String[] RARITIES = {
        "Common", "Common", "Common", "Common", "Common",
        "Uncommon", "Uncommon", "Uncommon", "Rare", "Rare",
        "Rare", "Ultra Rare", "Ultra Rare", "Ultra Rare", "Legendary"
    };
    
    public CardController() {
        loadPokemonData();
    }

    public String getImagePath() {
        return IMAGE_DIRECTORY;
    }
    
    private void loadPokemonData() {
        Connection connection = null;
        try {
            connection = DBConnectionManager.getConnection();
            
            //sql query to get pokemon data
            String query = "SELECT p.pok_id, p.pok_name, t.type_name, " +
                           "s.base_hp, s.base_atk, s.b_def, s.b_sp_atk, s.b_sp_def, s.b_speed " +
                           "FROM pokemon p " +
                           "LEFT JOIN pokemon_types pt ON p.pok_id = pt.pok_id " +
                           "LEFT JOIN types t ON pt.type_id = t.type_id " +
                           "LEFT JOIN base_stats s ON p.pok_id = s.pok_id " +
                           "ORDER BY p.pok_id";
            
            PreparedStatement stmt = connection.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                id = rs.getInt("pok_id");
                String name = rs.getString("pok_name");
                String type = rs.getString("type_name");
                int hp = rs.getInt("base_hp");
                int attack = rs.getInt("base_atk");
                int defense = rs.getInt("b_def");
                int specialAttack = rs.getInt("b_sp_atk");
                int specialDefense = rs.getInt("b_sp_def");
                int speed = rs.getInt("b_speed");
                


                // for if null
                if (name == null) {
                    name = "Unknown";
                }
                
                
                if (type == null) {
                    type = "Normal";
                }
                
                if(hp == 0) {
                    hp = getIntOrDefault(rs, "hp", 50 + random.nextInt(100));
                }
                if (attack == 0) {
                    attack = getIntOrDefault(rs, "attack", 40 + random.nextInt(80));
                }
                if (defense == 0) {
                    defense = getIntOrDefault(rs, "defense", 40 + random.nextInt(80));
                }
                if (specialAttack == 0) {
                    specialAttack = getIntOrDefault(rs, "special_attack", 40 + random.nextInt(80));
                }
                if (specialDefense == 0) {
                    specialDefense = getIntOrDefault(rs, "special_defense", 40 + random.nextInt(80));
                }
                if (speed == 0) {
                    speed = getIntOrDefault(rs, "speed", 40 + random.nextInt(80));
                }
                
                pokemonList.add(new Pokemon(id, name, type, hp, attack, defense, 
                                           specialAttack, specialDefense, speed));
            }
            
            System.out.println("Loaded " + pokemonList.size() + " Pokemon");
            
            // for if empty data set
            if (pokemonList.isEmpty()) {
                
                pokemonList.add(new Pokemon(1, "Bulbasaur", "Grass", 45, 49, 49, 65, 65, 45));
                pokemonList.add(new Pokemon(2, "Charmander", "Fire", 39, 52, 43, 60, 50, 65));
                pokemonList.add(new Pokemon(3, "Squirtle", "Water", 44, 48, 65, 50, 64, 43));
                System.out.println("Using default Pokemon data");
            }
            
        } catch (SQLException e) {
            System.err.println("Error loading Pokemon data: " + e.getMessage());
            e.printStackTrace();
            
            
            pokemonList.add(new Pokemon(1, "Bulbasaur", "Grass", 45, 49, 49, 65, 65, 45));
            pokemonList.add(new Pokemon(2, "Charmander", "Fire", 39, 52, 43, 60, 50, 65));
            pokemonList.add(new Pokemon(3, "Squirtle", "Water", 44, 48, 65, 50, 64, 43));
            System.out.println("Using default Pokemon data due to DB error");
        } finally {
            
        }
    }
    
    @FXML
    private void initialize() {

        try{loadPlayerCollection();}
        catch (Exception e) {
            System.err.println("Error loading player collection: " + e.getMessage());
            e.printStackTrace();
        }

        updateCollectionView();
        
        
        if (currentCardPane.getChildren().isEmpty()) {
            
            Rectangle cardBackground = new Rectangle(250, 400);
            cardBackground.setArcWidth(20);
            cardBackground.setArcHeight(20);
            cardBackground.setStyle("-fx-fill: linear-gradient(to bottom right, #E5E5E5, #FFFFFF); -fx-stroke: #3B4CCA; -fx-stroke-width: 3;");
            currentCardPane.getChildren().add(0, cardBackground);
        }

        
        
        
        currentCardPane.setVisible(false);
        addToCollectionButton.setDisable(true);

        

        
    }
    
    @FXML
    private void drawCard(ActionEvent event) {
        if (pokemonList.isEmpty()) {
            System.err.println("No Pokemon data available");
            return;
        }
        
       
        int index = random.nextInt(pokemonList.size());
        Pokemon selectedPokemon = pokemonList.get(index);
        
        
        int hp = selectedPokemon.getHp();
        int attack = selectedPokemon.getAttack();
        int defense = selectedPokemon.getDefense();
        
        
        String rarity = RARITIES[random.nextInt(RARITIES.length)];
        String color = getColorForType(selectedPokemon.getType());
        
        
        String imagePath = IMAGE_DIRECTORY + selectedPokemon.getName() + ".png";
        
        // adding stat 
        currentCard = new PokemonCard(
            selectedPokemon.getId(),
            selectedPokemon.getName(),
            selectedPokemon.getType(),
            selectedPokemon.getHp(),
            selectedPokemon.getAttack(),
            selectedPokemon.getDefense(),
            selectedPokemon.getSpecialAttack(),
            selectedPokemon.getSpecialDefense(),
            selectedPokemon.getSpeed(),
            rarity,
            color,
            imagePath
        );
        
        
        cardName.setText(currentCard.getName());
        cardType.setText(currentCard.getType());
        cardHP.setText(String.valueOf(currentCard.getHp()));
        cardAttack.setText(String.valueOf(currentCard.getAttack()));
        cardDefense.setText(String.valueOf(currentCard.getDefense()));
        cardRarity.setText(currentCard.getRarity());
        
        
        currentCardPane.getChildren().clear();
        
       
        Rectangle cardBackground = new Rectangle(250, 450);
        cardBackground.setArcWidth(20);
        cardBackground.setArcHeight(20);
        cardBackground.setStyle(
            "-fx-fill: linear-gradient(to bottom right, " + currentCard.getColor() + ", #FFFFFF);" +
            "-fx-stroke: #3B4CCA; -fx-stroke-width: 3;"
        );
        currentCardPane.getChildren().add(cardBackground);
        
        //load image
        try {
            File imageFile = new File(currentCard.getImagePath());
            if (imageFile.exists()) {
                Image pokemonImage = new Image(imageFile.toURI().toString());
                ImageView imageView = new ImageView(pokemonImage);
                imageView.setFitWidth(180);
                imageView.setFitHeight(180);
                imageView.setPreserveRatio(true);
                imageView.setTranslateY(-80); 
                currentCardPane.getChildren().add(imageView);
            } else {
                System.out.println("Image not found: " + currentCard.getImagePath());
            }
        } catch (Exception e) {
            System.err.println("Error loading image: " + e.getMessage());
        }
        
        
        VBox cardContent = new VBox(5);
        cardContent.setAlignment(Pos.CENTER);
        cardContent.setPadding(new Insets(10));
        cardContent.setTranslateY(60);
        
        
        Label nameLabel = new Label(currentCard.getName());
        nameLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 18;");
        
        Label typeLabel = new Label("Type: " + currentCard.getType());
        typeLabel.setStyle("-fx-font-style: italic; -fx-font-size: 14;");
        
        
        HBox statsBox = new HBox(20);
        statsBox.setAlignment(Pos.BOTTOM_CENTER);
        
        VBox basicStats = new VBox(3);
        basicStats.getChildren().addAll(
            new Label("HP: " + currentCard.getHp()),
            new Label("ATK: " + currentCard.getAttack()),
            new Label("DEF: " + currentCard.getDefense())
        );
        
        VBox specialStats = new VBox(3);
        specialStats.getChildren().addAll(
            new Label("SP.ATK: " + currentCard.getSpecialAttack()),
            new Label("SP.DEF: " + currentCard.getSpecialDefense()),
            new Label("SPEED: " + currentCard.getSpeed())
        );
        
        statsBox.getChildren().addAll(basicStats, specialStats);
        
        Label rarityLabel = new Label("Rarity: " + currentCard.getRarity());
        rarityLabel.setStyle("-fx-font-style: italic; -fx-font-size: 12; -fx-text-fill: #8B0000;");
        
        cardContent.getChildren().addAll(nameLabel, typeLabel, statsBox, rarityLabel);
        currentCardPane.getChildren().add(cardContent);
        
        
        currentCardPane.setVisible(true);
        addToCollectionButton.setDisable(false);
    }
    
    private String getColorForType(String type) {
        
        switch (type.toLowerCase()) {
            case "fire": return "#FF4500";
            case "water": return "#1E90FF";
            case "grass": return "#7CFC00";
            case "electric": return "#FFF200";
            case "psychic": return "#8A2BE2";
            case "fighting": return "#A52A2A";
            case "fairy": return "#FF69B4";
            case "dragon": return "#FF8C00";
            case "dark": return "#A9A9A9";
            case "steel": return "#E5E5E5";
            case "ice": return "#1E90FF";
            case "flying": return "#1E90FF";
            case "poison": return "#FF69B4";
            case "ghost": return "#A9A9A9";
            case "ground": return "#A52A2A";
            case "rock": return "#A52A2A";
            case "bug": return "#7CFC00";
            default: return "#E5E5E5"; 
        }
    }
    
    @FXML
    private void addToCollection(ActionEvent event) {
        if (currentCard != null) {
            if(PrimaryController.playerID > 0){
                saveCardToDatabase(currentCard);
            } else {
                collection.add(currentCard);
                updateCollectionView();
            }
            
             
            currentCardPane.setVisible(false);
            addToCollectionButton.setDisable(true);
            currentCard = null;

        }
    }

    public void loadPlayerCollection() {
        try (Connection connection = DBConnectionManager.getConnection()) {
            String sql = "SELECT c.pok_id, p.pok_name, t.type_name, " +
                         "s.base_hp, s.base_atk, s.b_def, s.b_sp_atk, s.b_sp_def, s.b_speed " +
                         "FROM cards c " +
                         "JOIN pokemon p ON c.pok_id = p.pok_id " +
                         "JOIN pokemon_types pt ON p.pok_id = pt.pok_id " +
                         "JOIN types t ON pt.type_id = t.type_id " +
                         "JOIN base_stats s ON p.pok_id = s.pok_id " +
                         "WHERE c.owned_by_player = 1 AND c.player_id = ?";
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setInt(1, PrimaryController.playerID);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                int id = rs.getInt("pok_id");
                String name = rs.getString("pok_name");
                String type = rs.getString("type_name");
                int hp = rs.getInt("base_hp");
                int attack = rs.getInt("base_atk");
                int defense = rs.getInt("b_def");
                int specialAttack = rs.getInt("b_sp_atk");
                int specialDefense = rs.getInt("b_sp_def");
                int speed = rs.getInt("b_speed");
                
                String rarity = RARITIES[random.nextInt(RARITIES.length)];
                String color = getColorForType(type);
                
                String imagePath = IMAGE_DIRECTORY + name + ".png";
                
                PokemonCard card = new PokemonCard(id, name, type, hp, attack, defense, specialAttack, specialDefense, speed, rarity, color, imagePath);
                collection.add(card);
            }
        } catch (SQLException e) {
            System.err.println("Error loading player collection: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void deleteCard(PokemonCard card) {

        Alert confirmation = new Alert(AlertType.CONFIRMATION);
        confirmation.setTitle("Delete Card");
        confirmation.setHeaderText("Delete Card" + card.getName());
        confirmation.setContentText("Are you sure you want to delete this card from your collection?");
        Optional<ButtonType> result = confirmation.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            try (Connection connection = DBConnectionManager.getConnection()) {
            String sql = "DELETE FROM cards WHERE pok_id = ? AND player_id = ?";
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setInt(1, card.getId());
            pstmt.setInt(2, PrimaryController.playerID);
            pstmt.executeUpdate();
            collection.remove(card);
            updateCollectionView();
        } catch (SQLException e) {
            System.err.println("Error deleting card from database: " + e.getMessage());
            e.printStackTrace();
        }
    }
        
    }

    @FXML
    private void saveCardToDatabase(PokemonCard card) {
        try (Connection connection = DBConnectionManager.getConnection()) {
            String sql = "INSERT INTO cards (pok_id, owned_by_player, player_id) VALUES (?, ?, ?)";
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setInt(1, card.getId());
            pstmt.setInt(2, 1);
            pstmt.setInt(3, PrimaryController.playerID);

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Card saved to database: " + card.getName());
                collection.add(card);
                updateCollectionView();
            } else {
                System.out.println("Card saved to database: " + card.getName());
                
                updateCollectionView();
            } 
        }
        catch (SQLException e) {
            System.err.println("Error saving card to database: " + e.getMessage());
            e.printStackTrace();
            
        }
    }
    private void updateCollectionView() {
        cardsRow.getChildren().clear();
        
        for (PokemonCard card : collection) {
            StackPane miniCard = createMiniCard(card);
            cardsRow.getChildren().add(miniCard);
        }

       
    }
    
    private StackPane createMiniCard(PokemonCard card) {
        StackPane cardPane = new StackPane();
        cardPane.setPrefSize(120, 180);
        
        
        Rectangle cardRect = new Rectangle(120, 180);
        cardRect.setArcWidth(15);
        cardRect.setArcHeight(15);
        cardRect.setStyle(
            "-fx-fill: linear-gradient(to bottom right, " + card.getColor() + ", #FFFFFF);" +
            "-fx-stroke: #3B4CCA; -fx-stroke-width: 2;"
        );
        cardPane.getChildren().add(cardRect);
        
        
        try {
            File imageFile = new File(card.getImagePath());
            if (imageFile.exists()) {
                Image pokemonImage = new Image(imageFile.toURI().toString());
                ImageView imageView = new ImageView(pokemonImage);
                imageView.setFitWidth(80);
                imageView.setFitHeight(80);
                imageView.setPreserveRatio(true);
                imageView.setTranslateY(-30); 
                cardPane.getChildren().add(imageView);
            }
        } catch (Exception e) {
           
            
        }
        
        
        BorderPane cardContent = new BorderPane();
        cardContent.setPadding(new Insets(5));
        
        
        Label nameLabel = new Label(card.getName());
        nameLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 10;");
        BorderPane.setAlignment(nameLabel, Pos.CENTER);
        cardContent.setTop(nameLabel);
        
        
        VBox statsBox = new VBox(2);
        statsBox.setAlignment(Pos.CENTER);
        statsBox.setTranslateY(25); 
        statsBox.getChildren().addAll(
            new Label("HP: " + card.getHp()),
            new Label("ATK: " + card.getAttack()),
            new Label("DEF: " + card.getDefense())
        );
        cardContent.setCenter(statsBox);
        
        
        VBox bottomBox = new VBox(2);
        bottomBox.setAlignment(Pos.CENTER);
        Label typeLabel = new Label(card.getType());
        typeLabel.setStyle("-fx-font-style: italic; -fx-font-size: 9;");
        Label rarityLabel = new Label(card.getRarity());
        rarityLabel.setStyle("-fx-font-style: italic; -fx-font-size: 9;");
        bottomBox.getChildren().addAll(typeLabel, rarityLabel);
        cardContent.setBottom(bottomBox);
        
        cardPane.getChildren().add(cardContent);

        Button deleteButton = new Button("X");
        deleteButton.setStyle("-fx-background-color: #FF0000; -fx-text-fill: white; " +
                             "-fx-font-weight: bold; -fx-padding: 2 5 2 5;");
        deleteButton.setOpacity(0); 
        deleteButton.setTranslateX(45); 
        deleteButton.setTranslateY(-75);
        
        
        cardPane.setOnMouseEntered(e -> deleteButton.setOpacity(0.8));
        cardPane.setOnMouseExited(e -> deleteButton.setOpacity(0));
        
        
        deleteButton.setOnAction(e -> deleteCard(card));
        
        cardPane.getChildren().add(deleteButton);
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

        @FXML
    private void clearLocalCollection() {
        if (!collection.isEmpty()) {
            Alert confirmation = new Alert(AlertType.CONFIRMATION);
            confirmation.setTitle("Clear Collection");
            confirmation.setHeaderText("Clear Local Collection");
            confirmation.setContentText("Are you sure you want to clear your local collection? " +
                                       "This will not affect cards saved to your account.");
            
            Optional<ButtonType> result = confirmation.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                collection.clear();
                updateCollectionView();
            }
        }
    }
    
    private int getIntOrDefault(ResultSet rs, String columnName, int defaultValue) {
        try {
            int value = rs.getInt(columnName);
            return rs.wasNull() ? defaultValue : value;
        } catch (SQLException e) {
            return defaultValue;
        }
    }
    
    
    private static class Pokemon {
        private int id;
        private String name;
        private String type;
        private int hp;
        private int attack;
        private int defense;
        private int specialAttack;
        private int specialDefense;
        private int speed;
        
        public Pokemon(int id, String name, String type, int hp, int attack, int defense, int specialAttack, int specialDefense, int speed) {
            this.id = id;
            this.name = name;
            this.type = type;
            this.hp = hp;
            this.attack = attack;
            this.defense = defense;
            this.specialAttack = specialAttack;
            this.specialDefense = specialDefense;
            this.speed = speed;
        }
        
        public int getId() { return id; }
        public String getName() { return name; }
        public String getType() { return type; }
        public int getHp() { return hp; }
        public int getAttack() { return attack; }
        public int getDefense() { return defense; }
        public int getSpecialAttack() { return specialAttack; }
        public int getSpecialDefense() { return specialDefense; }
        public int getSpeed() { return speed; }
    }
    
    
    public static class PokemonCard {
        private int id;
        private String name;
        private String type;
        private String rarity;
        private String color;
        private String imagePath;
        private int hp;
        private int attack;     
        private int defense;
        private int specialAttack;
        private int specialDefense;
        private int speed;

        public PokemonCard(int id, String name, String type, int hp, int attack, int defense, int specialAttack, int specialDefense, int speed, String rarity, String color, String imagePath) {
            this.id = id;
            this.name = name;
            this.type = type;
            this.hp = hp;
            this.attack = attack;
            this.defense = defense;
            this.rarity = rarity;
            this.color = color;
            this.imagePath = imagePath;
            this.specialAttack = specialAttack;
            this.specialDefense = specialDefense;   
            this.speed = speed;
        }
        
        public int getId() { return id; }
        public String getName() { return name; }
        public String getType() { return type; }
        public int getHp() { return hp; }
        public int getAttack() { return attack; }
        public int getDefense() { return defense; }
        public int getSpecialAttack() { return specialAttack; }
        public int getSpecialDefense() { return specialDefense; }
        public int getSpeed() { return speed; }
        public String getRarity() { return rarity; }
        public String getColor() { return color; }
        public String getImagePath() { return imagePath; }
        public void setName(String name) { this.name = name; }      
        public void setType(String type) { this.type = type; }
        public void setHp(int hp) { this.hp = hp; }
        public void setAttack(int attack) { this.attack = attack; }
        public void setDefense(int defense) { this.defense = defense; } 
        public void setRarity(String rarity) { this.rarity = rarity; }
        public void setColor(String color) { this.color = color; }

    }
}