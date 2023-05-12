package cz.cvut.fel.pvj.nejedly.monopoly.model;

import cz.cvut.fel.pvj.nejedly.monopoly.model.board.Board;
import cz.cvut.fel.pvj.nejedly.monopoly.model.player.Player;
import javafx.beans.property.SimpleBooleanProperty;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GameModelTest {
    private Player p1;
    private Player p2;
    private Player p3;
    private GameModel gameModel;

    @BeforeEach
    void setUp() {
        p1 = mock(Player.class);
        p2 = mock(Player.class);
        p3 = mock(Player.class);

        ArrayList<Player> players = new ArrayList<>(Arrays.asList(p1, p2, p3));
        gameModel = new GameModel(players, p1, mock(Board.class));
    }

    @Test
    void setNextPlayerAsActive() {
        // arrange
        when(p2.isBankrupt()).thenReturn(new SimpleBooleanProperty(true));
        when(p3.isBankrupt()).thenReturn(new SimpleBooleanProperty(false));

        // action
        gameModel.setNextPlayerAsActive();

        // assert
        assertEquals(p3,  gameModel.getActivePlayer());
    }

    @Test
    void hasGameEnded() {
        // arrange
        when(p1.isBankrupt()).thenReturn(new SimpleBooleanProperty(true));
        when(p2.isBankrupt()).thenReturn(new SimpleBooleanProperty(false));
        when(p3.isBankrupt()).thenReturn(new SimpleBooleanProperty(true));

        // action
        boolean result = gameModel.hasGameEnded();

        // assert
        assertTrue(result);
    }
}