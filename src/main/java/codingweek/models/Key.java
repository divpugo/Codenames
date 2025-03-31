package codingweek.models;
import codingweek.Observer;

import javafx.scene.paint.Color;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Key implements Serializable {

    // Singleton instance
    private static Key instance;

    // Grid to represent the grid
    public String[][] grille;

    // Grid dimensions from Game
    private int lignes;
    private int colonnes;

    // Observers (Cards)
    private final List<Observer> cards = new ArrayList<>();

    private Game game;

    private Key() {
        game = Game.getInstance();
    }

    public void newKey() {
        this.lignes = game.getBoardSize();
        this.colonnes = game.getBoardSize();
        grille = new String[lignes][colonnes];
        initialiserGrille();
        assignColorsToCards(); // Assign colors directly to cards
    }

    // Singleton getInstance method
    public static Key getInstance() {
        if (instance == null) {
            instance = new Key();
        }
        return instance;
    }

    public int getLignes() { return lignes; }
    public int getColonnes() { return colonnes; }
    public void setLignes(int lignes) { this.lignes = lignes; }
    public void setColonnes(int colonnes) { this.colonnes = colonnes; }

    // Register a card as an observer
    public void addObserver(Observer observer) {
        cards.add(observer);
    }

    // Notify all card observers
    private void notifyObservers() {
        for (Observer observer : cards) {
            observer.reagir();
        }
    }

    private void initialiserGrille() {
        List<String> couleurs = new ArrayList<>();
        boolean blueTurn = Game.getInstance().isBlueTurn();
        int totalCards = lignes * colonnes; // Dynamically calculate the number of cards
    
        // Add blue and red team colors
        for (int i = 0; i < (totalCards / 3); i++) { // Adjust ratio as needed
            couleurs.add("blue");
            couleurs.add("red");
        }
        couleurs.add("black");
    
        // Ensure the starting team's extra card
        if (blueTurn) {
            couleurs.add("blue");
        } else {
            couleurs.add("red");
        }

        // Fill remaining spaces with neutral colors
        int nombreNeutres = totalCards - couleurs.size();
        for (int i = 0; i < nombreNeutres; i++) {
            couleurs.add("neutral");
        }
    
        // Shuffle and assign colors to the grid
        Collections.shuffle(couleurs, new Random());
        int index = 0;
        for (int i = 0; i < lignes; i++) {
            for (int j = 0; j < colonnes; j++) {
                grille[i][j] = couleurs.get(index++);
            }
        }
    }
    

    // Assign colors from the grid to the corresponding cards
    private void assignColorsToCards() {
        Game game = Game.getInstance();
        Board board = game.getBoard();
        List<Card> cards = board.getCards();

        for (int i = 0; i < cards.size(); i++) {
            int row = i / lignes;
            int col = i % colonnes;
            String color = getCouleur(row, col).toString();
            cards.get(i).setColor(color);
        }

        // Notify all observers
        notifyObservers();
    }

    public Color getCouleur(int ligne, int colonne) {
        if (ligne >= 0 && ligne < lignes && colonne >= 0 && colonne < colonnes) {
            if (grille[ligne][colonne].equals("blue")) {
                return Color.web(Card.BLUE_COLOR);
            } else if (grille[ligne][colonne].equals("red")) {
                return Color.web(Card.RED_COLOR);
            } else if (grille[ligne][colonne].equals("black")) {
                return Color.BLACK;
            } else if (grille[ligne][colonne].equals("neutral")) {
                return Color.web(Card.NEUTRAL_COLOR);
            }
        }
        return Color.TRANSPARENT;
    }
}
