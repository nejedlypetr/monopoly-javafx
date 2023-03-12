package cz.cvut.fel.pvj.nejedly.monopoly.model;

public class Cards extends Square {

    public Cards(String name, int position) {
        super(name, position);
    }

    @Override
    boolean isOwnable() {
        return false;
    }
}
