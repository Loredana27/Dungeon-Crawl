package manager;

import com.codecool.dungeoncrawl.logic.actors.Player;
import com.codecool.dungeoncrawl.logic.actors.enemies.Enemy;
import com.codecool.dungeoncrawl.logic.actors.items.Item;

import java.util.ArrayList;
import java.util.Date;

public class GameDAO {
    private int id;
    private String name;
    private int actualMap;

    private Date savedata;
    private PlayerDAO player;
    private ArrayList<EnemyDAO> enemies;
    private ArrayList<ItemDAO> items;
    private ArrayList<AvailableItemDAO> availableItems;

    public GameDAO(String name, int actualMap, PlayerDAO player, ArrayList<EnemyDAO> enemies, ArrayList<ItemDAO> items, ArrayList<AvailableItemDAO> availableItems) {
        this.name = name;
        this.actualMap = actualMap;
        this.player = player;
        this.enemies = enemies;
        this.items = items;
        this.availableItems = availableItems;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getActualMap() {
        return actualMap;
    }

    public void setActualMap(int actualMap) {
        this.actualMap = actualMap;
    }

    public PlayerDAO getPlayer() {
        return player;
    }

    public void setPlayer(PlayerDAO player) {
        this.player = player;
    }

    public ArrayList<EnemyDAO> getEnemies() {
        return enemies;
    }

    public void setEnemies(ArrayList<EnemyDAO> enemies) {
        this.enemies = enemies;
    }

    public ArrayList<ItemDAO> getItems() {
        return items;
    }

    public void setItems(ArrayList<ItemDAO> items) {
        this.items = items;
    }

    public ArrayList<AvailableItemDAO> getAvailableItems() {
        return availableItems;
    }

    public void setAvailableItems(ArrayList<AvailableItemDAO> availableItems) {
        this.availableItems = availableItems;
    }

    public Date getSavedata() {
        return savedata;
    }

    public void setSavedata(Date savedata) {
        this.savedata = savedata;
    }
}
