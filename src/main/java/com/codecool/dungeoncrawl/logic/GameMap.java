package com.codecool.dungeoncrawl.logic;

import com.codecool.dungeoncrawl.logic.actors.Actor;
import com.codecool.dungeoncrawl.logic.actors.Player;

import java.util.ArrayList;
import java.util.HashMap;

public class GameMap {
    private int width;
    private int height;
    private Cell[][] cells;

    private HashMap<String,Integer> availableItems;

    private Player player;

    public GameMap(int width, int height, CellType defaultCellType) {
        this.width = width;
        this.height = height;
        cells = new Cell[width][height];
        this.availableItems = new HashMap<>();
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                cells[x][y] = new Cell(this, x, y, defaultCellType);
            }
        }
    }

    public Cell getCell(int x, int y) {
        return cells[x][y];
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void addItem(String item){
        if(availableItems.containsKey(item))
            availableItems.put(item,availableItems.get(item)+1);
        else availableItems.put(item,1);
    }

    public HashMap<String, Integer> getAvailableItems() {
        return availableItems;
    }

    public void removeItem(String item) {
        availableItems.put(item,availableItems.get(item)-1);
        if(availableItems.get(item) == 0) availableItems.remove(item);
    }
}
