package testscodingweek.testsmodels;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

import codingweek.models.Card;

import java.util.ArrayList;

public class CardTest {

    private Card card;

    @BeforeEach
    public void setUp(){
        card = new Card("Test Word", Card.BLUE_COLOR);
    }

    @Test
    public void testgetWord(){
        assertEquals("Test Word", card.getWord());
    }

    @Test
    public void testSetWord(){
        card.setWord("New Word");
        assertEquals("New Word", card.getWord());
    }

    @Test
    public void testGetColor(){
        card.setColor(Card.RED_COLOR);
        assertEquals(Card.RED_COLOR, card.getColor());
    }

    @Test
    public void testIsRevealed(){
        assertFalse(card.isRevealed());
    }

    @Test
    public void testSetRevealed(){
        card.setRevealed(true);
        assertTrue(card.isRevealed());
    }

    @Test
    public void testGetForbiddenWords(){
        assertNotNull(card.getForbiddenWords());
        assertTrue(card.getForbiddenWords().isEmpty());
    }

    @Test
    public void testAddForbiddenWords(){
        ArrayList<String> forbiddenWords = new ArrayList<>();
        forbiddenWords.add("Forbidden1");
        forbiddenWords.add("Forbidden2");
        card.addForbiddenWords(forbiddenWords);
        assertEquals(2, card.getForbiddenWords().size());
        assertTrue(card.getForbiddenWords().contains("Forbidden1"));
        assertTrue(card.getForbiddenWords().contains("Forbidden2"));
    }
}
