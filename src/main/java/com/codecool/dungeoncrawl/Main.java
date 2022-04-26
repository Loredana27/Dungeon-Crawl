package com.codecool.dungeoncrawl;

import com.codecool.dungeoncrawl.logic.Cell;
import com.codecool.dungeoncrawl.logic.GameMap;
import com.codecool.dungeoncrawl.logic.MapLoader;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.Objects;
import java.util.Optional;

public class Main extends Application {
    Stage mainStage;
    Scene scene;
    GridPane inventory;
    GridPane itemsToCollect;
    Button exitButton;
    GameMap map = MapLoader.loadMap();
    Canvas canvas = new Canvas(
            map.getWidth() * Tiles.TILE_WIDTH,
            map.getHeight() * Tiles.TILE_WIDTH);
    GraphicsContext context = canvas.getGraphicsContext2D();
    Label healthLabel = new Label();
    Label attackLabel = new Label();
    boolean gameRunning = true;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage){
        mainStage = primaryStage;

        initUI();

        Image icon = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/icon.png")));
        primaryStage.getIcons().add(icon);
        primaryStage.setTitle("Dungeon Crawl - By L.A");
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    private void onKeyPressed(KeyEvent keyEvent) {
        System.out.println(keyEvent.getCode());
        if (gameRunning) {
            switch (keyEvent.getCode()) {
                case UP:
                    map.getPlayer().move(0, -1);
                    refresh();
                    break;
                case DOWN:
                    map.getPlayer().move(0, 1);
                    refresh();
                    break;
                case LEFT:
                    map.getPlayer().move(-1, 0);
                    refresh();
                    break;
                case RIGHT:
                    map.getPlayer().move(1, 0);
                    refresh();
                    break;
            }
            checkForEnd();
        }
    }

    private void initUI(){
        BorderPane uiContainer = new BorderPane();
        uiContainer.getStyleClass().add("mainPain");
        uiContainer.getStylesheets().add(getClass().getResource("/style/ui.css").toExternalForm());

        GridPane ui = new GridPane();
        ui.getStyleClass().add("GridPane");
        ui.setPrefWidth(200);
        ui.setPadding(new Insets(10));

        ui.add(new Label("Health: "), 0, 0);
        ui.add(healthLabel, 1, 0);
        Label attackLabelText = new Label("Attack damage: ");
        ui.add(attackLabelText,0,1);
        GridPane.setConstraints(attackLabelText,0,1,2,1);
        ui.add(attackLabel,2,1);



        GridPane exitContainer = new GridPane();
        exitContainer.getStyleClass().add("ExitContainer");

        exitButton = new Button("Exit");
        exitButton.setOnAction(MouseEvent -> exit());

        exitContainer.add(exitButton, 1,0);

        uiContainer.setCenter(ui);
        uiContainer.setBottom(exitContainer);


        BorderPane borderPane = new BorderPane();

        borderPane.setCenter(canvas);
        borderPane.setRight(uiContainer);

        scene = new Scene(borderPane);
        mainStage.setScene(scene);

        canvas.setFocusTraversable(true);
        uiContainer.setFocusTraversable(true);
        scene.addEventHandler(KeyEvent.KEY_PRESSED,this::onKeyPressed);
        refresh();
    }


    private void exit(){
        System.exit(1);
    }

    private void checkForEnd(){
        if(map.getPlayer().getHealth()<=0) {
            gameRunning = false;
            showGameOver();
        }
    }

    private void refresh() {
        context.setFill(Color.BLACK);
        context.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        for (int x = 0; x < map.getWidth(); x++) {
            for (int y = 0; y < map.getHeight(); y++) {
                Cell cell = map.getCell(x, y);
                if (cell.getActor() != null) {
                    Tiles.drawTile(context, cell.getActor(), x, y);
                } else {
                    Tiles.drawTile(context, cell, x, y);
                }
            }
        }
        healthLabel.setText("" + map.getPlayer().getHealth());
        attackLabel.setText("" + map.getPlayer().getAttack());
    }

    public void showGameOver(){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION,null);
        alert.setTitle("You lost!");
        alert.setHeaderText(null);
        alert.setContentText("");
        alert.setGraphic(null);
        alert.initOwner(mainStage);

        alert.getButtonTypes().clear();
        ButtonType buttonYes = new ButtonType("Yes!", ButtonBar.ButtonData.OK_DONE);
        ButtonType buttonNo = new ButtonType("No...Sorry", ButtonBar.ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().add(0,buttonYes);
        alert.getButtonTypes().add(1,buttonNo);

        System.out.println();
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(getClass().getResource("/style/gameOver.css").toExternalForm());
        dialogPane.getStyleClass().add("gameOverPane");
        Optional<ButtonType> result = alert.showAndWait();
        if(result.isPresent() && result.get() == buttonYes){
            try {
                restartGame();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else if (result.get() == buttonNo) {
            System.exit(1);
        }
    }


    private void restartGame(){
        map = MapLoader.loadMap();
        gameRunning = true;
        refresh();
    }
}
