package cz.cvut.fel.pvj.nejedly.monopoly.model.decks.cards;

import cz.cvut.fel.pvj.nejedly.monopoly.model.board.Board;
import cz.cvut.fel.pvj.nejedly.monopoly.model.board.squares.Square;
import cz.cvut.fel.pvj.nejedly.monopoly.model.player.Player;

public class MoveToCard extends Card {
    private final Square square;

    public MoveToCard(CardType cardType, String text, Square square) {
        super(cardType, text);
        this.square = square;
    }

    public Square getSquare() {
        return square;
    }

    public int getSteps(Player player, Board board) {
        int steps = square.getPosition() - player.getBoardPosition().getValue();
        if (steps < 0) {
            steps += board.getBoardSquares().length;
        }
        return steps;
    }
}
