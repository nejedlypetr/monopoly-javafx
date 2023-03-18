package cz.cvut.fel.pvj.nejedly.monopoly.model.board.squares;

public class GoToJail extends Square {
    private final int jailPosition;

    public GoToJail(int position, int jailPosition) {
        super("Go To Jail", position);
        this.jailPosition = jailPosition;
    }

    public int getJailPosition() {
        return jailPosition;
    }

    @Override
    boolean isOwnable() {
        return false;
    }
}
