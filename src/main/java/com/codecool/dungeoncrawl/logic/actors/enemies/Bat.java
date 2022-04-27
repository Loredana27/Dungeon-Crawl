package com.codecool.dungeoncrawl.logic.actors.enemies;

import com.codecool.dungeoncrawl.logic.Cell;
import com.codecool.dungeoncrawl.logic.CellType;
import com.codecool.dungeoncrawl.logic.actors.Player;

import java.util.Random;

public class Bat extends Enemy {


    public Bat(Cell cell) {
        super(cell);
        this.attack = 1;
        this.health = 9;
    }

    @Override
    public String getTileName() {
        return "bat";
    }


    @Override
    public void chooseAMove(Player player) {
        moveToPatrol();
    }

    @Override
    public void moveToPatrol() {
        Random random = new Random();
        int dx = random.nextInt(3) - 1;
        int dy = random.nextInt(3) - 1;
        Cell nextCell = getCell().getNeighbor(dx,dy);
        switch (nextCell.getTileName()){
            case "floor":
            case "player":
                move(dx,dy);
                break;
            default:
                moveToPatrol();
        }
    }

    @Override
    public void moveToPlayer(Player player) {
    }

    @Override
    public boolean isPlayerInRange(Player player) {
        return false;
    }
}
