package cz.cvut.fel.pvj.nejedly.monopoly.model;

public class Tax extends Square {
    private final int tax;

    public Tax(String name, int position, int tax) {
        super(name, position);
        this.tax = tax;
    }

    @Override
    boolean isOwnable() {
        return false;
    }

    public int getTax() {
        return tax;
    }
}
