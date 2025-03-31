package testscodingweek.testsmodels;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import codingweek.Observer;
import codingweek.models.Subject;

import java.util.concurrent.atomic.AtomicInteger;

public class SubjectTest {

    private Subject subjectUnderTest;

    @BeforeEach
    void setUp() {
        subjectUnderTest = new Subject();
    }

    @Test
    void testAjouterObservateur() {
        // On ajoute un observateur (faux ou simulé) et vérifie que tout se passe sans exception
        Observer observer = new FakeObserver();
        assertDoesNotThrow(() -> {
            subjectUnderTest.ajouterObservateur(observer);
        }, "L’ajout d’un observateur ne doit pas lever d’exception");
    }

    @Test
    void testNotifierObservateurs() {
        // On va utiliser un compteur pour compter le nombre d'appels à reagir()
        AtomicInteger callCount = new AtomicInteger(0);

        // On crée un observer “lambda” qui incrémente le compteur dans reagir()
        Observer countingObserver = () -> callCount.incrementAndGet();

        // On ajoute 2 fois le même observer juste pour voir qu’il est bien appelé deux fois
        subjectUnderTest.ajouterObservateur(countingObserver);
        subjectUnderTest.ajouterObservateur(countingObserver);

        // On appelle notifierObservateurs()
        subjectUnderTest.notifierObservateurs();

        // Vérifie que reagir() a été appelé deux fois (une par observer)
        assertEquals(2, callCount.get(),
                "reagir() devrait avoir été appelée 2 fois (pour 2 observateurs).");
    }

    // Une classe interne ou un “dummy” observer
    private static class FakeObserver implements Observer {
        @Override
        public void reagir() {
            // Ne fait rien
        }
    }
}
