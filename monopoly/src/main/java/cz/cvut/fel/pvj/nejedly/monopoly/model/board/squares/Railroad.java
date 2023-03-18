package cz.cvut.fel.pvj.nejedly.monopoly.model.board.squares;

import cz.cvut.fel.pvj.nejedly.monopoly.model.Player;

public class Railroad extends Square implements Ownable {
    private final int rent;
    private Player owner;
    private final int purchasePrice;

    public Railroad(String name, int position) {
        super(name, position);
        this.rent = 200;
        this.purchasePrice = 200;
        this.owner = null;
    }

    @Override
    public boolean isOwnable() {
        return true;
    }

    public void setOwner(Player owner) {
        this.owner = owner;
    }

    public Player getOwner() {
        return owner;
    }

    @Override
    public int getRent() {
        return rent;
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
}
