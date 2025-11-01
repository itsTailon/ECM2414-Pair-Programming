package cardgame;

import java.io.*;
import java.nio.file.Files;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * A class representing the card game.
 */
public class CardGame {
    private static CardGame singletonInstance;

    private Player[] players;
    private Deck[] decks;
    private Deck pack;

    public volatile Player winner = null;

    private volatile boolean isGameRunning = true;

    private CardGame(int playerCount, String packFilename) {
        this.players = new Player[playerCount];

        this.decks = new Deck[playerCount];
        this.pack = new Deck();

        this.loadPackFromFile(packFilename, playerCount);
        this.initDecks();
        this.initPlayers();
    }

    public static CardGame newInstance(int playerCount, String packFilename) {
        CardGame.singletonInstance = new CardGame(playerCount, packFilename);
        return CardGame.singletonInstance;
    }

    public static CardGame getInstance() {
        return CardGame.singletonInstance;
    }

    public boolean isGameRunning() {
        return this.isGameRunning;
    }

    public void run() {

    }

    /**
     * Ending game as winner has been set
     * @param winner Player object of winning player
     */
    public synchronized void endGame(Player winner) {
        // Update winner of game
        if (this.winner == null) {
            this.winner = winner;
            this.isGameRunning = false;

            // logging winning player for all other players
            for (Player player : this.players) {
                // Ensure player isn't winner (to avoid re-logging)
                if (player == winner) {
                    continue;
                }
                // Adding log of other player's win
                player.log("player " + winner.playerNo + " has informed player " + player.playerNo + " that player " + winner.playerNo +  " has won");
            }
        }
        // Ignore as winner has early been established
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

        // Initialise each player's deck.
        for (int i = 0; i < this.players.length; i++) {
            this.decks[i] = new Deck();
            this.decks[i].setDeckNo(i+1);
        }

        // Distributing cards to each deck in a round-robin fashion.
       for (int j = 0; j < 4; j++) {
           // Iterate through players, adding a card to their deck.
           for (int i = 0; i < this.players.length; i++) {
               // Checking if pack is out of cards
               if (this.pack.size() == 0) {
                   break;
               }
               // Adding card to player's deck
               this.decks[i].insert(this.pack.draw());
           }
       }
    }


    private void initPlayers() {
        // Create the array holding each Player object.
        this.players = new Player[this.players.length];
        // Initialise decks and establish ownership.
        for (int i = 0; i < this.players.length; i++) {
            // If the last player to initialise, assign discard-deck as draw-deck of the first player.
            if (i == this.players.length - 1) {
                // Players numbered 1...n so the preferred card rank is i+1.
                this.players[i] = new Player(this.decks[i], this.decks[0], i+1);
            } else {
                this.players[i] = new Player(this.decks[i], this.decks[i+1], i+1);
            }
        }

        // iteration count - number of round-robin cycles.
        int iterationCount = 0;

        // Distributing cards to each hand in a round-robin fashion.
        while (iterationCount < 4) {
            // Iterate through players, adding a card to their hand from pack.
            for (int i = 0; i < this.players.length; i++) {
                // Adding card to player's deck
                this.players[i].insertIntoHand(this.pack.draw());
            }
            iterationCount++;
        }
    }

    public static void main(String[] args) {
        // TODO: Change logic to reduce actions taken in main to simplify unit testing output.
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
        CardGame gameInstance = CardGame.newInstance(playerCount, packFilename);
        gameInstance.run();
    }
}
