package cz.cvut.fel.pvj.nejedly.monopoly.model.board.squares;

import cz.cvut.fel.pvj.nejedly.monopoly.model.player.Player;
import cz.cvut.fel.pvj.nejedly.monopoly.model.die.Die;

public class Utility extends Square implements Ownable {
    private final int purchasePrice;
    private Player owner;

    public Utility(String name, int position) {
        super(name, position);
        this.purchasePrice = 150;
        this.owner = null;
    }

    @Override
    public int getRent() {
        throw new UnsupportedOperationException("Method not supported without arguments.");
    }

    public int getRent(Die die) {
        die.roll();
        if (owner.getNumberOfOwnedUtilities() > 1) {
            return 10 * die.getDieRollTotal();
        }
        return 4 * die.getDieRollTotal();
    }

    @Override
    public boolean isOwned() {
        return owner != null;
    }

    @Override
    public int getPurchasePrice() {
        return purchasePrice;
    }

    @Override
    public boolean isHabitable() {
        return false;
    }

    @Override
    public boolean isOwnable() {
        return true;
    }

    public Player getOwner() {
        return owner;
    }

    public void setOwner(Player owner) {
        this.owner = owner;
    }
}
