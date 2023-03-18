package cz.cvut.fel.pvj.nejedly.monopoly.model.board.squares;

public class Jail extends Square {

    public Jail(String name, int position) {
        super(name, position);
    }

    @Override
    public boolean isOwnable() {
        return false;
    }
}
