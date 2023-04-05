package cz.cvut.fel.pvj.nejedly.monopoly.model.player;

import cz.cvut.fel.pvj.nejedly.monopoly.model.board.squares.Square;
import cz.cvut.fel.pvj.nejedly.monopoly.model.decks.cards.GetOutOfJailFreeCard;

import java.util.ArrayList;

public class Player {
    private final String name;
    private int boardPosition;
    private int money;
    private boolean isBankrupt;
    private boolean isInJail;
    private final ArrayList<Square> ownedSquares;
    private final String spriteImage;
    private ArrayList<GetOutOfJailFreeCard> getOutOfJailFreeCards;

    public Player(String name, String spriteImage) {
        this.name = name;
        this.spriteImage = spriteImage;
        boardPosition = 0;
        money = 1_500;
        isBankrupt = false;
        isInJail = false;
        ownedSquares = new ArrayList<>();
        getOutOfJailFreeCards = new ArrayList<>();
    }

    public void addOwnedSquare(Square square) {
        if (!ownedSquares.contains(square)) {
            ownedSquares.add(square);
        }
    }

    public void removeOwnedSquare(Square square) {
        if (ownedSquares.contains(square)) {
            ownedSquares.remove(square);
        }
    }

    public ArrayList<Square> getOwnedSquares() {
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

    public int getPosition() {
        return boardPosition;
    }

    public void setPosition(int position) {
        this.boardPosition = position;
    }

    public void advancePositionBy(int steps) {
        this.boardPosition += steps; // todo: implement board range 0-39 squares
    }

    public void advancePositionTo(Square square) {
        //todo: implement method
    }

    public int getMoney() {
        return money;
    }

    public int getBoardPosition() {
        return boardPosition;
    }

    public String getSpriteImage() {
        return spriteImage;
    }

    public ArrayList<GetOutOfJailFreeCard> getGetOutOfJailFreeCards() {
        return getOutOfJailFreeCards;
    }
}
