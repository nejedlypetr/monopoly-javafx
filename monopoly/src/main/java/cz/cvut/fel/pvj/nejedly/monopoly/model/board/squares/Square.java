package cz.cvut.fel.pvj.nejedly.monopoly.model.board.squares;

public abstract class Square {
    private String name;
    private int position;
    abstract boolean isOwnable();

    public Square(String name, int position) {
        this.name = name;
        this.position = position;
    }
}
