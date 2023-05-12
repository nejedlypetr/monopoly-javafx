package cz.cvut.fel.pvj.nejedly.monopoly.model.board;

import cz.cvut.fel.pvj.nejedly.monopoly.model.board.squares.*;
import cz.cvut.fel.pvj.nejedly.monopoly.model.decks.ChanceDeck;
import cz.cvut.fel.pvj.nejedly.monopoly.model.decks.CommunityChestDeck;
import cz.cvut.fel.pvj.nejedly.monopoly.model.decks.cards.*;

public class Board {
    private final Square[] boardSquares;
    private final ChanceDeck chanceDeck;
    private final CommunityChestDeck communityChestDeck;

    public Board() {
        boardSquares = createBoard();
        chanceDeck = new ChanceDeck(createChanceCards());
        communityChestDeck = new CommunityChestDeck(createCommunityChestCards());

        chanceDeck.shuffle();
        communityChestDeck.shuffle();
    }

    private Square[] createBoard() {
        return new Square[]{
            new Go(0, 200),
            new Property("Mediterranean Avenue", 1, PropertyGroup.BROWN, 2, 60),
            new Cards("Community Chest", 2, CardType.COMMUNITY_CHEST),
            new Property("Baltic Avenue", 3, PropertyGroup.BROWN, 4, 60),
            new Tax("Income Tax", 4, 200),
            new Railroad("Reading Railroad", 5, 25),
            new Property("Oriental Avenue", 6, PropertyGroup.LIGHTBLUE, 6, 100),
            new Cards("Chance", 7, CardType.CHANCE),
            new Property("Vermont Avenue", 8, PropertyGroup.LIGHTBLUE, 6, 100),
            new Property("Connecticut Avenue", 9, PropertyGroup.LIGHTBLUE, 8, 120),
            new Jail("Jail", 10),
            new Property("St. Charles Place", 11, PropertyGroup.PINK, 10, 140),
            new Utility("Electric Company", 12),
            new Property("States Avenue", 13, PropertyGroup.PINK, 10, 140),
            new Property("Virginia Avenue", 14, PropertyGroup.PINK, 12, 160),
            new Railroad("Pennsylvania Railroad", 15, 25),
            new Property("St. James Place", 16, PropertyGroup.ORANGE, 14, 180),
            new Cards("Community Chest", 17, CardType.COMMUNITY_CHEST),
            new Property("Tennessee Avenue", 18, PropertyGroup.ORANGE, 14, 180),
            new Property("New York Avenue", 19, PropertyGroup.ORANGE, 16, 200),
            new FreeParking(20),
            new Property("Kentucky Avenue", 21, PropertyGroup.RED, 18, 220),
            new Cards("Chance", 22, CardType.CHANCE),
            new Property("Indiana Avenue", 23, PropertyGroup.RED, 18, 220),
            new Property("Illinois Avenue", 24, PropertyGroup.RED, 20, 240),
            new Railroad("B. & O. Railroad", 25, 25),
            new Property("Atlantic Avenue", 26, PropertyGroup.YELLOW, 22, 260),
            new Property("Ventnor Avenue", 27, PropertyGroup.YELLOW, 22, 260),
            new Utility("Water Works", 28),
            new Property("Marvin Gardens", 29, PropertyGroup.YELLOW, 24, 280),
            new GoToJail(30, 10),
            new Property("Pacific Avenue", 31, PropertyGroup.GREEN, 26, 300),
            new Property("North Carolina Avenue", 32, PropertyGroup.GREEN, 26, 300),
            new Cards("Community Chest", 33, CardType.COMMUNITY_CHEST),
            new Property("Pennsylvania Avenue", 34, PropertyGroup.GREEN, 28, 320),
            new Railroad("Short Line", 35, 25),
            new Cards("Chance", 36, CardType.CHANCE),
            new Property("Park Place", 37, PropertyGroup.BLUE, 35, 350),
            new Tax("Luxury Tax", 38, 100),
            new Property("Boardwalk", 39, PropertyGroup.BLUE, 50, 400)
        };
    }

    private Card[] createChanceCards() {
        return  new Card[]{
            new MoveToCard(CardType.CHANCE, "Advance to GO.\nCollect 200$.", boardSquares[0]),
            new ReceiveMoneyCard(CardType.CHANCE, "Your building and loan matures.\nCollect 150$.", 150),
            new MoveToCard(CardType.CHANCE, "Take a walk on the board walk.\nAdvance token to "+boardSquares[39].getName(), boardSquares[39]),
            new MoveToCard(CardType.CHANCE, "Advance to "+boardSquares[24].getName()+".", boardSquares[24]),
            new NearestSquareCard(CardType.CHANCE, "Advance token to nearest Utility.", Utility.class.getTypeName()),
            new MoveToCard(CardType.CHANCE, "Advance to "+boardSquares[11].getName()+".\nIf you pass GO, collect $200.", boardSquares[11]),
            new NearestSquareCard(CardType.CHANCE, "Advance token to nearest Railroad.", Railroad.class.getTypeName()),
            new PayMoneyCard(CardType.CHANCE, "You have been elected chairman of the board. Pay each player $50", 50, true),
            new MoveToCard(CardType.CHANCE, "Take a ride to the "+boardSquares[5].getName()+".", boardSquares[5]),
            new PayMoneyCard(CardType.CHANCE, "Pay poor tax of $15.", 15),
            new ReceiveMoneyCard(CardType.CHANCE, "Banks pays you dividend of $50.", 50),
            new NearestSquareCard(CardType.CHANCE, "Advance token to nearest Railroad.", Railroad.class.getTypeName())
        };
    }

    private Card[] createCommunityChestCards() {
        return new Card[]{
            new ReceiveMoneyCard(CardType.COMMUNITY_CHEST, "Life insurance matters.\nCollect $100.", 100),
            new PayMoneyCard(CardType.COMMUNITY_CHEST, "Pay school tax of $150.", 150),
            new ReceiveMoneyCard(CardType.COMMUNITY_CHEST, "Grand Opera opening.\nCollect $50 from each player for opening night seats.", 50, true),
            new PayMoneyCard(CardType.COMMUNITY_CHEST, "Doctor's fee, pay $50.", 50),
            new ReceiveMoneyCard(CardType.COMMUNITY_CHEST, "Xmas fund matures, collect $100.", 100),
            new ReceiveMoneyCard(CardType.COMMUNITY_CHEST, "From sale of stock you get $45", 45),
            new ReceiveMoneyCard(CardType.COMMUNITY_CHEST, "Income tax refund, collect $20", 20),
            new ReceiveMoneyCard(CardType.COMMUNITY_CHEST, "You inherit $100.", 100),
            new PayMoneyCard(CardType.COMMUNITY_CHEST, "Pay hospital $100.", 100),
            new ReceiveMoneyCard(CardType.COMMUNITY_CHEST, "Bank error in your favor, collect $100.", 100),
            new ReceiveMoneyCard(CardType.COMMUNITY_CHEST, "You have one a second price in a beauty contest, collect $10.", 10),
            new MoveToCard(CardType.COMMUNITY_CHEST, "Advance to GO.\nCollect 200$.", boardSquares[0]),
            new ReceiveMoneyCard(CardType.COMMUNITY_CHEST, "Receive for services $25.", 25)
        };
    }

    public Square[] getBoardSquares() {
        return boardSquares;
    }

    public ChanceDeck getChanceDeck() {
        return chanceDeck;
    }

    public CommunityChestDeck getCommunityChestDeck() {
        return communityChestDeck;
    }

    /**
     * Returns the nearest {@link Square} of a given type on the {@link Board}, starting at a given position.
     *
     * @param startPosition the position to start searching from
     * @param className the name of the class of the square type to search for
     * @return the nearest square of the given type, or null if no such {@link Square} is found
     */
    public Square getNearestSquareOfType(int startPosition, String className) {
        int max = boardSquares.length;
        for (int i = startPosition; i < (max + startPosition - 1); i++) {
            try {
                if (Class.forName(className).isInstance(boardSquares[i % max])) return boardSquares[i % max];
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
        return null;
    }
}
