package codingweek.controllers;

import codingweek.Observer;
import codingweek.models.Game;
import codingweek.models.Guess;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.util.Duration;
import javafx.animation.FadeTransition;

public class TopGuesserController implements Observer {
    @FXML
    Label turnLabel, clueLabel, numberLabel;

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
        reagir();
    }

    public void reagir() {
        if (game.isSpyTurn() && game.isBlueTurn()) {
            turnLabel.setText("It's blue spy's turn!");
            turnLabel.setStyle("-fx-text-fill: #003566; -fx-font-weight: bold; -fx-effect: dropshadow(gaussian, blue, 10, 0.5, 0, 0); -fx-font-size: 18px; ");
            stopFading();
        } else if (game.isSpyTurn() && !game.isBlueTurn()) {
            turnLabel.setText("It's red spy's turn!");
            turnLabel.setStyle("-fx-text-fill: #c1121f; -fx-font-weight: bold; -fx-effect: dropshadow(gaussian, red, 10, 0.5, 0, 0); -fx-font-size: 18px;");
            stopFading();
        } else if (!game.isSpyTurn() && game.isBlueTurn()) {
            turnLabel.setText("It's blue guesser's turn!");
            turnLabel.setStyle("-fx-text-fill: #003566; -fx-font-weight: bold; -fx-effect: dropshadow(gaussian, blue, 10, 0.5, 0, 0); -fx-font-size: 18px; ");
            startFading();
        } else {
            turnLabel.setText("It's red guesser's turn!");
            turnLabel.setStyle("-fx-text-fill: #c1121f; -fx-font-weight: bold; -fx-effect: dropshadow(gaussian, red, 10, 0.5, 0, 0); -fx-font-size: 18px;");
            startFading();
        }

        Guess lastGuess = game.getLastGuess();
        if (lastGuess != null) {
            clueLabel.setText("Clue: "+lastGuess.getClue());
            numberLabel.setText("Number of cards to be turned: "+ Integer.toString(lastGuess.getNbWords()));
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
}
