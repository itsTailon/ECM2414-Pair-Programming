package cardgame;

import java.io.*;
import java.nio.file.Files;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * A class representing the card game.
 */
public class CardGame {
    private Player[] players;
    private Deck[] decks;
    private Deck pack;

    public CardGame(int playerCount, String packFilename) {
        this.players = new Player[playerCount];

        this.loadPackFromFile(packFilename, playerCount);
        this.initDecks();
        this.initPlayers();
    }

    public void run() {

    }

    /**
     * Loads the contents of a pack file into the 'pack' property
     * @param filename The filename of the pack to be loaded
     * @param playerCount The number of players
     * @return True, if the pack file was valid and could be loaded. Otherwise, false.
     */
    private boolean loadPackFromFile(String filename, int playerCount) {
        if (!CardGame.isPackValid(filename, playerCount)) {
            return false;
        }

        try {
            BufferedReader br = new BufferedReader(new FileReader(filename));

            String line = br.readLine();
            while (line != null) {
                Card card = new Card(Integer.parseInt(line));
                this.pack.insert(card);

                // Read next line
                line = br.readLine();
            }

        } catch (IOException e) {
            return false;
        }

        return true;
    }

    /**
     * Validating the pack text file for the game
     * @param filename name given to pack text file
     * @param playerCount number of players for the game
     * @return true if the pack is valid, false if the pack is invalid or could not be read
     */
    public static boolean isPackValid(String filename, int playerCount) {
        // Check a pack file exists
        if (!(new File(filename)).exists()) {
            return false;
        }

        // Checking line count is equivalent to player count
        int lineCounter = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line = br.readLine();
            while (line != null) {
                try {
                    // Ensuring line content is integer
                    Integer lineValue = Integer.parseInt(line);

                    // Checking if value is non-negative
                    if (lineValue < 0) {
                        return false;
                    }
                } catch (NumberFormatException e) {
                    // Return false as value not an integer
                    return false;
                }

                lineCounter++;
                line = br.readLine();
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }

        return (lineCounter == (8*playerCount));
    }

    /**
     * Initialising each player's deck.
     */
    private void initDecks() {
        // Create an array to hold decks for each player.
        this.decks = new Deck[this.players.length];

        // Initialise each player's deck, populating by drawing cards from the pack.
        for (int i = 0; i < this.players.length; i++) {
            this.decks[i] = new Deck();
            for (int j = 0; j < 8; j++) { // The pack has size 8n, leaving 8 cards for each player.
                this.decks[i].insert(this.pack.draw());
            }
        }
    }

    private void initPlayers() {
        // Create the array holding each Player object.
        this.players = new Player[this.players.length];

        for (int i = 0; i < this.players.length; i++) {
            // If the last player to initialise, assign discard-deck as draw-deck of the first player.
            if (i == this.players.length - 1) {
                this.players[i] = new Player(this.decks[i], this.decks[0]);
            } else {
                this.players[i] = new Player(this.decks[i], this.decks[i+1]);
            }
        }
    }

    public static void main(String[] args) {
        // Initialise variables for player count and pack filepath to 'empty' values
        int playerCount = 0;
        String packFilename = null;

        // Initialise a Scanner to be used to collect user input from the console.
        Scanner scanner = new Scanner(System.in);

        // Repeatedly ask for user input until valid input is received
        do {
            try {
                // Prompt user for, and collect, the number of players to be participating in the game.
                System.out.print("Please enter the number of players: ");
                playerCount = Integer.valueOf(scanner.nextLine());
            } catch (NumberFormatException e) {
                continue;
            }
        } while (playerCount < 2);

        // Repeatedly ask for user input until valid input is received
        do {
            // Ask for, and collect, user input for the filepath/filename of the pack to load
            System.out.print("Please enter location of pack to load: ");
            String input = scanner.nextLine();

            // Validating user input for the pack
            packFilename = (CardGame.isPackValid(input, playerCount)) ? input : null;

        } while (packFilename == null);

        // Create CardGame instance, and begin gameplay.
        CardGame gameInstance = new CardGame(playerCount, packFilename);
        gameInstance.run();
    }
}
