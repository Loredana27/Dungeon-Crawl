package com.codecool.dungeoncrawl;

import com.codecool.dungeoncrawl.logic.Cell;
import com.codecool.dungeoncrawl.logic.CellType;
import com.codecool.dungeoncrawl.logic.GameMap;
import com.codecool.dungeoncrawl.logic.MapLoader;
import com.codecool.dungeoncrawl.logic.actors.OpenedDoor;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.io.File;
import java.util.HashMap;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

public class Main extends Application {
    Stage mainStage;
    Scene scene;

    int actualMap = 1;
    String firstMap= "/map.txt";
    String secondMap = "/map2.txt";
    String thirdMap= "/map3.txt";

    BorderPane uiContainer;
    GridPane uiDetails;

    GridPane ui;
    GridPane inventory;
    GridPane itemsToCollect;
    Button exitButton;
    GameMap map = MapLoader.loadMap(MapLoader.class.getResourceAsStream(firstMap));
    Canvas canvas = new Canvas(
            map.getWidth() * Tiles.TILE_WIDTH,
            map.getHeight() * Tiles.TILE_WIDTH);
    GraphicsContext context = canvas.getGraphicsContext2D();
    Label healthLabel = new Label();
    Label attackLabel = new Label();
    Label nameLabel = new Label();
    boolean gameRunning = true;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage){
        mainStage = primaryStage;

        initUI();

        BorderPane borderPane = new BorderPane();


        borderPane.setTop(initMenuBar());
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
        primaryStage.setTitle("Dungeon Crawl By L.A");
        primaryStage.setResizable(false);
        primaryStage.show();

//        map = MapLoader.loadMap(MapLoader.class.getResourceAsStream(thirdMap));
//        refresh();

        playSound();
        getPlayerName();
    }


    public void playSound(){
        Media sound = new Media(getClass().getResource("/sounds/music.mp3").toExternalForm());
        MediaPlayer mediaPlayer = new MediaPlayer(sound);
        mediaPlayer.play();
    }

    private void onKeyPressed(KeyEvent keyEvent) {
        if (gameRunning) {
            String text;
            int health = map.getPlayer().getHealth();
            switch (keyEvent.getCode()) {
                case SPACE:
                    map.getPlayer().getEnemies().forEach(enemy -> {
                        if(map.getPlayer().getHealth() > 0) {
                            gameRunning = map.getPlayer().startFight(enemy);
                            if(gameRunning) map.removeEnemy(enemy);
                        }
                    });
                    refresh();
                    break;
                case H:
                    showControlsDialog();
                    break;
                case E:
                    try{
                        String item = map.getPlayer().getCell().getTempItem();
                        map.removeItem(map.getPlayer().getCell().getTempItem());
                        map.getPlayer().pickupItem();
                        refresh();
                        text = String.format("You picked up a %s!",item);
                        showCanvasMessage(text,140,20);
                    }catch (NullPointerException ignored){}
                    break;
                case UP:
                case W:
                    map.getGameAI().forEach(AI -> AI.chooseAMove(map.getPlayer()));
                    map.getPlayer().move(0, -1);
                    if(map.getDoor() != null)
                        if(map.getPlayer().checkForKey() && map.getDoor().getTileName().equals("door"))
                            map.setDoor(new OpenedDoor(map.getDoor().getCell()));
                    refresh();
                    break;
                case DOWN:
                case S:
                    map.getGameAI().forEach(AI -> AI.chooseAMove(map.getPlayer()));
                    map.getPlayer().move(0, 1);
                    if(map.getDoor() != null)
                        if(map.getPlayer().checkForKey() && map.getDoor().getTileName().equals("door"))
                            map.setDoor(new OpenedDoor(map.getDoor().getCell()));
                    refresh();
                    break;
                case LEFT:
                case A:
                    map.getGameAI().forEach(AI -> AI.chooseAMove(map.getPlayer()));
                    if(map.getDoor() != null)
                        if(map.getPlayer().checkForKey() && map.getDoor().getTileName().equals("door"))
                            map.setDoor(new OpenedDoor(map.getDoor().getCell()));
                    map.getPlayer().move(-1, 0);
                    refresh();
                    break;
                case RIGHT:
                case D:
                    map.getGameAI().forEach(AI -> AI.chooseAMove(map.getPlayer()));
                    if(map.getDoor() != null)
                        if(map.getPlayer().checkForKey() && map.getDoor().getTileName().equals("door"))
                            map.setDoor(new OpenedDoor(map.getDoor().getCell()));
                    map.getPlayer().move(1, 0);
                    refresh();
                    break;
            }

            if(map.getPlayer().getCell().getType().equals(CellType.PORTAL)){
                map.teleport();
                refresh();
            }
            if(map.getPlayer().getCell().getType().equals(CellType.CUP)) showYouWin();
            if(health > map.getPlayer().getHealth()) {
                text = String.format("You lost %s health points in battle!", health - map.getPlayer().getHealth());
                showCanvasMessage(text, 190, 20);
            }
            if(map.getDoor() != null) if(map.getPlayer().getCell().equals(map.getDoor().getCell())) nextLevel();
            checkForEnd();
        }
    }

    private void getPlayerName(){
        TextInputDialog inputDialog = new TextInputDialog();
        inputDialog.initOwner(mainStage);
        inputDialog.setGraphic(null);
        inputDialog.setTitle("Character name needed!");
        inputDialog.setHeaderText(null);
        inputDialog.setContentText("Please insert your name: ");
        inputDialog.getDialogPane().getStylesheets().add(getClass().getResource("/style/getName.css").toExternalForm());
        inputDialog.getDialogPane().getStyleClass().add("getInputPane");
        inputDialog.getEditor().getStyleClass().add("textInput");
        inputDialog.getDialogPane().getButtonTypes().clear();
        ButtonType buttonTypeOK = new ButtonType("Insert", ButtonBar.ButtonData.OK_DONE);
        ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        inputDialog.getDialogPane().getButtonTypes().add(buttonTypeOK);
        inputDialog.getDialogPane().getButtonTypes().add(buttonTypeCancel);
        Optional<String> result = inputDialog.showAndWait();
        if(result.isPresent() && !result.get().equals("")){
            try {
                nameLabel.setText(inputDialog.getEditor().getText());
                map.getPlayer().setName(inputDialog.getEditor().getText());
            } catch (Exception e) {
//                throw new RuntimeException(e);
                e.printStackTrace();
            }
        } else {
            System.exit(1);
        }
        refresh();
    }

    private void nextLevel() {
        int playerAD = map.getPlayer().getAttack();
        int playerHP = map.getPlayer().getHealth();
        HashMap<String,Integer> items = map.getPlayer().getItems();
        String nextMap;
        if(actualMap == 1) {
            nextMap = secondMap;
            actualMap++;
        }
        else if(actualMap == 2) {
            nextMap = thirdMap;
            actualMap++;
        }
        else{
            gameRunning = false;
            return;
        }
        if(!nextMap.equals(""))
            map = MapLoader.loadMap(MapLoader.class.getResourceAsStream(nextMap));
        map.getPlayer().setAttack(playerAD);
        map.getPlayer().setHealth(playerHP);
        map.getPlayer().setItems(items);
        map.getPlayer().setName(nameLabel.getText());

        refresh();
    }

    private MenuBar initMenuBar(){
        MenuBar menuBar = new MenuBar();
        menuBar.getStyleClass().add("menubar");
        menuBar.getStylesheets().add(getClass().getResource("/style/menubar.css").toExternalForm());

        Menu gameMenu = new Menu("Game");
        gameMenu.getStyleClass().add("menu");

        MenuItem newGame = new MenuItem("New Game");
        MenuItem saveGameDB = new MenuItem("Save Game in Database");
        MenuItem saveGameFile = new MenuItem("Save Game in File");
        MenuItem loadDBGame = new MenuItem("Load Database Game");
        MenuItem loadFileGame = new MenuItem("Load File Game");
        MenuItem exit = new MenuItem("Exit");
        newGame.setOnAction(e->{
            getPlayerName();
            restartGame();
        });
        exit.setOnAction(e-> System.exit(1));

        gameMenu.getItems().add(newGame);
        gameMenu.getItems().add(saveGameDB);
        gameMenu.getItems().add(saveGameFile);
        gameMenu.getItems().add(loadDBGame);
        gameMenu.getItems().add(loadFileGame);
        gameMenu.getItems().add(exit);



        Menu help = new Menu("Help");
//        help.getStyleClass().add("menu");


        MenuItem controls = new MenuItem("Controls");
        MenuItem about = new MenuItem("About...");

        controls.setOnAction(e -> showControlsDialog());
        about.setOnAction(e -> showAboutDialog());

        help.getItems().add(controls);
        help.getItems().add(about);


        menuBar.getMenus().add(gameMenu);
        menuBar.getMenus().add(help);
        return menuBar;
    }

    private void initUI(){
        uiContainer = new BorderPane();
        uiContainer.getStyleClass().add("mainPain");
        uiContainer.getStylesheets().add(getClass().getResource("/style/ui.css").toExternalForm());

        uiDetails = new GridPane();
        uiDetails.getStyleClass().add("GridPane");
        uiDetails.setPrefWidth(200);
        uiDetails.setPadding(new Insets(10));

        uiDetails.add(new Label("Name: "),0,0);
        uiDetails.add(nameLabel,1,0);
        GridPane.setConstraints(nameLabel,1,0,2,1);

        uiDetails.add(new Label("Health: "), 0, 1);
        uiDetails.add(healthLabel, 1, 1);
        Label attackLabelText = new Label("Attack damage: ");
        uiDetails.add(attackLabelText,0,2);
        GridPane.setConstraints(attackLabelText,0,2,2,1);
        uiDetails.add(attackLabel,2,2);

        inventory = new GridPane();
        inventory.getStyleClass().add("inventory");
        inventory.getChildren().add(new Label("Inventory:"));


        itemsToCollect = new GridPane();
        itemsToCollect.getStyleClass().add("itemsToCollect");
        itemsToCollect.getChildren().add(new Label("Available Itemes:"));

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
        HashMap<String, Integer> bounds = map.getBounds();
        int difX = bounds.get("minX");
        int difY = bounds.get("minY");
        for (int x = bounds.get("minX"); x < bounds.get("maxX"); x++) {
            for (int y = bounds.get("minY"); y < bounds.get("maxY"); y++) {
                Cell cell = map.getCell(x, y);
                if (cell.getActor() != null) {
                    Tiles.drawTile(context, cell.getActor(), x-difX, y-difY);
                } else {
                    Tiles.drawTile(context, cell, x-difX, y-difY);
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
//                throw new RuntimeException(e);
                e.printStackTrace();
            }
        } else if (result.get() == buttonNo) {
            System.exit(1);
        }
    }


    public void showYouWin(){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION,null);
        alert.setTitle("You Won!");
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
        dialogPane.getStylesheets().add(getClass().getResource("/style/youWon.css").toExternalForm());
        dialogPane.getStyleClass().add("youWonPane");
        Optional<ButtonType> result = alert.showAndWait();
        if(result.isPresent() && result.get() == buttonYes){
            try {
                restartGame();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (result.get() == buttonNo) {
            System.exit(1);
        }
    }


    private void restartGame(){
        map = MapLoader.loadMap(MapLoader.class.getResourceAsStream(firstMap));
        actualMap = 1;
        gameRunning = true;
        map.getPlayer().setName(nameLabel.getText());
        refresh();
    }

    private void showCanvasMessage(String text ,double width, double height){
        double x;
        double y;
        double playerX = map.getPlayer().getX() < 12 ? map.getPlayer().getX() : (map.getPlayer().getX() < map.getWidth() - 13 ? 12 : map.getWidth()-25+map.getPlayer().getX());
        double initialX = playerX*32 - width/2;
        if(initialX < 0) x = 5;
        else if(initialX + width > 25*32) x = 25*32 - width - 30;
        else x = initialX;
        double initialY = map.getPlayer().getY()*32-height;
        if(initialY < 0) y = 10;
        else if(initialY > 20*32) y = 20*32-height-40;
        else y = initialY;
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setTextAlign(TextAlignment.CENTER);
        gc.setTextBaseline(VPos.CENTER);
        gc.setFill(Color.WHITE);
        gc.fillRoundRect(x-5,y-5,width+10,height+10,10,10);
        gc.setFill(Color.GRAY);
        gc.fillRect(x,y,width,height);
        gc.setFill(Color.BLACK);
        gc.fillText(text,x+width/2,y+10);
    }


    public void showControlsDialog(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.initOwner(mainStage);
        alert.setHeaderText(null);
        alert.setGraphic(null);
        alert.setTitle("How to Play");
        alert.getDialogPane().getStyleClass().add("controlPane");
        alert.getDialogPane().getStylesheets().add(getClass().getResource("/style/controls.css").toExternalForm());

        System.out.println(alert.getButtonTypes());
        alert.getButtonTypes().clear();
        ButtonType buttonTypeOK = new ButtonType("Close", ButtonBar.ButtonData.OK_DONE);
        alert.getButtonTypes().add(buttonTypeOK);

        alert.showAndWait();
    }

    public void showAboutDialog(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.initOwner(mainStage);
        alert.setHeaderText(null);
        alert.setGraphic(null);
        alert.setTitle("About the L.A team");
        alert.getDialogPane().getStyleClass().add("aboutPane");
        alert.getDialogPane().getStylesheets().add(getClass().getResource("/style/about.css").toExternalForm());

        System.out.println(alert.getButtonTypes());
        alert.getButtonTypes().clear();
        ButtonType buttonTypeOK = new ButtonType("Close", ButtonBar.ButtonData.OK_DONE);
//        alert.getButtonTypes().set(0,)
        alert.getButtonTypes().add(buttonTypeOK);
        Button closeButton = (Button) alert.getDialogPane().lookupButton(buttonTypeOK);
        closeButton.setAlignment(Pos.CENTER);

        alert.showAndWait();
    }
}
