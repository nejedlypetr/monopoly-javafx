package cz.cvut.fel.pvj.nejedly.monopoly.model.board.squares;

import cz.cvut.fel.pvj.nejedly.monopoly.model.player.Player;

import java.util.logging.Logger;

public class Property extends Square implements Ownable {
    private final static Logger LOGGER = Logger.getLogger(Property.class.getName());
    private final PropertyGroup group;
    private final int rent;
    private Player owner;
    private final int purchasePrice;

    public Property(String name, int position, PropertyGroup group, int rent, int purchasePrice) {
        super(name, position);
        this.group = group;
        this.rent = rent;
        this.purchasePrice = purchasePrice;
        this.owner = null;
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

    public Player getOwner() {
        return owner;
    }

    public PropertyGroup getGroup() {
        return group;
    }

    @Override
    public String toString() {
        String ownerText = (owner == null) ? "---" : owner.getName();
        return getName() + System.lineSeparator() + System.lineSeparator() +
                "Owner: " + ownerText + System.lineSeparator() +
                "Rent: $" + rent + System.lineSeparator() +
                "Purchase price: $" + purchasePrice + System.lineSeparator() +
                "Group: " + group;
    }

    @Override
    public void setOwner(Player player) {
        owner = player;

        if (player == null) LOGGER.info(this.getName() + " set owner: null");
        else LOGGER.info(this.getName() + " set owner: " + player.getName());
    }
}
