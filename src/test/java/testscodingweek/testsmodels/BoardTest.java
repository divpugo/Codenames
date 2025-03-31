package testscodingweek.testsmodels;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

import codingweek.models.Board;
import codingweek.models.Card;

import java.util.ArrayList;

public class BoardTest {

    private Board board;

    @BeforeEach
    public void setUp(){
        board = Board.getInstance();
        board.cleanCards();
    }

    @Test
    public void testGetInstance() {
        assertNotNull(board);
    }

    @Test
    public void testGetCards() {
        assertNotNull(board.getCards());
        assertTrue(board.getCards().isEmpty());
    }

    @Test
    public void testSetCards() {
        ArrayList<Card> cards = new ArrayList<>();
        cards.add(new Card("Test Card 1","color1"));
        cards.add(new Card("Test Card 2","color2"));
        board.setCards(cards);
        assertEquals(2, board.getCards().size());
    }

    @Test
    public void testAddCards(){
        Card card = new Card("Test Card","testColor");
        board.addCard(card);
        assertEquals(1, board.getCards().size());
        assertEquals(card, board.getCards().get(0));
    }

    @Test
    public void testCleanCards(){
        board.addCard(new Card("Test Card", "testColor"));
        board.cleanCards();
        assertTrue(board.getCards().isEmpty());
    }
}
