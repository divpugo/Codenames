package codingweek.controllers;

import codingweek.models.Game;
import codingweek.models.JsonReader;
import codingweek.models.Key;
import codingweek.models.PageManager;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Button;


import java.io.IOException;
import java.util.Map;

public class ConfigWindowController {

    @FXML
    private Button returnMenuButton;

    @FXML
    private TextField boardSizeInput, timeLimitInput;

    @FXML
    private ComboBox<String> categoryDropdown;

    @FXML
    private CheckBox imagesModeCheckbox;

    private final Game game;
    private final PageManager pageManager;
    private final Key key;

    public ConfigWindowController() {
        this.game = Game.getInstance();
        this.pageManager = PageManager.getInstance();
        this.key = Key.getInstance();
    }

    @FXML
    public void initialize() {
        returnMenuButton.setOnAction(e -> {
            pageManager.loadMenuWindowView();
        });
        // Affecte et affiche les categories du JSON dans le dropdown
        try {
            Map<String, ?> categories = JsonReader.getCategories("mots.json");
            categoryDropdown.getItems().addAll(categories.keySet());
            categoryDropdown.getItems().add("All categories");
            categoryDropdown.setValue("All categories");
        } catch (IOException e) {
            showError("Error loading categories", "Unable to load categories from mots.json.");
        }
    }

    @FXML
    private void onStartGame() {
        try {
            // Valider et affecter la taille du plateau
            int boardSize = Integer.parseInt(boardSizeInput.getText());
            if (boardSize < 3 || boardSize > 6) {
                throw new IllegalArgumentException("The board must have between 3 and 6 columns and rows.");
            }
    
            // Valider et affecter la categorie
            String selectedCategory = categoryDropdown.getValue();
            if (selectedCategory == null || selectedCategory.isEmpty()) {
                throw new IllegalArgumentException("Please select a category.");
            }
            if (selectedCategory.equals("All categories")) {
                selectedCategory = "all";
            } else {
                int requiredWordCount = boardSize * boardSize; // Le nombre de mots requis dépend de la taille du plateau
                int wordCount = JsonReader.getCategoryWordCount(selectedCategory); // Obtenez le nombre de mots dans la catégorie sélectionnée
                if (wordCount < requiredWordCount) {
                    throw new IllegalArgumentException("The category \"" + selectedCategory + "\" does not have enough words to fill a board of size " + boardSize + "x" + boardSize + ".");
                }
            }

            // Valider et affecter la limite de temps
            String timeLimit = timeLimitInput.getText();
            if (!timeLimit.equals("unlimited") && !(isNumeric(timeLimit) && Integer.parseInt(timeLimit) >= 10)) {
                showError("Invalid input", "Please enter a valid time limit. It must be 'unlimited' or an integer greater than or equal to 10.");
                return;
            }

            // Valider et affecter le mode d'images
            boolean imagesMode = imagesModeCheckbox.isSelected();
    
            // Initialiser le jeu apres que les configurations sont choisies
            game.initializeGame(boardSize, selectedCategory, timeLimit, imagesMode);

            // Initialiser la key
            key.newKey();
    
            // Charger les autres fenetres
            pageManager.loadGuesserView();
            pageManager.loadSpyView();    
        } catch (NumberFormatException e) {
            showError("Invalid Input", "The board size must be an integer.");
        } catch (IllegalArgumentException e) {
            showError("Invalid Input", e.getMessage());
        } catch (Exception e) {
            showError("Erreur", "An unknown error occurred: " + e.getMessage());
        }
    }
    
    private boolean isNumeric(String str) {
        return str.matches("-?\\d+(\\.\\d+)?");
    }

    private void showError(String title, String content) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
