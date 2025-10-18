package tests;

import cardgame.Deck;
import cardgame.Player;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class PlayerTest {

    @Test
    public void testSaveLogFile() throws FileNotFoundException {
        Player p = new Player(new Deck(), new Deck());

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
}
