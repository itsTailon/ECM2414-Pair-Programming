package tests;

import cardgame.Card;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CardTest {

    @Test
    public void testConstructorAndGetRank() {
        assertThrows(IllegalArgumentException.class, () -> { new Card(-1); });

        Card c = new Card(1);

        assertEquals(1, c.getRank());
    }

}
