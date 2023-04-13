package cz.cvut.fel.pvj.nejedly.monopoly.controller;

import cz.cvut.fel.pvj.nejedly.monopoly.model.GameModel;
import cz.cvut.fel.pvj.nejedly.monopoly.model.player.Player;
import cz.cvut.fel.pvj.nejedly.monopoly.view.GameView;
import cz.cvut.fel.pvj.nejedly.monopoly.view.MenuView;
import javafx.animation.Animation;
import javafx.animation.PauseTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.Scene;
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
        advancePlayerPositionBy(gameModel.getActivePlayer(), steps);
    }

    public void endTurnButtonPressed() {
        gameModel.setNextPlayerAsActive();
        gameView.getRollButton().setDisable(false);
    }

    private void advancePlayerPositionBy(Player player, int steps) {
        SequentialTransition sequentialTransition = new SequentialTransition();

        // create sprite movement animation
        int currentPosition = player.getBoardPosition().get();
        for (int i = 1; i <= steps; i++) {
            int[] previousPosition = calculateSpritePositionOnBoard((currentPosition + i - 1) % 40);
            int[] nextPosition = calculateSpritePositionOnBoard((currentPosition+i) % 40);

            TranslateTransition t = new TranslateTransition(Duration.seconds(0.5), gameView.getSprites().get(player.getName()));
            t.setByX(nextPosition[0] - previousPosition[0]);
            t.setByY(nextPosition[1] - previousPosition[1]);

            sequentialTransition.getChildren().add(t);
            sequentialTransition.getChildren().add(new PauseTransition(Duration.seconds(0.1)));
        }

        // disable rollButton and endTurnButton while a sprite animation is running
        gameView.getRollButton().disableProperty().bind(sequentialTransition.statusProperty().isEqualTo(Animation.Status.RUNNING));
        gameView.getEndTurnButton().disableProperty().bind(sequentialTransition.statusProperty().isEqualTo(Animation.Status.RUNNING));

        if (gameModel.getDie().isDoubles()) {
            // remove a binding to be able to use rollButton.set() method
            sequentialTransition.setOnFinished(actionEvent -> gameView.getRollButton().disableProperty().unbind());
        } else {
            // disable rollButton after the animation is finished
            sequentialTransition.setOnFinished(actionEvent -> {
                gameView.getRollButton().disableProperty().unbind();
                gameView.getRollButton().setDisable(true);
            });
        }

        player.advancePositionBy(steps);
        sequentialTransition.play();
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
}