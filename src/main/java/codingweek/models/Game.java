package codingweek.models;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.Stack;

public class Game extends Subject implements Serializable {
    private int boardSize;
    private int timeLimit; // in seconds
    private boolean blueTurn; // true for blue, false for red
    private boolean spyTurn; // true for spy, false for guesser
    private Stack<Guess> guesses;
    private Board board;
    private static Game instance;
    private String category;
    private boolean[][] revealedTiles;
    private int nbCardReturned;
    private int clueNb; // Nombre donné par l'espion
    private transient PageManager pageManager;
    private boolean isTimerRunning;
    private int blueReturned; // Nombre de carte bleu retournée
    private int redReturned; // Nombre de carte rouge retournée
    private boolean blueBegin;
    private static Stats stats = new Stats();
    private int correctGuesses;
    private boolean isGameOver;
    private boolean imagesMode;
    private boolean isSaved;

    private Game() {
        this.board = Board.getInstance();
        this.spyTurn = true;
        this.blueTurn = Math.random() > 0.5;
        this.blueBegin = blueTurn;
        this.category = "Métier";
        this.guesses = new Stack<Guess>();
        revealedTiles = new boolean[boardSize][boardSize];
        this.nbCardReturned = 0;
        this.blueReturned = 0;
        this.redReturned = 0;
        isSaved = true;
    }

    public void initializeGame(int boardSize, String category, String timeLimit, boolean imagesMode) {
        isGameOver = false;
        stats.incrementGamesLaunched();
        this.boardSize = boardSize;
        this.category = category;
        this.guesses.clear();
        this.revealedTiles = new boolean[boardSize][boardSize];
        if (timeLimit.equals("unlimited")) {
            this.timeLimit = 0;
        } else {
            this.timeLimit = Integer.parseInt(timeLimit);
        }
        this.blueReturned = 0;
        this.redReturned = 0;
        this.blueTurn = Math.random() > 0.5;
        this.blueBegin = blueTurn;
        this.spyTurn = true;
        this.imagesMode = imagesMode;
        initializeRevealedTiles();
        initializeBoard();
        isSaved = true;
    }

    public static Game getInstance() {
        if (instance == null) {
            instance = new Game();
        }
        return instance;
    }

    // Renvoie les stats
    public static Stats getStats() {
        return stats;
    }

    // Affectation des categories
    public void setCategory(String category) {
        this.category = category;
    }

    public int getBoardSize() {
        return boardSize;
    }

    public void setBoardSize(int boardSize) {
        this.boardSize = boardSize;
        initializeRevealedTiles();
        initializeBoard();
    }

    public int getTimeLimit() {
        return timeLimit;
    }

    public void setTimeLimit(int timeLimit) {
        this.timeLimit = timeLimit;
    }

    public Board getBoard() {
        return board;
    }

    public void addGuess(Guess guess) {
        // Fonction pour ajouter un nouveau guess fait par un espion
        guesses.push(guess);
    }

    public Guess getLastGuess() {
        if (guesses.isEmpty()) {
            return null;
        }
        return guesses.peek();
    }

    public boolean isBlueTurn() {
        return blueTurn;
    }

    public boolean isSpyTurn() {
        return spyTurn;
    }

    public int submitClue(String clue, int clueNb) {
        if (clueIsValid(clue) && 0 < clueNb && clueNb <= (int) Math.pow(this.boardSize, 2) && this.spyTurn) {
            Guess guess = new Guess(clue, clueNb);
            addGuess(guess);
            this.clueNb = clueNb;
            changeTurn();
            notifierObservateurs();
            this.isSaved = false;
            return 1;
        } else {
            return 0;
        }
    }
    
    public boolean clueIsValid(String clue) {
        clue = clue.toLowerCase();
        for (Card card : board.getCards()) {
            if (!card.isRevealed() && (card.getWord().equals(clue) || card.getForbiddenWords().contains(clue) || Arrays.asList("gauche", "droite", "haut", "bas", "centre").contains(clue))) {
                return false;
            }
        }
        return true;
    }

    private void initializeRevealedTiles() {
        for (int row = 0; row < boardSize; row++) {
            for (int col = 0; col < boardSize; col++) {
                revealedTiles[row][col] = false;
            }
        }
    }
    
    // Marque qu'une case du plateau a ete revelee
    public void revealTile(int row, int col) {
        revealedTiles[row][col] = true;
    }

    // Verfier si une case du plateau est revelée
    public boolean isTileRevealed(int row, int col) {
        return revealedTiles[row][col];
    }

    public void changeTurn() {
        this.nbCardReturned = 0;
        if (spyTurn) {
            spyTurn = false;
            startTimer();
        } else if (blueTurn) {
            blueTurn = false;
            spyTurn = true;
            stopTimer();
        } else {
            blueTurn = true;
            spyTurn = true;
            stopTimer();
        }
        notifierObservateurs();
    }

    private void initializeBoard() {
        try {
            int totalCards = boardSize * boardSize; // Calcule le nombre de cartes necessaire
            
            // Recupere les cartes et les melange
            ArrayList<Card> cards = new ArrayList<Card>();
            if (this.category.equals("all")) {
                Map<String, ?> categories = JsonReader.getCategories("mots.json");
                for (String categorie : categories.keySet()) {
                    cards.addAll(JsonReader.jsonReader("mots.json", categorie, imagesMode));
                }
            } else {
                cards = JsonReader.jsonReader("mots.json", category, imagesMode);
            }

            // On mélange les cartes
            Collections.shuffle(cards);

            // Il faut assez de cartes pour remplir le plateau
            if (cards.size() < totalCards) {
                throw new IllegalArgumentException("Not enough card to populate board.");
            }

            // Efface et peuple le plateau
            populateBoard(cards, totalCards);
    
            // Reinitialise l'array des revealedTiles pour le plateau de nouvelle taille
            initializeRevealedTiles();
    
        } catch (IOException e) {
            System.err.println("Error during board initialization: " + e);
        }
    }

    private void populateBoard(ArrayList<Card> cards, int totalCards) {
        board.cleanCards();
        for (int i = 0; i < totalCards; i++) {
            board.addCard(cards.get(i));
        }
    }

    // Gere la logique quand les carte sont retournees
    public void returnCard(Card card) {
        boolean turnChanged = false;
        this.isSaved = false;
        if (blueTurn && card.getColor().equals("0x003566ff")) {
            // Au tour de l'equipe bleue et la couleur de la carte est revelee
            this.nbCardReturned += 1;
            this.blueReturned += 1;
            this.correctGuesses++;
            if (this.nbCardReturned == this.clueNb + 1) {
                turnChanged = true;
            }
        } else if (!blueTurn && card.getColor().equals("0xc1121fff")) {
            // Au tour de l'equipe rouge et la couleur de la carte est revelee
            this.nbCardReturned += 1;
            this.redReturned += 1;
            this.correctGuesses++;
            if (this.nbCardReturned == this.clueNb + 1) {
                turnChanged = true;
            }
        } else if (card.getColor().equals("0xf0ead2ff")) {
            // Si la carte retournee est neutre le tour passe
            turnChanged = true;
        } else if (card.getColor().equals("0x000000ff")) {
            // Appelle la fonction qui gere quand la carte de l'assasine est retournee
            isGameOver = true;
            if (blueTurn) {
                stats.addBlueTeamClue(clueNb, correctGuesses);
            } else {
                stats.addRedTeamClue(clueNb, correctGuesses);
            }
            correctGuesses = 0;
            handleAssassinCard();
            return; 
        } else if (!blueTurn && card.getColor().equals("0x003566ff")) {
            // Carte de l'adversaire est retournee
            this.blueReturned += 1;
            turnChanged = true;
        } else {
            this.redReturned += 1;
            turnChanged = true;
        }

        // Gere les changements de tour
        if (turnChanged) {
            if (blueTurn) {
                stats.addBlueTeamClue(clueNb, correctGuesses);
            } else {
                stats.addRedTeamClue(clueNb, correctGuesses);
            }
            correctGuesses = 0;
            stats.addCorrectGuesses(this.correctGuesses); // Add to total guesses
            changeTurn();
        }
        // Verifie les conditions de victoire
        checkWinConditions();
    }
    
    private void handleAssassinCard() {
        if (blueTurn) {
            stats.incrementRedTeamWins();
            pageManager.closeSpyView();
            pageManager.loadGameOverViewRedWin();
        } else {
            stats.incrementBlueTeamWins();
            pageManager.closeSpyView();
            pageManager.loadGameOverViewBlueWin();
        }
    }
    
    private void checkWinConditions() {
        if (blueBegin) {
            if (blueReturned == boardSize * boardSize / 3 + 1) {
                isGameOver = true;
                stats.incrementBlueTeamWins();
                pageManager.loadGameOverViewBlueWin();
            } else if (redReturned == boardSize * boardSize / 3) {
                isGameOver = true;
                stats.incrementRedTeamWins();
                pageManager.loadGameOverViewRedWin();
            }
        } else {
            if (redReturned == boardSize * boardSize / 3 + 1) {
                isGameOver = true;
                stats.incrementRedTeamWins();
                stats.incrementGamesLaunched();
                pageManager.loadGameOverViewRedWin();
            } else if (blueReturned == boardSize * boardSize / 3) {
                isGameOver = true;
                stats.incrementBlueTeamWins();
                stats.incrementGamesLaunched();
                pageManager.loadGameOverViewBlueWin();
            }
        }
    }
    
    

    public int getNbCardsReturned() {
        return this.nbCardReturned;
    }

    public void setPageManager() {
        pageManager = PageManager.getInstance();
    }

    public void startTimer() {
        if (this.timeLimit > 0) {
            isTimerRunning = true;
            notifierObservateurs();    
        }
    }

    private void stopTimer() {
        isTimerRunning = false;
        notifierObservateurs();
    }

    public boolean isTimerRunning() {
        return isTimerRunning;
    }

    public int getBlueReturned(){
        return blueReturned;
    }

    public int getRedReturned(){
        return redReturned;
    }

    public boolean getBlueBegin(){
        return blueBegin;
    }

    public boolean getImagesMode(){
        return this.imagesMode;
    }

    public void loadGame(Game loadedGame) {
        this.boardSize = loadedGame.getBoardSize();
        this.timeLimit = loadedGame.getTimeLimit();
        this.blueTurn = loadedGame.isBlueTurn();
        this.spyTurn = loadedGame.isSpyTurn();
        this.guesses = loadedGame.guesses;
        this.board.cleanCards();
        this.board.setCards(loadedGame.getBoard().getCards());
        this.category = loadedGame.category;
        this.revealedTiles = loadedGame.revealedTiles;
        this.nbCardReturned = loadedGame.nbCardReturned;
        this.clueNb = loadedGame.clueNb;
        this.isTimerRunning = loadedGame.isTimerRunning;
        this.blueReturned = loadedGame.blueReturned;
        this.redReturned = loadedGame.redReturned;
        this.blueBegin = loadedGame.blueBegin;
        this.imagesMode = loadedGame.imagesMode;
        pageManager.loadGuesserView();
        pageManager.closeSpyView();
        pageManager.loadSpyView();
        this.notifierObservateurs();
        this.isSaved = true;
    }

    public boolean isSaved() {
        return isSaved;
    }

    public boolean setSaved(boolean isSaved) {
        return this.isSaved = isSaved;
    }

    public boolean isGameOver() {
        return isGameOver;
    }
}
