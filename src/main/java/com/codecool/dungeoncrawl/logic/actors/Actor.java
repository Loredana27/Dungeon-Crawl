package com.codecool.dungeoncrawl.logic.actors;

import com.codecool.dungeoncrawl.logic.Cell;
import com.codecool.dungeoncrawl.logic.CellType;
import com.codecool.dungeoncrawl.logic.Drawable;

import java.util.ArrayList;

public abstract class Actor implements Drawable{
    private Cell cell;
    protected int health = 10;

    protected int attack;

    private ArrayList<Actor> items;

    public Actor(Cell cell) {
        this.cell = cell;
        this.cell.setActor(this);
        items = new ArrayList<>();
    }

    public void move(int dx, int dy) {
        Cell nextCell = cell.getNeighbor(dx, dy);
        if(!nextCell.getTileName().equalsIgnoreCase("WALL")){

            if(nextCell.getActor()!= null) {
                String tileActor = nextCell.getActor().getTileName();
                switch (tileActor){
                    case "sword":
                        this.addItem(nextCell.getActor());
                        cell.setActor(null);
                        nextCell.setActor(this);
                        setAttack(attack + 3);
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

    public void addItem(Actor item){
        items.add(item);
    }

    public ArrayList<Actor> getItems() {
        return items;
    }
}
