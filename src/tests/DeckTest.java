package tests;

import cardgame.Card;
import cardgame.Deck;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

public class DeckTest {

    @Test
    public void testInsertAndDraw() {
        Deck deck = new Deck();

        Card card1 = new Card(1);
        Card card2 = new Card(2);

        deck.insert(card1);
        deck.insert(card2);

        assertEquals(card1, deck.draw());
        assertEquals(card2, deck.draw());

        assertThrows(NoSuchElementException.class, () -> deck.draw());
    }

    @Test
    public void testSaveDeckContentsFile() throws IOException {
        // Set up a deck such that its output file should read: 'deck1 contents: 1 2 3 4'
        Deck deck = new Deck();
        deck.setDeckNo(1);
        deck.insert(new Card(1));
        deck.insert(new Card(2));
        deck.insert(new Card(3));
        deck.insert(new Card(4));

        // Invoke the saveDeckContentsFile method to save a deck output file as 'test_deck_output.txt'
        String filename = "test_deck_output.txt";
        deck.saveDeckContentsFile(filename);

        // Create a file object for the output file
        File outputFile = new File(filename);

        // Ensure that a file was created with the correct name
        assertTrue(outputFile.exists());

        // Ensure that the file has the correct content
        BufferedReader reader = new BufferedReader(new FileReader(outputFile));
        assertEquals("deck1 contents: 1 2 3 4", reader.readLine());

        // Ensure that the file only has one line
        assertNull(reader.readLine());

        // Cleanup
        reader.close();
        outputFile.delete();
    }
}
