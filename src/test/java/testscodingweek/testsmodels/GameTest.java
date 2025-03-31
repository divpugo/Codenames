package testscodingweek.testsmodels;

import static org.junit.jupiter.api.Assertions.*;

import codingweek.models.Game;
import codingweek.models.Guess;
import codingweek.models.Card;
import codingweek.models.PageManager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Stack;

public class GameTest {

    private Game gameUnderTest;

    @BeforeEach
    void setUp() {
        // Si tu veux ré-initialiser l'instance à chaque test,
        // il faut un moyen de réinitialiser le singleton.
        // Par exemple, ajouter un 'reset()' dans la classe 'Game' qui met 'instance = null'.
        // Pour le moment, on se contente de récupérer l'instance existante :

        gameUnderTest = Game.getInstance();

        // On peut ré-initialiser certains champs pour éviter l'état résiduel :
        // (si la classe le permet, par exemple 'initializeGame')
        gameUnderTest.initializeGame(5, "Métier", "unlimited",false);


    }

    @Test
    void testSingleton() {
        Game anotherInstance = Game.getInstance();
        assertSame(gameUnderTest, anotherInstance,
                "Game devrait être un singleton (même instance).");
    }

    @Test
    void testInitializeGame() {
        // On a déjà un initializeGame(5, "Métier", "illimité") dans setUp().
        // Vérifions ses effets :
        assertEquals(5, gameUnderTest.getBoardSize(),
                "Le boardSize devrait être 5 après initializeGame(5, ...).");
        assertEquals(0, gameUnderTest.getTimeLimit(),
                "timeLimit devrait être 0 si on passe 'illimité'.");
        assertTrue(gameUnderTest.isSpyTurn(),
                "Après initializeGame, spyTurn devrait être true.");
        // On ne peut pas prédire isBlueTurn() car c'est un random => on vérifie juste qu'il y a pas d'erreur :
        boolean isBlue = gameUnderTest.isBlueTurn();
        // On s'attend juste à un booléen, pas grand-chose de plus.
    }

    @Test
    void testSetBoardSize() {
        gameUnderTest.setBoardSize(4);
        assertEquals(4, gameUnderTest.getBoardSize(),
                "Le boardSize devrait être 4 après setBoardSize(4).");
    }

    @Test
    void testSetTimeLimit() {
        gameUnderTest.setTimeLimit(60);
        assertEquals(60, gameUnderTest.getTimeLimit(),
                "timeLimit devrait être 60 après setTimeLimit(60).");
    }

    @Test
    void testAddGuessAndGetLastGuess() {
        Guess guess = new Guess("Indice", 2);
        gameUnderTest.addGuess(guess);

        Guess lastGuess = gameUnderTest.getLastGuess();
        assertNotNull(lastGuess, "Le dernier guess ne doit pas être nul après un addGuess.");
        assertEquals("Indice", lastGuess.getClue(),
                "Le clue du dernier guess devrait être 'Indice'.");
        assertEquals(2, lastGuess.getNbWords(),
                "Le nbWords du dernier guess devrait être 2.");
    }

    @Test
    void testGetLastGuessEmptyStack() {
        // Vide la pile si besoin
        while (gameUnderTest.getLastGuess() != null) {
            // On retire (si on avait un pop, par ex.):
            // Ici, la classe Game ne fournit pas de pop.
            // On se contente de ré-initialiser :
            gameUnderTest.initializeGame(5, "Métier", "illimité",false);
        }
        // Ou on s'assure que la stack est vide d'une autre manière
        // Cf. code: guesses.clear() n'est pas exposé directement,
        // donc on se base sur un re-init.

        assertNull(gameUnderTest.getLastGuess(),
                "Le dernier guess doit être null si la pile est vide.");
    }

    @Test
    void testSubmitClueValid() {
        // spyTurn est true => on peut soumettre
        int result = gameUnderTest.submitClue("TestClue", 3);
        // On s'attend à 1 si clueIsValid => c'est le cas par défaut (pas dans la liste interdite)
        assertEquals(1, result,
                "submitClue() devrait renvoyer 1 si le clue est valide, spyTurn=true, et nbWords dans la limite.");
    }

    @Test
    void testSubmitClueInvalidBecauseSpyTurnFalse() {
        // On force un changement de tour
        gameUnderTest.changeTurn(); // now spyTurn should be false
        int result = gameUnderTest.submitClue("unIndice", 2);
        assertEquals(0, result,
                "submitClue() devrait renvoyer 0 si spyTurn est false.");
    }

    @Test
    void testChangeTurn() {
        // État initial: spyTurn = true
        boolean initialSpy = gameUnderTest.isSpyTurn();
        boolean initialBlue = gameUnderTest.isBlueTurn();

        gameUnderTest.changeTurn();
        // Après le changeTurn, on s'attend à ce que nbCardReturned = 0
        assertEquals(0, gameUnderTest.getNbCardsReturned(),
                "Le nbCardReturned devrait repasser à 0 après un changeTurn.");

        boolean newSpy = gameUnderTest.isSpyTurn();
        boolean newBlue = gameUnderTest.isBlueTurn();

        // Vérifions que quelque chose a changé
        // ex. si spyTurn était true, ça devient false, etc.
        // Cf. ta logique, c'est un peu plus complexe (un if/else if/else)
        if (initialSpy) {
            assertFalse(newSpy,
                    "Si spyTurn était true, il devrait passer à false après changeTurn().");
        } else {
            // spyTurn devait repasser à true
            assertTrue(newSpy,
                    "Si spyTurn était false, il devrait passer à true après changeTurn().");
        }
        // On pourrait tester plus loin isBlueTurn, etc.
    }

    @Test
    void testRevealTileAndIsTileRevealed() {
        gameUnderTest.revealTile(2, 2);
        assertTrue(gameUnderTest.isTileRevealed(2, 2),
                "La case (2,2) devrait être marquée comme révélée.");
        assertFalse(gameUnderTest.isTileRevealed(3, 3),
                "La case (3,3) ne devrait pas être révélée si on ne l’a pas revealTile.");
    }

}
