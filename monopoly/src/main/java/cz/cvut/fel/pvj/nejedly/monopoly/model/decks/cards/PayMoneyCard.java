package cz.cvut.fel.pvj.nejedly.monopoly.model.decks.cards;

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

    public int getAmount() {
        return amount;
    }

    public boolean isPerPlayer() {
        return perPlayer;
    }
}