package cz.cvut.fel.pvj.nejedly.monopoly.model.decks.cards;

public class NearestSquareCard extends Card {
    private final Class<?> squareType;

    public NearestSquareCard(CardType cardType, String text, Class<?> squareType) {
        super(cardType, text);
        this.squareType = squareType;
    }

    public Class<?> getSquareType() {
        return squareType;
    }
}
