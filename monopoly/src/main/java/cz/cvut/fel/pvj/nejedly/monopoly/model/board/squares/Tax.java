package cz.cvut.fel.pvj.nejedly.monopoly.model.board.squares;

public class Tax extends Square {
    private final int tax;

    public Tax(String name, int position, int tax) {
        super(name, position);
        this.tax = tax;
    }

    public int getTax() {
        return tax;
    }
}
