package codingweek.controllers;

import codingweek.Observer;
import codingweek.models.Game;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.util.Duration;
import javafx.animation.FadeTransition;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class TopSpyController implements Observer {
    @FXML
    Label turnLabel;

    @FXML
    TextField clueField, numberField;    

    private Game game;
    private FadeTransition fadeTransition;

    public void initialize() {
        game = Game.getInstance();
        game.ajouterObservateur(this);
        fadeTransition = new FadeTransition(Duration.seconds(0.5), turnLabel);
        fadeTransition.setFromValue(1.0); // Opacité maximale (visible)
        fadeTransition.setToValue(0.0);   // Opacité minimale (invisible)
        fadeTransition.setCycleCount(FadeTransition.INDEFINITE);   // Le texte disparaît puis réapparaît
        fadeTransition.setAutoReverse(true); // Revenir à l'état initial (visible)

        // Ajout de listeners pour détecter la touche Entrée dans les TextField
        clueField.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ENTER)) {
                submitClue();
            }
        });

        numberField.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ENTER)) {
                submitClue();
            }
        });

        reagir();
    }

    public void reagir() {
        if (game.isSpyTurn() && game.isBlueTurn()) {
            turnLabel.setText("It's blue spy's turn!");
            turnLabel.setStyle("-fx-text-fill: #003566; -fx-font-weight: bold; -fx-effect: dropshadow(gaussian, blue, 10, 0.5, 0, 0); -fx-font-size: 18px; ");
            startFading();
        } else if (game.isSpyTurn() && !game.isBlueTurn()) {
            turnLabel.setText("It's red spy's turn!");
            turnLabel.setStyle("-fx-text-fill: #c1121f; -fx-font-weight: bold; -fx-effect: dropshadow(gaussian, red, 10, 0.5, 0, 0); -fx-font-size: 18px;");
            startFading();
        } else if (!game.isSpyTurn() && game.isBlueTurn()) {
            turnLabel.setText("It's blue guesser's turn!");
            turnLabel.setStyle("-fx-text-fill: #003566; -fx-font-weight: bold; -fx-effect: dropshadow(gaussian, blue, 10, 0.5, 0, 0); -fx-font-size: 18px;");
            stopFading();
        } else {
            turnLabel.setText("It's red guesser's turn!");
            turnLabel.setStyle("-fx-text-fill: #c1121f; -fx-font-weight: bold; -fx-effect: dropshadow(gaussian, red, 10, 0.5, 0, 0); -fx-font-size: 18px;");
            stopFading();
        }
        
    }

    public void submitClue() {
        try {
            int number = Integer.parseInt(numberField.getText());
            String clue = clueField.getText();
    
            // Vérifiez si l'indice est valide
            if (!game.clueIsValid(clue)) {
                showError("Invalid Clue", "The clue you provided is not valid. Please try again.");
                return;
            }
    
            // Soumission de l'indice au jeu
            if (game.submitClue(clue, number) == 1) {
                clueField.clear();
                numberField.clear();
            } else {
                showError("Submission Error", "Failed to submit the clue. Please check your input.");
            }
        } catch (NumberFormatException e) {
            // Gérer l'erreur de conversion en integer
            numberField.clear();
            numberField.setPromptText("Please enter a valid number.");
            showError("Invalid Number", "The number you entered is not valid. Please enter a valid integer.");
        }
    }

    private void startFading() {
        fadeTransition.play();
    }

    // Méthode pour arrêter l'animation de fondu
    private void stopFading() {
        if (fadeTransition != null) {
            fadeTransition.stop();  // Arrêter l'animation
            turnLabel.setOpacity(1.0);  // Assurer que le label reste visible
        }
    }

    private void showError(String title, String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
