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

    /**
     * Calculates the number of steps the {@link Player} needs to take to reach the current {@link Square}.
     *
     * @param player the {@link Player} who is moving
     * @param board the game {@link Board}
     * @return the number of steps the {@link Player} needs to take to reach the current {@link Square}
     */
    public int getSteps(Player player, Board board) {
        int steps = square.getPosition() - player.getBoardPosition().getValue();
        if (steps < 0) {
            steps += board.getBoardSquares().length;
        }
        return steps;
    }
}
