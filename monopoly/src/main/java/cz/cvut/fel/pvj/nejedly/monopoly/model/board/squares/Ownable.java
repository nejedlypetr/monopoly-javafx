package cz.cvut.fel.pvj.nejedly.monopoly.model.board.squares;

import cz.cvut.fel.pvj.nejedly.monopoly.model.player.Player;

public interface Ownable {
    int getRent();
    boolean isOwned();
    int getPurchasePrice();
    Player getOwner();
    void setOwner(Player player);
    boolean isHabitable();
}
