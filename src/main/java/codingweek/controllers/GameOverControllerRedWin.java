package codingweek.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import codingweek.models.PageManager;


public class GameOverControllerRedWin {
    @FXML
    private ImageView imageCenter;

    @FXML
    private Button accueilButton;

    @FXML
    private Button statsButton;

    private PageManager pageManager;

    public GameOverControllerRedWin() {
        pageManager = PageManager.getInstance();
    }

    @FXML
    public void initialize() {
        Image img = new Image(getClass().getResourceAsStream("/Images/GameOverBleu.png"));
        imageCenter.setImage(img);

        // Gérer l'action sur le bouton Accueil
        accueilButton.setOnAction(e -> {
            pageManager.loadMenuWindowView();
        });

        // Gérer l'action sur le bouton Afficher les stats
        statsButton.setOnAction(e -> {
            pageManager.loadStatsView();
            pageManager.closeSpyView();
        });
    }
}
