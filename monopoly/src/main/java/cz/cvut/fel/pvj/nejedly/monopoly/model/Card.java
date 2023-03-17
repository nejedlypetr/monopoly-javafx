package cz.cvut.fel.pvj.nejedly.monopoly.model;

public class Card {
    private final CardType cardType;
    private final String text;

    public Card(CardType cardType, String text) {
        this.cardType = cardType;
        this.text = text;
    }

    @Override
    public String toString() {
        return cardType + " card - " + text;
    }
}
