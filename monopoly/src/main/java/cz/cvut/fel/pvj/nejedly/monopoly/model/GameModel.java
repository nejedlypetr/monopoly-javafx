package cz.cvut.fel.pvj.nejedly.monopoly.model;

import cz.cvut.fel.pvj.nejedly.monopoly.model.board.Board;
import cz.cvut.fel.pvj.nejedly.monopoly.model.die.Die;
import cz.cvut.fel.pvj.nejedly.monopoly.model.player.Player;
import javafx.beans.property.SimpleObjectProperty;

import java.util.ArrayList;

public class GameModel {
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

    private ArrayList<Player> configurePlayers(int numberOfPlayers) {
        if (numberOfPlayers < 2 || numberOfPlayers > 6) {
            throw new IllegalArgumentException("The number of players must be within the range of 2-6.");
        }

        String[] SPRITES = new String[]{"boot.png", "car.png", "dog.png", "hat.png", "ship.png", "iron.png"};
        ArrayList<Player> listOfPlayers = new ArrayList<>();
        for (int i = 1; i <= numberOfPlayers; i++) {
            listOfPlayers.add(new Player("Player " + i, SPRITES[i-1]));
        }

        // choose a player that will be starting
        activePlayer.set(listOfPlayers.get(0));

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
        players = configurePlayers(numberOfPlayers);
        board = new Board();
        die = new Die();
    }

    public Player getActivePlayer() {
        return activePlayer.get();
    }

    public SimpleObjectProperty<Player> activePlayerProperty() {
        return activePlayer;
    }
}
