package com.codecool.dungeoncrawl.logic.actors;

import com.codecool.dungeoncrawl.logic.Cell;
import com.codecool.dungeoncrawl.logic.Drawable;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class Actor implements Drawable{
    private Cell cell;
    protected int health = 10;

    protected int attack;



    private ArrayList<Enemy> enemies;

    private HashMap<String,Integer> items;

    public Actor(Cell cell) {
        this.cell = cell;
        this.cell.setActor(this);
        items = new HashMap<>();
        setNeighborEnemies();
    }

    public void move(int dx, int dy) {
        Cell nextCell = cell.getNeighbor(dx, dy);
        if(!(nextCell.getTileName().equalsIgnoreCase("WALL") || nextCell.getTileName().equalsIgnoreCase("door"))){
            if(nextCell.getActor()!= null) {
                String tileActor = nextCell.getActor().getTileName();
                switch (tileActor){
                    case "sword":
                        if(this instanceof Player)
                            setAttack(attack + 3);
                    case "key":
                        if(this instanceof Player){
                            this.addItem(nextCell.getActor().getTileName());
                            cell.setActor(null);
                            nextCell.setActor(this);
                            cell = nextCell;
                        }
                        break;
                    case "skeleton":
                        if(this instanceof Player)
                            this.isAttacked(nextCell.getActor().getAttack());
                        break;
                    case "player":
                        nextCell.getActor().isAttacked(this.attack);
                        break;
                    case "opened-door":
                        if(this instanceof Player){
                            cell.setActor(null);
                            nextCell.setActor(this);
                            cell = nextCell;
                        }
                        break;
                }
            }
            else{
                cell.setActor(null);
                nextCell.setActor(this);
                cell = nextCell;
            }
            setNeighborEnemies();
        }
    }


    private void setNeighborEnemies(){
        enemies = new ArrayList<>();
        for(int dx = -1; dx <= 1; dx++)
            for(int dy = -1; dy <= 1; dy++) {
                if(dx != 0 || dy != 0){
                    try {
                        Actor enemy = cell.getNeighbor(dx, dy).getActor();
                        if (enemy != null)
                            switch (enemy.getTileName()) {
                                case "skeleton":
                                    enemies.add((Enemy) enemy);
                            }
                    }catch (IndexOutOfBoundsException e){

                    }
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
}
