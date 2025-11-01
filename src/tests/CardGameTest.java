package tests;

import cardgame.Card;
import cardgame.CardGame;
import cardgame.Deck;
import cardgame.Player;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CardGameTest {

    private static File createValidTestPackFile(String filename, int playerCount) throws IOException {
        // Create a valid pack file for the purposes of testing...
        File validPackFile = new File(filename);
        validPackFile.createNewFile();

        // Construct contents of test valid pack file
        Integer rank = 0;
        String validPackContents = "";
        for (int i = 0; i < playerCount * 8; i++) {
            validPackContents += rank.toString() + "\n";
            rank++;
        }

        // Write to file
        FileWriter fileWriter = new FileWriter(validPackFile);
        fileWriter.write(validPackContents);
        fileWriter.close();

        return validPackFile;
    }

    @Test
    public void testIsPackValid_forValidInput() throws IOException {
        int playerCount = 4;
        File validPackFile = CardGameTest.createValidTestPackFile("test_valid_pack.txt", playerCount);

        // Test CardGame.isPackValid
        assertTrue(CardGame.isPackValid(validPackFile.getName(), playerCount));

        // Cleanup (delete file)
        validPackFile.delete();
    }

    @Test
    public void testIsPackValid_forErroneousInput() throws IOException {
        // Create an invalid pack file for the purposes of testing...
        File invalidPackFile = new File("test_invalid_pack.txt");
        invalidPackFile.createNewFile();

        // Construct contents of test invalid pack file
        int playerCount = 4;
        int nPlayerCards = 1; // Invalid number of cards will be in the pack (total cards should equal 8 * player count)
        Integer rank = -10; // Some invalid card ranks will be included in the pack (card ranks should be non-negative)
        String invalidPackContents = "";
        for (int i = 0; i < playerCount * nPlayerCards; i++) {
            invalidPackContents += rank.toString() + "\n";
            rank++;
        }

        // Write to file
        FileWriter fileWriter = new FileWriter(invalidPackFile);
        fileWriter.write(invalidPackContents);
        fileWriter.close();

        // Test CardGame.isPackValid
        assertFalse(CardGame.isPackValid(invalidPackFile.getName(), playerCount));

        // Cleanup (delete file)
        invalidPackFile.delete();
    }

    @Test
    public void testIsPackValid_forInputWithValidRanksAndInvalidCardCount() throws IOException {
        // Create an invalid pack file for the purposes of testing...
        File invalidPackFile = new File("test_invalid_pack.txt");
        invalidPackFile.createNewFile();

        // Construct contents of test invalid pack file
        int playerCount = 4;
        int nPlayerCards = 1; // Invalid number of cards will be in the pack (total cards should equal 8 * player count)
        Integer rank = 0; // All valid card ranks will be in the pack (card ranks should be non-negative)
        String invalidPackContents = "";
        for (int i = 0; i < playerCount * nPlayerCards; i++) {
            invalidPackContents += rank.toString() + "\n";
            rank++;
        }

        // Write to file
        FileWriter fileWriter = new FileWriter(invalidPackFile);
        fileWriter.write(invalidPackContents);
        fileWriter.close();

        // Test CardGame.isPackValid
        assertFalse(CardGame.isPackValid(invalidPackFile.getName(), playerCount));

        // Cleanup (delete file)
        invalidPackFile.delete();
    }

    private static File createInvalidTestPackFile(String filename, int playerCount) throws IOException {
        // Create an invalid pack file for the purposes of testing...
        File invalidPackFile = new File(filename);
        invalidPackFile.createNewFile();

        // Construct contents of test invalid pack file
        int nPlayerCards = 8; // Valid number of cards will be in the pack
        Integer rank = -10; // Some invalid card ranks will be included in the pack (card ranks should be non-negative)
        String invalidPackContents = "";
        for (int i = 0; i < playerCount * nPlayerCards; i++) {
            invalidPackContents += rank.toString() + "\n";
            rank++;
        }

        // Write to file
        FileWriter fileWriter = new FileWriter(invalidPackFile);
        fileWriter.write(invalidPackContents);
        fileWriter.close();

        return invalidPackFile;
    }

    @Test
    public void testIsPackValid_forInputWithInvalidRanksAndValidCardCount() throws IOException {
        int playerCount = 4;
        File invalidPackFile = CardGameTest.createInvalidTestPackFile("test_invalid_pack.txt", playerCount);

        // Test CardGame.isPackValid
        assertFalse(CardGame.isPackValid(invalidPackFile.getName(), playerCount));

        // Cleanup (delete file)
        invalidPackFile.delete();
    }

    @Test
    public void testIsPackValid_forEmptyInputFile() throws IOException {
        // Create an empty pack file for the purposes of testing...
        File emptyPackFile = new File("test_empty_pack.txt");
        emptyPackFile.createNewFile();

        // Test CardGame.isPackValid with range of player counts
        for (int playerCount = 1; playerCount < 4; playerCount++) {
            assertFalse(CardGame.isPackValid(emptyPackFile.getName(), playerCount));
        }

        // Cleanup (delete file)
        emptyPackFile.delete();
    }

    @Test
    public void testIsPackValid_forInputFileWithInvalidDataType() throws IOException {
        // Create an invalid pack file for the purposes of testing...
        File invalidPackFile = new File("test_invalid_pack.txt");
        invalidPackFile.createNewFile();

        // Construct contents of test invalid pack file
        int playerCount = 4;
        int nPlayerCards = 8; // Valid number of cards will be in the pack
        Integer rank = 0;
        String invalidPackContents = "";
        for (int i = 0; i < playerCount * nPlayerCards; i++) {
            // Add some non-numerical characters to the file (for each odd value of i)
            if (i % 2 != 0) {
                invalidPackContents += 'a';
                invalidPackContents += '%';
                i++; // We are adding two characters in one loop iteration, so compensate by incrementing i twice.
            } else {
                invalidPackContents += rank.toString() + "\n";
            }
            rank++;
        }

        // Write to file
        FileWriter fileWriter = new FileWriter(invalidPackFile);
        fileWriter.write(invalidPackContents);
        fileWriter.close();

        // Test CardGame.isPackValid
        assertFalse(CardGame.isPackValid(invalidPackFile.getName(), playerCount));

        // Cleanup (delete file)
        invalidPackFile.delete();
    }

    @Test
    public void testLoadPackFromFile_forValidInput() throws NoSuchMethodException, IOException, InvocationTargetException, IllegalAccessException, NoSuchFieldException {
        // Use Reflection to prepare for accessing the private 'loadPackFromFile' method of CardGame
        Method loadPackFromFileMethod = CardGame.class.getDeclaredMethod("loadPackFromFile", String.class, int.class);
        loadPackFromFileMethod.setAccessible(true);

        // Create valid pack file for testing
        String filename = "test_valid_pack.txt";
        int playerCount = 4;
        File packFile = CardGameTest.createValidTestPackFile(filename, playerCount);

        // Get CardGame instance
        CardGame cardGame = CardGame.getInstance() == null ? CardGame.newInstance(4, filename) : CardGame.getInstance();

        // CardGame.loadPackFromFile should return true if the pack was successfully loaded, which it should be, as it is valid.
        assertTrue((Boolean) loadPackFromFileMethod.invoke(cardGame, filename, playerCount));

        // Use Reflection to prepare for accessing the private 'pack' fields of the CardGame instance
        Field pack = cardGame.getClass().getDeclaredField("pack");
        pack.setAccessible(true);

        // Valid would be cards 0-(playerCount * 8 - 1)
        for (int rank = 0; rank < playerCount * 8; rank++) {
            assertEquals(rank, ((Deck) pack.get(cardGame)).draw().getRank());
        }

        // Cleanup
        packFile.delete();
    }

    @Test
    public void testLoadPackFromFile_forInvalidInput() throws NoSuchMethodException, IOException, InvocationTargetException, IllegalAccessException, NoSuchFieldException {
        // Use Reflection to prepare for accessing the private 'loadPackFromFile' method of CardGame
        Method loadPackFromFileMethod = CardGame.class.getDeclaredMethod("loadPackFromFile", String.class, int.class);
        loadPackFromFileMethod.setAccessible(true);

        // Create invalid pack file for testing
        String invalidPackFilename = "test_invalid_pack.txt";
        int playerCount = 4;
        File invalidPackFile = CardGameTest.createInvalidTestPackFile(invalidPackFilename, playerCount);

        // Get/create CardGame instance. If creating the singleton instance, use a valid pack file, as the constructor will call a method which checks if the passed filename corresponds to a valid pack file.
        String validPackFilename = "test_valid_pack.txt";
        File validPackFile = CardGameTest.createValidTestPackFile(validPackFilename, playerCount);
        CardGame cardGame = CardGame.getInstance() == null ? CardGame.newInstance(4, validPackFilename) : CardGame.getInstance();

        // CardGame.loadPackFromFile should return false if the pack was successfully loaded, which it should be, as it is invalid.
        assertFalse((Boolean) loadPackFromFileMethod.invoke(cardGame, invalidPackFilename, playerCount));

        // Cleanup
        validPackFile.delete();
        invalidPackFile.delete();
    }

    @Test
    public void testEndGame_forValidInput() throws IOException, NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
        // Creating a test file for test
        File validPackFile = CardGameTest.createValidTestPackFile("testFile.txt", 4);
        // Creating instance of Card Game
        CardGame cardGame = CardGame.newInstance(4, validPackFile.getName());

        // Use Reflection to prepare for accessing the private 'players' fields of the CardGame instance
        Field players = cardGame.getClass().getDeclaredField("players");
        players.setAccessible(true);

        // Choosing player that wins
        Player[] cardGamePlayers = (Player[]) players.get(cardGame);
        Player winningPlayer = cardGamePlayers[0];

        // Calling endGame function
        cardGame.endGame(winningPlayer);

        // Check game is running or not
        assertFalse(cardGame.isGameRunning());
        // Check internal winner variable within Card Game instance
        Field winner = cardGame.getClass().getDeclaredField("winner");
        winner.setAccessible(true);
        assertEquals(winningPlayer, (Player)winner.get(cardGame));
        // Check log output for non-winning players
        for (Player player : cardGamePlayers) {
            if (winningPlayer == player) {
                // move on to the next Player object
                continue;
            }
            // Check for the presence of the log message within the Player object
            Field logLines = player.getClass().getDeclaredField("logLines");
            logLines.setAccessible(true);

            ArrayList<String> lines = (ArrayList<String>) logLines.get(player);
            assertTrue(lines.contains("player " + winningPlayer.playerNo + " has informed player " + player.playerNo + " that player " + winningPlayer.playerNo +  " has won"));
        }

        // Cleanup
        validPackFile.delete();
    }

    @Test
    public void testInitDecks() throws IOException, NoSuchMethodException, InvocationTargetException, IllegalAccessException, NoSuchFieldException {
        // Create a valid pack file for testing
        int playerCount = 4;
        File validPackFile = CardGameTest.createValidTestPackFile("test_valid_pack.txt", playerCount);

        // Create CardGame instance
        CardGame cardGame = CardGame.newInstance(4, "test_valid_pack.txt");

        // Use Reflection to access the private 'decks' field of CardGame, and reset it to null (CardGame's constructor calls the initDecks method, so we must reverse its actions)
        Field decks = CardGame.class.getDeclaredField("decks");
        decks.setAccessible(true);
        decks.set(cardGame, null);

        // Use Reflection to access the private 'pack' field of CardGame, and reset it to null (CardGame's constructor calls the initDecks method, so we must reverse its actions)
        Field pack = CardGame.class.getDeclaredField("pack");
        pack.setAccessible(true);
        pack.set(cardGame, new Deck());

        // Use Reflection to access the private 'loadPackFromFile' method of CardGame, and invoke it
        Method loadPackFromFile = CardGame.class.getDeclaredMethod("loadPackFromFile", String.class, int.class);
        loadPackFromFile.setAccessible(true);
        loadPackFromFile.invoke(cardGame, "test_valid_pack.txt", playerCount);

        // Use Reflection to prepare to access the private 'initDecks' method of CardGame
        Method initDecks = CardGame.class.getDeclaredMethod("initDecks");
        initDecks.setAccessible(true);

        Deck cardGamePack = (Deck) pack.get(cardGame);
        int cardsInPackBeforeInitDecksInvoked = cardGamePack.size();

        // Invoke private cardGame.initDecks method
        initDecks.invoke(cardGame);

        int cardsInPackAfterInitDecksInvoked = ((Deck) pack.get(cardGame)).size();

        // After invoking initDecks, there should be (4*playerCount) cards left in the pack
        assertEquals(playerCount * 4, cardsInPackBeforeInitDecksInvoked - cardsInPackAfterInitDecksInvoked);

        // Use Reflection to get current value of cardGame.decks
        Deck[] cardGameDecks = (Deck[]) decks.get(cardGame);

        // The number of decks should be 4 (equal to number of players)
        assertEquals(4, cardGameDecks.length);

        // Check that each deck has been assigned the correct deck number
        for (int i = 0; i < cardGameDecks.length; i++) {
            assertEquals(i + 1, cardGameDecks[i].getDeckNo());
        }

        // Cleanup
        validPackFile.delete();
    }

    @Test
    public void testInitPlayers() throws IOException, NoSuchMethodException, InvocationTargetException, IllegalAccessException, NoSuchFieldException {
        // Create a valid pack file for testing
        int playerCount = 4;
        File validPackFile = CardGameTest.createValidTestPackFile("test_valid_pack.txt", playerCount);

        // Create CardGame instance
        CardGame cardGame = CardGame.newInstance(playerCount, "test_valid_pack.txt");

        // Use Reflection to access the private 'decks' field of CardGame, and reset it to null (CardGame's constructor calls the initPlayers method, so we must reverse its actions)
        Field decks = CardGame.class.getDeclaredField("decks");
        decks.setAccessible(true);
        decks.set(cardGame, null);

        // Use Reflection to access the private 'pack' field of CardGame, and reset it to null (CardGame's constructor calls the initPlayers method, so we must reverse its actions)
        Field pack = CardGame.class.getDeclaredField("pack");
        pack.setAccessible(true);
        pack.set(cardGame, new Deck());

        // Use Reflection to call loadPackFromFile
        Method loadPackFromFile = CardGame.class.getDeclaredMethod("loadPackFromFile", String.class, int.class);
        loadPackFromFile.setAccessible(true);
        loadPackFromFile.invoke(cardGame, "test_valid_pack.txt", playerCount);

        // Use Reflection to prepare to access the private 'initDecks' method of CardGame
        Method initDecks = CardGame.class.getDeclaredMethod("initDecks");
        initDecks.setAccessible(true);
        initDecks.invoke(cardGame);

        int cardsInPackBeforeInitPlayersInvoked = ((Deck) pack.get(cardGame)).size();

        // Use Reflection to access the private 'initPlayers' method of CardGame, and invoke it
        Method initPlayers = CardGame.class.getDeclaredMethod("initPlayers");
        initPlayers.setAccessible(true);
        initPlayers.invoke(cardGame);

        int cardsInPackAfterInitPlayersInvoked = ((Deck) pack.get(cardGame)).size();

        // After the initPlayers method has completed, there should be (4*playerCount) fewer cards in the pack, as each player should be dealt 4 cards.
        assertEquals(4 * playerCount, cardsInPackBeforeInitPlayersInvoked - cardsInPackAfterInitPlayersInvoked);

        // Using Reflection to access players array within CardGame instance
        Field players = CardGame.class.getDeclaredField("players");
        players.setAccessible(true);

        // After invoking initPlayers, ensure players array contains the right number of Player objects
        assertEquals(4, ((Player[])(players.get(cardGame))).length);

        // Ensuring size of player hands is right
        for (Player player : ((Player[])(players.get(cardGame)))) {
            // Use Reflection to get player's hand
            Field hand = Player.class.getDeclaredField("hand");
            hand.setAccessible(true);

            assertEquals(4, ((ArrayList<Card>)(hand.get(player))).size());
        }

        // Ensuring that each player has the correct draw and discard decks.
        // Additionally check that each player has the correct player number
        Player[] playerObjects = ((Player[])(players.get(cardGame)));
        int lastPlayerIndex = playerObjects.length - 1;
        for (int i = 0; i < playerObjects.length; i++) {
            // The correct player number for the n-th player is n (i.e. i + 1)
            assertEquals(i + 1, playerObjects[i].playerNo);

            // Use Reflection to get player's draw and discard decks
            Field drawDeck = Player.class.getDeclaredField("drawDeck");
            drawDeck.setAccessible(true);
            Deck playerDrawDeck = (Deck) drawDeck.get(playerObjects[i]);

            Field discardDeck = Player.class.getDeclaredField("discardDeck");
            discardDeck.setAccessible(true);
            Deck playerDiscardDeck = (Deck) discardDeck.get(playerObjects[i]);

            if (i != lastPlayerIndex) {
                // For players 1..(n-1), the draw deck number should match the player number, and the discard deck number should match that next player's draw deck
                assertEquals(playerObjects[i].playerNo, playerDrawDeck.getDeckNo());
                assertEquals(playerObjects[i].playerNo + 1, playerDiscardDeck.getDeckNo());
            } else {
                // For player n, the draw deck number should be that player's number, and their discard deck should be the draw deck of the first player
                assertEquals(playerObjects[i].playerNo, playerDrawDeck.getDeckNo());
                assertEquals(1, playerDiscardDeck.getDeckNo());
            }

        }

        // Cleanup
        validPackFile.delete();

    }
}
