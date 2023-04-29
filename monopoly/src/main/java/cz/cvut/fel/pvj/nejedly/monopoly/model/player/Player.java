package cz.cvut.fel.pvj.nejedly.monopoly.model.player;

import com.github.cliftonlabs.json_simple.JsonArray;
import com.github.cliftonlabs.json_simple.JsonObject;
import cz.cvut.fel.pvj.nejedly.monopoly.model.board.Board;
import cz.cvut.fel.pvj.nejedly.monopoly.model.board.squares.*;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.Comparator;
import java.util.logging.Logger;

public class Player {
    private final static Logger LOGGER = Logger.getLogger(Player.class.getName());
    private final String name;
    private final SimpleIntegerProperty boardPosition;
    private final SimpleIntegerProperty money;
    private final SimpleBooleanProperty isBankrupt;
    private final SimpleBooleanProperty isInJail;
    private final ObservableList<Ownable> ownedSquares;
    private final String spriteImage;

    public Player(String name, String spriteImage) {
        this.name = name;
        this.spriteImage = spriteImage;
        boardPosition = new SimpleIntegerProperty(0);
        money = new SimpleIntegerProperty(1_500);
        isBankrupt = new SimpleBooleanProperty(false);
        isInJail = new SimpleBooleanProperty(false);
        ownedSquares = FXCollections.observableArrayList();
    }

    public Player(String name, int boardPosition, int money, boolean isBankrupt, boolean isInJail, ObservableList<Ownable> ownedSquares, int spriteIndex) {
        this.name = name;
        this.boardPosition = new SimpleIntegerProperty(boardPosition);
        this.money = new SimpleIntegerProperty(money);
        this.isBankrupt = new SimpleBooleanProperty(isBankrupt);
        this.isInJail = new SimpleBooleanProperty(isInJail);
        this.ownedSquares = ownedSquares;

        try {
            String[] SPRITES = new String[]{"boot.png", "car.png", "dog.png", "hat.png", "ship.png", "iron.png"};
            spriteImage = SPRITES[spriteIndex];
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public boolean purchaseSquare(Square square) {
        LOGGER.info("Run purchase square.");

        if (!(square instanceof Ownable ownable) || ownedSquares.contains(square)) return false;
        if (!ownable.isOwned() && money.get() >= ownable.getPurchasePrice()) {
            changeMoneyBalanceBy(-ownable.getPurchasePrice());
            ownable.setOwner(this);
            LOGGER.info("Set "+name+" as a owner of "+square.getName());
            return ownedSquares.add((Ownable) square);
        }
        return false;
    }

    public boolean sellOwnedSquare(Square square) {
        LOGGER.info("Run sell owned square.");

        if (!(square instanceof Ownable ownable) || !ownedSquares.contains(square)) return false;
        changeMoneyBalanceBy(ownable.getPurchasePrice());
        ownable.setOwner(null);
        LOGGER.info(name+" sold "+square.getName());
        return ownedSquares.remove(ownable);
    }

    public ObservableList<Ownable> getOwnedSquares() {
        return ownedSquares;
    }

    public SimpleBooleanProperty isBankrupt() {
        return isBankrupt;
    }

    public void setBankrupt(boolean bankrupt) {
        LOGGER.fine("Set "+name+" isBankrupt to "+bankrupt);
        isBankrupt.set(bankrupt);
    }

    public SimpleBooleanProperty isInJail() {
        return isInJail;
    }

    public void setInJail(boolean inJail) {
        LOGGER.fine("Set "+name+" isInJail to "+inJail);
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
        LOGGER.info("Advance "+name+"'s board position by "+steps+" steps.");

        int futurePosition = ((boardPosition.getValue() + steps) % 40);
        if ((boardPosition.getValue() > futurePosition) && !isInJail.getValue()) {
            changeMoneyBalanceBy(200); // player receives $200 salary when passing GO square
        }
        boardPosition.set(futurePosition);

        LOGGER.info(name+"'s new board position is "+futurePosition);
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

    public int getNumberOfOwnedUtilities() {
        LOGGER.fine(name+" get number of owned Utilities.");

        int numberOfOwnedUtilities = 0;
        for (Ownable ownable : ownedSquares) {
            if (ownable instanceof Utility) numberOfOwnedUtilities++;
        }
        return numberOfOwnedUtilities;
    }

    public int getNumberOfOwnedRailroads() {
        LOGGER.fine(name+" get number of owned Railroads.");

        int numberOfOwnedRailroads = 0;
        for (Ownable ownable : ownedSquares) {
            if (ownable instanceof Railroad) numberOfOwnedRailroads++;
        }
        return numberOfOwnedRailroads;
    }

    public void changeMoneyBalanceBy(int amount) {
        LOGGER.info(name+" change money balance by "+amount);

        if ((-amount) > money.getValue()) {
            autoSellProperties(-amount);
        }

        if ((money.get() + amount) < 0) {
            money.set(0);
        } else {
            money.set(money.get() + amount);
        }

        LOGGER.info(name+" new money balance is "+money.getValue());
    }

    private void autoSellProperties(int moneyNeeded) {
        LOGGER.info("Run auto-sell properties.");

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

    public JsonObject toJsonObject() {
        LOGGER.info(name+" create JsonObject.");

        JsonObject player = new JsonObject();
        player.put("name", name);
        player.put("boardPosition", boardPosition.getValue());
        player.put("money", money.getValue());
        player.put("isBankrupt", isBankrupt.getValue());
        player.put("isInJail", isInJail.getValue());

        JsonArray ownedSquaresJsonArray = new JsonArray();
        for (Ownable ownable : ownedSquares) {
            ownedSquaresJsonArray.add(((Square) ownable).getPosition());
        }
        player.put("ownedSquares", ownedSquaresJsonArray);

        return player;
    }

    public static Player fromJsonObject(JsonObject jsonObject, Board board, int spriteIndex) {
        LOGGER.info("Create Player from JsonObject.");

        String name = (String) jsonObject.get("name");
        int boardPosition = Integer.parseInt(jsonObject.get("boardPosition").toString());
        int money = Integer.parseInt(jsonObject.get("money").toString());
        boolean isBankrupt = (Boolean) jsonObject.get("isBankrupt");
        boolean isInJail = (Boolean) jsonObject.get("isInJail");

        ObservableList<Ownable> ownedSquares = FXCollections.observableArrayList();
        JsonArray ownedSquaresJsonArray = (JsonArray) jsonObject.get("ownedSquares");
        for (Object obj : ownedSquaresJsonArray) {
            Square square = board.getBoardSquares()[Integer.parseInt(obj.toString())];
            ownedSquares.add((Ownable) square);
        }

        return new Player(name, boardPosition, money, isBankrupt, isInJail, ownedSquares, spriteIndex);
    }
}
