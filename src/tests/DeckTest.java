package tests;

import cardgame.Card;
import cardgame.Deck;
import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

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

}
