package cz.cvut.fel.pvj.nejedly.monopoly.view;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class MenuView {
    private final Scene scene;
    private final Pane pane;
    private final ImageView backgroundImage;

    public MenuView() {
        pane = new VBox();
        scene = new Scene(pane);
        backgroundImage = new ImageView();
    }

    public void init() {
        scene.getStylesheets().add("/stylesheets/MenuViewStyles.css");
        pane.setPrefSize(800, 800);

        initBackgroundImage();

        Label label1 = new Label("START A NEW GAME");
        label1.setId("heading1");

        HBox hBox1 = new HBox();
        Button twoPlayersBtn = new Button("2 PLAYERS");
        Button threePlayersBtn = new Button("3 PLAYERS");
        hBox1.getChildren().addAll(twoPlayersBtn, threePlayersBtn);

        HBox hBox2 = new HBox();
        Button fourPlayersBtn = new Button("4 PLAYERS");
        Button fivePlayersBtn = new Button("5 PLAYERS");
        Button sixPlayersBtn = new Button("6 PLAYERS");
        hBox2.getChildren().addAll(fourPlayersBtn, fivePlayersBtn, sixPlayersBtn);

        Label label2 = new Label("OTHER");
        label2.setId("heading2");

        Button loadGameBtn = new Button("LOAD A GAME");
        Button exitBtn = new Button("EXIT");

        pane.getChildren().addAll(backgroundImage, label1, hBox1, hBox2, label2, loadGameBtn, exitBtn);
    }

    private void initBackgroundImage() {
        backgroundImage.setImage(new Image("/images/monopoly_logo.png"));
        backgroundImage.setFitWidth(350);
        backgroundImage.setPreserveRatio(true);
    }

    public Scene getScene() {
        return scene;
    }
}
