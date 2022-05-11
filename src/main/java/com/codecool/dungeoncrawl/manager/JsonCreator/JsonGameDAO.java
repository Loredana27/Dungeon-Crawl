package com.codecool.dungeoncrawl.manager.JsonCreator;

import com.codecool.dungeoncrawl.manager.DAOs.AvailableItemDAO;
import com.codecool.dungeoncrawl.manager.DAOs.EnemyDAO;
import com.codecool.dungeoncrawl.manager.DAOs.ItemDAO;
import com.codecool.dungeoncrawl.manager.DAOs.PlayerDAO;


import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import org.json.simple.JSONObject;
import org.json.simple.JSONArray;

public class JsonGameDAO {
    protected String name;
    protected int actualMap;

    protected Date saveDate;
    protected PlayerDAO player;
    protected ArrayList<EnemyDAO> enemies;
    protected ArrayList<ItemDAO> items;
    protected ArrayList<AvailableItemDAO> availableItems;

    public JsonGameDAO(String name, int actualMap, PlayerDAO player, ArrayList<EnemyDAO> enemies, ArrayList<ItemDAO> items, ArrayList<AvailableItemDAO> availableItems) {
        this.name = name;
        this.actualMap = actualMap;
        this.player = player;
        this.enemies = enemies;
        this.items = items;
        this.availableItems = availableItems;
        this.saveDate = new java.sql.Date(System.currentTimeMillis());
    }

    public String toJson(){
        JSONObject game = new JSONObject();
        game.put("name", name);
        game.put("actualMap", actualMap);
        game.put("saveDate", String.valueOf(saveDate));
        JSONObject playerJson = new JSONObject();
        playerJson.put("name",player.getName());
        playerJson.put("posX",player.getPosX());
        playerJson.put("posY",player.getPosY());
        game.put("player", playerJson);
        JSONArray enemiesJson = new JSONArray();
        enemies.forEach(e -> {
            JSONObject obj = new JSONObject();
            obj.put("type", e.getType());
            obj.put("posX", e.getPosX());
            obj.put("posY", e.getPosY());
            enemiesJson.add(obj);
        });
        game.put("enemies", enemiesJson);
        JSONArray itemsJson = new JSONArray();
        items.forEach(e->{
            JSONObject obj = new JSONObject();
            obj.put("type", e.getType());
            itemsJson.add(obj);
        });
        game.put("items", itemsJson);
        JSONArray availableItemsJson = new JSONArray();
        availableItems.forEach(e ->{
            JSONObject obj = new JSONObject();
            obj.put("type", e.getType());
            obj.put("posX",e.getPosX());
            obj.put("posY",e.getPosY());
            availableItemsJson.add(obj);
        });
        game.put("availableItems", availableItemsJson);
        return game.toJSONString()
                .replace(",",",\n")
                .replace("{","{\n")
                .replace("}","\n}\n");
    }
}
