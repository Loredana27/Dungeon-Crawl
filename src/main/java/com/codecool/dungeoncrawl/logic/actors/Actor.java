package com.codecool.dungeoncrawl.logic.actors;

import com.codecool.dungeoncrawl.logic.Cell;
import com.codecool.dungeoncrawl.logic.Drawable;

import java.util.HashMap;

public abstract class Actor implements Drawable{
    private Cell cell;
    protected int health = 10;

    protected int attack;

//    private ArrayList<Actor> items;

    private HashMap<String,Integer> items;

    public Actor(Cell cell) {
        this.cell = cell;
        this.cell.setActor(this);
        items = new HashMap<>();
    }

    public void move(int dx, int dy) {
        Cell nextCell = cell.getNeighbor(dx, dy);
        if(!nextCell.getTileName().equalsIgnoreCase("WALL")){

            if(nextCell.getActor()!= null) {
                String tileActor = nextCell.getActor().getTileName();
                switch (tileActor){
                    case "sword":
                        setAttack(attack + 3);
                    case "key":
                        this.addItem(nextCell.getActor().getTileName());
                        cell.setActor(null);
                        nextCell.setActor(this);
                        break;
                    case "skeleton":
                        while(true){
                            nextCell.getActor().isAttacked(this.attack);
                            if(nextCell.getActor().getHealth() <= 0) break;
                            this.isAttacked(nextCell.getActor().getAttack());
                            if(this.health <= 0) break;
                        }
                        if(this.health <= 0){
                            cell.setActor(null);
                        }
                        else{
                            cell.setActor(null);
                            nextCell.setActor(this);
                        }
                        break;
                    case "door":
                        cell.setActor(null);
                        nextCell.setActor(this);
                        break;

                }
            }
            else{
                cell.setActor(null);
                nextCell.setActor(this);
            }
            cell = nextCell;
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
}
