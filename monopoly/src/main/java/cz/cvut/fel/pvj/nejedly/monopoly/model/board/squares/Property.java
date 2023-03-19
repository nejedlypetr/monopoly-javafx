package cz.cvut.fel.pvj.nejedly.monopoly.model.board.squares;

import cz.cvut.fel.pvj.nejedly.monopoly.model.player.Player;

public class Property extends Square implements Ownable, Habitable {
    private final PropertyGroup group;
    private final int rent;
    private Player owner;
    private int purchasePrice;

    public Property(String name, int position, PropertyGroup group, int rent, int purchasePrice) {
        super(name, position);
        this.group = group;
        this.rent = rent;
        this.purchasePrice = purchasePrice;
        this.owner = null;
    }

    @Override
    public boolean isOwnable() {
        return true;
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

    public void setOwner(Player player) {
        owner = player;
    }

    public Player getOwner() {
        return owner;
    }

    public PropertyGroup getGroup() {
        return group;
    }

    @Override
    public boolean isHabitable() {
        return true;
    }
}
