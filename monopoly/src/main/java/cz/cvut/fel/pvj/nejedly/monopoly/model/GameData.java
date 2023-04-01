package cz.cvut.fel.pvj.nejedly.monopoly.model;

import cz.cvut.fel.pvj.nejedly.monopoly.model.board.Board;
import cz.cvut.fel.pvj.nejedly.monopoly.model.decks.ChanceDeck;
import cz.cvut.fel.pvj.nejedly.monopoly.model.decks.CommunityChestDeck;
import cz.cvut.fel.pvj.nejedly.monopoly.model.die.Die;
import cz.cvut.fel.pvj.nejedly.monopoly.model.player.Player;

import java.util.ArrayList;

public class GameData {
    private final Board board;
    private final ChanceDeck chanceDeck;
    private final CommunityChestDeck communityChestDeck;
    private final Die die;
    private final ArrayList<Player> players;

    public GameData(int numberOfPlayers) {
        board = new Board();
        chanceDeck = new ChanceDeck();
        communityChestDeck = new CommunityChestDeck();
        die = new Die();
        players = configurePlayers(numberOfPlayers);
    }

    private ArrayList<Player> configurePlayers(int numberOfPlayers) {
        if (numberOfPlayers < 2 || numberOfPlayers > 5) {
            throw new IllegalArgumentException("Number of players must be within range 2-5.");
        }

        String[] SPRITES = new String[]{"boot.png", "car.png", "dog.png", "hat.png", "ship.png", "iron.png"};
        ArrayList<Player> listOfPlayers = new ArrayList<>();
        for (int i = 1; i <= numberOfPlayers; i++) {
            listOfPlayers.add(new Player("Player " + i, SPRITES[i-1]));
        }
        return listOfPlayers;
    }

    public Board getBoard() {
        return board;
    }

    public ChanceDeck getChanceDeck() {
        return chanceDeck;
    }

    public CommunityChestDeck getCommunityChestDeck() {
        return communityChestDeck;
    }

    public Die getDie() {
        return die;
    }
}
