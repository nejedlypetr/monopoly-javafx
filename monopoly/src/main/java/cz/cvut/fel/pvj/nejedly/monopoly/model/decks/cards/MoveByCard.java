package cz.cvut.fel.pvj.nejedly.monopoly.model.decks.cards;

public class MoveByCard extends Card {
    private final int steps;
    public MoveByCard(CardType cardType, String text, int steps) {
        super(cardType, text);
        this.steps = steps;
    }

    public int getSteps() {
        return steps;
    }
}
