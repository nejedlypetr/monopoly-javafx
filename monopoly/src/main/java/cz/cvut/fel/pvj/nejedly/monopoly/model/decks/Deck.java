package cz.cvut.fel.pvj.nejedly.monopoly.model.decks;

import cz.cvut.fel.pvj.nejedly.monopoly.model.decks.cards.Card;

import java.util.Random;
import java.util.logging.Logger;

public class Deck {
    private final static Logger LOGGER = Logger.getLogger(Deck.class.getName());
    private int currentTopCardIndex;
    private final Card[] cards;

    public Deck(Card[] cards) {
        this.cards = cards;
        currentTopCardIndex = 0;
    }

    public Card[] shuffle() {
        LOGGER.info("Shuffle deck.");

        Random random = new Random();
        for (int i = cards.length; i > 1; i--) {
            swap(i - 1, random.nextInt(i));
        }
        return cards;
    }

    public Card drawCard() {
        LOGGER.info("Draw card.");

        Card topCard = cards[currentTopCardIndex];
        currentTopCardIndex++;
        if (currentTopCardIndex == cards.length) currentTopCardIndex = 0;
        return topCard;
    }

    private void swap(int i, int j) {
        Card temp = cards[i];
        cards[i] = cards[j];
        cards[j] = temp;
    }
}
