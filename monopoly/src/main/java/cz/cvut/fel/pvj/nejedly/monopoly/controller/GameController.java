package cz.cvut.fel.pvj.nejedly.monopoly.controller;

import com.github.cliftonlabs.json_simple.JsonException;
import cz.cvut.fel.pvj.nejedly.monopoly.model.GameModel;
import cz.cvut.fel.pvj.nejedly.monopoly.model.board.squares.*;
import cz.cvut.fel.pvj.nejedly.monopoly.model.decks.cards.Card;
import cz.cvut.fel.pvj.nejedly.monopoly.model.decks.cards.MoveToCard;
import cz.cvut.fel.pvj.nejedly.monopoly.model.decks.cards.NearestSquareCard;
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

import java.io.FileNotFoundException;
import java.util.logging.Logger;

public class GameController {
    private final static Logger LOGGER = Logger.getLogger(GameController.class.getName());
    private MenuView menuView;
    private GameView gameView;
    private GameModel gameModel;

    public void initialize() {
        LOGGER.info("Initializing controller.");

        menuView = new MenuView(this);
        menuView.init();

        Stage stage = new Stage();
        stage.setTitle("Monopoly");
        stage.setResizable(false);
        stage.setScene(menuView.getScene());
        stage.show();

        LOGGER.info("Initializing controller ended.");
    }

    public void startNewGameButtonPressed(Scene oldScene, int numberOfPlayers) {
        LOGGER.info("Start new button pressed.");

        gameModel = new GameModel();
        gameModel.startNewGame(numberOfPlayers);

        gameView = new GameView(gameModel, this);
        gameView.init();

        changeScenes(oldScene, gameView.getScene());
    }

    private void changeScenes(Scene oldScene, Scene newScene) {
        LOGGER.info("Changing scenes.");

        Stage stage = (Stage) oldScene.getWindow();
        stage.setScene(newScene);
        stage.show();

        LOGGER.info("Changing scenes ended.");
    }

    public void rollButtonPressed() {
        LOGGER.info("Roll button pressed.");

        int steps = gameModel.getDie().roll();

        // player in jail logic
        if (gameModel.getActivePlayer().isInJail().getValue()) {
            if (!gameModel.getDie().isDoubles()) {
                gameView.getRollButton().setDisable(true);
                return;
            }

            // set player free from jail
            gameModel.getActivePlayer().setInJail(false);
            LOGGER.info("Set player free from jail.");
            new Alert(Alert.AlertType.INFORMATION, "Congratulations! You managed to get out of jail.", ButtonType.OK).show();
        }

        // advance player and play sprite animation
        movePlayer(steps);
    }

    private void movePlayer(int steps) {
        LOGGER.info("Moving player.");

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

        LOGGER.info("Moving player ended.");
    }

    public void endTurnButtonPressed(Scene oldScene) {
        LOGGER.info("End turn button pressed.");

        if (gameModel.hasGameEnded()) {
            changeScenes(oldScene, menuView.getScene());

            new Alert(
                    Alert.AlertType.INFORMATION,
                    "The winner is "+gameModel.getWinner().getName()+", congratulations!\n The game has ended, but you can always start a new one.",
                    ButtonType.OK
            ).showAndWait();
            LOGGER.fine("Show winner alert. Winner is: "+gameModel.getWinner().getName());
        } else {
            gameModel.setNextPlayerAsActive();
            gameView.getRollButton().setDisable(false);
        }
    }

    public void purchasePropertyButtonPressed() {
        LOGGER.info("Purchase property button pressed.");

        Player activePlayer = gameModel.getActivePlayer();
        int position = activePlayer.getBoardPosition().get();
        Square square = gameModel.getBoard().getBoardSquares()[position];

        if (!gameModel.getActivePlayer().purchaseSquare(square)) {
           new Alert(Alert.AlertType.INFORMATION, "This square is already purchased or cannot be purchased or you do not have enough funds.").showAndWait();
           LOGGER.fine("Show cannot be purchased alert.");
        }
    }

    public void saveGameButtonPressed(Scene oldScene) {
        LOGGER.info("Save game button pressed.");

        gameModel.save();
        changeScenes(oldScene, menuView.getScene());
    }

    public void loadGameButtonPressed(Scene oldScene) {
        LOGGER.info("Load game button pressed.");

        try {
            gameModel = GameModel.load();

            gameView = new GameView(gameModel, this);
            gameView.init();

            changeScenes(oldScene, gameView.getScene());
        } catch (FileNotFoundException e) {
            new Alert(Alert.AlertType.INFORMATION, "File not found.").showAndWait();
            throw new RuntimeException(e);
        } catch (JsonException e) {
            new Alert(Alert.AlertType.INFORMATION, "Parsing JSON error.").showAndWait();
            throw new RuntimeException(e);
        }
    }

    private SequentialTransition advancePlayerPositionBy(Player player, int steps) {
        LOGGER.info("Advance player's position animation started.");

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

            LOGGER.finest("Add transition to animation. Coordination X: "+t.getByX()+", Y: "+t.getByY());
        }

        // disable rollButton and endTurnButton while a sprite animation is running
        gameView.getRollButton().disableProperty().bind(sequentialTransition.statusProperty().isEqualTo(Animation.Status.RUNNING));
        gameView.getEndTurnButton().disableProperty().bind(sequentialTransition.statusProperty().isEqualTo(Animation.Status.RUNNING));

        player.advancePositionBy(steps);

        sequentialTransition.play();
        LOGGER.info("Play animation of "+player.getName()+"'s sprite.");
        LOGGER.info("Advance player's position animation ended.");

        return sequentialTransition;
    }

    public int[] calculateSpritePositionOnBoard(int boardPosition) {
        int x = 0;
        int y = 0;

        if (boardPosition < 0 || boardPosition > 39) {
            LOGGER.warning("Board position out of valid range 0-39. Board position: " + boardPosition);
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

        LOGGER.finest("Board position calculated. Coordination X: "+x+", Y: "+y);
        return new int[]{x,y};
    }

    public void sellPropertyButtonPressed() {
        LOGGER.info("Sell property button pressed.");

        ComboBox<Ownable> comboBox = new ComboBox<>();
        comboBox.getItems().addAll(gameModel.getActivePlayer().getOwnedSquares());

        Button sellButton = new Button("Sell property");
        sellButton.setOnAction(actionEvent -> {
            Ownable selectedItem = comboBox.getValue();
            if (selectedItem != null) LOGGER.fine("Selected property for sale: "+selectedItem.toString());
            gameModel.getActivePlayer().sellOwnedSquare((Square) selectedItem);
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
        LOGGER.info("Stepped on Tax square.");

        player.stepOnTax(tax);

        new Alert(
                Alert.AlertType.INFORMATION,
                "You stepped on "+tax.getName()+" and have to pay $"+tax.getTax()+" on taxes.",
                ButtonType.OK
        ).show();
        LOGGER.info("Show stepped on Tax square alert.");
    }

    private void steppedOnGoToJail(Player player, GoToJail goToJail) {
        LOGGER.info("Stepped on Go To Jail square.");
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
        LOGGER.info("Show stepped on Go To Jail square alert.");
    }

    private void steppedOnCards(Player player, Cards cards) {
        LOGGER.info("Stepped on Cards square.");

        Card card = gameModel.steppedOnCards(cards, player);

        new Alert(
                Alert.AlertType.INFORMATION,
                card.toString(),
                ButtonType.OK
        ).show();
        LOGGER.info("Show stepped on Cards square alert.");

        if (card instanceof MoveToCard moveToCard) {
            int steps = moveToCard.getSteps(player, gameModel.getBoard());
            movePlayer(steps);
        } else if (card instanceof NearestSquareCard nearestSquareCard) {
            int steps = nearestSquareCard.getSteps(player, gameModel.getBoard());
            movePlayer(steps);
        }
    }

    private void steppedOnOwnable(Player player, Ownable ownable) {
        LOGGER.info("Stepped on Ownable square.");

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
        LOGGER.info("Show stepped on Ownable square, which is owned by "+ownable.getOwner().getName());
    }
}