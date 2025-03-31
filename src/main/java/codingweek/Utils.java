package codingweek;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

public class Utils {
    // Classe utilitaire pour les fonctions communes à plusieurs classes

    public static int saveConfirmation() {
        // Confirmation de sauvegarde

        // Création de la fenêtre de confirmation
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Do you want to save the game?");
        alert.setHeaderText(null);
        alert.setGraphic(null);
        alert.setContentText("If you don't save you'll lose your progress.");

        // Ajout des boutons
        ButtonType saveButton = new ButtonType("Save");
        ButtonType dontSaveButton = new ButtonType("Don't save");
        ButtonType cancelButton = new ButtonType("Cancel");
        alert.getButtonTypes().setAll(saveButton, dontSaveButton, cancelButton);

        // Affichage de la fenêtre et récupération du bouton cliqué
        ButtonType result = alert.showAndWait().orElse(cancelButton);
        if (result == saveButton) {
            return 0;
        } else if (result == dontSaveButton) {
            return 1;
        } else {
            return 2;
        }
    }
}
