package com.codecool.dungeoncrawl;

import com.codecool.dungeoncrawl.logic.Cell;
import com.codecool.dungeoncrawl.logic.GameMap;
import com.codecool.dungeoncrawl.logic.MapLoader;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.Objects;
import java.util.Optional;

public class Main extends Application {
    Stage mainStage;
    Scene scene;
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
        GridPane ui = new GridPane();
        ui.setPrefWidth(200);
        ui.setPadding(new Insets(10));

        ui.add(new Label("Health: "), 0, 0);
        ui.add(healthLabel, 1, 0);
        ui.add(new Label("Attack damage: "),0,1);
        ui.add(attackLabel,1,1);


        BorderPane borderPane = new BorderPane();

        borderPane.setCenter(canvas);
        borderPane.setRight(ui);

        Scene scene = new Scene(borderPane);
        mainStage.setScene(scene);
        refresh();
        scene.setOnKeyPressed(this::onKeyPressed);

        Image icon = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/icon.png")));
        primaryStage.getIcons().add(icon);
        primaryStage.setTitle("Dungeon Crawl - By L.A");
        primaryStage.show();
    }

    private void onKeyPressed(KeyEvent keyEvent) {
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
            if(map.getPlayer().getHealth()<=0) {
                gameRunning = false;
                showGameOver();
            }
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
        alert.setGraphic(new ImageView(this.getClass().getResource("/gameover2.png").toString()));
        alert.initOwner(mainStage);
        Optional<ButtonType> result = alert.showAndWait();
        if(result.isPresent() && result.get() == ButtonType.OK){
            try {
                restartGame();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }


    private void restartGame(){
        map = MapLoader.loadMap();
        gameRunning = true;
        refresh();
    }
}
