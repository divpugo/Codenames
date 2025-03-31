package codingweek.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

import java.io.IOException;


import codingweek.models.PageManager;

public class MenuWindowController {

    // Attributes
    @FXML
    private Button playButton;
    @FXML
    private Button rulesButton;
    @FXML
    private Button quitButton;
    @FXML
    private Button ajouterCartesButton;

    private PageManager pageManager;

    @FXML
    private void initialize() {
        pageManager = PageManager.getInstance();
        // Cette méthode est appelée automatiquement après le chargement du FXML

        playButton.setOnAction(e -> {
            pageManager.loadConfigWindowView();
        });

        rulesButton.setOnAction(e -> {
            String url = "https://iello.fr/wp-content/uploads/2016/10/Codenames_rulebook_FR-web_2018.pdf";
            openWebpage(url);
        });

        quitButton.setOnAction(e -> {
            // Logique pour quitter l'application
            System.exit(0);
        });

        ajouterCartesButton.setOnAction(e -> {
            // Logique pour quitter l'application
            pageManager.loadAddCardsWindowView();
        });
    }

    @SuppressWarnings("deprecation")
    private void openWebpage(String url) {
        try {
            // 2) Si Desktop non supporté ou browse non disponible,
            //    on tente manuellement selon l'OS.
            String osName = System.getProperty("os.name").toLowerCase();

            if (osName.contains("mac")) {
                // Sur MacOS, la commande 'open' lance le navigateur par défaut
                Runtime.getRuntime().exec("open " + url);
            }
            else if (osName.contains("nix") || osName.contains("nux") || osName.contains("aix")) {
                // Sur Linux, la commande 'xdg-open' lance le navigateur par défaut
                Runtime.getRuntime().exec("xdg-open " + url);
            }
            else if (osName.contains("win")) {
                // Sur Windows, la commande 'rundll32' ou 'start'
                // peut être utilisée, mais en général Desktop devrait marcher
                // Si besoin, on peut utiliser :
                Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + url);
            }
            else {
                System.err.println("OS not supported: " + osName);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}