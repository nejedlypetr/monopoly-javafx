package cz.cvut.fel.pvj.nejedly.monopoly.model.board.squares;

public abstract class Square {
    private final String name;
    private final int boardPosition;

    public Square(String name, int position) {
        this.name = name;
        this.boardPosition = position;
    }

    @Override
    public String toString() {
        return name;
    }

    public int getPosition() {
        return boardPosition;
    }

    public String getName() {
        return name;
    }
}
