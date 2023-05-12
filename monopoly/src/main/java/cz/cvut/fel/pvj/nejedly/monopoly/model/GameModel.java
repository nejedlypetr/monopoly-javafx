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

    /**
     * Sets the next player in the game as active.
     *
     * <p>This method sets the next player in the game as active. The player who is currently active
     * is determined based on the index of the current active player in the list of players. The next
     * player is determined by incrementing the current index by 1, and wrapping around to the start of
     * the list if the index exceeds the number of players. The method also skips the turns of bankrupt
     * players by checking if the player at the current index is bankrupt, and incrementing the index
     * until a non-bankrupt player is found.</p>
     */
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

    /**
     * Checks if the game has ended.
     *
     * <p>This method checks if the game has ended by counting the number of bankrupt players in the game.
     * If the number of bankrupt players is equal to the number of players minus one, the game is
     * considered to have ended. A game is considered to have ended when only one player remains
     * solvent.</p>
     *
     * @return true if the game has ended, false otherwise.
     */
    public boolean hasGameEnded() {
        LOGGER.fine("Run has game ended check.");

        int numberOfBankruptPlayers = 0;
        for (Player player : players) {
            if (player.isBankrupt().getValue()) numberOfBankruptPlayers++;
        }
        return numberOfBankruptPlayers == (players.size() - 1);
    }

    /**
     * Returns the winner of the game.
     *
     * <p>This method returns the winner of the game, which is the last player remaining solvent. The
     * method iterates over the list of players in the game, and returns the first player that is not
     * bankrupt. If no player is found, the method returns null.</p>
     *
     * @return the winner of the game, or null if no winner is found.
     */
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

    /**
     * Starts a new game with the specified number of players.
     *
     * <p>This method starts a new game with the specified number of players. The method initializes the
     * game by creating a new board and a new set of dice. It also configures the players in the game
     * by calling the private helper method `configurePlayers()`.</p>
     *
     * @param numberOfPlayers the number of players in the game.
     */
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

    /**
     * Processes the player stepping on an ownable square.
     *
     * <p>This method processes the player stepping on an ownable square. If the square is not owned or is
     * owned by the player, the method returns true. If the square is owned by another player, the
     * method deducts the appropriate rent from the current player's balance and adds it to the owner's
     * balance. If the ownable square is a utility, the rent is calculated based on the value of the
     * dice roll and the number of utilities owned by the owner.</p>
     *
     * @param ownable the ownable square that the player has stepped on.
     * @param player the player who has stepped on the ownable square.
     * @return true if the square is not owned or is owned by the player, false otherwise.
     */
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

    /**
     * Processes the player stepping on a cards square.
     *
     * <p>This method processes the player stepping on a cards square. If the square is a chance square,
     * the method draws a chance card from the game board's chance deck. If the square is a community
     * chest square, the method draws a community chest card from the game board's community chest
     * deck. The card's effect is then executed using the executeCard() method. The drawn card is
     * returned by the method.</p>
     *
     * @param cards the type of cards square that the player has stepped on.
     * @param player the player who has stepped on the cards square.
     * @return the card drawn from the deck and executed by the player.
     */
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

    /**
     * Saves the current game state to a JSON file.
     *
     * <p>This method saves the current state of the game to a JSON file named "saved-game.json". The method
     * first converts the game state to a JSON object using the toJsonObject() method, then writes the
     * JSON object to the file using a FileWriter. If an IOException occurs while writing the file, the
     * method logs a warning message and throws a RuntimeException.</p>
     *
     * @throws RuntimeException if an IOException occurs while writing the saved game file.
     */
    public void save() {
        LOGGER.info("Save game.");

        JsonObject jsonObject = toJsonObject();
        try (FileWriter fileWriter = new FileWriter("saved-game.json")) {
            fileWriter.write(jsonObject.toJson());
        } catch (IOException e) {
            LOGGER.warning("Save game IOException: " + e);
            throw new RuntimeException(e);
        }
    }

    /**
     * Loads a previously saved game state from a JSON file.
     *
     * <p>This method reads a previously saved game state from a JSON file named "saved-game.json". The
     * method first creates a FileReader object to read the file, then deserializes the JSON object.
     * The deserialized JSON object is then converted back into a GameModel
     * object using the fromJsonObject() method. If the file does not exist or cannot be read, the
     * method throws a FileNotFoundException. If the JSON object cannot be deserialized, the method
     * throws a JsonException.</p>
     *
     * @return a GameModel object representing the previously saved game state.
     * @throws FileNotFoundException if the saved game file cannot be found or read.
     * @throws JsonException if the saved game file cannot be deserialized.
     */
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
