package cz.cvut.fel.pvj.nejedly.monopoly.view;

import cz.cvut.fel.pvj.nejedly.monopoly.controller.GameController;
import cz.cvut.fel.pvj.nejedly.monopoly.model.GameModel;
import cz.cvut.fel.pvj.nejedly.monopoly.model.board.squares.*;
import cz.cvut.fel.pvj.nejedly.monopoly.model.player.Player;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;

public class GameView {
    private final GameModel gameModel;
    private final GameController controller;
    private final Scene scene;
    private final BorderPane pane;
    private final ImageView boardImageView;
    private final Button rollButton;
    private final Button endTurnButton;
    private final Button purchaseButton;
    private final Button sellButton;
    private final Button saveGameButton;

    public GameView(GameModel gameModel, GameController gameController) {
        this.gameModel = gameModel;
        controller = gameController;

        pane = new BorderPane();
        scene = new Scene(pane);
        boardImageView = createImage("/images/board.jpg", 700);;
        rollButton = new Button("ROLL DICE");
        rollButton.setOnAction(actionEvent -> controller.rollButtonPressed());
        endTurnButton = new Button("END TURN");
        purchaseButton = new Button("PURCHASE PROPERTY");
        sellButton = new Button("SELL PROPERTY");
        saveGameButton = new Button("SAVE GAME & EXIT");
    }

    public void init() {
        scene.getStylesheets().add("/stylesheets/GameViewStyles.css");
        pane.setPrefSize(1400, 875);

        initBorderPaneLeft();
        initBorderPaneCenter();
        initBorderPaneBottom();
    }

    private void initBorderPaneCenter() {
        HBox player = createCurrentPlayerLabel();
        VBox square = createSquareInformationBox(new Property("House", 3, PropertyGroup.GREEN, 3, 2));
        VBox roll = createRollInformationBox();

        HBox controls1 = new HBox(rollButton, endTurnButton);
        controls1.setAlignment(Pos.CENTER);
        controls1.setSpacing(30);

        HBox controls2 = new HBox(purchaseButton, sellButton);
        controls2.setAlignment(Pos.CENTER);
        controls2.setSpacing(30);

        VBox center = new VBox(player, square, roll, controls1, controls2, saveGameButton);
        center.setAlignment(Pos.CENTER);
        center.setSpacing(30);
        pane.setCenter(center);
    }

    private VBox createRollInformationBox() {
        Label label = new Label("LAST DICE ROLL");
        label.setId("heading3");
        Label description = new Label();
        description.textProperty().bind(Bindings.createStringBinding(() -> gameModel.getDie().toString(), gameModel.getDie().getDieOneRoll(), gameModel.getDie().getDieTwoRoll()));

        VBox vBox = new VBox(label, description);
        vBox.setAlignment(Pos.CENTER);
        return vBox;
    }

    private VBox createSquareInformationBox(Square square) {
        Label label = new Label("CURRENT SQUARE");
        label.setId("heading3");
        Label description = new Label();
        description.textProperty().bind(Bindings.createStringBinding(() -> gameModel.getBoard().getBoardSquares()[gameModel.getActivePlayer().getBoardPosition().getValue()].toString()));

        VBox vBox = new VBox(label, description);
        vBox.setAlignment(Pos.TOP_CENTER);
        vBox.setPrefHeight(150);
        return vBox;
    }

    private HBox createCurrentPlayerLabel() {
        ImageView avatar = createImage("/sprites/" + gameModel.getActivePlayer().getSpriteImage(), 40);

        Label playerName = new Label(" "+gameModel.getActivePlayer().getName()+"'s turn");
        playerName.setId("playerName");

        HBox hBox = new HBox(avatar, playerName);
        hBox.setAlignment(Pos.CENTER);
        return hBox;
    }

    private void initBorderPaneBottom() {
        HBox bottom = new HBox();
        for (Player player : gameModel.getPlayers()) {
            VBox playerInfoBox = createPlayerInfoBox(player);
            bottom.getChildren().add(playerInfoBox);
        }
        pane.setBottom(bottom);
    }

    private VBox createPlayerInfoBox(Player player) {
        ImageView avatar = createImage("/sprites/"+player.getSpriteImage(), 30);
        Label label = new Label(" "+player.getName()+" - ");
//        Label money = new Label(player.getMoney()+"$");

        Label money = new Label();
        money.textProperty().bind(Bindings.concat("$", player.getMoney().asString()));

        HBox playerInfo = new HBox(avatar, label, money);
        playerInfo.setPadding(new Insets(0, 0, 5, 5));

        TableView<Square> table = createEmptyTableView();
        table.setMaxHeight(130);
        table.setSelectionModel(null);
        table.setItems(player.getOwnedSquares());

        VBox infoBox = new VBox(playerInfo, table);
        HBox.setHgrow(infoBox, Priority.ALWAYS);
        return infoBox;
    }

    private TableView<Square> createEmptyTableView() {
        TableView<Square> table = new TableView<>();

        TableColumn<Square, String> nameColumn = new TableColumn<>("Property");
        TableColumn<Square, Number> priceColumn = new TableColumn<>("Price");
        TableColumn<Square, String> rentColumn = new TableColumn<>("Rent");
        TableColumn<Square, String> colorColumn = new TableColumn<>("Color");

        nameColumn.setSortable(false);
        priceColumn.setSortable(false);
        rentColumn.setSortable(false);
        colorColumn.setSortable(false);

        nameColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getName()));

        priceColumn.setCellValueFactory(data -> {
            Square square = data.getValue();
            if (square instanceof Property property) {
                return new SimpleIntegerProperty(property.getPurchasePrice());
            } else if (square instanceof Utility utility) {
                return new SimpleIntegerProperty(utility.getPurchasePrice());
            } else if (square instanceof Railroad railroad) {
                return new SimpleIntegerProperty(railroad.getPurchasePrice());
            }
            return null;
        });

        rentColumn.setCellValueFactory(data -> {
            Square square = data.getValue();
            if (square instanceof Property property) {
                return new SimpleStringProperty(Integer.toString(property.getRent()));
            } else if (square instanceof Utility) {
                return new SimpleStringProperty("5 * (dice roll)");
            } else if (square instanceof Railroad railroad) {
                return new SimpleStringProperty(Integer.toString(railroad.getRent()));
            }
            return null;
        });

        colorColumn.setCellValueFactory(data -> {
            Square square = data.getValue();
            if (square instanceof Property property) {
                return new SimpleStringProperty(property.getGroup().toString());
            }
            return new SimpleStringProperty("----");
        });

        table.getColumns().add(nameColumn);
        table.getColumns().add(priceColumn);
        table.getColumns().add(rentColumn);
        table.getColumns().add(colorColumn);

        return table;
    }

    private void initBorderPaneLeft() {
        Pane left = new Pane();
        left.setMaxSize(700, 700);
        left.getChildren().add(boardImageView);
        // todo add players sprites
        pane.setLeft(left);
    }

    private ImageView createImage(String uri, double width) {
        ImageView imageView = new ImageView();
        imageView.setImage(new Image(uri));
        imageView.setFitWidth(width);
        imageView.setPreserveRatio(true);
        return imageView;
    }

    public Scene getScene() {
        return scene;
    }
}