package cz.cvut.fel.pvj.nejedly.monopoly.model.decks.cards;

import cz.cvut.fel.pvj.nejedly.monopoly.model.board.squares.Square;

public class MoveToCard extends Card {
    private final Square square;

    public MoveToCard(CardType cardType, String text, Square square) {
        super(cardType, text);
        this.square = square;
    }

    public Square getSquare() {
        return square;
    }
}
