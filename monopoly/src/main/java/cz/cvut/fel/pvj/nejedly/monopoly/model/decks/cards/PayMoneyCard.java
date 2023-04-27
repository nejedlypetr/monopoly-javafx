package cz.cvut.fel.pvj.nejedly.monopoly.model.decks.cards;

import cz.cvut.fel.pvj.nejedly.monopoly.model.player.Player;

import java.util.ArrayList;

public class PayMoneyCard extends Card {
    private final int amount;
    private final boolean perPlayer;

    public PayMoneyCard(CardType cardType, String text, int amount) {
        super(cardType, text);
        this.amount = amount;
        perPlayer = false;
    }

    public PayMoneyCard(CardType cardType, String text, int amount, boolean perPlayer) {
        super(cardType, text);
        this.amount = amount;
        this.perPlayer = perPlayer;
    }

    public void execute(Player player, ArrayList<Player> players) {
        if (!perPlayer) {
            player.changeMoneyBalanceBy(-amount);
            return;
        }

        // pay every player
        for (Player p : players) {
            if (p.equals(player) || p.isBankrupt().getValue()) continue;

            p.changeMoneyBalanceBy(amount);
            player.changeMoneyBalanceBy(-amount);
        }
    }
}