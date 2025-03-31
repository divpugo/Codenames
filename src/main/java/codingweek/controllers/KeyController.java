package codingweek.controllers;

import codingweek.Observer;
import codingweek.models.Game;
import codingweek.models.Key;
import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.control.Button;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class KeyController implements Observer{

    @FXML
    private GridPane gridPane;

    @FXML
    private Button toggleKeyButton;

    private static final double CELL_SIZE = 25.0; // taille fixe des cellules
    private Game game;
    private Key key;

    @FXML
    public void initialize() {
        // Recupere la session et la taille du plateau
        game = Game.getInstance();
        key = Key.getInstance();
        game.ajouterObservateur(this);
        int boardSize = game.getBoardSize(); 

        // Peuple la grille avec la taille du plateau
        populateKeyGrid(boardSize);

        // Initially hide the grid and set the button text
        gridPane.setVisible(false);
        toggleKeyButton.setText("Show key");

        // Attach the event handler
        toggleKeyButton.setOnAction(event -> handleToggleKeyButton());

    }

    public void reagir() {
        updateKeyGrid();
    }

    private void handleToggleKeyButton() {
        // Toggle the visibility of the grid
        boolean isCurrentlyVisible = gridPane.isVisible();
        gridPane.setVisible(!isCurrentlyVisible);

        // Update the button text
        if (isCurrentlyVisible) {
            toggleKeyButton.setText("Show key");
        } else {
            toggleKeyButton.setText("Hide key");
        }
    }

    private void populateKeyGrid(int boardSize) {
        // Recupere une instance de clef unique

        // Vide la grille pour eviter les doublons lors des refresh
        gridPane.getChildren().clear();
        gridPane.getRowConstraints().clear();
        gridPane.getColumnConstraints().clear();

        // Etabli les colonnes et les lignes d'apres la taille du plateau
        for (int i = 0; i < boardSize; i++) {
            RowConstraints row = new RowConstraints();
            row.setPrefHeight(CELL_SIZE);
            gridPane.getRowConstraints().add(row);

            ColumnConstraints col = new ColumnConstraints();
            col.setPrefWidth(CELL_SIZE);
            gridPane.getColumnConstraints().add(col);
        }

        // Peupler la grille avec les couleurs
        for (int row = 0; row < boardSize; row++) {
            for (int col = 0; col < boardSize; col++) {
                Rectangle rect = new Rectangle(CELL_SIZE, CELL_SIZE); // Cellules bien carres
                rect.setFill(key.getCouleur(row, col)); // Recupere la couleur depuis le Key modele
                rect.setStroke(Color.BLACK); // Rebord

                // Ajoute le carre a la grille
                gridPane.add(rect, col, row);
            }
        }
    }

    public void updateKeyGrid() {
        int boardSize = game.getBoardSize();
        // Peupler la grille avec les couleurs
        for (int row = 0; row < boardSize; row++) {
            for (int col = 0; col < boardSize; col++) {
                Rectangle rect = new Rectangle(CELL_SIZE, CELL_SIZE); // Cellules bien carres
                rect.setFill(key.getCouleur(row, col)); // Recupere la couleur depuis le Key modele
                rect.setStroke(Color.BLACK); // Rebord

                // Ajoute le carre a la grille
                gridPane.add(rect, col, row);
            }
        }
    }
}
