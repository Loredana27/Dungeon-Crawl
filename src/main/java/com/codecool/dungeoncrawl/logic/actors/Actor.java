package com.codecool.dungeoncrawl.logic.actors;

import com.codecool.dungeoncrawl.logic.Cell;
import com.codecool.dungeoncrawl.logic.Drawable;
import com.codecool.dungeoncrawl.logic.actors.enemies.Enemy;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class Actor implements Drawable{

    private static ArrayList<String> collisionObjects;
    private Cell cell;
    protected int health = 10;

    protected int attack;


    private ArrayList<Enemy> enemies;

    private HashMap<String,Integer> items;

    public Actor(Cell cell) {
        this.cell = cell;
        this.cell.setActor(this);
        items = new HashMap<>();
        collisionObjects = new ArrayList<>();
        collisionObjects.add("wall");
        collisionObjects.add("door");
        collisionObjects.add("house");
    }

    public void move(int dx, int dy) {
        try {
            Cell nextCell = cell.getNeighbor(dx, dy);
            String nextCellType = nextCell.getTileName();
            if (!(collisionObjects.contains(nextCellType))) {
                if (nextCell.getActor() != null) {
                    String tileActor = nextCell.getActor().getTileName();
                    switch (tileActor) {
                        case "heal":
                        case "opened-door":
                        case "sword":
                        case "treasurykey":
                        case "key":
                            if (this instanceof Player) {
                                cell.setActor(null);
                                nextCell.setActor(this);
                                cell = nextCell;
                            }
                            break;
                        case "treasury":
                            if (this instanceof Player) {
                                if(items.containsKey("treasurykey")) {
                                    cell.setActor(null);
                                    nextCell.setActor(this);
                                    cell = nextCell;
                                }
                            }
                            break;
                        case "skeleton":
                            if (this instanceof Player)
                                this.isAttacked(nextCell.getActor().getAttack());
                            break;
                        case "player":
                            nextCell.getActor().isAttacked(this.attack);
                            break;
                    }
                } else {
                    cell.setActor(null);
                    nextCell.setActor(this);
                    cell = nextCell;
                }
                setNeighborEnemies();
            }
        }catch (IndexOutOfBoundsException ignored){}
    }

    public void pickupItem(){
        try {
            switch (cell.getTempItem()) {
                case "heal":
                    //                addItem("heal");
                    health += 10;
                    cell.cleanTempItem();
                    break;
                case "key":
                    addItem("key");
                    cell.cleanTempItem();
                    break;
                case "sword":
                    addItem("sword");
                    attack += 3;
                    cell.cleanTempItem();
                    break;
                case "treasury":
                    addItem("bigsword");
                    attack += 7;
                    health += 10;
                    items.remove("treasurykey");
                    cell.cleanTempItem();
                    break;
                case "treasurykey":
                    addItem("treasurykey");
                    cell.cleanTempItem();
                    break;
            }
        }catch (NullPointerException ignored){}
    }

    void setNeighborEnemies(){
        enemies = new ArrayList<>();
        for(int dx = -1; dx <= 1; dx++)
            for(int dy = -1; dy <= 1; dy++) {
                if(dx != 0 || dy != 0){
                    try {
                        Actor enemy = cell.getNeighbor(dx, dy).getActor();
                        if (enemy != null)
                            switch (enemy.getTileName()) {
                                case "skeleton":
                                case "bat":
                                case "bear":
                                case "farmer":
                                case "soldier":
                                    enemies.add((Enemy) enemy);
                            }
                    }catch (IndexOutOfBoundsException ignored){
                    }catch (NullPointerException ignored){}
                }
            }
    }

    public boolean startFight(Actor enemy){
        while(true){
            enemy.isAttacked(this.attack);
            if(enemy.getHealth() <= 0) break;
            this.isAttacked(enemy.getAttack());
            if(this.health <= 0) break;
        }
        if(this.health <= 0){
            cell.setActor(null);
            return false;
        }
        else{
            enemy.getCell().setActor(null);
            return true;
        }
    }

    private void isAttacked(int damage){
        health -= damage;
    }

    public int getHealth() {
        return health;
    }

    public Cell getCell() {
        return cell;
    }

    public int getX() {
        return cell.getX();
    }

    public int getY() {
        return cell.getY();
    }

    public int getAttack(){
        return attack;
    }

    public void setAttack(int attack) {
        this.attack = attack;
    }

    public void addItem(String item){
        if(items.containsKey(item))
            items.put(item,items.get(item)+1);
        else
            items.put(item,1);
    }

    public HashMap<String, Integer> getItems() {
        return items;
    }

    public void setHealth(int health){
        this.health = health;
    }

    public void setItems(HashMap<String, Integer> items) {
        this.items = items;
    }

    public ArrayList<Enemy> getEnemies() {
        return enemies;
    }

    public boolean checkForKey(){
        return items.containsKey("key");
    }
    public boolean checkForTresuryKey(){
        return items.containsKey("tresurykey");
    }
}
