package codingweek.models;

import java.io.IOException;
import java.net.URL;

import codingweek.Utils;
import codingweek.controllers.GuesserBoardController;
import codingweek.controllers.SpyBoardController;
import codingweek.controllers.StatsController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class PageManager extends Subject {

    private Stage primaryStage, spyStage;
    private static PageManager instance;
    private SpyBoardController spyBoardController;
    private GuesserBoardController guesserBoardController;

    private PageManager() {
        primaryStage = new Stage();
    }

    public static PageManager getInstance() {
        if (instance == null) {
            instance = new PageManager();
            Game.getInstance().setPageManager();
        }
        return instance;
    }

    public void loadGuesserView() {
        try {
            URL guesserViewURL = getClass().getResource("/guesserView.fxml");
            if (guesserViewURL == null) {
                throw new IOException("Could not find guesserView.fxml");
            }
            FXMLLoader loader = new FXMLLoader(guesserViewURL);
            Parent guesserView = loader.load();
            Scene guesserScene = new Scene(guesserView, 900, 900);

            primaryStage.setScene(guesserScene);
            primaryStage.setTitle("Guesser Window");

            primaryStage.setWidth(900);
            primaryStage.setHeight(900);
            primaryStage.setX(100);
            primaryStage.setY(100);
            primaryStage.setOnCloseRequest(this::windowClose);
            
            primaryStage.show();

            this.notifierObservateurs();
        } catch (IOException e) {
            System.err.println("Failed to load guesserView.fxml: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void loadSpyView() {
        try {
            URL spyViewURL = getClass().getResource("/spyView.fxml");
            if (spyViewURL == null) {
                throw new IOException("Could not find spyView.fxml");
            }
            FXMLLoader loader = new FXMLLoader(spyViewURL);
            Parent spyView = loader.load();
            Scene spyScene = new Scene(spyView, 900, 900);

            spyStage = new Stage();
            spyStage.setScene(spyScene);
            spyStage.setTitle("Spy Window");

            spyStage.setWidth(900);
            spyStage.setHeight(900);
            spyStage.setX(primaryStage.getX() + primaryStage.getWidth() + 20);
            spyStage.setY(primaryStage.getY());
            spyStage.setOnCloseRequest(this::windowClose);

            spyStage.show();

            this.notifierObservateurs();
        } catch (IOException e) {
            System.err.println("Failed to load spyView.fxml: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void loadMenuWindowView() {
        try {
            URL menuViewURL = getClass().getResource("/menuWindow.fxml");
            if (menuViewURL == null) {
                throw new IOException("Could not find menuWindow.fxml");
            }
            FXMLLoader loader = new FXMLLoader(menuViewURL);
            Parent menuView = loader.load();
            Scene menuScene = new Scene(menuView, 800, 600);
            primaryStage.setScene(menuScene);
            primaryStage.setTitle("Menu Window");

            primaryStage.setWidth(800);
            primaryStage.setHeight(600);
            primaryStage.setX(100);
            primaryStage.setY(100);

            primaryStage.show();
        } catch (IOException e) {
            System.err.println("Failed to load menuWindow.fxml: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public void loadStatsView() {
        try {
            URL statsViewURL = getClass().getResource("/stats.fxml");
            if (statsViewURL == null) {
                throw new IOException("Could not find stats.fxml");
            }
    
            FXMLLoader loader = new FXMLLoader(statsViewURL);
            Parent statsView = loader.load();
    
            // Ensure the controller is properly initialized
            StatsController statsController = loader.getController();
            if (statsController == null) {
                throw new IllegalStateException("StatsController is not initialized. Check fx:controller in stats.fxml.");
            }
    
            // Pass the Stats object to the controller
            Stats stats = Game.getStats();
            if (stats != null) {
                statsController.setStats(stats);
            } else {
                System.err.println("Stats object is null. Ensure Game.getStats() returns a valid instance.");
            }
    
            // Display the stats view
            Scene statsScene = new Scene(statsView, 800, 1000);
            primaryStage.setScene(statsScene);
            primaryStage.setWidth(800);
            primaryStage.setHeight(1000);
            primaryStage.setTitle("Game Statistics");
            primaryStage.show();
        } catch (IOException e) {
            System.err.println("Failed to load stats.fxml: " + e.getMessage());
            e.printStackTrace();
        } catch (IllegalStateException e) {
            System.err.println("Controller initialization error: " + e.getMessage());
            e.printStackTrace();
        }
    }
    

    public void loadConfigWindowView(){
        try {
            closeSpyView();
            URL configViewURL = getClass().getResource("/configWindow.fxml");
            if (configViewURL == null){
                System.err.println("Could not find configWindow.fxml");
                System.exit(1);
            }
            Parent configView = FXMLLoader.load(configViewURL);
            Scene configScene = new Scene(configView, 490, 250);
            primaryStage.setScene(configScene);
            primaryStage.setTitle("Configuration Window");
            // Set position and size explicitly
            primaryStage.setWidth(650);
            primaryStage.setHeight(360);
            primaryStage.setX(100);
            primaryStage.setY(100);
            primaryStage.show();
        } catch (IOException e){
            System.err.println("Failed to load configWindow.fxml: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }

    public void loadGameOverViewBlueWin(){
        try {
            loadGameOverSpyViewBlueWin();

            URL gameOverViewURL = getClass().getResource("/gameOverViewBlueWin.fxml");
            if (gameOverViewURL == null){
                System.err.println("Could not find gameOverViewBlueWin.fxml");
                System.exit(1);
            }
            Parent gameOverView = FXMLLoader.load(gameOverViewURL);
            Scene gameOverScene = new Scene(gameOverView, 800, 600);
            primaryStage.setScene(gameOverScene);
            primaryStage.setTitle("Guesser Window");
            // Set position and size explicitly
            primaryStage.setWidth(800);
            primaryStage.setHeight(600);
            primaryStage.setX(100);
            primaryStage.setY(100);
            primaryStage.show();
        } catch (IOException e){
            System.err.println("Failed to load gameOverWindow.fxml: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }

    public void loadGameOverViewRedWin(){
        try {
            closeSpyView();
            loadGameOverSpyViewRedWin();
            URL gameOverViewURL = getClass().getResource("/gameOverViewRedWin.fxml");
            if (gameOverViewURL == null){
                System.err.println("Could not find gameOverViewRedWin.fxml");
                System.exit(1);
            }
            Parent gameOverView = FXMLLoader.load(gameOverViewURL);
            Scene gameOverScene = new Scene(gameOverView, 800, 600);
            primaryStage.setScene(gameOverScene);
            primaryStage.setTitle("Guesser Window");
            // Set position and size explicitly
            primaryStage.setWidth(800);
            primaryStage.setHeight(600);
            primaryStage.setX(100);
            primaryStage.setY(100);
            primaryStage.show();
        } catch (IOException e){
            System.err.println("Failed to load gameOverWindow.fxml: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }

    public void loadGameOverSpyViewRedWin() {
        try {
            URL spyViewURL = getClass().getResource("/gameOverViewRedWin.fxml");
            if (spyViewURL == null) {
                throw new IOException("Could not find gameOverViewRedWin.fxml");
            }
            FXMLLoader loader = new FXMLLoader(spyViewURL);
            Parent spyView = loader.load();
            Scene spyScene = new Scene(spyView, 800, 600);

            spyStage = new Stage();
            spyStage.setScene(spyScene);
            spyStage.setTitle("Spy Window");

            spyStage.setWidth(800);
            spyStage.setHeight(600);
            spyStage.setX(primaryStage.getX() + primaryStage.getWidth() + 20);
            spyStage.setY(primaryStage.getY());

            spyStage.show();
        } catch (IOException e) {
            System.err.println("Failed to load spyView.fxml: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void loadGameOverSpyViewBlueWin() {
        try {
            URL spyViewURL = getClass().getResource("/gameOverViewBlueWin.fxml");
            if (spyViewURL == null) {
                throw new IOException("Could not find gameOverViewBlueWin.fxml");
            }
            FXMLLoader loader = new FXMLLoader(spyViewURL);
            Parent spyView = loader.load();
            Scene spyScene = new Scene(spyView, 800, 600);

            spyStage = new Stage();
            spyStage.setScene(spyScene);
            spyStage.setTitle("Spy Window");

            spyStage.setWidth(800);
            spyStage.setHeight(600);
            spyStage.setX(primaryStage.getX() + primaryStage.getWidth() + 20);
            spyStage.setY(primaryStage.getY());

            spyStage.show();
        } catch (IOException e) {
            System.err.println("Failed to load spyView.fxml: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void loadAddCardsWindowView(){
        try {
            closeSpyView();
            URL configViewURL = getClass().getResource("/addCardsView.fxml");
            if (configViewURL == null){
                System.err.println("Could not find configWindow.fxml");
                System.exit(1);
            }
            Parent configView = FXMLLoader.load(configViewURL);
            Scene configScene = new Scene(configView, 490, 250);
            primaryStage.setScene(configScene);
            primaryStage.setTitle("Add Cards Window");
            // Set position and size explicitly
            primaryStage.setWidth(600);
            primaryStage.setHeight(500);
            primaryStage.setX(100);
            primaryStage.setY(100);
            primaryStage.show();
        } catch (IOException e){
            System.err.println("Failed to load configWindow.fxml: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }

    public void closeSpyView() {
        if (spyStage != null) {
            spyStage.close();
        }
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public SpyBoardController getSpyBoardController() {
        return spyBoardController;
    }

    public GuesserBoardController getGuesserBoardController() {
        return guesserBoardController;
    }

    private void windowClose(javafx.stage.WindowEvent windowEvent) {
        // Gestion de la fermeture de la fenêtre pour avoir une confirmation de sauvegarde
        Game game = Game.getInstance();
        GameSave gamesave = GameSave.getInstance();
        if (!game.isSaved()) {
            int result = Utils.saveConfirmation();
            if (result == 0) {
                gamesave.saveGame();
            } else if (result == 2) {
                windowEvent.consume();
                return;
            }
        }
        System.exit(0);
    }
    
}
