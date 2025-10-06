package cardgame;

import java.util.ArrayList;

public class Player implements Runnable {
    private Deck drawDeck;
    private Deck discardDeck;
    private ArrayList<Card> hand;
    private ArrayList<String> logLines;

    public Player(Deck drawDeck, Deck discardDeck) {
        this.drawDeck = drawDeck;
        this.discardDeck = discardDeck;
        this.hand = new ArrayList<Card>();
        this.logLines = new ArrayList<String>();
    }

    @Override
    public void run() {}

    public void play() {
        //TODO: Implement and ensure it is thread-safe.
    }

    public void saveLogFile(String filename) {
        //TODO: Implement.
    }


}
