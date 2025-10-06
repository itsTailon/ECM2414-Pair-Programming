package cardgame;

/**
 * Represents a card with which the game is played.
 */
public class Card {
    /**
     * The 'rank' (i.e. value) of the card.
     *
     * Must be non-negative.
     */
    private int rank;

    public Card(int rank) {
        // Ensure that the rank is non-negative
        if (rank < 0) {
            throw new IllegalArgumentException("Card rank cannot be a negative.");
        }

        this.rank = rank;
    }

    /**
     * Returns the rank (i.e. value) of the card.
     * @return The rank of the card.
     */
    public int getRank() {
        return rank;
    }
}
