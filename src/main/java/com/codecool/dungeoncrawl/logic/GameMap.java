package com.codecool.dungeoncrawl.logic;

import com.codecool.dungeoncrawl.logic.actors.Actor;
import com.codecool.dungeoncrawl.logic.actors.Door;
import com.codecool.dungeoncrawl.logic.actors.enemies.Enemy;
import com.codecool.dungeoncrawl.logic.actors.Player;
import com.codecool.dungeoncrawl.logic.actors.items.Item;
import com.codecool.dungeoncrawl.manager.DAOs.AvailableItemDAO;
import com.codecool.dungeoncrawl.manager.DAOs.ItemDAO;

import java.util.ArrayList;
import java.util.HashMap;

public class GameMap {
    private int width;
    private int height;
    private Cell[][] cells;

    private ArrayList<AvailableItemDAO> availableItemDAOS;

    private HashMap<String,Integer> availableItems;

    private ArrayList<Enemy> gameAI;

    private Player player;

    private Door door;

    private Cell portalA;

    private Cell portalB;

    public GameMap(int width, int height, CellType defaultCellType) {
        this.width = width;
        this.height = height;
        cells = new Cell[width][height];
        this.availableItems = new HashMap<>();
        this.gameAI = new ArrayList<>();
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

    public HashMap<String,Integer> getBounds(){
        HashMap<String,Integer> bounds = new HashMap<>();
        if(width <=25) {
            bounds.put("minX", 0);
            bounds.put("maxX",width);
        }
        else {
            int toAddX = 0;
            if (player.getX() - 12 < 0){
                toAddX = Math.abs(player.getX() - 12);
                bounds.put("minX", 0);
            }else {
                bounds.put("minX", player.getX() - 12);
            }
            int toSubX = 0;
            if(player.getX() + 13 + toAddX > width){
                bounds.put("maxX",width);
                toSubX = player.getX() + 13 + toAddX -width;
            }
            else{
                bounds.put("maxX", player.getX() + 13 + toAddX);
            }
            if(bounds.get("minX") - toSubX < 0){
                bounds.put("minX", 0);
            }else{
                bounds.put("minX", bounds.get("minX") - toSubX);
            }
        }
        if(height < 20){
            bounds.put("minY",0);
            bounds.put("maxY",height);
        }
        else {
            int toAddY = 0;
            if (player.getY() - 10 < 0){
                toAddY = Math.abs(player.getY() - 10);
                bounds.put("minY", 0);
            }else {
                bounds.put("minY", player.getY() - 10);
            }
            int toSubY = 0;
            if(player.getY() + 10 + toAddY > height){
                bounds.put("maxY",height);
                toSubY = player.getY() + 10 + toAddY - height;
            }
            else{
                bounds.put("maxY", player.getY() + 10 + toAddY);
            }
            if(bounds.get("minY")- toSubY < 0){
                bounds.put("minY", 0);
            }
            else
                bounds.put("minY",bounds.get("minY")-toSubY);
        }
        return bounds;
    }

    public int getHeight() {
        return height;
    }

    public void addItem(String item){
        if(availableItems.containsKey(item))
            availableItems.put(item,availableItems.get(item)+1);
        else availableItems.put(item,1);
    }

    public void addAI(Enemy ai){
        gameAI.add(ai);
    }

    public void removeEnemy(Enemy ai){
        gameAI.remove(ai);
    }

    public ArrayList<Enemy> getGameAI() {
        return gameAI;
    }

    public Door getDoor() {
        return door;
    }

    public void setDoor(Door door) {
        this.door = door;
    }

    public HashMap<String, Integer> getAvailableItems() {
        return availableItems;
    }

    public void removeItem(String item) {
        availableItems.put(item,availableItems.get(item)-1);
        if(availableItems.get(item) == 0) availableItems.remove(item);
    }

    public void teleport(){
        if (player.getCell().equals(portalA)) {
            Cell portalBspot = getCell(portalB.getX() + 1, portalB.getY());
            player.getCell().setActor(null);
            portalBspot.setActor(player);
            player.setCell(portalBspot);
        } else if (this.getPlayer().getCell().equals(this.getPortalB())) {
            Cell portalAspot = getCell(portalA.getX() + 1, portalA.getY());
            player.getCell().setActor(null);
            portalAspot.setActor(player);
            player.setCell(portalAspot);
        }
    }

    public Cell getPortalA() {
        return portalA;
    }

    public void setPortalA(Cell portalA) {
        this.portalA = portalA;
    }

    public Cell getPortalB() {
        return portalB;
    }

    public void setPortalB(Cell portalB) {
        this.portalB = portalB;
    }

    public ArrayList<AvailableItemDAO> getAllAvailableItems(){
        availableItemDAOS = new ArrayList<>();
        for(int i=0; i<cells.length; i++){
            for (int j=0; j<cells[i].length; j++){
                if(cells[i][j].getActor() instanceof Item){
                    availableItemDAOS.add(new AvailableItemDAO(cells[i][j].getActor().getTileName(), cells[i][j].getX(), cells[i][j].getY()));
                }
            }
        }
        return availableItemDAOS;
    }

    public void setActorOnPosition(Actor actor, int x, int y){
        cells[x][y].setActor(actor);
    }

    public void cleanActors(){
        for(int i =0; i<cells.length; i++){
            for(int j = 0; j<cells[i].length; j++){
//                if(cells[i][j].getActor() != null){
                    cells[i][j].setActor(null);
//                }

            }
        }
         player = null;
        availableItems.clear();
        gameAI.clear();
    }
}
