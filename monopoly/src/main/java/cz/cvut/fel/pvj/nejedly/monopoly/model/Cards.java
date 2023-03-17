package cz.cvut.fel.pvj.nejedly.monopoly.model;

public class Cards extends Square {
    private final CardType cardType;

    public Cards(String name, int position, CardType cardType) {
        super(name, position);
        this.cardType = cardType;
    }

    @Override
    boolean isOwnable() {
        return false;
    }
}
