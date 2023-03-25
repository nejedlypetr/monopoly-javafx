package cz.cvut.fel.pvj.nejedly.monopoly.model.decks;

public class CommunityChestDeck extends Deck {

    public CommunityChestDeck() {
        super(
            new Card[] {
                new Card(CardType.COMMUNITY_CHEST, "Dummy card1"),
                new Card(CardType.COMMUNITY_CHEST, "Dummy card2"),
                new Card(CardType.COMMUNITY_CHEST, "Dummy card3"),
                new Card(CardType.COMMUNITY_CHEST, "Dummy card4"),
            }
        );
    }
}
