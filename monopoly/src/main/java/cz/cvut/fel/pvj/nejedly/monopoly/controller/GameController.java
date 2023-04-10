package cz.cvut.fel.pvj.nejedly.monopoly.controller;

import cz.cvut.fel.pvj.nejedly.monopoly.model.GameModel;
import cz.cvut.fel.pvj.nejedly.monopoly.view.GameView;
import cz.cvut.fel.pvj.nejedly.monopoly.view.MenuView;
import javafx.scene.Scene;
import javafx.stage.Stage;

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
        gameModel.getDie().roll();
    }
}