package cz.cvut.fel.pvj.nejedly.monopoly.model.board.squares;

public abstract class Square {
    private String name;
    private int position;
    public abstract boolean isOwnable();

    public Square(String name, int position) {
        this.name = name;
        this.position = position;
    }

    @Override
    public String toString() {
        return name + " (board position: " + position + ")";
    }
}
