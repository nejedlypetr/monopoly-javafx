package cz.cvut.fel.pvj.nejedly.monopoly.model;

import com.github.cliftonlabs.json_simple.JsonArray;
import com.github.cliftonlabs.json_simple.JsonException;
import com.github.cliftonlabs.json_simple.JsonObject;
import com.github.cliftonlabs.json_simple.Jsoner;
import cz.cvut.fel.pvj.nejedly.monopoly.model.board.Board;
import cz.cvut.fel.pvj.nejedly.monopoly.model.board.squares.Cards;
import cz.cvut.fel.pvj.nejedly.monopoly.model.board.squares.Ownable;
import cz.cvut.fel.pvj.nejedly.monopoly.model.board.squares.Square;
import cz.cvut.fel.pvj.nejedly.monopoly.model.board.squares.Utility;
import cz.cvut.fel.pvj.nejedly.monopoly.model.decks.cards.Card;
import cz.cvut.fel.pvj.nejedly.monopoly.model.decks.cards.CardType;
import cz.cvut.fel.pvj.nejedly.monopoly.model.decks.cards.PayMoneyCard;
import cz.cvut.fel.pvj.nejedly.monopoly.model.decks.cards.ReceiveMoneyCard;
import cz.cvut.fel.pvj.nejedly.monopoly.model.die.Die;
import cz.cvut.fel.pvj.nejedly.monopoly.model.player.Player;
import javafx.beans.property.SimpleObjectProperty;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Logger;

public class GameModel {
    private final static Logger LOGGER = Logger.getLogger(GameModel.class.getName());
    private Board board;
    private Die die;
    private ArrayList<Player> players;
    private final SimpleObjectProperty<Player> activePlayer;

    public GameModel() {
        board = new Board();
        die = new Die();
        players = new ArrayList<>();
        activePlayer = new SimpleObjectProperty<>();
    }

    public GameModel(ArrayList<Player> players, Player activePlayer, Board board) {
        this.board = board;
        die = new Die();
        this.players = players;
        this.activePlayer = new SimpleObjectProperty<>(activePlayer);
    }

    public void setNextPlayerAsActive() {
        LOGGER.info("Set next player as active.");

        int currentIndex = players.indexOf(activePlayer.get());
        currentIndex = (currentIndex + 1) % players.size();

        // bankrupt players' turns will be skipped
        while (players.get(currentIndex).isBankrupt().getValue()) {
            LOGGER.finest("Bankrupt player skipped: "+players.get(currentIndex).getName());
            currentIndex = (currentIndex + 1) % players.size();
        }

        activePlayer.set(players.get(currentIndex));
        LOGGER.info("Set active player: "+players.get(currentIndex).getName());
    }

    public boolean hasGameEnded() {
        LOGGER.fine("Run has game ended check.");

        int numberOfBankruptPlayers = 0;
        for (Player player : players) {
            if (player.isBankrupt().getValue()) numberOfBankruptPlayers++;
        }
        return numberOfBankruptPlayers == (players.size() - 1);
    }

    public Player getWinner() {
        LOGGER.fine("Run get winner.");

        for (Player player : players) {
            if (!player.isBankrupt().getValue()) return player;
        }
        return null;
    }

    private ArrayList<Player> configurePlayers(int numberOfPlayers) {
        LOGGER.info("Run configure players.");

        if (numberOfPlayers < 2 || numberOfPlayers > 6) {
            LOGGER.warning("The number of players is out of the range of 2-6. Current number: \"+numberOfPlayers");
            throw new IllegalArgumentException("The number of players must be within the range of 2-6. Current number: "+numberOfPlayers);
        }

        String[] SPRITES = new String[]{"boot.png", "car.png", "dog.png", "hat.png", "ship.png", "iron.png"};
        ArrayList<Player> listOfPlayers = new ArrayList<>();
        for (int i = 1; i <= numberOfPlayers; i++) {
            listOfPlayers.add(new Player("Player " + i, SPRITES[i-1]));
        }

        // choose a player that will start
        activePlayer.set(listOfPlayers.get(0));
        LOGGER.info("Set a player that will start: "+listOfPlayers.get(0).getName());

        return listOfPlayers;
    }

    public Board getBoard() {
        return board;
    }

    public Die getDie() {
        return die;
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public void startNewGame(int numberOfPlayers) {
        LOGGER.info("Start new game with "+numberOfPlayers+" players.");

        players = configurePlayers(numberOfPlayers);
        board = new Board();
        die = new Die();
    }

    public Player getActivePlayer() {
        return activePlayer.get();
    }

    public SimpleObjectProperty<Player> getActivePlayerProperty() {
        return activePlayer;
    }

    public boolean steppedOnOwnable(Ownable ownable, Player player) {
        LOGGER.info("Run "+player.getName()+" stepped on Ownable square: "+((Square) ownable).getName());

        if (!ownable.isOwned() || player.getOwnedSquares().contains(ownable)) return true;

        if (ownable instanceof Utility utility) {
            player.changeMoneyBalanceBy(-utility.getRent(die));
            ownable.getOwner().changeMoneyBalanceBy(utility.getRent(die));
        } else {
            player.changeMoneyBalanceBy(-ownable.getRent());
            ownable.getOwner().changeMoneyBalanceBy(ownable.getRent());
        }
        return false;
    }

    public Card steppedOnCards(Cards cards, Player player) {
        LOGGER.info("Run "+player.getName()+" stepped on Cards square: "+cards.toString());

        Card card = null;
        if (cards.getCardType().equals(CardType.CHANCE)) {
            card = board.getChanceDeck().drawCard();
        } else if (cards.getCardType().equals(CardType.COMMUNITY_CHEST)) {
            card = board.getCommunityChestDeck().drawCard();
        }
        executeCard(player, card);
        return card;
    }

    private void executeCard(Player player, Card card) {
        LOGGER.info(player.getName()+" executes card: "+card.toString());

        if (card instanceof PayMoneyCard payMoneyCard) {
            payMoneyCard.execute(player, players);
        } else if (card instanceof ReceiveMoneyCard receiveMoneyCard) {
            receiveMoneyCard.execute(player, players);
        }
    }

    private JsonObject toJsonObject() {
        LOGGER.info("Create JsonObject.");

        JsonArray playersJsonArray = new JsonArray();
        for (Player player : players) {
            playersJsonArray.add(player.toJsonObject());
        }

        JsonObject jsonObject = new JsonObject();
        jsonObject.put("activePlayer", activePlayer.getValue().getName());
        jsonObject.put("players", playersJsonArray);

        return jsonObject;
    }

    public void save() {
        LOGGER.info("Save game.");

        JsonObject jsonObject = toJsonObject();
        try (FileWriter fileWriter = new FileWriter("saved-game.json")) {
            fileWriter.write(jsonObject.toJson());
        } catch (IOException e) {
            LOGGER.warning("Save game IOException: "+e.toString());
            throw new RuntimeException(e);
        }
    }

    public static GameModel load() throws FileNotFoundException, JsonException {
        LOGGER.info("Load game.");

        FileReader fileReader = new FileReader("saved-game.json");
        JsonObject jsonObject = (JsonObject) Jsoner.deserialize(fileReader);
        return fromJsonObject(jsonObject);
    }

    private static Player getPlayerByName(ArrayList<Player> listOfPlayers, String name) {
        LOGGER.fine("Get player by name: "+name);

        for (Player player : listOfPlayers) {
            if (player.getName().equals(name)) return player;
        }
        return null;
    }

    private static GameModel fromJsonObject(JsonObject jsonObject) {
        LOGGER.info("Create GameModel from JsonObject.");

        Board board = new Board();
        ArrayList<Player> playerArrayList = new ArrayList<>();

        JsonArray playersJsonArray = (JsonArray) jsonObject.get("players");
        for (int i = 0; i < playersJsonArray.size(); i++) {
            Player player = Player.fromJsonObject((JsonObject) playersJsonArray.get(i), board, i);

            // set the owner of the square
            for (Ownable ownable : player.getOwnedSquares()) {
                ownable.setOwner(player);
            }

            playerArrayList.add(player);
        }

        String activePlayerName = (String) jsonObject.get("activePlayer");
        Player activePlayer = getPlayerByName(playerArrayList, activePlayerName);

        return new GameModel(playerArrayList, activePlayer, board);
    }
}
