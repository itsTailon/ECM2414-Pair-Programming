package cardgame;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.NoSuchElementException;

/**
 * Class representing deck of cards.
 */
public class Deck {
    /**
     * Queue used to store {@link Card} objects in deck.
     */
    private Queue<Card> cards;

    private int deckNo;

    public Deck() {
        this.cards = new Queue<Card>();
        this.deckNo = -1;
    }

    /**
     * Inserts new {@link Card} into cards {@link Queue}.
     * @param card {@link Card} to be inserted to the bottom of the deck.
     */
    public synchronized void insert(Card card) {
        this.cards.enqueue(card);
    }

    /**
     * Draw {@link Card} from the top of the deck.
     * @return {@link Card} drawn from the top of the deck.
     * @throws NoSuchElementException if the deck is empty.
     */
    public synchronized Card draw() throws NoSuchElementException {
        try {
            return this.cards.dequeue();
        } catch (NoSuchElementException e){
            throw e;
        }
    }

    public void setDeckNo(int deckNo) {
        this.deckNo = deckNo;
    }

    public int getDeckNo() {
        return this.deckNo;
    }

    public int size() { return this.cards.getSize(); }

    /**
     * Saving deck contents to a file.
     * @param filename name of output file.
     */
    public void saveDeckContentsFile(String filename) {
        try {
            // Create new file if it doesn't exist.
            File outputFile = new File(filename);
            outputFile.createNewFile();

            // Construct output to be written to file
            String output = "deck" + this.deckNo + " contents:";
            while (this.cards.getSize() > 0) {
                output += " " + this.cards.dequeue().getRank();
            }

            // Write deck contents to the file
            FileWriter fileWriter = new FileWriter(outputFile);
            fileWriter.write(output);
            fileWriter.close();

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
