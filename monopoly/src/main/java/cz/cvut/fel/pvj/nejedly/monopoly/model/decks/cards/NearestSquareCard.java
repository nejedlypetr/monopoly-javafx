package cz.cvut.fel.pvj.nejedly.monopoly.model.decks.cards;

import cz.cvut.fel.pvj.nejedly.monopoly.model.board.Board;
import cz.cvut.fel.pvj.nejedly.monopoly.model.board.squares.Square;
import cz.cvut.fel.pvj.nejedly.monopoly.model.player.Player;

public class NearestSquareCard extends Card {
    private final String squareType;

    public NearestSquareCard(CardType cardType, String text, String squareType) {
        super(cardType, text);
        this.squareType = squareType;
    }

    public int getSteps(Player player, Board board) {
        int playerBoardPosition = player.getBoardPosition().getValue();
        Square square = board.getNearestSquareOfType(playerBoardPosition, squareType);

        int steps = square.getPosition() - playerBoardPosition;
        if (steps < 0) {
            steps += board.getBoardSquares().length;
        }

        return steps;
    }
}
