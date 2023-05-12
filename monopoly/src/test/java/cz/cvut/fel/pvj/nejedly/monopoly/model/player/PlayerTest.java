package cz.cvut.fel.pvj.nejedly.monopoly.model.player;

import cz.cvut.fel.pvj.nejedly.monopoly.model.board.squares.Ownable;
import cz.cvut.fel.pvj.nejedly.monopoly.model.board.squares.Property;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {

    @Test
    void purchaseSquare() {
        // arrange
        Property property = new Property(null, 0, null, 0, 100);
        Player player = new Player(null, 0, 200, false, false, FXCollections.observableArrayList(), 0);

        // action
        player.purchaseSquare(property);

        // assert
        assertTrue(property.isOwned());
        assertEquals(100, player.getMoney().get());
        assertTrue(player.getOwnedSquares().contains(property));
    }

    @Test
    void sellOwnedSquare() {
        // arrange
        ObservableList<Ownable> ownedSquares = FXCollections.observableArrayList();
        Player player = new Player(null, 0, 200, false, false, ownedSquares, 0);
        Property property = new Property(null, 0, null, 0, 100);

        property.setOwner(player);
        ownedSquares.add(property);

        // action
        player.sellOwnedSquare(property);

        // assert
        assertFalse(property.isOwned());
        assertEquals(300, player.getMoney().get());
        assertFalse(player.getOwnedSquares().contains(property));
    }

    @ParameterizedTest(name = "Player money balance ${0} changed by -${1} should be equal to ${2}")
    @CsvSource({"100, 50, 150", "100, -50, 50", "100, -100, 0", "100, -101, 0", "100, -200, 0"})
    void changeMoneyBalanceByPositiveAmount(int playerMoney, int changeBy, int moneyBalanceResult) {
        // arrange
        Player player = new Player(null, 0, playerMoney, false, false, FXCollections.observableArrayList(), 0);

        // action
        player.changeMoneyBalanceBy(changeBy);

        // assert
        assertEquals(moneyBalanceResult, player.getMoney().get());
    }
}