package cz.cvut.fel.pvj.nejedly.monopoly.model.player;

import cz.cvut.fel.pvj.nejedly.monopoly.model.board.squares.*;
import cz.cvut.fel.pvj.nejedly.monopoly.model.decks.cards.GetOutOfJailFreeCard;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.Comparator;

public class Player {
    private final String name;
    private final SimpleIntegerProperty boardPosition;
    private final SimpleIntegerProperty money;
    private final SimpleBooleanProperty isBankrupt;
    private final SimpleBooleanProperty isInJail;
    private final ObservableList<Ownable> ownedSquares;
    private final String spriteImage;
    private final ArrayList<GetOutOfJailFreeCard> getOutOfJailFreeCards;

    public Player(String name, String spriteImage) {
        this.name = name;
        this.spriteImage = spriteImage;
        boardPosition = new SimpleIntegerProperty(0);
        money = new SimpleIntegerProperty(1_500);
        isBankrupt = new SimpleBooleanProperty(false);
        isInJail = new SimpleBooleanProperty(false);
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

    public SimpleBooleanProperty isBankrupt() {
        return isBankrupt;
    }

    public void setBankrupt(boolean bankrupt) {
        isBankrupt.set(bankrupt);
    }

    public SimpleBooleanProperty isInJail() {
        return isInJail;
    }

    public void setInJail(boolean inJail) {
        isInJail.set(inJail);
    }

    public String getName() {
        return name;
    }

    public String getNameWithStatus() {
        if (isBankrupt.getValue()) return "(bankrupt) " + name;
        else if (isInJail.getValue()) return "(in jail) " + name;
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
        if ((boardPosition.getValue() > futurePosition) && !isInJail.getValue()) {
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
            if (ownable instanceof Utility) numberOfOwnedUtilities++;
        }
        return numberOfOwnedUtilities;
    }

    public int getNumberOfOwnedRailroads() {
        int numberOfOwnedRailroads = 0;
        for (Ownable ownable : ownedSquares) {
            if (ownable instanceof Railroad) numberOfOwnedRailroads++;
        }
        return numberOfOwnedRailroads;
    }

    public boolean addCardToGetOutOfJailFreeCards(GetOutOfJailFreeCard card) {
        return getOutOfJailFreeCards.add(card);
    }

    public void changeMoneyBalanceBy(int amount) {
        if ((-amount) > money.getValue()) {
            autoSellProperties(-amount);
        }

        if ((money.get() + amount) < 0) {
            money.set(0);
        } else {
            money.set(money.get() + amount);
        }
    }

    private void autoSellProperties(int moneyNeeded) {
        int moneyGained = 0;

        ownedSquares.sort(Comparator.comparingInt(Ownable::getPurchasePrice)); // sort by purchase price
        for (Ownable ownable : ownedSquares) {
            if (moneyGained < moneyNeeded) {
                sellOwnedSquare((Square) ownable);
                moneyGained += ownable.getPurchasePrice();
            }
        }

        if (moneyGained < moneyNeeded) {
            setBankrupt(true);
            money.set(0);
        }
    }

    public void stepOnGoToJail() {
        isInJail.set(true);
    }

    public void stepOnTax(Tax tax) {
        changeMoneyBalanceBy(-tax.getTax());
    }
}
