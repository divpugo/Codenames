package codingweek.controllers;

import codingweek.models.Game;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;

public class SpyViewController implements codingweek.Observer {

     @FXML
    Button btnTurn;

    @FXML
    private SpyBoardController SpyBoardController;

    @FXML
    private BorderPane rootPane;

    private Game game;

    public void initialize() {
        game = Game.getInstance();
        game.ajouterObservateur(this);
        updateBackground();
    }

    public void reagir() {
        updateBackground();
    }

    private void updateBackground() {
        Boolean currentTeam = game.isBlueTurn();
        String backgroundColor;

        if (currentTeam) {
            backgroundColor = "#85C4FF";
        } else {
            backgroundColor = "#F6A2A7";
        }

        rootPane.setStyle("-fx-background-color: " + backgroundColor + ";");
    }

}

