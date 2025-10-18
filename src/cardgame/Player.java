package cardgame;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Player implements Runnable {
    private final int preferredRank;
    private Deck drawDeck;
    private Deck discardDeck;
    private ArrayList<Card> hand;
    private ArrayList<String> logLines;

    public Player(Deck drawDeck, Deck discardDeck, int preferredRank) {
        this.drawDeck = drawDeck;
        this.discardDeck = discardDeck;
        this.preferredRank = preferredRank;
        this.hand = new ArrayList<Card>();
        this.logLines = new ArrayList<String>();
    }

    @Override
    public void run() {}

    public void play() {

    }

    public void drawAndDiscard() {


    }

    /**
     * Saving log contents to log file.
     * @param filename name of file to store log information on.
     */
    public void saveLogFile(String filename) {
        try {
            // Create new file log if it doesn't exist.
            File logFile = new File(filename);
            logFile.createNewFile();

            // Iterate through log lines and add to output String to be written to log.
            String logOutput = "";
            for (String line : logLines) {
                logOutput += line + "\n";
            }

            // Writing to the log file.
            FileWriter fileWriter = new FileWriter(logFile);
            fileWriter.write(logOutput);
            fileWriter.close();

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Adding a log message to logLines array.
     * @param logMessage Message to be passed to logLines array.
     */
    public void log(String logMessage) {
        this.logLines.add(logMessage);
    }

}
