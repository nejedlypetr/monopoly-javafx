package cz.cvut.fel.pvj.nejedly.monopoly.model.player;

import cz.cvut.fel.pvj.nejedly.monopoly.model.board.squares.*;
import cz.cvut.fel.pvj.nejedly.monopoly.model.decks.cards.GetOutOfJailFreeCard;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;

public class Player {
    private final String name;
    private SimpleIntegerProperty boardPosition;
    private final SimpleIntegerProperty money;
    private boolean isBankrupt;
    private boolean isInJail;
    private final ObservableList<Ownable> ownedSquares;
    private final String spriteImage;
    private ArrayList<GetOutOfJailFreeCard> getOutOfJailFreeCards;

    public Player(String name, String spriteImage) {
        this.name = name;
        this.spriteImage = spriteImage;
        boardPosition = new SimpleIntegerProperty(0);
        money = new SimpleIntegerProperty(1_500);
        isBankrupt = false;
        isInJail = false;
        ownedSquares = FXCollections.observableArrayList();
        getOutOfJailFreeCards = new ArrayList<>();
    }

    public boolean purchaseSquare(Square square) {
        if (!(square instanceof Ownable ownable) || ownedSquares.contains(square)) return false;
        if (!ownable.isOwned() && money.get() >= ownable.getPurchasePrice()) {
            changeMoneyBalanceBy(-ownable.getPurchasePrice());
            ownable.setOwner(this);
            return ownedSquares.add((Ownable) square);
        }
        return false;
    }

    public boolean sellOwnedSquare(Square square) {
        if (!(square instanceof Ownable ownable) || !ownedSquares.contains(square)) return false;
        changeMoneyBalanceBy(ownable.getPurchasePrice());
        ownable.setOwner(null);
        return ownedSquares.remove(ownable);
    }

    public ObservableList<Ownable> getOwnedSquares() {
        return ownedSquares;
    }

    public boolean isBankrupt() {
        return isBankrupt;
    }

    public void setBankrupt(boolean bankrupt) {
        isBankrupt = bankrupt;
    }

    public boolean isInJail() {
        return isInJail;
    }

    public void setInJail(boolean inJail) {
        isInJail = inJail;
    }

    public String getName() {
        return name;
    }

    public SimpleIntegerProperty getBoardPosition() {
        return boardPosition;
    }

    public void setBoardPosition(int boardPosition) {
        this.boardPosition.set(boardPosition);
    }

    public void advancePositionBy(int steps) {
        int futurePosition = ((boardPosition.getValue() + steps) % 40);
        if ((boardPosition.getValue() > futurePosition) && !isInJail) {
            changeMoneyBalanceBy(200); // player receives $200 salary when passing GO square
        }
        boardPosition.set(futurePosition);
    }

    public void advancePositionTo(Square square) {
        //todo: implement method
    }

    public SimpleIntegerProperty getMoney() {
        return money;
    }

    public String getSpriteImage() {
        return spriteImage;
    }

    public ArrayList<GetOutOfJailFreeCard> getGetOutOfJailFreeCards() {
        return getOutOfJailFreeCards;
    }

    public int getNumberOfOwnedUtilities() {
        int numberOfOwnedUtilities = 0;
        for (Ownable ownable : ownedSquares) {
            if (ownable instanceof Utility) {
                numberOfOwnedUtilities++;
            }
        }
        return numberOfOwnedUtilities;
    }

    public int getNumberOfOwnedRailroads() {
        int numberOfOwnedRailroads = 0;
        for (Ownable ownable : ownedSquares) {
            if (ownable instanceof Railroad) {
                numberOfOwnedRailroads++;
            }
        }
        return numberOfOwnedRailroads;
    }

    public boolean addCardToGetOutOfJailFreeCards(GetOutOfJailFreeCard card) {
        return getOutOfJailFreeCards.add(card);
    }

    public void changeMoneyBalanceBy(int amount) {
        money.set(money.get() + amount);
        // todo implement bankrupt
    }

    public void stepOnGoToJail() {
        isInJail = true;
    }

    public void stepOnTax(Tax tax) {
        changeMoneyBalanceBy(-tax.getTax());
    }
}
