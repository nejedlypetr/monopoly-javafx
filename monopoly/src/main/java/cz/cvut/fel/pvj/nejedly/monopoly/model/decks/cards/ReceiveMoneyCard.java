package cz.cvut.fel.pvj.nejedly.monopoly.model.decks.cards;

import cz.cvut.fel.pvj.nejedly.monopoly.model.player.Player;

import java.util.ArrayList;

public class ReceiveMoneyCard extends Card {
    private final int amount;
    private final boolean fromEachPlayer;

    public ReceiveMoneyCard(CardType cardType, String text, int amount) {
        super(cardType, text);
        this.amount = amount;
        fromEachPlayer = false;
    }

    public ReceiveMoneyCard(CardType cardType, String text, int amount, boolean fromEachPlayer) {
        super(cardType, text);
        this.amount = amount;
        this.fromEachPlayer = fromEachPlayer;
    }

    public void execute(Player player, ArrayList<Player> players) {
        if (!fromEachPlayer) {
            player.changeMoneyBalanceBy(amount);
            return;
        }

        // receive money from every player
        for (Player p : players) {
            if (p.equals(player) || p.isBankrupt().getValue()) continue;

            p.changeMoneyBalanceBy(-amount);
            player.changeMoneyBalanceBy(amount);
        }
    }
}