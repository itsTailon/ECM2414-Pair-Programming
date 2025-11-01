package tests;

import cardgame.Card;
import cardgame.Deck;
import cardgame.Player;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.Field;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class PlayerTest {

    @Test
    public void testSaveLogFile() throws FileNotFoundException {
        Player p = new Player(new Deck(), new Deck(), 0);

        p.log("line 1");
        p.log("line 2");
        p.log("line 3");

        assertDoesNotThrow(() -> p.saveLogFile("test_log.txt"));

        BufferedReader reader = new BufferedReader(new FileReader(new File("test_log.txt")));

        String[] lines = new String[3];

        assertDoesNotThrow(() -> {
            int i = 0;
            String line;
            while ((line = reader.readLine()) != null) {
                lines[i] = line;
                i++;
            }
        });

        assertEquals("line 1", lines[0]);
        assertEquals("line 2", lines[1]);
        assertEquals("line 3", lines[2]);

    }

    @Test
    public void testDrawAndDiscard() throws NoSuchFieldException, IllegalAccessException {
        // Setup decks
        Deck drawDeck = new Deck();
        Deck discardDeck = new Deck();
        Card sampleCardInDeck = new Card(3);
        drawDeck.insert(sampleCardInDeck);

        Player player = new Player(drawDeck, discardDeck, 1);
        Card preferredCard = new Card(1);
        player.insertIntoHand(preferredCard);

        int cardsInDrawDeckBeforeDrawAndDiscardInvoked = drawDeck.size();

        player.drawAndDiscard();

        // Use Reflection to access the private 'hand' field of the Player object
        Field hand = Player.class.getDeclaredField("hand");
        hand.setAccessible(true);
        ArrayList<Card> playerHand = (ArrayList<Card>) (hand.get(player));

        int cardsInHandBeforeDrawAndDiscardInvoked = playerHand.size();

        // Check that player does not discard their preferred card
        //
        //      The player started with only their preferred card in their hand
        //      and the card that they drew should be a '3', which they thus
        //      should have discarded, leaving only the preferred card
        //      (preferredCard) in their hand.
        //
        assertTrue(playerHand.contains(preferredCard));

        // Ensure that the draw deck has lost a card
        assertEquals(1, cardsInDrawDeckBeforeDrawAndDiscardInvoked - drawDeck.size());

        // Ensure that the player's hand has the same number as cards as they started with
        assertTrue(cardsInHandBeforeDrawAndDiscardInvoked == ((ArrayList<Card>) (hand.get(player))).size());

        // Ensure that the (prev. empty) discard deck increased in size by 1
        assertEquals(1, discardDeck.size());

        // Ensure that the discard deck now contains the discarded card (sampleCardInDeck)
        assertTrue(discardDeck.draw() == sampleCardInDeck);
    }
}
