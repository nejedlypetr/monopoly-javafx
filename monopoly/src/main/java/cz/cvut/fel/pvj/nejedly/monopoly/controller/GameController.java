package cz.cvut.fel.pvj.nejedly.monopoly.controller;

import cz.cvut.fel.pvj.nejedly.monopoly.model.GameModel;
import cz.cvut.fel.pvj.nejedly.monopoly.model.board.squares.*;
import cz.cvut.fel.pvj.nejedly.monopoly.model.decks.cards.Card;
import cz.cvut.fel.pvj.nejedly.monopoly.model.player.Player;
import cz.cvut.fel.pvj.nejedly.monopoly.view.GameView;
import cz.cvut.fel.pvj.nejedly.monopoly.view.MenuView;
import javafx.animation.Animation;
import javafx.animation.PauseTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;
import javafx.util.Duration;

public class GameController {
    private MenuView menuView;
    private GameView gameView;
    private GameModel gameModel;

    public void initialize() {
        menuView = new MenuView(this);
        menuView.init();

        Stage stage = new Stage();
        stage.setTitle("Monopoly");
        stage.setResizable(false);
        stage.setScene(menuView.getScene());
        stage.show();
    }

    public void startNewGameButtonPressed(Scene oldScene, int numberOfPlayers) {
        gameModel = new GameModel();
        gameModel.startNewGame(numberOfPlayers);

        gameView = new GameView(gameModel, this);
        gameView.init();

        changeScenes(oldScene, gameView.getScene());
    }

    private void changeScenes(Scene oldScene, Scene newScene) {
        Stage stage = (Stage) oldScene.getWindow();
        stage.setScene(newScene);
        stage.show();
    }

    public void rollButtonPressed() {
        int steps = gameModel.getDie().roll();

        // player in jail logic
        if (gameModel.getActivePlayer().isInJail().getValue()) {
            if (!gameModel.getDie().isDoubles()) {
                gameView.getRollButton().setDisable(true);
                return;
            }

            // set player free from jail
            gameModel.getActivePlayer().setInJail(false);
            new Alert(Alert.AlertType.INFORMATION, "Congratulations! You managed to get out of jail.", ButtonType.OK).show();
        }

        // advance player and play sprite animation
        SequentialTransition spriteMovementAnimation = advancePlayerPositionBy(gameModel.getActivePlayer(), steps);
        spriteMovementAnimation.setOnFinished(actionEvent -> {
            if (gameModel.getDie().isDoubles()) {
                // remove a binding to be able to use rollButton.set() method
                gameView.getRollButton().disableProperty().unbind();
            } else {
                // disable rollButton after the animation is finished
                gameView.getRollButton().disableProperty().unbind();
                gameView.getRollButton().setDisable(true);
            }

            // run action based on what square has player landed on
            int activePlayerNewBoardPosition = gameModel.getActivePlayer().getBoardPosition().get();
            Square square = gameModel.getBoard().getBoardSquares()[activePlayerNewBoardPosition];
            stepOnSquare(gameModel.getActivePlayer(), square);
        });
    }

    public void endTurnButtonPressed(Scene oldScene) {
        if (gameModel.hasGameEnded()) {
            changeScenes(oldScene, menuView.getScene());
            new Alert(
                    Alert.AlertType.INFORMATION,
                    "The winner is "+gameModel.getWinner().getName()+", congratulations!\n The game has ended, but you can always start a new one.",
                    ButtonType.OK
            ).showAndWait();
        } else {
            gameModel.setNextPlayerAsActive();
            gameView.getRollButton().setDisable(false);
        }
    }

    public void purchasePropertyButtonPressed() {
        Player activePlayer = gameModel.getActivePlayer();
        int position = activePlayer.getBoardPosition().get();
        Square square = gameModel.getBoard().getBoardSquares()[position];

        if (!gameModel.getActivePlayer().purchaseSquare(square)) {
           new Alert(Alert.AlertType.INFORMATION, "This square is already purchased or cannot be purchased or you do not have enough funds.").showAndWait();
        }
    }

    private SequentialTransition advancePlayerPositionBy(Player player, int steps) {
        SequentialTransition sequentialTransition = new SequentialTransition();

        // create sprite movement animation
        int currentPosition = player.getBoardPosition().get();
        for (int i = 1; i <= steps; i++) {
            int[] previousPosition = calculateSpritePositionOnBoard((currentPosition + i - 1) % 40);
            int[] nextPosition = calculateSpritePositionOnBoard((currentPosition+i) % 40);

            TranslateTransition t = new TranslateTransition(Duration.seconds(0.3), gameView.getSprites().get(player.getName()));
            t.setByX(nextPosition[0] - previousPosition[0]);
            t.setByY(nextPosition[1] - previousPosition[1]);

            sequentialTransition.getChildren().add(t);
            sequentialTransition.getChildren().add(new PauseTransition(Duration.seconds(0.08)));
        }

        // disable rollButton and endTurnButton while a sprite animation is running
        gameView.getRollButton().disableProperty().bind(sequentialTransition.statusProperty().isEqualTo(Animation.Status.RUNNING));
        gameView.getEndTurnButton().disableProperty().bind(sequentialTransition.statusProperty().isEqualTo(Animation.Status.RUNNING));

        player.advancePositionBy(steps);
        sequentialTransition.play();
        return sequentialTransition;
    }

    public int[] calculateSpritePositionOnBoard(int boardPosition) {
        int x = 0;
        int y = 0;

        if (boardPosition < 0 || boardPosition > 39) {
            throw new RuntimeException("Board position out of valid range 0-39. Board position: " + boardPosition);
        }

        if (boardPosition <= 9) { // squares 0-9
            y = 785;
            if (boardPosition == 0) {
                x = 785;
            } else {
                int xCoordinateOfBoardPosition1 = 677;
                x = xCoordinateOfBoardPosition1 - ((boardPosition - 1) * 68);
            }
        } else if (boardPosition <= 19) { // squares 10-19
            x = 25;
            if (boardPosition == 10) {
                y = 785;
            } else {
                int yCoordinateOfBoardPosition11 = 677;
                y = yCoordinateOfBoardPosition11 - ((boardPosition-11) * 68);
            }
        } else if (boardPosition <= 29) { // squares 20-29
            y = 25;
            if (boardPosition == 20) {
                x = 25;
            } else {
                int xCoordinateOfBoardPosition21 = 133;
                x = xCoordinateOfBoardPosition21 + ((boardPosition-21) * 68);
            }
        } else { // squares 30-39
            x = 785;
            if (boardPosition == 30) {
                y = 25;
            } else {
                int yCoordinateOfBoardPosition11 = 133;
                y = yCoordinateOfBoardPosition11 + ((boardPosition-31) * 68);
            }
        }

        return new int[]{x,y};
    }

    public void sellPropertyButtonPressed() {
        ComboBox<Ownable> comboBox = new ComboBox<>();
        comboBox.getItems().addAll(gameModel.getActivePlayer().getOwnedSquares());

        Button sellButton = new Button("Sell property");
        sellButton.setOnAction(actionEvent -> {
            Ownable selectedItem = comboBox.getValue();
            gameModel.getActivePlayer().sellOwnedSquare((Square) selectedItem);
            System.out.println(gameModel.getActivePlayer().getName() + " sold " + selectedItem); // todo LOGGER
            ((Stage) sellButton.getScene().getWindow()).close();
        });

        gameView.showSellPropertyDialog(comboBox, sellButton);
    }

    private void stepOnSquare(Player player, Square square) {
        if (square instanceof Ownable ownable) {
            steppedOnOwnable(player, ownable);
        } else if (square instanceof Cards cards) {
            steppedOnCards(player, cards);
        } else if (square instanceof GoToJail goToJail) {
            steppedOnGoToJail(player, goToJail);
        } else if (square instanceof Tax tax) {
            steppedOnTax(player, tax);
        }
    }

    private void steppedOnTax(Player player, Tax tax) {
        player.stepOnTax(tax);

        new Alert(
                Alert.AlertType.INFORMATION,
                "You stepped on "+tax.getName()+" and have to pay $"+tax.getTax()+" on taxes.",
                ButtonType.OK
        ).show();
    }

    private void steppedOnGoToJail(Player player, GoToJail goToJail) {
        player.stepOnGoToJail();

        // animate sprite movement to jail square
        SequentialTransition spriteMovementAnimation = advancePlayerPositionBy(player, (player.getBoardPosition().getValue() - goToJail.getJailPosition()));
        spriteMovementAnimation.setOnFinished(actionEvent -> {
            // disable roll button
            gameView.getRollButton().disableProperty().unbind();
            gameView.getRollButton().setDisable(true);
        });

        new Alert(
                Alert.AlertType.INFORMATION,
                "You have been send to jail! You stepped on Go To Jail square.\nTo get out of jail you need to roll doubles.",
                ButtonType.OK
        ).show();
    }

    private void steppedOnCards(Player player, Cards cards) {
        Card card = gameModel.steppedOnCards(cards, player);

        new Alert(
                Alert.AlertType.INFORMATION,
                card.toString(),
                ButtonType.OK
        ).show();
    }

    private void steppedOnOwnable(Player player, Ownable ownable) {
        if (gameModel.steppedOnOwnable(ownable, player)) return;

        int rent;
        if (ownable instanceof Utility utility) {
            rent = utility.getRent(gameModel.getDie());
        } else rent = ownable.getRent();

        new Alert(
                Alert.AlertType.INFORMATION,
                "You stepped on "+((Square) ownable).getName()+", which is owned by "+ownable.getOwner().getName()+".\nPay rent $"+rent+".",
                ButtonType.OK
        ).show();
    }
}