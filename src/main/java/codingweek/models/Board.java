package codingweek.models;

import java.io.Serializable;
import java.util.ArrayList;

import codingweek.Observer;

public class Board implements Serializable{
    private ArrayList<Card> cards; // Doit être de taille boardSize * boardSize (25 par défaut)
    private static Board instance;

    private Board() {
        this.cards = new ArrayList<Card>();
    }

    public static Board getInstance() {
        if (instance == null) {
            instance = new Board();
        }
        return instance;
    }

    public ArrayList<Card> getCards() {
        return cards;
    }

    public void setCards(ArrayList<Card> cards) {
        this.cards = cards;
    }
    // Il y a pas forcément besoin de setCards et addCard, 
    public void addCard(Card card) {
        cards.add(card);
    }

    public void addCardObservers(Observer object) {
        for (Card card : cards) {
            card.ajouterObservateur(object);
        }
    }

    public void cleanCards() {
        this.cards.clear();
    }
}
