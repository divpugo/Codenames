package codingweek;
import javafx.application.Application;
import javafx.stage.Stage;
import codingweek.models.Game;
import codingweek.models.PageManager;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) {
        PageManager pageManager = PageManager.getInstance();
        // Load and display the Menu Window View
        pageManager.setPrimaryStage(primaryStage);
        pageManager.loadMenuWindowView();
        // Load and display the Guesser View
        // loadGuesserView(primaryStage);
        // Load and display the Spy View in a new stage
        // loadSpyView(primaryStage);
    }

    public static void main(String[] args) {
        Game.getInstance();
        launch();
    }
}
