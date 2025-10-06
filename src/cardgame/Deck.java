package cardgame;

import java.util.NoSuchElementException;

/**
 * Class representing deck of cards.
 */
public class Deck {
    /**
     * Queue used to store {@link Card} objects in deck.
     */
    private Queue<Card> cards;

    public Deck() {
        this.cards = new Queue<Card>();
    }

    /**
     * Inserts new {@link Card} into cards {@link Queue}.
     * @param card {@link Card} to be inserted to the bottom of the deck.
     */
    public void insert(Card card) {
        this.cards.enqueue(card);
    }

    /**
     * Draw {@link Card} from the top of the deck.
     * @return {@link Card} drawn from the top of the deck.
     * @throws NoSuchElementException if the deck is empty.
     */
    public Card draw() throws NoSuchElementException {
        try {
            return this.cards.dequeue();
        } catch (NoSuchElementException e){
            throw e;
        }

    }
}
