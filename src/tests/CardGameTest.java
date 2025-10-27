package tests;

import cardgame.CardGame;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class CardGameTest {

    @Test
    public void testIsPackValid_forValidInput() throws IOException {
        // Create a valid pack file for the purposes of testing...
        File validPackFile = new File("test_valid_pack.txt");
        validPackFile.createNewFile();

        // Construct contents of test valid pack file
        int playerCount = 4;
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

    @Test
    public void testIsPackValid_forInputWithInvalidRanksAndValidCardCount() throws IOException {
        // Create an invalid pack file for the purposes of testing...
        File invalidPackFile = new File("test_invalid_pack.txt");
        invalidPackFile.createNewFile();

        // Construct contents of test invalid pack file
        int playerCount = 4;
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
}
