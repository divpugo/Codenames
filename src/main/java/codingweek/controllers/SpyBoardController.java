package codingweek.controllers;

import codingweek.Observer;
import codingweek.models.Board;
import codingweek.models.Card;
import codingweek.models.Game;
import codingweek.models.PageManager;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class SpyBoardController implements Observer {

    @FXML
    private GridPane boardGrid;

    private final Game game = Game.getInstance();
    private final Board board = game.getBoard();
    private final PageManager pageManager = PageManager.getInstance();

    public void initialize() {
        pageManager.ajouterObservateur(this);
        game.ajouterObservateur(this);
        updateBackgroundColor();
        int gridSize = game.getBoardSize();
        populateBoard(gridSize);
        board.addCardObservers(this);
    }
    
    public void reagir() {
        updateBackgroundColor();
        updateRevealedTiles();
    }

    private void updateRevealedTiles() {
        List<Card> cards = board.getCards();
        int gridSize = game.getBoardSize();
    
        int cardIndex = 0;
        for (int row = 0; row < gridSize; row++) {
            for (int col = 0; col < gridSize; col++) {
                Card card = cards.get(cardIndex++);
                if (card.isRevealed()) {
                    StackPane cardPane = (StackPane) getNodeByRowColumnIndex(row, col, boardGrid);
    
                    if (cardPane != null) {
                        String backgroundColor = convertColorToCSS(card.getColor());
                        cardPane.setStyle("-fx-border-color: black; -fx-background-color: " +
                                backgroundColor +
                                "; -fx-padding: 10;" +
                                "-fx-background-radius: 15; " +
                                "-fx-border-radius: 15;");
                    }
                }
            }
        }

    }

    private StackPane getNodeByRowColumnIndex(int row, int col, GridPane gridPane) {
        for (javafx.scene.Node node : gridPane.getChildren()) {
            if (GridPane.getRowIndex(node) == row && GridPane.getColumnIndex(node) == col) {
                return (StackPane) node;
            }
        }
        return null;
    }

    private String convertColorToCSS(String javafxColor) {
        if (javafxColor.startsWith("0x")) {
            return "#" + javafxColor.substring(2, 8); 
        }
        return javafxColor; 
    }

    private void populateBoard(int gridSize) {
        List<Card> cards = board.getCards();
        int totalCards = gridSize * gridSize; // Verifie le nombre de cartes
        if (cards.size() != totalCards) {
            throw new IllegalStateException("There are not enough cards.");
        }
    
        int cardIndex = 0;
        for (int row = 0; row < gridSize; row++) {
            for (int col = 0; col < gridSize; col++) {
                StackPane cardPane = new StackPane();
                Card card = cards.get(cardIndex++);
    
                cardPane.setStyle("-fx-border-color: black; " +
                        "-fx-background-color: lightgrey; " +
                        "-fx-padding: 10;" +
                        "-fx-background-radius: 15; " +
                        "-fx-border-radius: 15;");
                cardPane.setPrefSize(100, 100);
    
                if (game.getImagesMode()) {
                    try {
                        javafx.scene.image.ImageView imageView = new javafx.scene.image.ImageView(card.getWord());
                        imageView.setFitWidth(80);
                        imageView.setFitHeight(80);
                        imageView.setPreserveRatio(true);
                        cardPane.getChildren().add(imageView);
                    } catch (Exception e) {
                        // On récupère le filename pour afficher le mot à la place de l'image
                        Path path = Paths.get(card.getWord());
                        String fileNameWithExtension = path.getFileName().toString();
                        int lastDotIndex = fileNameWithExtension.lastIndexOf('.');
                        String fileNameWithoutExtension = lastDotIndex == -1 ? 
                                                        fileNameWithExtension : 
                                                        fileNameWithExtension.substring(0, lastDotIndex);
                        Label wordLabel = new Label(fileNameWithoutExtension);
                        wordLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: black;");
                        cardPane.getChildren().add(wordLabel);
                    }
                } else {
                    Label wordLabel = new Label(card.getWord());
                    wordLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: black;");
                    cardPane.getChildren().add(wordLabel);
                }
    
                boardGrid.add(cardPane, col, row);
            }
        }
    }

    private void updateBackgroundColor() {
        Boolean currentTeam = game.isBlueTurn();
        if (currentTeam) {
            boardGrid.setStyle("-fx-padding: 20; -fx-background-color: #85C4FF;");
        } else {
            boardGrid.setStyle("-fx-padding: 20; -fx-background-color: #F6A2A7;");
        }
    }

}
