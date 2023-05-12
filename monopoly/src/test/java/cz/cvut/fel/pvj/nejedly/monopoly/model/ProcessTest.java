package cz.cvut.fel.pvj.nejedly.monopoly.model;

import cz.cvut.fel.pvj.nejedly.monopoly.model.board.Board;
import cz.cvut.fel.pvj.nejedly.monopoly.model.board.squares.*;
import cz.cvut.fel.pvj.nejedly.monopoly.model.decks.ChanceDeck;
import cz.cvut.fel.pvj.nejedly.monopoly.model.decks.cards.Card;
import cz.cvut.fel.pvj.nejedly.monopoly.model.decks.cards.CardType;
import cz.cvut.fel.pvj.nejedly.monopoly.model.decks.cards.PayMoneyCard;
import cz.cvut.fel.pvj.nejedly.monopoly.model.player.Player;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ProcessTest {

    @Test
    /* Test process:
     *   1) Player advanced by 4 steps.
     *   2) Player stepped on Tax square.
     *   3) Player did not have enough money to cover the tax.
     *   4) Auto-sell the cheapest property in order to cover the tax.
     *   5) After selling one property Player had enough money to cover the tax.
     */
    void stepOnTaxWithoutEnoughMoneyButWithEnoughPropertiesToSell() {
        // arrange
        ObservableList<Ownable> ownedSquares = FXCollections.observableArrayList();
        Player player1 = new Player("Player 1", 0, 100, false, false, ownedSquares, 0);
        Property property1 = new Property("Property 1", 10, null, 0, 250);
        Property property2 = new Property("Property 2", 20, null, 0, 150);
        Tax tax = new Tax("Income Tax", 4, 200);

        property1.setOwner(player1);
        property2.setOwner(player1);

        ownedSquares.add(property1);
        ownedSquares.add(property2);

        // action
        player1.advancePositionBy(4);

        // assert
        assertEquals(4, player1.getBoardPosition().get());

        // action
        player1.stepOnTax(tax);

        // assert
        assertEquals(50, player1.getMoney().get());
        assertTrue(player1.getOwnedSquares().contains(property1));
        assertFalse(player1.getOwnedSquares().contains(property2));
        assertFalse(property2.isOwned());
    }


    @Test
    /* Test process:
     *   1) Player 1 advanced by 3 steps.
     *   2) Player 1 stepped on Ownable.
     *   3) Player 1 bought the Ownable.
     *   4) Player 2 advanced by 3 steps.
     *   5) Player 2 stepped on Ownable owned by Player 1.
     *   6) Player 2 paid rent to Player 1.
     */
    void playerBoughtPropertyAndThenAnotherPlayerSteppedOnIt() {
        // arrange
        Player player1 = new Player("Player 1", 0, 500, false, false, FXCollections.observableArrayList(), 0);
        Player player2 = new Player("Player 2", 0, 500, false, false, FXCollections.observableArrayList(), 0);

        Property property1 = new Property("Property 1", 3, null, 50, 300);

        GameModel gameModel = new GameModel();

        // action
        player1.advancePositionBy(3);

        // assert
        assertEquals(3, player1.getBoardPosition().get());

        // action
        boolean isUnowned = gameModel.steppedOnOwnable(property1, player1);

        // assert
        assertTrue(isUnowned);
        assertEquals(500, player1.getMoney().get());

        // action
        player1.purchaseSquare(property1);

        // assert
        assertEquals(200, player1.getMoney().get());
        assertTrue(player1.getOwnedSquares().contains(property1));
        assertTrue(property1.isOwned());

        // action
        player2.advancePositionBy(3);

        // assert
        assertEquals(3, player2.getBoardPosition().get());

        // action
        gameModel.steppedOnOwnable(property1, player2);

        // assert
        assertEquals(450, player2.getMoney().get());
        assertEquals(250, player1.getMoney().get());
    }

    @Test
    /* Test process:
     *   1) Player advanced to Go To Jail square.
     *   2) Player stepped on Go To Jail.
     *   3) Player was sent to Jail.
     */
    void playerSteppedOnGoToJailSquareAndIsSendToJail() {
        // arrange
        Player player = new Player("Player 1", 0, 500, false, false, FXCollections.observableArrayList(), 0);
        GoToJail goToJail = new GoToJail(30, 10);

        // action
        player.advancePositionBy(goToJail.getPosition());

        // assert
        assertEquals(30, player.getBoardPosition().get());

        // action
        player.stepOnGoToJail();
        player.advancePositionBy((player.getBoardPosition().getValue() - goToJail.getJailPosition()));

        // assert
        assertEquals(goToJail.getJailPosition(), player.getBoardPosition().get());
        assertTrue(player.isInJail().getValue());
    }

    @Test
    /* Test process:
     *   1) Player 1 advanced to Cards square.
     *   2) Player 1 stepped on Cards square.
     *   3) Player 1 drew a card from Chance deck.
     *   4) Card - Player 1 had to pay each player $50. (bankrupt players do not receive any money)
     *   5) Player 1 paid each player $50, but he did not have enough funds, so now he is in debt.
     *   6) Player 1 is bankrupt and other (solvent) players have $50.
     */
    void playerSteppedOnCardsAndDrawPayEachPlayerCard() {
        // arrange
        Player player1 = new Player("Player 1", 0, 100, false, false, FXCollections.observableArrayList(), 0);
        Player player2 = new Player("Player 2", 0, 0, false, false, null, 0);
        Player player3 = new Player("Player 3", 0, 0, false, false, null, 0);
        Player player4 = new Player("Player 4", 0, 0, true, false, null, 0);
        Player player5 = new Player("Player 5", 0, 0, false, false, null, 0);

        Board board = mock(Board.class);
        ArrayList<Player> players = new ArrayList<>(Arrays.asList(player1, player2, player3, player4, player5));

        GameModel gameModel = new GameModel(players, player1, board);

        Cards cards = new Cards("Chance", 7, CardType.CHANCE);
        Card[] chanceCards = new Card[]{
                new PayMoneyCard(CardType.CHANCE, "You have been elected chairman of the board. Pay each player $50", 50, true)
        };
        when(board.getChanceDeck()).thenReturn(new ChanceDeck(chanceCards));

        // action
        player1.advancePositionBy(cards.getPosition());

        // assert
        assertEquals(cards.getPosition(), player1.getBoardPosition().get());

        // action
        gameModel.steppedOnCards(cards, player1);

        // assert
        assertEquals(0, player1.getMoney().get());
        assertTrue(player1.isBankrupt().getValue());

        assertEquals(50, player2.getMoney().get());
        assertEquals(50, player3.getMoney().get());
        assertEquals(50, player5.getMoney().get());

        assertEquals(0, player4.getMoney().get());
    }
}
