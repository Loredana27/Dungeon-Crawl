package com.codecool.dungeoncrawl.logic.actors;

import com.codecool.dungeoncrawl.logic.Cell;
import com.codecool.dungeoncrawl.manager.DAOs.ItemDAO;

import java.util.ArrayList;
import java.util.HashMap;


public class Player extends Actor {

    private String name;

    public Player(Cell cell) {
        super(cell);
        attack = 5;
        setNeighborEnemies();
    }

    public String getTileName() {
        return "player";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<ItemDAO> getAllItems(){
        HashMap<String, Integer> items = getItems();
        ArrayList<ItemDAO> itemDAOS = new ArrayList<>();
        items.keySet().forEach(key ->{
            for(int i=0; i<items.get(key); i++){
                itemDAOS.add(new ItemDAO(key));
            }
        });
        return itemDAOS;
    }
}
