package cardgame;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class Player implements Runnable {
    public final int playerNo;
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
    public void run() {
        // Wait until the game begins
        while (!CardGame.getInstance().isGameRunning()) {
            // Wait...
        }

        this.play();
    }

    public void play() {
        while (CardGame.getInstance().isGameRunning()) {
            // winning condition to break checked each instance
            boolean hasWon = true;

            // Compare ranks of cards within card to check if winning conditions have been met
            int firstCardFromHand = this.hand.get(0).getRank();

            // Loop checking if hasWon shall remain True or should be false
            for (int i = 1; i < 4; i++) {
                // Check current card being inspected has the same value as first card or not
                // If not, update hasWon to signify the continuation of the game
                if (firstCardFromHand != this.hand.get(i).getRank()) {
                    hasWon = false;
                    break;
                }
            }

            // If hasWon is still true, initiate the end of the game
            if (hasWon) {
                // Adding winning declaration to log
                this.log("player " + this.playerNo + " wins");

                CardGame.getInstance().endGame(this);
                break;
            } else {
                // Otherwise, draw and discard cards
                try {
                    this.drawAndDiscard();
                } catch (InterruptedException e) {
                    System.out.println(e.getMessage());
                }
            }
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
    public void drawAndDiscard() throws InterruptedException {
        synchronized (this.drawDeck) {
            if (this.drawDeck.size() == 0) {
                this.drawDeck.wait();
            }

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

                    this.discardDeck.notify();

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
            // Create new log file if it doesn't exist.
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
