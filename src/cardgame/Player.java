package cardgame;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Player implements Runnable {
    private final int playerNo;
    private Deck drawDeck;
    private Deck discardDeck;
    private ArrayList<Card> hand;
    private ArrayList<String> logLines;

    public Player(Deck drawDeck, Deck discardDeck, int playerNo) {
        this.drawDeck = drawDeck;
        this.discardDeck = discardDeck;
        this.playerNo = playerNo;
        this.hand = new ArrayList<Card>();
        this.logLines = new ArrayList<String>();
    }

    @Override
    public void run() {}

    public void play() {
        while (CardGame.getInstance().isGameRunning()) {
            // Gameplay logic...
        }
    }

    /**
     * Adds a card to the player's hand.
     * @param card the card to add.
     */
    public void insertIntoHand(Card card) {
        this.hand.add(card);
    }

    /**
     * Atomic operation that draws a card and subsequently discards a card.
     */
    public void drawAndDiscard() {
        synchronized (this.drawDeck) {
            Card card = this.drawDeck.draw();
            this.hand.add(card);

            this.log("player " + this.playerNo + " draws a " + card.getRank() + " from deck " + this.drawDeck.getDeckNo());
        }

        synchronized (this.discardDeck) {
            // Discard a card from this player's hand, making sure not to discard a card of the preferred rank
            for (int i = 0; i < this.hand.size(); i++) {
                Card card = this.hand.get(i);
                if (card.getRank() != this.playerNo) {
                    this.discardDeck.insert(card);
                    this.hand.remove(i);

                    this.log("player " + this.playerNo + " discards a " + card.getRank() + " to deck " + this.discardDeck.getDeckNo());

                    break;
                }
            }
        }
    }

    /**
     * Saving log contents to log file.
     * @param filename name of file to store log information on.
     */
    public void saveLogFile(String filename) {
        try {
            // Create new file log if it doesn't exist.
            File logFile = new File(filename);
            logFile.createNewFile();

            // Iterate through log lines and add to output String to be written to log.
            String logOutput = "";
            for (String line : logLines) {
                logOutput += line + "\n";
            }

            // Writing to the log file.
            FileWriter fileWriter = new FileWriter(logFile);
            fileWriter.write(logOutput);
            fileWriter.close();

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Adding a log message to logLines array.
     * @param logMessage Message to be passed to logLines array.
     */
    public void log(String logMessage) {
        this.logLines.add(logMessage);
    }

}
