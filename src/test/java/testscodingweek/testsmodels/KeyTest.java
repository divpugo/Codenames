package testscodingweek.testsmodels;

import static org.junit.jupiter.api.Assertions.*;

import javafx.scene.paint.Color;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import codingweek.models.Key;
import codingweek.models.Game;
import codingweek.models.Board;

public class KeyTest {

    private Key keyUnderTest;

    @BeforeEach
    void setUp() {

        Game.getInstance().initializeGame(5, "Métier", "unlimited", false);
        keyUnderTest = Key.getInstance();
    }

    @Test
    void testSingleton() {
        // Vérifie que Key est bien un singleton
        Key anotherInstance = Key.getInstance();
        assertSame(keyUnderTest, anotherInstance,
                "getInstance() devrait toujours retourner la même instance");
    }

    @Test
    void testNewKeyInitializesGrid() {
        // On appelle newKey()
        keyUnderTest.newKey();

        // Vérifie que les dimensions correspondent à celles de Game (par défaut 5x5 ?)
        int lignes = keyUnderTest.getLignes();
        int colonnes = keyUnderTest.getColonnes();
        assertTrue(lignes > 0 && colonnes > 0,
                "Les dimensions de la grille devraient être supérieures à 0");

        // Vérifie qu'on peut récupérer une couleur correcte
        Color c = keyUnderTest.getCouleur(0, 0);
        assertNotNull(c, "La première case (0,0) ne devrait pas être nulle");
        assertNotEquals(Color.TRANSPARENT, c,
                "La première case (0,0) ne devrait pas être TRANSPARENT après initialisation");
    }

    @Test
    void testGetCouleurOutOfBounds() {
        keyUnderTest.newKey();
        // Suppose qu'on a 5x5 : un indice hors bornes devrait renvoyer TRANSPARENT
        Color outOfBoundColor = keyUnderTest.getCouleur(999, 999);
        assertEquals(Color.TRANSPARENT, outOfBoundColor,
                "Un indice hors bornes devrait renvoyer Color.TRANSPARENT");
    }

    @Test
    void testAssignColorsToCards() {
        // On appelle newKey pour initialiser et assigner les couleurs
        keyUnderTest.newKey();

        // Récupérer les cartes depuis Game
        Game game = Game.getInstance();
        Board board = game.getBoard();
        // On part du principe que Board possède une liste de 25 cartes (si boardSize=5)
        // Test de base : chaque carte a bien été assignée (color != null)
        board.getCards().forEach(card -> {
            assertNotNull(card.getColor(),
                    "Chaque carte devrait avoir une couleur assignée");
        });
    }
}
