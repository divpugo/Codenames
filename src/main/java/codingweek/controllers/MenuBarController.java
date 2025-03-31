package codingweek.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import codingweek.Utils;
import codingweek.models.Game;
import codingweek.models.GameSave;
import codingweek.models.PageManager;

public class MenuBarController {

    private PageManager pageManager;
    private GameSave gamesave;
    private Game game;

    public void initialize() {
        pageManager = PageManager.getInstance();
        gamesave = GameSave.getInstance();
        game = Game.getInstance();
    }

    @FXML
    private void Charger(ActionEvent event) {
        if (!game.isSaved()) {
            int result = Utils.saveConfirmation();
            if (result == 0) {
                gamesave.saveGame();
            } else if (result == 2) {
                return;
            }
        }
        gamesave.loadGame();
    }

    @FXML
    private void Sauvegarder(ActionEvent event) {
        gamesave.saveGame();
    }

    @FXML
    private void RetourAcceuil(ActionEvent event) {
        if (!game.isSaved()) {
            int result = Utils.saveConfirmation();
            if (result == 0) {
                gamesave.saveGame();
            } else if (result == 2) {
                return;
            }
        }
        pageManager.closeSpyView();
        pageManager.loadMenuWindowView();   
    }

    @FXML
    private void AfficherStatistiques(ActionEvent event) {
        pageManager.closeSpyView();
        pageManager.loadStatsView();
    }
}
