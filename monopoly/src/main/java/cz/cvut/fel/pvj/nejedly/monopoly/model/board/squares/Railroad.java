package cz.cvut.fel.pvj.nejedly.monopoly.model.board.squares;

import cz.cvut.fel.pvj.nejedly.monopoly.model.player.Player;

import java.util.logging.Logger;

public class Railroad extends Square implements Ownable {
    private final static Logger LOGGER = Logger.getLogger(Railroad.class.getName());
    private int rent = 0;
    private Player owner;
    private final int purchasePrice;

    public Railroad(String name, int position, int rent) {
        super(name, position);
        this.rent = rent;
        this.purchasePrice = 200;
        this.owner = null;
    }

    @Override
    public void setOwner(Player player) {
        owner = player;

        if (player == null) LOGGER.info(this.getName() + " set owner: null");
        else LOGGER.info(this.getName() + " set owner: " + player.getName());
    }

    public Player getOwner() {
        return owner;
    }

    private int calculateRent(int numberOfOwnedRailroads) {
        return switch (numberOfOwnedRailroads) {
            case 2 -> (rent * 2);
            case 3 -> (rent * 4);
            case 4 -> (rent * 8);
            default -> rent;
        };
    }

    @Override
    public int getRent() {
        return calculateRent(owner.getNumberOfOwnedRailroads());
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
    public String toString() {
        String ownerText = (owner == null) ? "---" : owner.getName();
        return getName() + System.lineSeparator() + System.lineSeparator() +
                "Owner: " + ownerText + System.lineSeparator() +
                "Rent 1: $" + calculateRent(1) + System.lineSeparator() +
                "Rent 2: $" + calculateRent(2) + System.lineSeparator() +
                "Rent 3: $" + calculateRent(3) + System.lineSeparator() +
                "Rent 4: $" + calculateRent(4) + System.lineSeparator() +
                "Purchase price: $" + purchasePrice;
    }
}
