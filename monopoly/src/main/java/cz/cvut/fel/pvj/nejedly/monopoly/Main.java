package cz.cvut.fel.pvj.nejedly.monopoly;

import cz.cvut.fel.pvj.nejedly.monopoly.controller.GameController;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        GameController gameController = new GameController();
        gameController.initialize();
    }
}
