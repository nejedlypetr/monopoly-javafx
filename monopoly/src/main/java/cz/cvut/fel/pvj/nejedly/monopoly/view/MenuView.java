package cz.cvut.fel.pvj.nejedly.monopoly.view;

import cz.cvut.fel.pvj.nejedly.monopoly.controller.GameController;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MenuView {
    private final Scene scene;
    private final Pane pane;
    private final GameController controller;

    public MenuView(GameController controller) {
        this.controller = controller;

        pane = new VBox();
        scene = new Scene(pane);
    }

    public void init() {
        scene.getStylesheets().add("/stylesheets/MenuViewStyles.css");
        pane.setPrefSize(800, 800);

        Label newGameLabel = new Label("START A NEW GAME");
        newGameLabel.setId("heading1");

        Label otherLabel = new Label("OTHER");
        otherLabel.setId("heading2");

        Button loadGameButton = new Button("LOAD A GAME");
        loadGameButton.setOnAction(actionEvent -> controller.loadGameButtonPressed(scene));

        Button exitButton = new Button("EXIT");
        exitButton.setOnAction(actionEvent -> {
            Stage stage = (Stage) scene.getWindow();
            stage.close();
        });

        pane.getChildren().addAll(
            createBackgroundImage(),
            newGameLabel,
            createNewGameControls(2, 2),
            createNewGameControls(3, 4),
            otherLabel,
            loadGameButton,
            exitButton
        );
    }

    private HBox createNewGameControls(int numberOfButtons, int startingNumberOfPlayers) {
        HBox newGameButtons = new HBox();
        for (int i = startingNumberOfPlayers; i < (startingNumberOfPlayers + numberOfButtons); i++) {
            Button newGameButton = createNewGameButton(i);
            newGameButtons.getChildren().add(newGameButton);
        }
        return newGameButtons;
    }

    private ImageView createBackgroundImage() {
        ImageView backgroundImage = new ImageView();
        backgroundImage.setImage(new Image("/images/monopoly_logo.png"));
        backgroundImage.setFitWidth(350);
        backgroundImage.setPreserveRatio(true);
        return backgroundImage;
    }

    private Button createNewGameButton(int i) {
        Button button = new Button(i+" PLAYERS");
        button.setOnAction(actionEvent -> controller.startNewGameButtonPressed(scene, i));
        return button;
    }

    public Scene getScene() {
        return scene;
    }
}
