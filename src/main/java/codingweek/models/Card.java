package codingweek.models;
import java.util.ArrayList;
import java.io.Serializable;

public class Card extends Subject implements Serializable {

    // Attributes
    private String word;
    private ArrayList<String> forbiddenWords;
    private boolean isRevealed = false;
    private String color;
    // Black : 0x000000ff
    // Red : 0xc1121fff
    // Blue : 0x003566ff
    // Neutral : 0xf0ead2ff

    // Predefined colors with their hash codes
    public static final String RED_COLOR = "#c1121f";
    public static final String BLUE_COLOR = "#003566";
    public static final String NEUTRAL_COLOR = "#f0ead2";

    // Constructor
    public Card(String word, String color) {
        this.forbiddenWords = new ArrayList<>();
        this.word = word;
        this.color = color;
    }

    // Getters and Setters
    public String getWord() { return this.word; }
    public String getColor() { return this.color; }
    public boolean isRevealed() { return this.isRevealed; }
    public void setWord(String word) { this.word = word; }
    public void setColor(String color) { this.color = color; }

    
    public void setRevealed(boolean revealed) {
        this.isRevealed = revealed;
        notifierObservateurs();; 
    }

    public ArrayList<String> getForbiddenWords() { return this.forbiddenWords; }
    public void addForbiddenWords(ArrayList<String> forbiddenWords) { this.forbiddenWords.addAll(forbiddenWords); }
    
}
