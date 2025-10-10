package cardgame;

import java.io.File;
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

        this.loadPackFromFile(packFilename);
        this.initDecks();
        this.initPlayers();
    }

    public void run() {

    }

    private void loadPackFromFile(String filename) {

    }

    private void initDecks() {

    }

    private void initPlayers() {

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

            // Check if the input given is a valid path to a file that exists
            packFilename = (new File(input).exists()) ? input : null;

        } while (packFilename == null);

        // Create CardGame instance, and begin gameplay.
        CardGame gameInstance = new CardGame(playerCount, packFilename);
        gameInstance.run();
    }
}
