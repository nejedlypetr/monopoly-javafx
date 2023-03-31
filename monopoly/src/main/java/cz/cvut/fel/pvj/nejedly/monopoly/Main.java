package cz.cvut.fel.pvj.nejedly.monopoly;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        Stage stage = new Stage();
        Scene scene = new Scene(new Pane());
        stage.setTitle("Monopoly");
        stage.setScene(scene);
        stage.show();
    }
}
