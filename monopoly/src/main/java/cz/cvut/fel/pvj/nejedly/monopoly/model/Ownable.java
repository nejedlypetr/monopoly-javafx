package cz.cvut.fel.pvj.nejedly.monopoly.model;

public interface Ownable {
    int getRent();
    boolean isOwned();
    int getPurchasePrice();
    boolean isHabitable();
}
