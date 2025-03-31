package testscodingweek.testsmodels;

import static org.junit.jupiter.api.Assertions.*;

import codingweek.models.Guess;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


public class GuessTest {

    private Guess guessUnderTest;

    @BeforeEach
    void setUp() {
        // On instancie un Guess avec des valeurs de test
        guessUnderTest = new Guess("Fruit", 3);
    }

    @Test
    void testConstructorAndGetters() {
        // Vérifie que le clue est bien récupéré
        assertEquals("Fruit", guessUnderTest.getClue(),
                "Le clue devrait être 'Fruit'");
        // Vérifie que nbWords est bien récupéré
        assertEquals(3, guessUnderTest.getNbWords(),
                "Le nbWords devrait être 3");
    }

    @Test
    void testNegativeOrZeroNbWords() {
        // On peut tester un cas plus extrême : un guess avec nbWords négatif ou zéro
        Guess guessWithZero = new Guess("Vide", 0);
        assertEquals(0, guessWithZero.getNbWords(),
                "Ici, nbWords est 0 (cas limite).");

        Guess guessWithNegative = new Guess("Invalide", -1);
        assertEquals(-1, guessWithNegative.getNbWords(),
                "Ici, nbWords est -1 (cas limite).");
    }

    @Test
    void testEmptyClue() {
        // On teste un indice vide
        Guess emptyClueGuess = new Guess("", 2);
        assertEquals("", emptyClueGuess.getClue(),
                "Le clue devrait être une chaîne vide");
    }
}