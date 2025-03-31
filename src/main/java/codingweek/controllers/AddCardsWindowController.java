package codingweek.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;

import java.io.IOException;
import java.util.Map;

import codingweek.models.JsonReader;
import codingweek.models.PageManager;

public class AddCardsWindowController {

    @FXML
    private Button returnMenuButton;

    @FXML
    private ComboBox<String> categoryDropdown;

    @FXML
    private TextField newCard;

    @FXML 
    private TextField newCategory;

    private PageManager pageManager;

    @FXML
    private void initialize() {
        pageManager = PageManager.getInstance();

        returnMenuButton.setOnAction(e -> {
            pageManager.loadMenuWindowView();
        });

        try {
            // Charger les catégories à partir du fichier JSON
            Map<String, ?> categories = JsonReader.getCategories("mots.json");
            for (String categoryName : categories.keySet()) {
                categoryDropdown.getItems().add(categoryName);
            }
            categoryDropdown.setValue("Animaux");
        } catch (IOException e) {
            showError("Error loading categories", "Can't load categories from mots.json");
        }
    }

    @FXML
    private void addCardToCategory() {
        String selectedCategory = categoryDropdown.getValue();
        String newCardText = newCard.getText().trim();

        if (selectedCategory == null || newCardText.isEmpty()) {
            showError("Input error", "Please select a category and enter a word.");
            return;
        }

        try {
            // Ajouter le mot à la catégorie
            boolean success = JsonReader.addWordToCategory("mots.json", selectedCategory, newCardText);
            if (success) {
            // Afficher un message de succès
                showSuccess("Succes", "The word was successfully added to the category " + selectedCategory + ".");
            }
            newCard.clear();
        } catch (IOException e) {
            showError("File Error", "Unable to read or write to the JSON file.");
        }
    }

    @FXML
    private void addCategory() {
        String newCategoryName = newCategory.getText().trim();

        if (newCategoryName.isEmpty()) {
            showError("Input Error", "Please enter a name for the new category.");
            return;
        }

        try {
            // Ajouter la nouvelle catégorie au fichier JSON
            boolean success = JsonReader.addCategory("mots.json", newCategoryName);
            if (success) {
                // Mettre à jour la ComboBox
                categoryDropdown.getItems().add(newCategoryName);
                showSuccess("Success", "The category \"" + newCategoryName + "\" was successfully added.");
                newCategory.clear();
            }
        } catch (IOException e) {
            showError("File Error", "Unable to read or write to the JSON file.");
        }
    }

    private void showError(String title, String content) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void showSuccess(String title, String content) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
