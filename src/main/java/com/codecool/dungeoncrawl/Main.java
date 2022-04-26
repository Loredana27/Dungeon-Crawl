package com.codecool.dungeoncrawl;

import com.codecool.dungeoncrawl.logic.Cell;
import com.codecool.dungeoncrawl.logic.GameMap;
import com.codecool.dungeoncrawl.logic.MapLoader;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

public class Main extends Application {
    Stage mainStage;
    Scene scene;

    BorderPane uiContainer;
    GridPane uiDetails;

    GridPane ui;
    GridPane inventory;
    GridPane itemsToCollect;
    Button exitButton;
    GameMap map = MapLoader.loadMap(MapLoader.class.getResourceAsStream("/map.txt"));
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

        BorderPane borderPane = new BorderPane();

        borderPane.setCenter(canvas);
        borderPane.setRight(uiContainer);

        scene = new Scene(borderPane);
        mainStage.setScene(scene);

        canvas.setFocusTraversable(true);
        uiContainer.setFocusTraversable(true);
        scene.addEventHandler(KeyEvent.KEY_PRESSED,this::onKeyPressed);
        refresh();

        Image icon = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/icon.png")));
        primaryStage.getIcons().add(icon);
        primaryStage.setTitle("Dungeon Crawl - By L.A");
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    private void onKeyPressed(KeyEvent keyEvent) {
        if (gameRunning) {
            switch (keyEvent.getCode()) {
                case SPACE:
                    map.getPlayer().getEnemies().forEach(enemy -> {
                        if(map.getPlayer().getHealth() > 0)
                            gameRunning = map.getPlayer().startFight(enemy);
                    });
                    refresh();
                    break;
                case UP:
                    if(map.getPlayer().getCell().getNeighbor(0, -1).getActor() != null) {
                        String item = map.getPlayer().getCell().getNeighbor(0, -1).getActor().getTileName();
                        if (checkForKey(item)) break;
                    }
                    map.getPlayer().move(0, -1);
                    refresh();
                    break;
                case DOWN:
                    if(map.getPlayer().getCell().getNeighbor(0, 1).getActor()!=null) {
                        String item = map.getPlayer().getCell().getNeighbor(0, 1).getActor().getTileName();
                        if (checkForKey(item)) break;
                    }
                    map.getPlayer().move(0, 1);
                    refresh();
                    break;
                case LEFT:
                    if(map.getPlayer().getCell().getNeighbor(-1, 0).getActor()!=null) {
                        String item = map.getPlayer().getCell().getNeighbor(-1, 0).getActor().getTileName();
                        if(checkForKey(item)) break;
                    }
                    map.getPlayer().move(-1, 0);
                    refresh();
                    break;
                case RIGHT:
                    if(map.getPlayer().getCell().getNeighbor(1, 0).getActor() != null){
                        String item = map.getPlayer().getCell().getNeighbor(1, 0).getActor().getTileName();
                        if (checkForKey(item)) break;
                    }
                    map.getPlayer().move(1, 0);
                    refresh();
                    break;
            }
//            System.out.printf("X: %s   Y:%s\n",map.getPlayer().getX(),map.getPlayer().getY());
            checkForEnd();
        }
    }

    private boolean checkForKey(String item) {
        if (item.equals("door")) {
            if(map.getPlayer().getItems().containsKey("key")) {
                nextLevel();
            }
            return true;
        } else if (item.equals("sword") || item.equals("key")) {
            if (map.getAvailableItems().containsKey(item)) map.removeItem(item);
        }
        return false;
    }

    private void nextLevel() {
        int playerAD = map.getPlayer().getAttack();
        int playerHP = map.getPlayer().getHealth();
        HashMap<String,Integer> items = map.getPlayer().getItems();
        map = MapLoader.loadMap(MapLoader.class.getResourceAsStream("/map.txt"));
        map.getPlayer().setAttack(playerAD);
        map.getPlayer().setHealth(playerHP);

        map.getPlayer().setItems(items);
        refresh();
    }

    private void initUI(){
        uiContainer = new BorderPane();
        uiContainer.getStyleClass().add("mainPain");
        uiContainer.getStylesheets().add(getClass().getResource("/style/ui.css").toExternalForm());

        uiDetails = new GridPane();
        uiDetails.getStyleClass().add("GridPane");
        uiDetails.setPrefWidth(200);
        uiDetails.setPadding(new Insets(10));

        uiDetails.add(new Label("Health: "), 0, 0);
        uiDetails.add(healthLabel, 1, 0);
        Label attackLabelText = new Label("Attack damage: ");
        uiDetails.add(attackLabelText,0,1);
        GridPane.setConstraints(attackLabelText,0,1,2,1);
        uiDetails.add(attackLabel,2,1);

        inventory = new GridPane();
        inventory.getStyleClass().add("inventory");
        inventory.getChildren().add(new Label("Inventory:"));


        itemsToCollect = new GridPane();
        itemsToCollect.getStyleClass().add("itemsToCollect");
        itemsToCollect.getChildren().add(new Label("Available Itemes:"));

//        uiDetails.add(inventory,0,3);
//        GridPane.setConstraints(inventory,0,3,3,1);
//        GridPane.setConstraints(itemsToCollect,0,4,3,1);
//        ui.add(itemsToCollect,0,4);

        GridPane exitContainer = new GridPane();
        exitContainer.getStyleClass().add("ExitContainer");

        exitButton = new Button("Exit");
        exitButton.setOnAction(MouseEvent -> exit());

        exitContainer.add(exitButton, 1,0);

        uiContainer.setTop(uiDetails);
        uiContainer.setBottom(exitContainer);

    }

    private void refreshUI(){
        refreshInventory();
        refreshItems();
        ui = new GridPane();
        ui.getStyleClass().add("items");
        inventory.getStyleClass().add("inventory");
        itemsToCollect.getStyleClass().add("itemsToCollect");
        ui.add(inventory,0,0);
        ui.add(itemsToCollect,0,1);
        uiContainer.setCenter(ui);
    }

    private void refreshInventory(){
        inventory = new GridPane();
        AtomicInteger row = new AtomicInteger(1);
        Label label = new Label("Inventory:");
        inventory.add(label,0,0);
        GridPane.setConstraints(label,0,0,2,1);
        HashMap<String,Integer> items = map.getPlayer().getItems();
        items.keySet().forEach(item -> {
            inventory.add(new Label("•"),0,row.get());
            inventory.add(new Label(String.format("%s: %s",item,items.get(item))),1,row.get());
            row.getAndIncrement();
        });
    }

    private void refreshItems(){
        itemsToCollect = new GridPane();
        AtomicInteger row = new AtomicInteger(1);
        Label label = new Label("Available items: ");
        itemsToCollect.add(label,0,0);
        GridPane.setConstraints(label,0,0,2,1);
        HashMap<String,Integer> items = map.getAvailableItems();
        items.keySet().forEach(item -> {
            itemsToCollect.add(new Label("•"),0,row.get());
            itemsToCollect.add(new Label(String.format("%s: %s",item,items.get(item))),1,row.get());
            row.getAndIncrement();
        });
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
        refreshUI();
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
        map = MapLoader.loadMap(MapLoader.class.getResourceAsStream("/map.txt"));
        gameRunning = true;
        refresh();
    }
}
