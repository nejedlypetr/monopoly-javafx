package cz.cvut.fel.pvj.nejedly.monopoly.model.board.squares;

import cz.cvut.fel.pvj.nejedly.monopoly.model.player.Player;
import cz.cvut.fel.pvj.nejedly.monopoly.model.die.Die;

import java.util.logging.Logger;

public class Utility extends Square implements Ownable {
    private final static Logger LOGGER = Logger.getLogger(Utility.class.getName());
    private final int purchasePrice;
    private Player owner;

    public Utility(String name, int position) {
        super(name, position);
        this.purchasePrice = 150;
        this.owner = null;
    }

    @Override
    public int getRent() {
        LOGGER.warning("Method not supported without arguments.");
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

    public Player getOwner() {
        return owner;
    }

    @Override
    public void setOwner(Player player) {
        owner = player;

        if (player == null) LOGGER.info(this.getName() + " set owner: null");
        else LOGGER.info(this.getName() + " set owner: " + player.getName());
    }

    @Override
    public String toString() {
        String ownerText = (owner == null) ? "---" : owner.getName();
        return getName() + System.lineSeparator() + System.lineSeparator() +
                "Owner: " + ownerText + System.lineSeparator() +
                "Rent 1: $(dice * 4)" + System.lineSeparator() +
                "Rent 2: $(dice * 10)" + System.lineSeparator() +
                "Purchase price: $" + purchasePrice;
    }
}
