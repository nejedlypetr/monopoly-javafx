package cz.cvut.fel.pvj.nejedly.monopoly.model;

import java.util.Random;

public class ChanceDeck {
    private static int currentTopCardIndex = 0;
    private static final Card[] cards = {
        new Card(CardType.CHANCE, "Dummy card1"),
        new Card(CardType.CHANCE, "Dummy card2"),
        new Card(CardType.CHANCE, "Dummy card3"),
        new Card(CardType.CHANCE, "Dummy card4"),
    };

    private ChanceDeck() {}

    public static Card[] shuffle() {
        Random random = new Random();
        for (int i = cards.length; i > 1; i--) {
            swap(i - 1, random.nextInt(i));
        }
        return cards;
    }

    public static Card drawCard() {
        Card topCard = cards[currentTopCardIndex];
        currentTopCardIndex++;
        if (currentTopCardIndex == cards.length) currentTopCardIndex = 0;
        return topCard;
    }

    private static void swap(int i, int j) {
        Card temp = cards[i];
        cards[i] = cards[j];
        cards[j] = temp;
    }
}
