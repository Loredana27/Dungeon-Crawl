package com.codecool.dungeoncrawl.logic.actors;

import com.codecool.dungeoncrawl.logic.Cell;
import com.codecool.dungeoncrawl.logic.CellType;
import com.codecool.dungeoncrawl.logic.Drawable;

public abstract class Actor implements Drawable {
    private Cell cell;
    protected int health = 10;

    protected int attack;

    public Actor(Cell cell) {
        this.cell = cell;
        this.cell.setActor(this);
    }

    public void move(int dx, int dy) {
        Cell nextCell = cell.getNeighbor(dx, dy);
        if(!nextCell.getTileName().equalsIgnoreCase("WALL")){

            if(nextCell.getActor()!= null) {
                System.out.println(nextCell.getActor().getTileName());
                if (nextCell.getActor().getTileName().equals("sword")) {
                    cell.setType(CellType.FLOOR);
                    setAttack(attack + 3);
                } else if (nextCell.getActor().getTileName().equals("skeleton")) {
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
}
