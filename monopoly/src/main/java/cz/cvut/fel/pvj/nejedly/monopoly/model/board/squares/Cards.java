package cz.cvut.fel.pvj.nejedly.monopoly.model.board.squares;

import cz.cvut.fel.pvj.nejedly.monopoly.model.decks.CardType;

public class Cards extends Square {
    private final CardType cardType;

    public Cards(String name, int position, CardType cardType) {
        super(name, position);
        this.cardType = cardType;
    }

    @Override
    public boolean isOwnable() {
        return false;
    }
}
