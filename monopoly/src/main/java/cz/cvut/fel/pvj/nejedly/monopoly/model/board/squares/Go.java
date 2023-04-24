package cz.cvut.fel.pvj.nejedly.monopoly.model.board.squares;

public class Go extends Square {
    private final int salary;
    public Go(int position, int salary) {
        super("Go", position);
        this.salary = salary;
    }
}
