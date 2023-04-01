package cz.cvut.fel.pvj.nejedly.monopoly;

import cz.cvut.fel.pvj.nejedly.monopoly.view.GameView;
import cz.cvut.fel.pvj.nejedly.monopoly.view.MenuView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        Stage stage = new Stage();


        MenuView gameMenu = new MenuView();
        gameMenu.init();
        Scene gameMenuScene = gameMenu.getScene();

        GameView gameBoard = new GameView();
        gameBoard.init();
        Scene gameBoardScene = gameBoard.getScene();

        stage.setScene(gameBoardScene);
        stage.setTitle("Monopoly");
        stage.setResizable(false);
        stage.show();
    }
}
