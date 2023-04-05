package cz.cvut.fel.pvj.nejedly.monopoly.model.decks.cards;

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

    public int getAmount() {
        return amount;
    }

    public boolean isFromEachPlayer() {
        return fromEachPlayer;
    }
}