package com.codecool.dungeoncrawl;

import com.codecool.dungeoncrawl.logic.Cell;
import com.codecool.dungeoncrawl.logic.CellType;
import com.codecool.dungeoncrawl.logic.GameMap;
import com.codecool.dungeoncrawl.logic.MapLoader;
import com.codecool.dungeoncrawl.logic.actors.OpenedDoor;
import com.codecool.dungeoncrawl.manager.DAOs.*;
import com.codecool.dungeoncrawl.manager.DaoJDBCs.GameDAOJdbc;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

public class Main extends Application {

    PlayerDAO playerDAO;
    EnemyDAO enemyDAO;
    ItemDAO itemDAO;
    AvailableItemDAO availableItemDAO;

    GameDAO gameDAO;
    GameDAOJdbc gameDAOJdbc;
    Screen screen;
    Stage mainStage;
    Scene scene;
    BorderPane borderPane;

    int actualMap = 0;
    String firstMap= "/map.txt";
    String secondMap = "/map2.txt";
    String thirdMap= "/map3.txt";

    BorderPane uiContainer;
    GridPane uiDetails;

    GridPane ui;
    GridPane inventory;
    GridPane itemsToCollect;
    Button exitButton;
    Button musicButton;
    GameMap map;
    Canvas canvas;
    GraphicsContext context;
    Label healthLabel = new Label();
    Label attackLabel = new Label();
    Label nameLabel = new Label();
    boolean gameRunning = false;

    Media sound = new Media(Objects.requireNonNull(getClass().getResource("/sounds/music.mp3")).toExternalForm());
    MediaPlayer mediaPlayer = new MediaPlayer(sound);

    public static void main(String[] args) {
        System.out.println("Start running...");
        launch(args);
    }

    @Override
    public void start(Stage primaryStage){
        try{
            screen = Screen.getPrimary();

            mainStage = primaryStage;


            borderPane = new BorderPane();
            gameRunning = false;


            startApplicationState();

            scene = new Scene(borderPane);
            mainStage.setScene(scene);

            uiContainer.setFocusTraversable(true);
            scene.addEventHandler(KeyEvent.KEY_PRESSED, this::onKeyPressed);

            showMainStage();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void startApplicationState(){
        initStartApplicationstateUI();
        borderPane.setTop(initMenuBar());
        borderPane.setCenter(new ImageView(new Image(Objects.requireNonNull(Main.class.getResource("/main2.png")).toExternalForm())));
        borderPane.setRight(uiContainer);
    }

    private void initStartApplicationstateUI(){
        initUI();
        uiDetails.getChildren().clear();
        inventory.getChildren().clear();
        itemsToCollect.getChildren().clear();
        Button startGameButton = new Button("Start Game");
        startGameButton.getStyleClass().add("startButton");
        startGameButton.setOnAction(MouseEvent -> {
            map = MapLoader.loadMap(MapLoader.class.getResourceAsStream(firstMap));
            canvas = new Canvas(
                    map.getWidth() * Tiles.TILE_WIDTH,
                    map.getHeight() * Tiles.TILE_WIDTH);
            context = canvas.getGraphicsContext2D();
            getPlayerName();
            if(nameLabel.getText().equals("")){
                startApplicationState();
            }else {
                initUI();
                restartGame();
                uiContainer.setFocusTraversable(true);
                canvas.setFocusTraversable(true);
                borderPane.setCenter(canvas);

                refresh();
            }

        });
        uiDetails.add(startGameButton,1,0);
        uiContainer.setTop(uiDetails);
        uiContainer.setCenter(ui);
    }


    private void showMainStage(){
        Image icon = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/icon.png")));
        mainStage.getIcons().add(icon);
        mainStage.setTitle("Dungeon Crawl By L.A");
        mainStage.setResizable(false);
        mainStage.show();
        mainStage.setX(getScreenWidth()/2 - mainStage.getWidth()/2);
        mainStage.setY(getScreenHeight()/2 - mainStage.getHeight()/2);
    }


    private void playSound(){
        mediaPlayer.play();
        musicButton.setText("Music OFF");
        musicButton.setOnMouseClicked(mouseEvent -> stopSound());
    }

    private void stopSound(){
        mediaPlayer.stop();
        musicButton.setText("Music ON");
        musicButton.setOnMouseClicked(mouseEvent -> playSound());
    }

    private void onKeyPressed(KeyEvent keyEvent) {
        if(gameRunning) {
            borderPane.setCenter(canvas);
            String text;
            int health = map.getPlayer().getHealth();
            switch (keyEvent.getCode()) {
                case SPACE:
                    map.getPlayer().getEnemies().forEach(enemy -> {
                        if (map.getPlayer().getHealth() > 0) {
                            gameRunning = map.getPlayer().startFight(enemy);
                            if (gameRunning) map.removeEnemy(enemy);
                        }
                    });
                    refresh();
                    break;
                case H:
                    showControlsDialog();
                    break;
                case E:
                    try {
                        String item = map.getPlayer().getCell().getTempItem();
                        map.removeItem(map.getPlayer().getCell().getTempItem());
                        map.getPlayer().pickupItem();
                        refresh();
                        text = String.format("You picked up a %s!", item);
                        showCanvasMessage(text, 140);
                    } catch (NullPointerException ignored) {
                    }
                    break;
                case UP:
                case W:
                    map.getGameAI().forEach(AI -> AI.chooseAMove(map.getPlayer()));
                    map.getPlayer().move(0, -1);
                    if (map.getDoor() != null)
                        if (map.getPlayer().checkForKey() && map.getDoor().getTileName().equals("door"))
                            map.setDoor(new OpenedDoor(map.getDoor().getCell()));
                    refresh();
                    break;
                case DOWN:
                case S:
                    map.getGameAI().forEach(AI -> AI.chooseAMove(map.getPlayer()));
                    map.getPlayer().move(0, 1);
                    if (map.getDoor() != null)
                        if (map.getPlayer().checkForKey() && map.getDoor().getTileName().equals("door"))
                            map.setDoor(new OpenedDoor(map.getDoor().getCell()));
                    refresh();
                    break;
                case LEFT:
                case A:
                    map.getGameAI().forEach(AI -> AI.chooseAMove(map.getPlayer()));
                    if (map.getDoor() != null)
                        if (map.getPlayer().checkForKey() && map.getDoor().getTileName().equals("door"))
                            map.setDoor(new OpenedDoor(map.getDoor().getCell()));
                    map.getPlayer().move(-1, 0);
                    refresh();
                    break;
                case RIGHT:
                case D:
                    map.getGameAI().forEach(AI -> AI.chooseAMove(map.getPlayer()));
                    if (map.getDoor() != null)
                        if (map.getPlayer().checkForKey() && map.getDoor().getTileName().equals("door"))
                            map.setDoor(new OpenedDoor(map.getDoor().getCell()));
                    map.getPlayer().move(1, 0);
                    refresh();
                    break;
            }

            if (map.getPlayer().getCell().getType().equals(CellType.PORTAL)) {
                map.teleport();
                refresh();
            }
            if (map.getPlayer().getCell().getType().equals(CellType.CUP)) showYouWin();
            if (health > map.getPlayer().getHealth()) {
                text = String.format("You lost %s health points in battle!", health - map.getPlayer().getHealth());
                showCanvasMessage(text, 190);
            }
            if (map.getDoor() != null) if (map.getPlayer().getCell().equals(map.getDoor().getCell())) nextLevel();
            checkForEnd();
        }
        else if(keyEvent.getCode().equals(KeyCode.H)){
            showControlsDialog();
        } else if (keyEvent.getCode().equals(KeyCode.ENTER)) {
            getPlayerName();
            if(nameLabel.getText().equals("")) startApplicationState();
            else restartGame();
        }
    }


    private void getPlayerName(){
        TextInputDialog inputDialog = new TextInputDialog();
        inputDialog.initOwner(mainStage);
        inputDialog.setGraphic(null);
        inputDialog.setTitle("Character name needed!");
        inputDialog.setHeaderText(null);
        inputDialog.setContentText("Please insert your name: ");
        inputDialog.getDialogPane().getStylesheets().add(Objects.requireNonNull(getClass().getResource("/style/getName.css")).toExternalForm());
        inputDialog.getDialogPane().getStyleClass().add("getInputPane");
        inputDialog.getEditor().getStyleClass().add("textInput");
        inputDialog.getDialogPane().getButtonTypes().clear();
        ButtonType buttonTypeOK = new ButtonType("Insert", ButtonBar.ButtonData.OK_DONE);
        ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        inputDialog.getDialogPane().getButtonTypes().add(buttonTypeOK);
        inputDialog.getDialogPane().getButtonTypes().add(buttonTypeCancel);
        Optional<String> result = inputDialog.showAndWait();
        if(result.isPresent() && !result.get().equals("")){
            System.out.println("something");
            try {
                nameLabel.setText(inputDialog.getEditor().getText());
                map.getPlayer().setName(inputDialog.getEditor().getText());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void nextLevel() {
        int playerAD = map.getPlayer().getAttack();
        int playerHP = map.getPlayer().getHealth();
        HashMap<String,Integer> items = map.getPlayer().getItems();
        items.remove("key");
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
        menuBar.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/style/menubar.css")).toExternalForm());

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
            if(nameLabel.getText().equals("")) startApplicationState();
            else restartGame();
        });

        saveGameDB.setOnAction(e-> saveDatabaseGame());
        saveGameFile.setOnAction(e-> saveFileGame());
        loadDBGame.setOnAction(e-> loadDatabaseGame());
        loadFileGame.setOnAction(e-> loadFileGame());

        exit.setOnAction(e-> System.exit(0));


        gameMenu.getItems().add(newGame);
        gameMenu.getItems().add(saveGameDB);
        gameMenu.getItems().add(saveGameFile);
        gameMenu.getItems().add(loadDBGame);
        gameMenu.getItems().add(loadFileGame);
        gameMenu.getItems().add(exit);


        Menu help = new Menu("Help");


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
        uiContainer.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/style/ui.css")).toExternalForm());

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
        musicButton = new Button("Music ON");
        musicButton.setOnMouseClicked(mouseEvent -> playSound());

        exitContainer.add(musicButton,1,0);

        uiContainer.setTop(uiDetails);
        uiContainer.setBottom(exitContainer);
        borderPane.setRight(uiContainer);
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

    public void refreshInventory(){
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

    public void refreshItems(){
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
        System.exit(0);
    }

    public void checkForEnd(){
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
                    Tiles.drawTile(context, cell.getActor(), x - difX, y - difY);
                } else {
                    Tiles.drawTile(context, cell, x - difX, y - difY);
                }
            }
        }
        healthLabel.setText("" + map.getPlayer().getHealth());
        attackLabel.setText("" + map.getPlayer().getAttack());

        refreshUI();
    }

    private void showGameOver(){
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
        dialogPane.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/style/gameOver.css")).toExternalForm());
        dialogPane.getStyleClass().add("gameOverPane");

        alert.show();

        alert.setX(getScreenWidth()/2 - alert.getWidth()/2);
        alert.setY(getScreenHeight()/2 - alert.getHeight()/2);

        alert.setOnHiding((event)->{
            Optional<ButtonType> result = Optional.ofNullable(alert.getResult());
            if(result.isPresent() && result.get() == buttonYes){
                try {
                    restartGame();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            } else if (result.isPresent() && result.get() == buttonNo) {
                nameLabel.setText("");
                ui = null;
                startApplicationState();
            }
        });
    }


    private void showYouWin(){
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
        dialogPane.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/style/youWon.css")).toExternalForm());
        dialogPane.getStyleClass().add("youWonPane");



            alert.show();

            alert.setX(getScreenWidth()/2 - alert.getWidth()/2);
            alert.setY(getScreenHeight()/2 - alert.getHeight()/2);

            alert.setOnHiding(event -> {
                Optional<ButtonType> result = Optional.ofNullable(alert.getResult());
                if(result.isPresent() && result.get() == buttonYes){
                    try {
                        restartGame();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (result.isPresent() && result.get() == buttonNo) {
                    nameLabel.setText("");
                    startApplicationState();
                }
            });

    }


    public void restartGame(){
        map = MapLoader.loadMap(MapLoader.class.getResourceAsStream(firstMap));
        actualMap = 1;
        gameRunning = true;
        map.getPlayer().setName(nameLabel.getText());
        refresh();
    }

    private void showCanvasMessage(String text ,double width){
        double x;
        double y;
        double playerX = map.getPlayer().getX() < 12 ? map.getPlayer().getX() : (map.getPlayer().getX() < map.getWidth() - 13 ? 12 : map.getWidth()-25+map.getPlayer().getX());
        double initialX = playerX*32 - width/2;
        if(initialX < 0) x = 5;
        else if(initialX + width > 25*32) x = 25*32 - width - 30;
        else x = initialX;
        double initialY = map.getPlayer().getY()*32- (double) 20;
        if(initialY < 0) y = 10;
        else if(initialY > 20*32) y = 20*32- (double) 20 -40;
        else y = initialY;
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setTextAlign(TextAlignment.CENTER);
        gc.setTextBaseline(VPos.CENTER);
        gc.setFill(Color.WHITE);
        gc.fillRoundRect(x-5,y-5,width+10, (double) 20 +10,10,10);
        gc.setFill(Color.GRAY);
        gc.fillRect(x,y,width, 20);
        gc.setFill(Color.BLACK);
        gc.fillText(text,x+width/2,y+10);
    }


    private void showControlsDialog(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.initOwner(mainStage);
        alert.setHeaderText(null);
        alert.setGraphic(null);
        alert.setTitle("How to Play");
        alert.getDialogPane().getStyleClass().add("controlPane");
        alert.getDialogPane().getStylesheets().add(Objects.requireNonNull(getClass().getResource("/style/controls.css")).toExternalForm());

        alert.getButtonTypes().clear();
        ButtonType buttonTypeOK = new ButtonType("Close", ButtonBar.ButtonData.OK_DONE);
        alert.getButtonTypes().add(buttonTypeOK);


        alert.show();

        alert.setX(getScreenWidth()/2 - alert.getWidth()/2);
        alert.setY(getScreenHeight()/2 - alert.getHeight()/2);
    }

    private void showAboutDialog(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.initOwner(mainStage);
        alert.setHeaderText(null);
        alert.setGraphic(null);
        alert.setTitle("About the L.A team");
        alert.getDialogPane().getStyleClass().add("aboutPane");
        alert.getDialogPane().getStylesheets().add(Objects.requireNonNull(getClass().getResource("/style/about.css")).toExternalForm());

        alert.getButtonTypes().clear();
        ButtonType buttonTypeOK = new ButtonType("Close", ButtonBar.ButtonData.OK_DONE);
        alert.getButtonTypes().add(buttonTypeOK);
        Button closeButton = (Button) alert.getDialogPane().lookupButton(buttonTypeOK);
        closeButton.setAlignment(Pos.CENTER);

        alert.show();
        alert.setX(getScreenWidth()/2 - alert.getWidth()/2);
        alert.setY(getScreenHeight()/2 - alert.getHeight()/2);
    }

    private void saveDatabaseGame(){
        String saveName = getName();
        if (saveName != null){
            ArrayList<AvailableItemDAO> availableItemDAOs = map.getAllAvailableItems();
            ArrayList<ItemDAO> itemDAOs = map.getPlayer().getAllItems();
            ArrayList<EnemyDAO> enemyDAOs = new ArrayList<>();
            playerDAO = new PlayerDAO(nameLabel.getText(), map.getPlayer().getX(), map.getPlayer().getY());
            map.getPlayer().getEnemies().forEach(enemy -> {
                enemyDAOs.add(new EnemyDAO(enemy.getTileName(), enemy.getX(), enemy.getY()));
            });
            gameDAO = new GameDAO(
                    saveName,
                    actualMap,
                    playerDAO,
                    enemyDAOs,
                    itemDAOs,
                    availableItemDAOs

            );
        }

    }

    private String getName(){
        TextInputDialog inputDialog = new TextInputDialog();
        inputDialog.initOwner(mainStage);
        inputDialog.setGraphic(null);
        inputDialog.setTitle("Game name needed!");
        inputDialog.setHeaderText(null);
        inputDialog.setContentText("Please insert your game: ");
        inputDialog.getDialogPane().getStylesheets().add(Objects.requireNonNull(getClass().getResource("/style/getName.css")).toExternalForm());
        inputDialog.getDialogPane().getStyleClass().add("getInputPane");
        inputDialog.getEditor().getStyleClass().add("textInput");
        inputDialog.getDialogPane().getButtonTypes().clear();
        ButtonType buttonTypeOK = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        inputDialog.getDialogPane().getButtonTypes().add(buttonTypeOK);
        inputDialog.getDialogPane().getButtonTypes().add(buttonTypeCancel);
        Optional<String> result = inputDialog.showAndWait();
        if(result.isPresent() && !result.get().equals("")){
            try {
                return inputDialog.getEditor().getText();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        return null;
    }

    private void loadDatabaseGame(){

    }

    private void saveFileGame(){

    }

    private void loadFileGame(){

    }

    private double getScreenWidth(){
        return screen.getVisualBounds().getWidth();
    }

    private double getScreenHeight(){
        return screen.getVisualBounds().getHeight();
    }

}
