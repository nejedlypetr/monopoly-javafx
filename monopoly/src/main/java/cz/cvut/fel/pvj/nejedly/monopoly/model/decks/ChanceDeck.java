package cz.cvut.fel.pvj.nejedly.monopoly.model.decks;

public class ChanceDeck extends Deck {

    public ChanceDeck() {
        super(
            new Card[] {
                new Card(CardType.CHANCE, "Dummy card1"),
                new Card(CardType.CHANCE, "Dummy card2"),
                new Card(CardType.CHANCE, "Dummy card3"),
                new Card(CardType.CHANCE, "Dummy card4"),
            }
        );
    }
}
